package com.xueyuan.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xueyuan.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xueyuan.eduservice.entity.QueryTeacher;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author jesse941223
 * @since 2019-03-19
 */
public interface EduTeacherService extends IService<EduTeacher> {

    void pageList(Page<EduTeacher> pageTeacher, QueryTeacher queryTeacher);

    String upload(MultipartFile file);
    String FileUpload(MultipartFile file);
}
