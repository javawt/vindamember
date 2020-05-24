package com.songlanyun.jymall.modules.shop.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.common.utils.StatusMsgEnum;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import com.songlanyun.jymall.modules.shop.entity.TbShopOwnerApplyInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopInfoService;
import com.songlanyun.jymall.modules.shop.service.TbShopOwnerApplyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 店铺申请表(TbShopOwnerApplyInfo)表控制层
 *
 * @author makejava
 * @since 2020-05-11 10:57:15
 */
@RestController
@RequestMapping("/tbShopOwnerApplyInfo")
public class TbShopOwnerApplyInfoController {
    /**
     * 服务对象
     */
    @Resource
    private TbShopOwnerApplyInfoService tbShopOwnerApplyInfoService;

    @Resource
    private TbShopInfoService tbShopInfoService;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 保存店铺申请信息接口
     * 需要是平台的注册用户方可申请
     */
    @PostMapping("/save")
    public R saveShopOwnerApply(@RequestBody TbShopOwnerApplyInfo tbShopOwnerApplyInfo) {
        R r = new R();
        //判断手机号验证码是否正确
        int chkRes = sysUserService.checkVerCode(tbShopOwnerApplyInfo.getPhone(), tbShopOwnerApplyInfo.getVerCode());
        if (chkRes == -1) { //redis 已经没有此phone的对应验证码了，
            return R.error(StatusMsgEnum.SMSCODESEND_TIMEOUT);
        }
        if (chkRes == -2) {  //验证码录入错误
            return R.error(StatusMsgEnum.SMSCODES_NOMATCH);
        }
        //判断是否已存在店铺，一个用户只能有一个店铺
        int num = tbShopInfoService.selectCount(ShiroUtils.getUserId());
        if(num>0){
            return r.error(212,"您已有店铺，请勿重复申请！");
        }
        this.tbShopOwnerApplyInfoService.save(tbShopOwnerApplyInfo);
        return r;
    }

    /**
     * 查询用户上次申请的记录信息
     */
    @PostMapping("/selectNewRecode")
    public R selectNewRecode() {
        R r = new R();
        return r.put("data",this.tbShopOwnerApplyInfoService.selectNewRecode());
    }

    /**
     * 后台审核店铺信息
     * checkStatus  checkReason id
     */
    @PostMapping("/checkApply")
    public R checkApply(@RequestBody TbShopOwnerApplyInfo tbShopOwnerApplyInfo) {
        return this.tbShopOwnerApplyInfoService.checkApply(tbShopOwnerApplyInfo);
    }

    /**
     * 后台审核信息列表（按申请时间倒序）
     * 搜索条件：申请时间、申请姓名（模糊）、审核状态
     * int 给0默认为null
     */
    @PostMapping("/selectApplyList")
    public R selectApplyList(@RequestBody TbShopOwnerApplyInfo tbShopOwnerApplyInfo) {
        return this.tbShopOwnerApplyInfoService.selectApplyList(tbShopOwnerApplyInfo);
    }

}