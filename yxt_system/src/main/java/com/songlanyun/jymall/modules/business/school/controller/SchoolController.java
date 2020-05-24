package com.songlanyun.jymall.modules.business.school.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadGroup;
import com.songlanyun.jymall.modules.business.oss.service.UploadGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sys/school")
public class SchoolController {

    @Autowired
    private UploadGroupService uploadGroupService;

    @GetMapping("/grpList")
    public R grpList() {
        List<YjUploadGroup> page = uploadGroupService.findAll(new Sort(Sort.Direction.ASC, "sort"));
        return R.ok().put("data", page);
    }

}
