package com.songlanyun.jymall.modules.business.sys.service.impl;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.sys.dao.SysUserAdviceDao;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserAdvice;
import com.songlanyun.jymall.modules.business.sys.service.SysUserAdviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysUserAdviceServiceImpl implements SysUserAdviceService {

    @Resource
    private SysUserAdviceDao sysUserAdviceDao;


    @Override
    public void addUserAdvice(SysUserAdvice sysUserAdvice) {
        sysUserAdvice.setCreateBy(ShiroUtils.getUserId());//创建人id
        sysUserAdviceDao.addUserAdvice(sysUserAdvice);
    }
    @Override
    public void updateUserAdvice(SysUserAdvice sysUserAdvice) {
        sysUserAdviceDao.updateUserAdvice(sysUserAdvice);
    }

    @Override
    public R selectUserAdvice(SysUserAdvice sysUserAdvice) {
        //入参校验
        if (sysUserAdvice.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (sysUserAdvice.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String,Object>> resList = sysUserAdviceDao.selectUserAdvice(sysUserAdvice);
        resMap.put("page", sysUserAdvice.getPage());
        resMap.put("size", sysUserAdvice.getSize());
        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((sysUserAdvice.getPage() - 1) * sysUserAdvice.getSize()).
                    limit(sysUserAdvice.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }
}
