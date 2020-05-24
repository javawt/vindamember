package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.YjCategory;
import com.songlanyun.jymall.modules.business.goods.repository.YjCategoryRepository;
import com.songlanyun.jymall.modules.business.goods.service.YjCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YjCategoryServiceImpl extends BaseServiceImpl<YjCategory, Integer, YjCategoryRepository> implements YjCategoryService {
    public YjCategoryServiceImpl(YjCategoryRepository repository) {
        super(repository);
    }

    @Override
    public List<YjCategory> findAllByParentId(Integer parentId) {
        return this.repository.findAllByParentId(parentId);
    }
}
