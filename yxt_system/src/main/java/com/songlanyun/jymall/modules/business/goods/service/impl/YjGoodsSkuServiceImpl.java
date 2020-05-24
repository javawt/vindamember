package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import com.songlanyun.jymall.modules.business.goods.repository.YjGoodsSkuRepository;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsSkuService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class YjGoodsSkuServiceImpl extends BaseServiceImpl<YjGoodsSku, Integer, YjGoodsSkuRepository> implements YjGoodsSkuService {
    public YjGoodsSkuServiceImpl(YjGoodsSkuRepository repository) {
        super(repository);
    }

    @Override
    public void deleteYjGoodsSkuByGoodsId(Integer goodsId) {
        repository.deleteYjGoodsSkuByGoodsId(goodsId);
    }

    @Override
    public List<YjGoodsSku> selectYjGoodsSkuByGoodsId(Integer goodsId) {
        return this.repository.selectYjGoodsSkuByGoodsId(goodsId);
    }

    @Override
    public List<YjGoodsSku> findAllByGoodsSkuIdIn(List<Integer> goodsIds) {
        return this.repository.findAllByGoodsSkuIdIn(goodsIds);
    }
}
