package com.songlanyun.jymall.modules.business.jysys.dict.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.dict.entity.YjDictEntity;
import com.songlanyun.jymall.modules.business.jysys.dict.service.YjDictService;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商城字典表
 *
 * @author zhaoyong
 * @email zy765203718@gmail.com
 * @date 2019-11-14 10:26:30 init/dict/getDictList
 */
@RestController
@RequestMapping("init/dict")
public class YjDictController {
    @Autowired
    private YjDictService yjDictService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        YjDictEntity yjDict = yjDictService.getById(id);
        return R.ok().put("yjDict", yjDict);
    }

    /**
     * 保存
     */
    @RequestMapping("/updateDict")
    //@RequiresPermissions("init:dict:add")
    //@CacheEvict(value = "sly:yjshop:dict", allEntries = true)
    @ApiOperation(value = "新增或保存字典表", httpMethod = "POST")
    public R updateDict(@RequestBody @ApiParam(name = "字典表Vo", value = "dtvo") YjDictEntity dtvo) {
        //tsh 2020.1.19 注释校验代码
        /*if (dtvo.getDkey() == 0 || dtvo.getDtype() == 0 || dtvo.getDvalue().equals("")) {
            return R.error("字典数据不完整");
        }*/
        //是新增
        if (dtvo.getId() == 0) {
            if (yjDictService.getDictIsExist(dtvo.getDtype(), dtvo.getDkey()) > 0) {
                return R.error("此类型的编号已经存在");
            }
            if (yjDictService.getDictValueIsExist(dtvo.getDtype(), dtvo.getDvalue()) > 0) {
                return R.error("此类型的值已经存在");
            }
        }
        //直接保存
        if (!yjDictService.saveOrUpdate(dtvo)) {
            return R.error("保存失败");
        }
        redisTemplate.delete("sly:yjshop:dict");
        return R.ok("更新成功").put("yjdict", dtvo);
    }

    @ApiOperation(value = "删除字典表某key", notes = "删除字典表某key ", httpMethod = "POST")
    //@RequiresPermissions("init:dict:del")
    @RequestMapping(value = "/delDictItem", method = {RequestMethod.POST, RequestMethod.GET})
    public R delDictItem(@RequestBody @ApiParam(name = "字典表dkey", value = "key") int key) {
        R resp = null;
        yjDictService.del_dict(key);
        return R.ok("删除成功！");
    }

    @ApiOperation(value = "得到字典表", notes = "得到字典表key ", httpMethod = "POST")
    @RequestMapping(value = "/getDictList")
    public R getDictList() {
        R resp = null;
        List<YjDictEntity> dictList = yjDictService.getDictList();
        resp = R.ok("得到字典表数据成功").put("dictList", dictList);
        return resp;


    }

    @RequestMapping(value = "/getAdminDictList")
    public R getAdminDictList(@RequestBody Statistical statistical) {
        //入参校验
        if (statistical.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (statistical.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        List<YjDictEntity> resList =new ArrayList<>();
        if (null != statistical.getDtype()) {
            resList = yjDictService.getDictListByType(statistical.getDtype());
        }else{
            resList =  yjDictService.getDictList();
        }

        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((statistical.getPage() - 1) * statistical.getSize()).
                    limit(statistical.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        resMap.put("list", resList);
        return R.ok().put("dictList", resMap);
    }
}
