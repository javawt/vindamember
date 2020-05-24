package com.songlanyun.jymall.modules.business.jysys.protocol.controller;

import com.songlanyun.jymall.common.annotation.SysLog;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.protocol.entity.Protocol;
import com.songlanyun.jymall.modules.business.jysys.protocol.service.ProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zenghang on 2019/11/22.
 */
@RestController
@RequestMapping("/protocol")
public class ProtocolController {
    @Autowired
    private ProtocolService protocolService;

    /**
     * 列表
     */
    @SysLog("协议列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        PageUtils pageUtils = protocolService.queryPage(param);
        return R.ok().put("data", pageUtils);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id")Integer id) {
        return R.ok().put("data", protocolService.findById(id));
    }

    /**
     * 保存
     */

    @SysLog("协议修改")
    @PostMapping("/save")
    public R save(@RequestBody Protocol protocol) {
        protocolService.save(protocol);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @GetMapping("/delete/{id}")
    public R delete(@PathVariable("id")Integer id) {
        protocolService.deleteById(id);
        return R.ok("删除成功");
    }
}
