package com.songlanyun.jymall.modules.shop.service.impl;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopClerkInfo;
import com.songlanyun.jymall.modules.shop.dao.TbShopClerkInfoDao;
import com.songlanyun.jymall.modules.shop.service.TbShopClerkInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺店员信息表(TbShopClerkInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-05-22 16:17:31
 */
@Service("tbShopClerkInfoService")
public class TbShopClerkInfoServiceImpl implements TbShopClerkInfoService {
    @Resource
    private TbShopClerkInfoDao tbShopClerkInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TbShopClerkInfo queryById(Long id) {
        return this.tbShopClerkInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<TbShopClerkInfo> queryAllByLimit(int offset, int limit) {
        return this.tbShopClerkInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tbShopClerkInfo 实例对象
     * @return 实例对象
     */
    @Override
    public TbShopClerkInfo insert(TbShopClerkInfo tbShopClerkInfo) {
        this.tbShopClerkInfoDao.insert(tbShopClerkInfo);
        return tbShopClerkInfo;
    }

    /**
     * 修改数据
     *
     * @param tbShopClerkInfo 实例对象
     * @return 实例对象
     */
    @Override
    public R update(TbShopClerkInfo tbShopClerkInfo) {
        R r =new R();
        this.tbShopClerkInfoDao.update(tbShopClerkInfo);
        return r.put("data",this.queryById(tbShopClerkInfo.getId()));
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public R deleteById(Long id) {
        R r = new R();
        int num=this.tbShopClerkInfoDao.deleteById(id);
        if(num > 0){
            return r;
        }else{
            return r.error(500,"删除失败,记录不存在！");
        }
    }

    @Override
    public R select(TbShopClerkInfo TbShopClerkInfo) {
        R r =new R();
        if(TbShopClerkInfo.getPage()>0){
        }else{
            return r.error(400,"page页码参数输入错误");
        }
        if(TbShopClerkInfo.getSize()>=0){
        }else{
            return r.error(400,"size页数参数输入错误");
        }
        List<TbShopClerkInfo> resultList = tbShopClerkInfoDao.select(TbShopClerkInfo);
        if(null!=resultList && resultList.size()>0){
            r.put("total", resultList.size());
            r.put("data", resultList.stream().skip((TbShopClerkInfo.getPage() - 1) * TbShopClerkInfo.getSize()).
                    limit(TbShopClerkInfo.getSize()).collect(Collectors.toList()));
            r.put("totalPages", (resultList.size()-1)/TbShopClerkInfo.getSize()+1);
        }else{
            r.put("total", 0);
            r.put("data",  new ArrayList<>());
            r.put("totalPages", 0);
        }
        return r;
    }

    @Override
    public R save(TbShopClerkInfo TbShopClerkInfo) {
        R r = new R();
        try{
            this.tbShopClerkInfoDao.insert(TbShopClerkInfo);
            return r;
        }catch (Exception e){
            return r.error(500,"保存失败");
        }
    }
}