package com.songlanyun.jymall.service.impl;

import com.songlanyun.jymall.domain.ServerTime;
import com.songlanyun.jymall.repository.ServerTimeRepository;
import com.songlanyun.jymall.service.ServerTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author ww
 * @date 2019-12-13
 */
@Service
public class ServerTimeServiceImpl implements ServerTimeService {

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private ServerTimeRepository serverTimeRepository;

  /*  @Autowired
    private OrderService orderService;*/


    @Override
    public List<ServerTime> add(List<ServerTime> goodsDTO) {
        List<ServerTime> newSrvPrj = serverTimeRepository.saveAll(goodsDTO);
        return newSrvPrj;
    }

    /**
     * 批量插入 服务时间
     **/
    @Override
    @Transactional
    public void batchInsert(List list) {
        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if (i % 30 == 0) {
                em.flush();
                em.clear();
            }
        }
    }

    /**
     * 批量更新服务时间
     **/
    @Override
    @Transactional
    public void batchUpdate(List list) {
        for (int i = 0; i < list.size(); i++) {
            em.merge(list.get(i));
            if (i % 30 == 0) {
                em.flush();
                em.clear();
            }
        }
    }


    /**
     * 删除某服务项目
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        serverTimeRepository.deleteById(id);
    }

//
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params) {
//        params.put("desc", "createTime");
//        if (params.containsKey("page") && params.containsKey("size")) {
//            int page = Integer.parseInt(String.valueOf(params.get("page")));
//            int size = Integer.parseInt(String.valueOf(params.get("size")));
//            Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "createTime");
//            Page<ServerPrj> pageList = serverTimeRepository.findAll(new PageRequest(page, size));
//            return R.ok(StatusMsgEnum.QUERY_SUCCESS).put("data",pageList);
//        }
//        else{
//            return R.error("需要分页参数");
//        }
//
//    }

    /***
     * 更新某条服务时间段记录
     * @param goodsDTO
     * @return
     */
    @Override
    public ServerTime update(ServerTime goodsDTO) {
        ServerTime newSrvPrj = serverTimeRepository.save(goodsDTO);
        return newSrvPrj;
    }

    /** 得到某店铺，当天+5天后的 各时间段可预约数据  **/
    public List<ServerTime> getSrvTime(Map<String, Object> params){
        long shopId = Long.parseLong(String.valueOf(params.get("shopId")));
        long srvId = Long.parseLong(String.valueOf(params.get("srvId")));
        //1、得到此店铺此服务所有时间段
        List<ServerTime> srvList=serverTimeRepository.findAllByShopIdAndSrvIdOrderByStartTime(shopId,srvId);
        //2、生成今天+5天的所有记录
        // orderService.getSrvOrderCount(shopId,srvId)


        return  null;



    }

}