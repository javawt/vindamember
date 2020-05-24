package com.songlanyun.jymall.modules.business.jysys.uploadfile.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.entity.UploadFile;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.service.UploadFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:28
 */
@RestController
@RequestMapping("/uploadFile")
public class UploadFileController {
    @Autowired
    private UploadFilesService uploadFileService;

    /**
     * 列表
     */
    @GetMapping("/find")
    public R find(@RequestParam Map<String, Object> param) {
        if(null!=param){
            if(null==param.get("fileId")){
                return R.error("缺少参数fileId");
            }
        }else{
            return R.error("缺少参数fileId");
        }
        UploadFile uploadFile= uploadFileService.findById(Integer.parseInt(param.get("fileId").toString()));
        //PageUtils pageUtils = uploadFileService.queryPage(param);
        return R.ok().put("data", uploadFile);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", uploadFileService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody UploadFile uploadFile) {
        if (uploadFile.getFileId() == null) {
            uploadFile.setCreateTime(new Date());
        }
        uploadFileService.save(uploadFile);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        uploadFileService.deleteById(id);
        return R.ok("删除成功");
    }
}
