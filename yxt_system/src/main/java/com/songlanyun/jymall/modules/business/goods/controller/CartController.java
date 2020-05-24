package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.goods.entity.Cart;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import com.songlanyun.jymall.modules.business.goods.service.CartService;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsService;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsSkuService;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecValueService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/11/27
 * Time: 10:46
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private YjGoodsService goodsService;
    @Autowired
    private YjGoodsSkuService yjGoodsSkuService;
    @Autowired
    private YjSpecValueService yjSpecValueService;
    @Resource
    private SysAdminDao sysAdminDao;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        PageUtils pageUtils = cartService.queryPage(param);
        return R.ok().put("data", pageUtils);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", cartService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Cart cart) {
        if (cart.getCartId() == null) {
            cart.setUserId(ShiroUtils.getUserId());
            cart.setCreateTime(new Date());
            cart.setUpdateTime(new Date());
            //判断新增的是否已存在，如果已存在则增加相同用户同商品和sku商品数量即可
            Cart sameCart = sysAdminDao.getSameCart(cart);
            if(null!=sameCart){
                //更新商品数量即可
                sameCart.setNum(sameCart.getNum()+cart.getNum());
                cartService.save(sameCart);
            }else{
                cartService.save(cart);
            }
        } else {
            cart.setUpdateTime(new Date());
            cart.setCreateTime(new Date());
            cartService.save(cart);
        }

        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        cartService.deleteById(id);
        return R.ok("删除成功");
    }

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    @PostMapping("/batch/delete")
    public R delete(String ids) {

        for (String id : ids.split(",")) {
            cartService.deleteById(Integer.valueOf(id));
        }
        return R.ok("删除成功");
    }

    /**
     * 获取当前用户的购物车
     */
    @GetMapping("/current/cart")
    @Transactional
    public R currentCart() {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", ShiroUtils.getUserId());
        List<Cart> carts = cartService.findAll(param);
        if(null!=carts && carts.size()>0){
        }else{
            return R.ok().put("data",carts);
        }
       /* Integer[] goodIds = new Integer[carts.size()];
        for (int i = 0; i < carts.size(); i++) {
            goodIds[i] = carts.get(i).getGoodsId();
        }*/

        //List<YjGoods> goodss = goodsService.findAllByGoodsIdIn(goodIds);
        for (Cart cart : carts) {

            YjGoods target=new YjGoods();

            YjGoods yjGoods= goodsService.findById(cart.getGoodsId());
            if(null==yjGoods){
                cartService.deleteById(cart.getCartId());
                return R.error("购物车商品:"+cart.getGoodsName()+"过期，已从购物车移除该商品！");
            }
            YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(cart.getGoodsSkuId());
            //查询skuValue值
            if (null != yjGoodsSku) {
               /* String specSkuIDS = yjGoodsSku.getSpecSkuId();
                if (StringUtils.isNotBlank(specSkuIDS)) {
                    List<String> contents = Arrays.asList(specSkuIDS.split(","));
                    if (null != contents && contents.size() > 0) {
                        for (String s : contents) {
                            if (StringUtils.isNotBlank(yjGoodsSku.getSpecValue())) {
                                yjGoodsSku.setSpecValue(yjGoodsSku.getSpecValue() + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                            } else {
                                yjGoodsSku.setSpecValue(yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                            }
                        }
                    }
                }
                yjGoods.setPrice(yjGoodsSku.getGoodsPrice().doubleValue());
                yjGoods.setDiscount(yjGoodsSku.getLinePrice().doubleValue());
                yjGoods.setGoodsSku(yjGoodsSku);
                //tsh 2019.12.21    新增限购数量
                yjGoods.setWsLimitnum(yjGoodsSku.getWsLimitnum());*/
            }
            if(null!=yjGoods){
                //非空判断
                BeanUtils.copyProperties(yjGoods,target);
            }
            cart.setGoods(target);
            /*for (YjGoods goods : goodss) {
                List<YjGoodsSku> yjGoodsSkuList=yjGoodsSkuService.selectYjGoodsSkuByGoodsId(goods.getGoodsId());
                if(null!=yjGoodsSkuList && yjGoodsSkuList.size()>0){
                    goods.setPrice(yjGoodsSkuList.get(0).getGoodsPrice().doubleValue());
                    goods.setDiscount(yjGoodsSkuList.get(0).getLinePrice().doubleValue());
                    //tsh 2019.12.21    新增限购数量
                    goods.setWsLimitnum(yjGoodsSkuList.get(0).getWsLimitnum());
                }

                if (cart.getGoodsId().equals(goods.getGoodsId())) {
                    cart.setGoods(goods);
                    break;
                }
            }*/
        }

        return R.ok().put("data", carts);
    }

}
