package com.songlanyun.jymall.modules.business.score.controller;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.jysys.apply.service.ApplyService;
import com.songlanyun.jymall.modules.business.jysys.bank.service.BankBindService;
import com.songlanyun.jymall.modules.business.jysys.dict.service.YjDictService;
import com.songlanyun.jymall.modules.business.score.entity.CashStatistical;
import com.songlanyun.jymall.modules.business.score.entity.YjCash;
import com.songlanyun.jymall.modules.business.score.service.YjCashService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.service.StatisticalService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/12/5
 * Time: 17:22
 */
@RestController
@RequestMapping("/yjCash")
public class YjCashController {
    @Autowired
    private YjCashService yjCashService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private BankBindService bankBindService;
    @Resource
    private SysAdminDao sysAdminDao;
    @Resource
    private StatisticalService statisticalService;

    @Autowired
    private YjDictService dictService;

    /**
     * 用户个人银行卡支付宝列表接口
     */

    @GetMapping("/bandapplylist")
    public R bandapplylist() {
        Map<String,Object> param=new HashMap<>();
        if (!ShiroUtils.getUserId().equals("1")) {
            param.put("userId", ShiroUtils.getUserId());
        }
        Map<String,Object> reMap=new HashMap<>();
        reMap.put("bank",bankBindService.findAll(param));
        reMap.put("apply",applyService.findAll(param));
        return R.ok().put("data", reMap);
    }
    /**
     * 列表
     *
     * 列表显示：ID、用户名、提现金额、提现手续费、
     * 实提金额、账户类型（银行卡/支付宝）、账户姓名（开户姓名）、
     * 账户账号（银行/支付宝账号）、银行名称、银行地址、备注、操作人、状态、创建时间、
     * 审核时间、操作（审核通过、审核失败（审核失败，钱要退回给余额））
     */

    @PostMapping("/cashList")
    public R cashList(@RequestBody CashStatistical cashStatistical) {
        /*if (!ShiroUtils.getUserId().equals("1")) {
            cashStatistical.setUserId(ShiroUtils.getUserId());
        }*/
        return statisticalService.cashList(cashStatistical);
    }


    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        if (!ShiroUtils.getUserId().equals("1")) {
            param.put("userId", ShiroUtils.getUserId());
        }
        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));

            param.remove("page");
            param.remove("size");
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<YjCash> pages = yjCashService.findAll((Specification<YjCash>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ShiroUtils.getUserId().equals("1")) {
                predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        List<YjCash> list= pages.getContent();
        if(null!=list && list.size()>0){
            for (YjCash yjCash:list){
                yjCash.setCardPwd("");
                yjCash.setUsername(sysUserService.getUserById(yjCash.getUserId().toString()).getUsername());
                if(null!=yjCash.getOpterId()){
                    yjCash.setOptername(sysUserService.getUserById(yjCash.getOpterId().toString()).getUsername());
                }
            }
        }
        PageUtils pageUtils = new PageUtils(list, (int) pages.getTotalElements(), size, page);
        return R.ok().put("data", pageUtils);}
        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", yjCashService.findById(id));
    }

    /**
     * 保存
     */
    /*@PostMapping("/save")
    @Transactional
    public R save(@RequestBody YjCash yjCash) {
        if(null==yjCash.getId()){
            //校验钱包密码。。。。
            SysUserEntity user=sysUserService.getUserById(ShiroUtils.getUserId());
            if(!(user.getPayPsw()).equals(new Sha256Hash(yjCash.getCardPwd(), user.getSalt()).toHex())){
                return R.error("支付密码错误");
            }
            //2020.2.11 新增提现金额必须大于等于100
            //结果是:   -1：小于；   0 ：等于；   1 ：大于
            BigDecimal bigDecimal=new BigDecimal(100);
            if(yjCash.getMoney().compareTo(bigDecimal)==-1){
                return R.error("提现金额必须大于等于100");
            }
            if(yjCash.getMoney().compareTo(user.getBalance())==1){
                return R.error("提现金额大于余额");
            }
            yjCash.setUserId(Integer.parseInt(ShiroUtils.getUserId().toString()));
            yjCash.setCreateDate(new Date());
            yjCash.setStatus(0);//申请审核
            String tax= dictService.getDictListByType(19).get(0).getDvalue();
            if(StringUtils.isBlank(tax)){
                tax="0";
            }
            yjCash.setTax(tax);
            BigDecimal taBig=BigDecimal.valueOf(1-Double.valueOf(tax)).setScale(2,BigDecimal.ROUND_HALF_UP);
            yjCash.setTaxMoney(yjCash.getMoney().multiply(taBig).setScale(2,BigDecimal.ROUND_HALF_UP));
            yjCashService.save(yjCash);
            BigDecimal syMoney=user.getBalance();
            syMoney=syMoney.subtract(yjCash.getMoney());
            user.setBalance(syMoney);
            sysUserService.updateUserBalance(user);//更新用户钱包扣钱
            //支付订单汇总
            PaymentSummary paymentSummary=new PaymentSummary();
            paymentSummary.setUserId(user.getUserId());
            paymentSummary.setRelationId(0L);
            paymentSummary.setPaymentType(1);//0收入 1支出
            paymentSummary.setOrderNo(null);
            paymentSummary.setOperaType(7);//1寄售2订单支付3推荐奖4分红5红包6转账7提现8退款处理9升级代理商
            paymentSummary.setMoney(yjCash.getMoney());
            paymentSummary.setBeforeMoney(user.getBalance().add(yjCash.getMoney()));
            paymentSummary.setAfterMoney(user.getBalance());
            paymentSummary.setPayType(1);//1余额2微信3支付宝4易货币
            paymentSummary.setRemark("用户提现申请金额:"+paymentSummary.getMoney()+"元,剩余余额:"+paymentSummary.getAfterMoney()+"元");
            sysAdminDao.savePaymentSummary(paymentSummary);
        }else{
            yjCash.setOpterDate(new Date());
            yjCash.setOpterId(Integer.parseInt(ShiroUtils.getUserId().toString()));
            if(yjCash.getStatus()==2){//审核拒绝则返现到钱包
                //审核不通过则退款钱包
                Integer uid=yjCashService.findById(yjCash.getId()).getUserId();
                SysUserEntity sue= sysUserService.getUserById(Long.parseLong(uid.toString())); //获取用户信息
                BigDecimal syMoney=sue.getBalance();
                syMoney=syMoney.add(yjCashService.findById(yjCash.getId()).getMoney());
                sue.setBalance(syMoney);
                sysUserService.updateUserBalance(sue);//更新用户钱包
                //支付订单汇总
                PaymentSummary paymentSummary=new PaymentSummary();
                paymentSummary.setUserId(sue.getUserId());
                paymentSummary.setRelationId(0L);
                paymentSummary.setPaymentType(0);//0收入 1支出
                paymentSummary.setOrderNo(null);
                paymentSummary.setOperaType(7);//1寄售2订单支付3推荐奖4分红5红包6转账7提现8退款处理9升级代理商
                paymentSummary.setMoney(yjCashService.findById(yjCash.getId()).getMoney());
                paymentSummary.setBeforeMoney(sue.getBalance().subtract(yjCashService.findById(yjCash.getId()).getMoney()));
                paymentSummary.setAfterMoney(sue.getBalance());
                paymentSummary.setPayType(1);//1余额2微信3支付宝4易货币
                paymentSummary.setRemark("拒绝提现申请金额:"+paymentSummary.getMoney()+"元,剩余余额:"+paymentSummary.getAfterMoney()+"元");
                sysAdminDao.savePaymentSummary(paymentSummary);
            }
            yjCashService.updateCheckStatus(yjCash);
        }
        return R.ok("申请成功");
    }*/


    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        yjCashService.deleteById(id);
        return R.ok("删除成功");
    }

    @RequestMapping("/cashExport")
    public void cashExport(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception{
        statisticalService.cashExport(request,response,cashStatistical);
    }

}
