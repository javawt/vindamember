package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.common.JpaUtils.SpecificationUtil;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;
import com.songlanyun.jymall.modules.business.goods.repository.YjGoodsRepository;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("yjGoodsServiceJpa")
public class YjGoodsServiceImpl extends BaseServiceImpl<YjGoods, Integer, YjGoodsRepository> implements YjGoodsService {

    public YjGoodsServiceImpl(YjGoodsRepository repository) {
        super(repository);
    }

    @Override
    public List<YjGoods> findAllByGoodsIdIn(Integer[] ids) {
        return this.repository.findAllByGoodsIdIn(ids);
    }

    @Override
    public void updateByActivityId(Integer activityId) {
        this.repository.updateByActivityId(activityId);
    }

    public YjGoods saveGoods(YjGoods _goodsVo) {
        YjGoods goodsVo = this.repository.saveAndFlush(_goodsVo);
        return goodsVo;
    }

    @Override
    public void updateGoodsType(Integer goodsId, String goodsType) {
        repository.updateGoodsType(goodsId, goodsType);
    }

    @Cacheable(value = "activityGoods", key = "#p0 + '' + #p1 + '' + #p2")
    @Override
    public PageUtils findActivityGoods(int activityId, int page, int size) {
        Map<String, Object> findActivityGoods = new HashMap<>();
        findActivityGoods.put("hasActivity", true);
        findActivityGoods.put("activityId", activityId);
        findActivityGoods.put("asc", "goodsSort");
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<YjGoods> pages = this.findAll(new SpecificationUtil<YjGoods>().getSpecification(findActivityGoods), pageable);
        return new PageUtils(pages.getContent().stream().peek(l -> l.setContent("")).collect(Collectors.toList()), pages.getTotalPages(), size, page);
    }

    @CacheEvict(value = "activityGoods", allEntries = true)
    @Override
    public void cleanActivityGoods() {

    }

    @Override
    public int getAllGoods() {
        return this.repository.getAllGoods();
    }

}
