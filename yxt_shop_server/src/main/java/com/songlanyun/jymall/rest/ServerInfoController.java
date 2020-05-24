package com.songlanyun.jymall.rest;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.common.utils.StatusMsgEnum;

import com.songlanyun.jymall.domain.ServerGoods;
import com.songlanyun.jymall.domain.ServerTime;
import com.songlanyun.jymall.service.ServerInfoService;
import com.songlanyun.jymall.service.ServerTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.*;

/**
* @author hupeng
* @date 2020-01-09
*/
@RestController
@RequestMapping("/api/srvInfo")
public class ServerInfoController {

    @Autowired
    private ServerInfoService serverInfoService;

    @Autowired
    private ServerTimeService serverTimeService;

    /** 新增服务  **/
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody ServerGoods serverGoods) {
        //创建时间
        serverGoods.setCreateTime(new Timestamp(new Date().getTime()));
        ServerGoods newServerVo=serverInfoService.addServer(serverGoods);
        return R.ok(StatusMsgEnum.ADD_SUCCESS).put("data", newServerVo);
    }

    /**  删除服务  **/
     @PostMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id){
         this.serverInfoService.deleteById(id);
         return R.ok(StatusMsgEnum.DELETE_SUCCESS);
    }

    /** 分页 ，按条件查询  **/
    @PostMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
          //上架时间倒排
        serverInfoService.list(params);
        return null;
    }

    /** 批量添加某服务的营业时间  **/
    @PostMapping("/addSrvTime")
    public R addSrvTime(@RequestParam List<ServerTime> params){
        serverTimeService.batchInsert(params);
        return R.ok(StatusMsgEnum.ADD_SUCCESS);
    }

    /** 批量更新某服务的营业时间  **/
    @PostMapping("/updateSrvTime")
    public R updateSrvTime(@RequestParam List<ServerTime> params){
        //上架时间倒排
        serverTimeService.batchUpdate(params);
        return R.ok(StatusMsgEnum.ADD_SUCCESS);
    }

    /** 批量更新某服务的营业时间  **/
    @PostMapping("/getSrvTime")
    public R getSrvTime(@RequestParam  Map<String, Object> params){
        //上架时间倒排
        serverTimeService.getSrvTime(params);
        return R.ok(StatusMsgEnum.ADD_SUCCESS);
    }


    //    @ApiOperation("新增素材管理")
//    public ResponseEntity<Object> create(@Validated @RequestBody YxMaterial resources){
//        resources.setCreateId(SecurityUtils.getUsername());
//        return new ResponseEntity<>(yxMaterialService.create(resources),HttpStatus.CREATED);
//    }


//
//    @GetMapping(value = "/page")
//    @ApiOperation("查询素材管理")
//    public ResponseEntity<Object> getYxMaterials(YxMaterialQueryCriteria criteria, Pageable pageable){
//        return new ResponseEntity<>(yxMaterialService.queryAll(criteria,pageable),HttpStatus.OK);
//    }

//    @PostMapping
//    @Log("新增素材管理")
//    @ApiOperation("新增素材管理")
//    public ResponseEntity<Object> create(@Validated @RequestBody YxMaterial resources){
//        resources.setCreateId(SecurityUtils.getUsername());
//        return new ResponseEntity<>(yxMaterialService.create(resources),HttpStatus.CREATED);
//    }

}