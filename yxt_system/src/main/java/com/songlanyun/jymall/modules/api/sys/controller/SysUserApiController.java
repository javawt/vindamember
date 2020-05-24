

package com.songlanyun.jymall.modules.api.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.songlanyun.jymall.common.upload.controller.FileController;
import com.songlanyun.jymall.common.utils.*;
import com.songlanyun.jymall.modules.business.jysys.sysuser.service.SysUserLoginInfoService;
import com.songlanyun.jymall.modules.business.sys.controller.AbstractController;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.dao.SysUserDao;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntity;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntityDTO;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import com.songlanyun.jymall.modules.business.sys.service.SysUserRoleService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * 系统用户
 *
 */
@RestController
@RequestMapping("/api/sys/user")
@Slf4j
public class SysUserApiController extends AbstractController {
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
    @Value("${nginx_path}")
    private String nginx_path;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    //第一个用户的邀请码
    @Value("${baseInvitation_code}")
    private String fristInvCode;
    @Resource
    private SysUserDao sysUserDao;

    @Value("${sms.templateupdate}")
    private String templateupdate = "";



    @ApiOperation(value = "注册", notes = "注册新用户", httpMethod = "POST")
    @RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
    @Transactional
    public R register(@RequestBody SysUserEntityDTO _userVo) {
        R resp = R.error(StatusMsgEnum.USER_IS_EXISTS);
        if (StringUtils.isBlank(_userVo.getUsername())) {
            return R.error("手机号不能为空");
        }
        if (StringUtils.isBlank(_userVo.getPassword())) {
            return R.error("密码不能为空");
        }
        /*if (StringUtils.isBlank(_userVo.getPayPsw())) {
            return R.error("支付密码不能为空");
        }*/
        /*if (StringUtils.isBlank(_userVo.getInvitationCode())) {
            return R.error("邀请码不能为空");
        }
        String userInvCode = _userVo.getInvitationCode();*/
        //判断手机号是否已注册
        SysUserLoginInfo userVo = sysUserService.queryByUserName(_userVo.getUsername());
        if (userVo != null) {
            //手机号已被注册
            return resp;
        }
        //检查验证码是否正确或过期---测试期间屏蔽
        int chkRes = sysUserService.checkVerCode(_userVo.getUsername(), _userVo.getVerCode());
        if (chkRes == -1) { //redis 已经没有此phone的对应验证码了，
            return R.error(StatusMsgEnum.SMSCODESEND_TIMEOUT);
        }
        if (chkRes == -2) {  //验证码录入错误
            return R.error(StatusMsgEnum.SMSCODES_NOMATCH);
        }
        if (StringUtils.isNotBlank(_userVo.getInvitationCode())) {
            //从用户表找出此邀请码对应的用户来检查邀请码是否正确
            QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Invitation_code", _userVo.getInvitationCode());
            SysUserEntity invCodeUser = sysUserDao.selectOne(queryWrapper);
            if (invCodeUser != null) {  //根据邀请码找到了人，则将其userId写入
                _userVo.setReferee(invCodeUser.getUserId());
            }
            /*if (invCodeUser == null && (!_userVo.getInvitationCode().equals(fristInvCode))) {
                //人也找不到，录入的邀请码也不是第一个，报错
                return R.error(StatusMsgEnum.INVICTCODE_ERR);
            }*/
        }


        try {
            //保存新进此人  ---手机号生成邀请码
            //String shareCode = ShareCodeUtils.idToCode(Long.parseLong(_userVo.getUsername()));
            //tsh 2019-12-23 生成邀请码
            String shareCode = greateInvitation();
            _userVo.setInvitationCode(shareCode);
            _userVo.setStatus(1);
            _userVo.setMobile(_userVo.getUsername());
            //将用户信息分别保存至sys_user表和sys_user_login_info表
            SysUserEntity adSysUserEntity = new SysUserEntity();
            adSysUserEntity.setUserId(UUIDUtil.getUUID());
            adSysUserEntity.setEmail(_userVo.getEmail());
            adSysUserEntity.setMobile(_userVo.getMobile());
            adSysUserEntity.setCreateUserId(_userVo.getCreateUserId());
            adSysUserEntity.setCreateTime(new Date());
            adSysUserEntity.setGender(_userVo.getGender());
            adSysUserEntity.setCountry(_userVo.getCountry());
            adSysUserEntity.setProvince(_userVo.getProvince());
            adSysUserEntity.setCity(_userVo.getCity());
            adSysUserEntity.setBalance(_userVo.getBalance());
            adSysUserEntity.setIsDelete(_userVo.getIsDelete());
            adSysUserEntity.setUpdateTime(_userVo.getUpdateTime());
            adSysUserEntity.setInvitationCode(_userVo.getInvitationCode());
            adSysUserEntity.setAvatarUrl(_userVo.getAvatarUrl());
            adSysUserEntity.setNickName(_userVo.getNickName());
            adSysUserEntity.setReferee(_userVo.getReferee());
            sysUserService.saveUser(adSysUserEntity);
            SysUserLoginInfo adSysUserLoginInfo = new SysUserLoginInfo();
            adSysUserLoginInfo.setUserId(adSysUserEntity.getUserId());
            adSysUserLoginInfo.setUsername(_userVo.getUsername());
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            adSysUserLoginInfo.setPassword(new Sha256Hash(_userVo.getPassword(), salt).toHex());
            adSysUserLoginInfo.setSalt(salt);
            adSysUserLoginInfo.setStatus(_userVo.getStatus());
            if (StringUtils.isNotBlank(_userVo.getPayPsw())) {
                adSysUserLoginInfo.setPayPsw(new Sha256Hash(_userVo.getPayPsw(), salt).toHex());
            }
            adSysUserLoginInfo.setUserType(0);
            adSysUserLoginInfo.setIsMember(0);
            adSysUserLoginInfo.setIsInfluencer(0);
            sysUserLoginInfoService.save(adSysUserLoginInfo);
            resp = R.ok("用户注册成功！");
        } catch (Exception err) {
            resp = R.error(StatusMsgEnum.NOCAN_REGISTER);
        }
        return resp;
    }

    /**
     * 上传商品图片
     */
    @PostMapping("/modify/uploadGoodsPic")
    public R uploadGoodsPic(@RequestParam MultipartFile file) {
        //fileController.savePicture(file,path);
        return R.ok().put("data", fileController.savePictures(file));
    }

    @PostMapping("/modify/uploadPic")
    public R uploadPic(@RequestParam MultipartFile file) {
        //fileController.savePicture(file,path);
        return R.ok().put("data", fileController.uploadPic(file));
    }


    /**
     * 上传idcard
     */
    @PostMapping("/modify/upLoadIdcard")
    public R upLoadIdcard(@RequestParam MultipartFile file) {
        return R.ok().put("data", fileController.upLoadIdcard(file));
    }

    /**
     * 上传idcard
     */
    @PostMapping("/modify/upLoadExpressLogo")
    public R upLoadExpressLogo(@RequestParam MultipartFile file) {
        return R.ok().put("data", fileController.upLoadExpressLogo(file));
    }


    //递归生成要求的六位邀请码，比对无重复方可
    public String greateInvitation() {
        String invCode = ShareCodeUtils.createInvitation();
        if (sysUserService.isExistInvition(invCode) > 0) {
            //用户邀请码重复
            greateInvitation();
        }
        return invCode;
    }

    @ApiOperation(value = "获得验证码", notes = "根据电话号码获得验证码", httpMethod = "POST")
    @RequestMapping(value = "/getVerfityCode", method = {RequestMethod.POST, RequestMethod.GET})
    public R getVerfityCode(@RequestBody String phone) throws Exception {
        /*if (StringUtils.isNotBlank(phone)) {
            phone=phone.replaceAll("phone:","");
        }*/
        //判断手机号是否注册
        /*int num= sysAdminDao.getUserIdByUserName(phone);
        if(num<=0){
            return R.error("手机号："+phone+",未注册！");
        }*/
        String smsCode = getVerfityCodeService(phone);
        System.out.println("手机验证码----------------"+smsCode);
        R resp;
        /*if (StringUtils.isEmpty(smsCode)) {
            resp = R.error(StatusMsgEnum.SMSCODE_SENDED_FAIL);
            return resp;
        }*/

        String content = template.replace("%1", smsCode + "");

        if (smsSend.send(phone, content)) {
            resp = R.ok(StatusMsgEnum.SUCCESS.getMsg());
        } else {
            resp = R.error(StatusMsgEnum.SMSCODESEND_FAIL);
        }
        return resp;

    }

    @ApiOperation(value = "获得找回验证码", notes = "根据电话号码获得验证码", httpMethod = "POST")
    @RequestMapping(value = "/getVerfityCodes", method = {RequestMethod.POST, RequestMethod.GET})
    public R getVerfityCodes(@RequestBody String phone) throws Exception {
        /*if (StringUtils.isNotBlank(phone)) {
            phone=phone.replaceAll("phone:","");
        }*/
        int num= sysAdminDao.getUserIdByUserName(phone);
        if(num<=0){
            return R.error("手机号："+phone+",未注册！");
        }
        String smsCode = getVerfityCodeService(phone);
        R resp;
        /*if (StringUtils.isEmpty(smsCode)) {
            resp = R.error(StatusMsgEnum.SMSCODE_SENDED_FAIL);
            return resp;
        }*/

        String content = templateupdate.replace("@", smsCode + "");

        if (smsSend.send(phone, content)) {
            resp = R.ok(StatusMsgEnum.SUCCESS.getMsg());
        } else {
            resp = R.error(StatusMsgEnum.SMSCODESEND_FAIL);
        }
        return resp;

    }

    /**
     * 得到并发送短信验证码
     **/
    public String getVerfityCodeService(String phone) {
        String stringValue = (String) redisTemplate.opsForValue().get(RedisKeys.VERFITY_CODE + phone);
        String code = "";
        //reids 中不存在 此电话对应验证码证明已经过了 verCodeTm 秒，则可以重新生成
        code = ShareCodeUtils.smsCode();
        redisTemplate.opsForValue().set(RedisKeys.VERFITY_CODE + phone,code,verCodeTm, TimeUnit.SECONDS);
        return code;
        /*if (stringValue == null) {
            code = ShareCodeUtils.smsCode();
            redisTemplate.opsForValue().set(RedisKeys.VERFITY_CODE + phone,code,verCodeTm,TimeUnit.SECONDS);
            return code;
        }
        return ""; */

    }

    /**
     * 上传文件到指定路径的Nginx
     */
    @RequestMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 判断文件是否为空
        String path="yxtPic";
        if (!file.isEmpty()) {
            try {
                Long time = System.currentTimeMillis();
                File directory = new File("");
                String basePath = nginx_path+ "/";
                basePath = basePath.replaceAll("\\\\", "/");

                String os = System.getProperty("os.name");
                if (!os.toLowerCase().startsWith("win")) {
                    basePath = "/" + basePath;
                }

                File filepath = new File(basePath + path);

                if (!filepath.exists()) {
                    filepath.mkdirs();
                }
                // 文件保存路径
                String savePath = basePath + path + "/" + time + "_" + file.getOriginalFilename();
                // 转存文件
                file.transferTo(new File(savePath));
                return path + "/" + time + "_" + file.getOriginalFilename();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";

    }
}
