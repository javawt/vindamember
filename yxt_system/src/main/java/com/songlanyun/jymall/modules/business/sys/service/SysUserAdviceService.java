package com.songlanyun.jymall.modules.business.sys.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserAdvice;

public interface SysUserAdviceService {

    void addUserAdvice(SysUserAdvice sysUserAdvice);

    void updateUserAdvice(SysUserAdvice sysUserAdvice);

    R selectUserAdvice(SysUserAdvice sysUserAdvice);
}
