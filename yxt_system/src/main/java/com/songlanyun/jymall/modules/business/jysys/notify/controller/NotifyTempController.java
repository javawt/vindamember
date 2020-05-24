package com.songlanyun.jymall.modules.business.jysys.notify.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.NotifyTemp;
import com.songlanyun.jymall.modules.business.jysys.notify.service.NotifyTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/12/17
 * Time: 10:55
 */
@RestController
@RequestMapping("/notify/temp")
public class NotifyTempController {
    @Autowired
    private NotifyTempService notifyTempService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @Cacheable( value="sly:xlgshop:notifyTemp",keyGenerator = "keyGenerator")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils pageUtils = notifyTempService.queryPage(params);
        return R.ok().put("data", pageUtils);
    }

    /**
     * 获取全部列表
     */
    @GetMapping("/list/all")
    public R list() {
        Map<String, Object> params = new HashMap<>();
        params.put("isUse", true);
        List<NotifyTemp> list = notifyTempService.findAll(params);
        return R.ok().put("data", list);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", notifyTempService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @CacheEvict(value="sly:xlgshop:notifyTemp",allEntries=true)
    public R save(@RequestBody NotifyTemp notifyTemp) {
        if (notifyTemp.getContent() == null) {
            return R.error("请输入内容");
        }

        int index = 0, num = 0, upIndex = 0;
        /*while ((index = notifyTemp.getContent().indexOf("%s", index)) != -1) {
            if (index != 0 && upIndex != 0 && index - upIndex < 2) {
                return R.error("两个相隔的占位符之间必须超过两个字");
            }
            num++;
            upIndex = index = index + 2;
        }*/

        /*if (num == 0) {
            return R.error("请至少指定一个占位符");
        }

        if (num * 2 == notifyTemp.getContent().length()) {
            return R.error("内容不能全部都由占位符组成");
        }*/

        notifyTemp.setNum(num);
        if (notifyTemp.getId() == null) {
            notifyTemp.setCreateUser(ShiroUtils.getUserId());
        }
        notifyTempService.save(notifyTemp);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @CacheEvict(value="sly:xlgshop:notifyTemp",allEntries=true)
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        notifyTempService.deleteById(id);
        return R.ok("删除成功");
    }
}
