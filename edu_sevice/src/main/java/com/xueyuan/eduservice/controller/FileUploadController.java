package com.xueyuan.eduservice.controller;

import com.xueyuan.eduservice.service.EduTeacherService;
import com.xueyuan.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduservice/oss")
@CrossOrigin
public class FileUploadController {

    @Autowired
    EduTeacherService eduTeacherService;

    @PostMapping("upload")
    public R upload( @RequestParam("file") MultipartFile file){
        String url= eduTeacherService.FileUpload(file);
        return R.ok().data("uploadUrl",url);
    }

}
