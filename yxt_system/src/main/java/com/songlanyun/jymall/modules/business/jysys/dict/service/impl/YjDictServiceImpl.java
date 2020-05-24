package com.songlanyun.jymall.modules.business.jysys.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.Query;
import com.songlanyun.jymall.modules.business.jysys.dict.dao.YjDictDao;
import com.songlanyun.jymall.modules.business.jysys.dict.entity.YjDictEntity;
import com.songlanyun.jymall.modules.business.jysys.dict.service.YjDictService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("yjDictService")
public class YjDictServiceImpl extends ServiceImpl<YjDictDao, YjDictEntity> implements YjDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<YjDictEntity> page = this.page(
                new Query<YjDictEntity>().getPage(params),
                new QueryWrapper<YjDictEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    //@CacheEvict(value="sly:yjshop:dict",allEntries=true)
    public void del_dict(int key) {
        QueryWrapper<YjDictEntity> removeQueryWrapper = new QueryWrapper<>();
        removeQueryWrapper.eq("dkey",key);
        this.baseMapper.delete(removeQueryWrapper);
    }


    @Override
    //@Cacheable( value="sly:yjshop:dict")
    public List<YjDictEntity> getDictList() {
        return this.baseMapper.selectList(null);
    }

    @Override
    //@Cacheable( value="sly:yjshop:dict",keyGenerator = "keyGenerator")
    public List<YjDictEntity> getDictListByType(int type) {
        QueryWrapper<YjDictEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dtype", type);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    //@Cacheable( value="sly:yjshop:dict",keyGenerator = "keyGenerator")
    public int getDictIsExist(int key, int type) {
        QueryWrapper<YjDictEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dkey", key);
        queryWrapper.eq("dtype", type);
        Integer count = this.baseMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    //@Cacheable( value="sly:yjshop:dict",keyGenerator = "keyGenerator")
    public int getDictValueIsExist(int key, String value) {
        QueryWrapper<YjDictEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dkey", key);
        queryWrapper.eq("dvalue", value);
        Integer count = this.baseMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    //@Cacheable( value="sly:yjshop:dict",keyGenerator = "keyGenerator")
    public String getDictValue(int dtype,int dkey) {
        QueryWrapper<YjDictEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dkey", dkey);
        queryWrapper.eq("dtype", dtype);
        String value = this.baseMapper.selectOne(queryWrapper).getDvalue();
        return value;
    }

}
