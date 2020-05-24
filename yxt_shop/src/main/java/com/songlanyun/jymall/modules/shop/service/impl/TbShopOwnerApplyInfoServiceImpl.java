package com.songlanyun.jymall.modules.shop.service.impl;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.shop.dao.TbShopOwnerApplyInfoDao;
import com.songlanyun.jymall.modules.shop.entity.TbShopInfo;
import com.songlanyun.jymall.modules.shop.entity.TbShopOwnerApplyInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopInfoService;
import com.songlanyun.jymall.modules.shop.service.TbShopOwnerApplyInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺申请表(TbShopOwnerApplyInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-05-11 10:57:14
 */
@Service("tbShopOwnerApplyInfoService")
public class TbShopOwnerApplyInfoServiceImpl implements TbShopOwnerApplyInfoService {
    @Resource
    private TbShopOwnerApplyInfoDao tbShopOwnerApplyInfoDao;
    @Resource
    private TbShopInfoService tbShopInfoService;



    @Override
    public void save(TbShopOwnerApplyInfo tbShopOwnerApplyInfo) {
        tbShopOwnerApplyInfo.setUserId(ShiroUtils.getUserId()); //添加申请店铺的user_id
        tbShopOwnerApplyInfoDao.save(tbShopOwnerApplyInfo);
    }

    @Override
    public TbShopOwnerApplyInfo selectNewRecode() {
        return tbShopOwnerApplyInfoDao.selectNewRecode(ShiroUtils.getUserId());
    }

    /**
     * 审核通过则向店铺信息表插入数据，未通过则更新审核的申请记录信息
     * @param tbShopOwnerApplyInfo
     */
    @Override
    @Transactional
    public R checkApply(TbShopOwnerApplyInfo tbShopOwnerApplyInfo) {
        //checkStatus： 1 未审核 2 通过 3 不通过
        R r=new R();
        TbShopOwnerApplyInfo tbShopOwnerApplyInfos= tbShopOwnerApplyInfoDao.getShopOwnerApplyInfo(tbShopOwnerApplyInfo.getId());
        if(null!=tbShopOwnerApplyInfos){
            if(tbShopOwnerApplyInfos.getCheckStatus()!=1){
                return r.error(101,"该记录已审核,请勿重复审核！");
            }
        }
        tbShopOwnerApplyInfo.setCheckId(ShiroUtils.getUserId());
        if(tbShopOwnerApplyInfo.getCheckStatus()==2){
            //审核通过
            tbShopOwnerApplyInfoDao.checkApply(tbShopOwnerApplyInfo);
            //保存店铺信息
            TbShopInfo tbShopInfo = new TbShopInfo();
            tbShopInfo.setUserId(tbShopOwnerApplyInfos.getUserId());
            tbShopInfo.setShopName(tbShopOwnerApplyInfos.getShopName());
            tbShopInfo.setShortInfo(tbShopOwnerApplyInfos.getShortInfo());
            tbShopInfo.setShopAvatar(tbShopOwnerApplyInfos.getHeadUrl());
            tbShopInfo.setProvince(tbShopOwnerApplyInfos.getProvince());
            tbShopInfo.setCity(tbShopOwnerApplyInfos.getCity());
            tbShopInfo.setArea(tbShopOwnerApplyInfos.getArea());
            tbShopInfo.setAddress(tbShopOwnerApplyInfos.getAddress());
            tbShopInfo.setContact(tbShopOwnerApplyInfos.getServerContact());
            tbShopInfo.setApplyInfoId(tbShopOwnerApplyInfos.getId());
            tbShopInfoService.saveInfo(tbShopInfo);
        }else if(tbShopOwnerApplyInfo.getCheckStatus()==3){
            //审核不通过
            tbShopOwnerApplyInfoDao.checkApply(tbShopOwnerApplyInfo);
        }
        return  r ;
    }

    /**
     * 后台审核信息列表（按申请时间倒序）
     * 搜索条件：申请时间、申请姓名（模糊）、审核状态
     *
     */
    @Override
    public R selectApplyList(TbShopOwnerApplyInfo tbShopOwnerApplyInfo) {
        R r =new R();
        if(tbShopOwnerApplyInfo.getPage()>0){
        }else{
            return r.error(400,"page页码参数输入错误");
        }
        if(tbShopOwnerApplyInfo.getSize()>=0){
        }else{
            return r.error(400,"size页数参数输入错误");
        }
        List<TbShopOwnerApplyInfo> resultList = tbShopOwnerApplyInfoDao.selectApplyList(tbShopOwnerApplyInfo);
        if(null!=resultList && resultList.size()>0){
            r.put("total", resultList.size());
            r.put("data", resultList.stream().skip((tbShopOwnerApplyInfo.getPage() - 1) * tbShopOwnerApplyInfo.getSize()).
                    limit(tbShopOwnerApplyInfo.getSize()).collect(Collectors.toList()));
            r.put("totalPages", (resultList.size()-1)/tbShopOwnerApplyInfo.getSize()+1);
        }else{
            r.put("total", 0);
            r.put("data",  new ArrayList<>());
            r.put("totalPages", 0);
        }
        return r;
    }
}