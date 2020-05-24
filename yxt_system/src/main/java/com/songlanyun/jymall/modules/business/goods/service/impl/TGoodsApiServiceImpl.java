package com.songlanyun.jymall.modules.business.goods.service.impl;


import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.goods.repository.TGoodsApiDao;
import com.songlanyun.jymall.modules.business.goods.service.TGooodsApiService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class TGoodsApiServiceImpl implements TGooodsApiService {

    @Resource
    private TGoodsApiDao tGoodsApiDao;

    @Override
    public R getBarrage() {
        List<Map<String, Object>> resList = tGoodsApiDao.getBarrage();
        List returnList=new ArrayList<>();
        if(null!=resList && resList.size()>0){
            resList.forEach(temp ->{
                if(null!=temp.get("mobile")){
                    if(StringUtils.isNotBlank(temp.get("mobile").toString())){
                        temp.put("mobile",temp.get("mobile").toString().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                        returnList.add(temp.get("mobile")+" 购买了"+temp.get("goodsName"));
                    }
                }

            });
        }
        return R.ok().put("data",returnList);
    }

}
