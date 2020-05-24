package com.songlanyun.jymall.modules.business.jysys.notify.controller;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.Notify;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.NotifyTemp;
import com.songlanyun.jymall.modules.business.jysys.notify.model.NotifyModel;
import com.songlanyun.jymall.modules.business.jysys.notify.service.NotifyService;
import com.songlanyun.jymall.modules.business.jysys.notify.service.NotifyTempService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by zenghang.
 * Date: 2019/11/29
 * Time: 9:56
 */
@RestController
@RequestMapping("/notify")
public class NotifyController {
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private NotifyTempService notifyTempService;
    @Resource
    private SysAdminDao sysAdminDao;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
       /* if (ShiroUtils.getUserId() != 1) {
            param.put("userId", ShiroUtils.getUserId());
        }*/

       /* PageUtils pageUtils = notifyService.queryPage(param);
        return R.ok().put("data", pageUtils);*/


        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));
            param.remove("page");
            param.remove("size");
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Notify> pageData = notifyService.findAll((Specification<Notify>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                //in查询
                /*if (ShiroUtils.getUserId() != 1) {
                    predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
                }*/


                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), size, page);
            return R.ok().put("data", pageUtils);
        } else {
            return R.error("查询条件中未包含指定参数：page and size");
        }
    }

    @GetMapping("/applist")
    public R applist(@RequestParam Map<String, Object> param) {
       /* if (ShiroUtils.getUserId() != 1) {
            param.put("userId", ShiroUtils.getUserId());
        }*/

       /* PageUtils pageUtils = notifyService.queryPage(param);
        return R.ok().put("data", pageUtils);*/


        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));
            param.remove("page");
            param.remove("size");
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Notify> pageData = notifyService.findAll((Specification<Notify>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                //in查询
                if (!ShiroUtils.getUserId().equals("1")) {
                    predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), size, page);
            return R.ok().put("data", pageUtils);
        } else {
            return R.error("查询条件中未包含指定参数：page and size");
        }
    }

    /** 保存消息 **/
    public R saveMsg(int tempId,String replaceStr,long userId){
        NotifyModel notifyModel = new NotifyModel();
        notifyModel.setNotifyTempId(tempId);
        List<String> contents = Arrays.asList(replaceStr.split(" ") );
        //有多少个占位符就add多少个，超过的无用，少了自动使用空字符串替换
        notifyModel.setContents(contents);

        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);

        //需要发送多少个用户就add多少个
        notifyModel.setUserIds(userIds);

        //如果全部推送可以传null,或者空的list
        notifyModel.setUserIds(new ArrayList<>());
       return sendNotify(notifyModel);
    }



    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        Notify notify = notifyService.findById(id);
        if (!ShiroUtils.getUserId().equals("1")) {
            return R.ok().put("data", notify);
        }

        if (ShiroUtils.getUserId().equals(notify.getUserId())) {
            //清除消息通知是否已读状态
            if (notify.getIsRead().equals("0")) {
                notify.setIsRead("1");
                notifyService.save(notify);
            }

            return R.ok().put("data", notify);
        }

        return R.error("异常操作");
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Notify notify) {
        notifyService.save(notify);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        //notifyService.deleteById(id);
        sysAdminDao.deleteNotifyById(id);
        return R.ok("删除成功");
    }

    /**
     * 批量删除
     */
    @PostMapping("/batch/delete")
    public R delete(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            //notifyService.deleteById(id);
            sysAdminDao.deleteNotifyById(id);
        }
        return R.ok("删除成功");
    }

    /**
     * 获取当前登录用户是否有消息通知
     */
    @GetMapping("/is/notice")
    public R isNotice() {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", ShiroUtils.getUserId());
        param.put("isRead", "0");
        return R.ok().put("number", notifyService.count(param));
    }

    @PostMapping("/send/notify")
    public R sendNotify(@RequestBody NotifyModel notifyModel) {
        if (notifyModel.isTemp()) {
            Map<String, Object> param = new HashMap<>();
            param.put("isUse", true);
            param.put("id", notifyModel.getNotifyTempId());
            /*if (notifyModel.getNotifyTempId() == null) {
                param.put("id", notifyModel.getNotifyTempId());
            } else {
                param.put("used", notifyModel.getType());
            }*/
            NotifyTemp notifyTemp = notifyTempService.findOne(param);

            if (notifyTemp == null) {
                throw new RRException("没有找到指定的模板");
            }

            sendNotify(notifyTemp, notifyModel);

            return R.ok("消息保存成功");
        }

        sendNotifyNoTemp(notifyModel);

        return R.ok("消息保存成功");
    }

    /**
     * 使用消息模板发送消息
     */
    public void sendNotify(NotifyTemp notifyTemp, NotifyModel notifyModel) {
        //判断消息通知内容
        if (notifyModel.getContents() == null || notifyModel.getContents().size() == 0) {
            throw new RRException("请输入消息内容");
        }
        //获取占位符和消息内容数的差
        int loopContent = notifyTemp.getNum() - notifyModel.getContents().size();
        //将多余的占位符用空字符串代替
        if (loopContent > 0) {
            for (int i = 0; i < loopContent; i++) {
                notifyModel.getContents().add("");
            }
        }
        Notify notify = new Notify();
        notify.setTitle(notifyTemp.getTitle());

        String[] con = new String[notifyModel.getContents().size()];
        for (int i = 0; i < con.length; i++) {
            con[i] = notifyModel.getContents().get(i);
        }
        //模板类容替换
        notify.setContent(String.format(notifyTemp.getContent(), con));

        notify.setType(notifyTemp.getUsed());
        notify.setRemark(notifyModel.getRemark());

        notifyService.send(notify, notifyModel.getUserIds());
    }

    /**
     * 不使用消息模板发送消息
     */
    public void sendNotifyNoTemp(NotifyModel notifyModel) {
        if (notifyModel.getContent() == null || notifyModel.getContent().length() == 0) {
            throw new RRException("请输入消息内容");
        }

        Notify notify = new Notify();
        notify.setTitle(notifyModel.getTitle());
        notify.setContent(notifyModel.getContent());
        notify.setType(notifyModel.getType());
        notify.setRemark(notifyModel.getRemark());

        notifyService.send(notify, notifyModel.getUserIds());
    }

}
