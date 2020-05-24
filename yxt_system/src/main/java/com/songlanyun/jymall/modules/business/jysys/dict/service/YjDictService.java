package com.songlanyun.jymall.modules.business.jysys.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.modules.business.jysys.dict.entity.YjDictEntity;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * 商城字典表
 *
 * @author zhaoyong
 * @email zy765203718@gmail.com
 * @date 2019-11-14 10:26:30
 */
public interface YjDictService extends IService<YjDictEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /** 字典表相关  **/
    void del_dict(int key);

    /** 得到列表  **/
    List<YjDictEntity> getDictList();

    /** 得到某类型的字典表数据  **/
    public List<YjDictEntity> getDictListByType(int type);

    /**
     * 字典表是否有同key,type值
     */
    public int getDictIsExist(int key, int type);

    /** 此type下同名的value是否存在 **/
    public int getDictValueIsExist(int key, String value);

    @Cacheable( value="sly:yjshop:dict",keyGenerator = "keyGenerator")
    String getDictValue(int dtype, int dkey);
}

