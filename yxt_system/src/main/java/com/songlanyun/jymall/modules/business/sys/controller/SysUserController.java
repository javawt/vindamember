

package com.songlanyun.jymall.modules.business.sys.controller;

import com.songlanyun.jymall.common.annotation.SysLog;
import com.songlanyun.jymall.common.upload.controller.FileController;
import com.songlanyun.jymall.common.utils.*;
import com.songlanyun.jymall.common.validator.Assert;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.common.validator.group.AddGroup;
import com.songlanyun.jymall.common.validator.group.UpdateGroup;
import com.songlanyun.jymall.modules.business.jysys.sysuser.service.SysUserLoginInfoService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.dao.SysUserDao;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntity;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntityDTO;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import com.songlanyun.jymall.modules.business.sys.form.PasswordForm;
import com.songlanyun.jymall.modules.business.sys.service.SysUserRoleService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 */
@RestController
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private FileController fileController;
    @Resource
    private SysAdminDao sysAdminDao;
    @Resource
    private SysUserLoginInfoService sysUserLoginInfoService;
    // 短信模板
    @Value("${sms.template}")
    private String template = "";
    @Autowired
    private Send smsSend;
    @Value("${redis.verCodeTm}")
    private int verCodeTm;  //验证码过期时间
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    //第一个用户的邀请码
    @Value("${baseInvitation_code}")
    private String fristInvCode;
    @Resource
    private SysUserDao sysUserDao;

    /**
     * 所有用户列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        //tsh   2019/12/20  放开用户查看权限
        /*if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }*/
        //tsh  2019.12.21

        PageUtils page = sysUserService.queryPage(params);
        List<SysUserEntity> list = (List<SysUserEntity>) page.getList();
        if (null != list && list.size() > 0) {
            list.forEach(l -> {
                l.setReName(sysAdminDao.getUserNameById(l.getReferee()));
            });
        }
        page.setList(list);
        return R.ok().put("page", page);
    }

    /**
     * 获取登录的用户信息
     */
    @GetMapping("/info")
    public R info() {
        return R.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    public R password(@RequestBody PasswordForm form) {
        Assert.isBlank(form.getNewPassword(), "新密码不为能空");

        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
        SysUserEntityDTO sysUserEntityDTO = getUser();
        if(!sysUserEntityDTO.getPassword().equals(password)){
            return R.error("原密码不正确");
        }
        //更新密码
        sysUserService.updatePassword(getUserId(), newPassword);

        return R.ok();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId}")
    //@RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") String userId) {
        SysUserEntity user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<Integer> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "POST")
    @RequestMapping(value = "/userExist", method = {RequestMethod.POST})
    public R userExist(@RequestBody String name) {
        SysUserLoginInfo userVo = sysUserService.queryByUserName(name);
        R resp = R.error(StatusMsgEnum.USER_IS_EXISTS1);
        if (userVo != null)
            resp = R.error(StatusMsgEnum.CAN_REGISTER);
        return resp;
    }


    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @Transactional
    public R save(@RequestBody SysUserEntityDTO user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);
        user.setCreateUserId(getUserId());
        //校验用户名和手机号不可重复
        int a = sysAdminDao.getCountUserByUsername(user.getUsername());
        if (a > 0) {
            return R.error("用户名已存在");
        }
        int b = sysAdminDao.getCountUserByMobile(user.getMobile());
        if (b > 0) {
            return R.error("手机号已存在");
        }
        try {
            //将用户信息分别保存至sys_user表和sys_user_login_info表
            SysUserEntity adSysUserEntity = new SysUserEntity();
            adSysUserEntity.setUserId(UUIDUtil.getUUID());
            adSysUserEntity.setEmail(user.getEmail());
            adSysUserEntity.setMobile(user.getMobile());
            adSysUserEntity.setCreateUserId(user.getCreateUserId());
            adSysUserEntity.setCreateTime(new Date());
            adSysUserEntity.setGender(user.getGender());
            adSysUserEntity.setCountry(user.getCountry());
            adSysUserEntity.setProvince(user.getProvince());
            adSysUserEntity.setCity(user.getCity());
            adSysUserEntity.setBalance(user.getBalance());
            adSysUserEntity.setIsDelete(user.getIsDelete());
            adSysUserEntity.setUpdateTime(user.getUpdateTime());
            adSysUserEntity.setInvitationCode(user.getInvitationCode());
            adSysUserEntity.setAvatarUrl(user.getAvatarUrl());
            adSysUserEntity.setNickName(user.getNickName());
            adSysUserEntity.setReferee(user.getReferee());
            sysUserService.saveUser(adSysUserEntity);
            SysUserLoginInfo adSysUserLoginInfo = new SysUserLoginInfo();
            adSysUserLoginInfo.setUserId(adSysUserEntity.getUserId());
            adSysUserLoginInfo.setUsername(user.getUsername());
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            adSysUserLoginInfo.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
            adSysUserLoginInfo.setSalt(salt);
            adSysUserLoginInfo.setStatus(user.getStatus());
            if (StringUtils.isNotBlank(user.getPayPsw())) {
                adSysUserLoginInfo.setPayPsw(new Sha256Hash(user.getPayPsw(), salt).toHex());
            }
            adSysUserLoginInfo.setUserType(1);
            adSysUserLoginInfo.setIsMember(0);
            adSysUserLoginInfo.setIsInfluencer(0);
            sysUserLoginInfoService.save(adSysUserLoginInfo);
            return R.ok();
        } catch (Exception e) {
            log.error("新增用户失败" + e.getMessage());
            return R.error("新增用户失败");

        }
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    //@RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.update(user);

        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @PostMapping("/delete")
    //@RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return R.ok();
    }

    @RequestMapping("/getUserBalance/{userId}")
    public R getUserBalance(@PathVariable("userId") String userId) {
        return sysUserService.getUserBalance(userId);
    }

    /**
     * 修改用户头像
     */
    @PostMapping("/modify/avatar")
    public R modifyAvatar(@RequestParam MultipartFile file) {
        String imgUrl = (String) fileController.savePicture(file).get("data");
        SysUserEntityDTO user = ShiroUtils.getUserEntity();
        user.setAvatarUrl(imgUrl);
        sysUserDao.updateUserAvatarurl(user);
        return R.ok("头像修改成功").put("data", user);
    }

    /**
     * 获取所有用户的列表
     */
    /*@GetMapping("/all/user")
    public R allUser() {
        List<SysUserEntity> list = sysUserService.list().stream().map(l -> {
            SysUserEntity userEntity = new SysUserEntity();
            userEntity.setUserId(l.getUserId());
            userEntity.setUsername(l.getUsername());
            userEntity.setNickName(l.getNickName());
            userEntity.setReferee(l.getReferee());
            userEntity.setGradeId(l.getGradeId());
            return userEntity;
        }).collect(Collectors.toList());

        return R.ok().put("data", list);
    }*/

    @RequestMapping("/updateNickName")
    public R updateNickName(@RequestBody SysUserEntity sysUserEntity) {
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("userId", ShiroUtils.getUserId());
        parMap.put("nickName", sysUserEntity.getNickName());
        return R.ok().put("data", sysUserService.updateNickName(parMap));
    }
    @RequestMapping("/updateGender")
    public R updateGender(@RequestBody SysUserEntity sysUserEntity) {
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("userId", ShiroUtils.getUserId());
        parMap.put("gender", sysUserEntity.getGender());
        return R.ok().put("data", sysUserService.updateGender(parMap));
    }

    @RequestMapping("/updateShortInfo")
    public R updateShortInfo(@RequestBody SysUserEntity sysUserEntity){
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("userId", ShiroUtils.getUserId());
        parMap.put("shortInfo", sysUserEntity.getShortInfo());
        return R.ok().put("data", sysUserService.updateShortInfo(parMap));
    }

    /**
     * 上传apk
     */
    @PostMapping("/modify/upLoadApk")
    public R upLoadApk(@RequestParam MultipartFile file) {
        return R.ok().put("data", fileController.upLoadApk(file));
    }

}
