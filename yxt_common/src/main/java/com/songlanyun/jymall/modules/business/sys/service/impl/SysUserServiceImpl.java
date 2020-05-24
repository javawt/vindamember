/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.*;
import com.songlanyun.jymall.modules.business.sys.dao.SysUserDao;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntity;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntityDTO;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import com.songlanyun.jymall.modules.business.sys.service.SysRoleService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserRoleService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {


    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;


    /** 检查注册时的验证码是否正确  **/
    @Override
    public int checkVerCode(String phone,String vcode){
        String code = (String) redisTemplate.opsForValue().get(RedisKeys.VERFITY_CODE + phone);

        if (code==null)
            return -1;
        if (code.equals(vcode))
            return 0;
        else
            return  -2;
    }

    @Override
    public SysUserEntityDTO updateNickName(Map<String ,Object> parMap) {
        //更新 ，查找
        baseMapper.updateNickName(parMap);
        return baseMapper.getUserById(parMap.get("userId").toString());
    }

    @Override
    public SysUserEntityDTO updateGender(Map<String, Object> parMap) {
        //更新 ，查找
        baseMapper.updateGender(parMap);
        return baseMapper.getUserById(parMap.get("userId").toString());
    }

    @Override
    public SysUserEntityDTO updateShortInfo(Map<String ,Object> parMap) {
        //更新 ，查找
        baseMapper.updateShortInfo(parMap);
        return baseMapper.getUserById(parMap.get("userId").toString());
    }

    @Override
    public Integer isExistInvition(String iCode) {
        return baseMapper.isExistInvition(iCode);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");
        Long createUserId = (Long) params.get("createUserId");
        String nickName = (String) params.get("nickName");
        String gradeId = (String) params.get("gradeId");

        IPage<SysUserEntity> page = this.page(
                new Query<SysUserEntity>().getPage(params),
                new QueryWrapper<SysUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .like(StringUtils.isNotBlank(nickName), "nick_name", nickName)
                        .eq(StringUtils.isNotBlank(gradeId), "grade_id", gradeId)
                        .eq(createUserId != null, "create_user_id", createUserId)
                        .orderByDesc(true,"create_time")
        );

        return new PageUtils(page);
    }

    @Override
    public List<String> queryAllPerms(String userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(String userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    public SysUserLoginInfo queryByUserName(String username) {
        return baseMapper.queryByUserName(username);
    }

    @Override
    @Transactional
    public void saveUser(SysUserEntity user) {
        sysUserDao.saveUser(user);
        //检查角色是否越权
        checkRole(user);
        //保存用户与角色关系

        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void saveAPPUser(SysUserEntity user) {
        sysUserDao.saveUser(user);
        //检查角色是否越权
        checkRole(user);
        //保存用户与角色关系

        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void update(SysUserEntity user) {
        /*if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }

        if (StringUtils.isBlank(user.getPayPsw())) {
            user.setPayPsw(null);
        } else {
            user.setPayPsw(new Sha256Hash(user.getPayPsw(), user.getSalt()).toHex());
        }*/

        this.updateById(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        //sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    public void deleteBatch(Long[] userId) {
        this.removeByIds(Arrays.asList(userId));
    }

    @Override
    public void updatePassword(String userId,String newPassword) {
        Map<String,Object> parMap=new HashMap<>();
        parMap.put("userId",userId);
        parMap.put("newPassword",newPassword);
        baseMapper.updateUserPassword(parMap);
    }

    @Override
    public void updateCoupon(Map map) {
        baseMapper.updateCoupon(map);
    }

    @Override
    public Double getCoupon(String userId) {
        return baseMapper.getCoupon(userId);
    }

    @Override
    public String getUserName(String userId) {
        return baseMapper.getUserName(userId);
    }


    @Override
    public SysUserEntityDTO getUserById(String userId) {
        return baseMapper.getUserById(userId);
    }

    @Override
    public void updateUserBalance(SysUserEntity sysUserEntity) {
        baseMapper.updateUserBalance(sysUserEntity);
    }

    @Override
    public void updateUserGrade(SysUserEntity sysUserEntity) {
        baseMapper.updateUserGrade(sysUserEntity);
    }

    @Override
    public Integer getUserCount() {
        return baseMapper.getUserCount();
    }

    @Override
    public Integer getTodayAddCount() {
        return baseMapper.getTodayAddCount();
    }

    @Override
    public R getUserBalance(String userId) {
        SysUserEntityDTO sysUserEntity= getUserById(userId);
        Map<String , Object> resMap=new HashMap<>();
        resMap.put("balance",baseMapper.getUserBalance(userId));
        resMap.put("cashMoney",baseMapper.getUserCashMoney(userId));
        resMap.put("coupon",baseMapper.getCoupon(userId));
        return R.ok().put("data",resMap);
    }

    @Override
    public SysUserEntity getUserByMobile(String mobile) {
        return baseMapper.getUserByMobile(mobile);
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUserEntity user) {
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId().equals(Constant.SUPER_ADMIN)) {
            return;
        }

        //查询用户创建的角色列表
        List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

        //判断是否越权
        if (!roleIdList.containsAll(user.getRoleIdList())) {
            throw new RRException("新增用户所选角色，不是本人创建");
        }
    }


}