package com.songlanyun.jymall.rest;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.StatusMsgEnum;
import com.songlanyun.jymall.domain.ServerGoods;
import com.songlanyun.jymall.domain.ServerPrj;
import com.songlanyun.jymall.service.ServerInfoService;
import com.songlanyun.jymall.service.ServerPrjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
* @author hupeng
* @date 2020-01-09
*/
@RestController
@RequestMapping("/api/srvPrj")
public class ServerProjectController {

    @Autowired
    private ServerPrjService serverPrjService;

    /** 新增服务  **/
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody ServerPrj serverGoods) {
        //创建时间
        serverGoods.setCreateTime(new Timestamp(new Date().getTime()));
        ServerPrj newServerVo=serverPrjService.add(serverGoods);
        return R.ok(StatusMsgEnum.ADD_SUCCESS).put("data", newServerVo);
    }

    /** 删除服务项  **/
     @PostMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id){
         this.serverPrjService.deleteById(id);
         return R.ok(StatusMsgEnum.DELETE_SUCCESS);
    }

    /** 更新服务项  **/
    public R update(@RequestBody ServerPrj serverGoods) {
        //创建时间
         ServerPrj newServerVo=serverPrjService.add(serverGoods);
        return R.ok(StatusMsgEnum.ADD_SUCCESS).put("data", newServerVo);
    }
    /** 分页 ，按条件查询  **/
    public R list(@RequestParam Map<String, Object> params){
          //上架时间倒排


        return null;
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