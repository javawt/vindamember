package com.songlanyun.jymall.repository;

import com.songlanyun.jymall.domain.ServerGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


public interface ServerGoodsRepository extends JpaRepository<ServerGoods, Long>, JpaSpecificationExecutor<ServerGoods> {
    @Query(
            value = "select t.shop_id,sp.shop_name,t.id,u.nick_name,p.name as pname,t.srv_title,t.imgs,t.price " +
                    ",t.create_time,t.status,t.sum_money,t.sum_count" +
                    " from yj_server_goods t " +
                    " LEFT JOIN tb_shop_info sp on t.shop_id = sp.id" +
                    " Left Join sys_user u on t.create_userid=u.user_id" +
                    " left join yj_server_prj p on t.srv_pid=p.id" +
                    " WHERE (sp.shop_name = ?1 OR ?1 IS NULL) " +
                    " or  (t.srv_title = ?2 OR ?2 IS NULL)" +
                    " or (t.shop_id=?3 or ?3 is null)" +
                    " or (t.id =?4 or ?4 is null)",
            nativeQuery = true
    )
    Page< List<Map<String, Object>>> getSrvList(String shop_name,  String srv_title, Long shopid,Long srv_id,Pageable pageable);
}