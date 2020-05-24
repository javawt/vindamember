package com.songlanyun.jymall.modules.business.sys.dao;


import com.songlanyun.jymall.modules.business.sys.entity.SysUserAdvice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户意见反馈
 *
 */
@Mapper
public interface SysUserAdviceDao {

    void addUserAdvice(SysUserAdvice sysUserAdvice);

    void updateUserAdvice(SysUserAdvice sysUserAdvice);

    List<Map<String,Object>> selectUserAdvice(SysUserAdvice sysUserAdvice);


}
