package com.xueyuan.eduservice.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueyuan.eduservice.entity.EduTeacher;
import com.xueyuan.eduservice.entity.QueryTeacher;
import com.xueyuan.eduservice.handler.OSSValueUtils;
import com.xueyuan.eduservice.mapper.EduTeacherMapper;
import com.xueyuan.eduservice.service.EduTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author jesse941223
 * @since 2019-03-19
 */
@Slf4j
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService{

    @Override
    public void pageList(Page<EduTeacher> pageTeacher, QueryTeacher queryTeacher) {
  /*      try {
            //为了测试，模拟异常，以后删除
            int a =8/0;
        }catch (Exception e){
          throw new  EduException(20001,"抛出了一个自定义异常");

        }*/

        System.out.println("这个对象是" + queryTeacher);

        if (queryTeacher == null) {
            baseMapper.selectPage(pageTeacher, null);

        }

        String name = queryTeacher.getName();//姓名
        Integer level = queryTeacher.getLevel();
        String begin = queryTeacher.getBegin();
        String end = queryTeacher.getEnd();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {//name不为空
            wrapper.like("name", name);

        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);

        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        baseMapper.selectPage(pageTeacher, wrapper);//查询
    }

    @Override
    public String upload(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = OSSValueUtils.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = OSSValueUtils.ACCESS_KEY_ID;
        String accessKeySecret = OSSValueUtils.ACCESS_KEY_SECRET;//1.获取云存储相关常量
        String bucketName = OSSValueUtils.BUCKET_NAME;
        String fileHost = OSSValueUtils.FILE_HOST;


        try {
            //获取文件输入流
            InputStream in = file.getInputStream();
            //创建 oss对象
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            //调用OssClient 的putObject（）方法
            String butketName = "osstest0416";
            //上传文件的名称
            String fileName = file.getOriginalFilename();
            ossClient.putObject(butketName, fileName, in);
            //关闭服务
            ossClient.shutdown();

            //返回上传oss的路径
            //http://deu-service.oss-cn-beijing.aliyuncs.com/avatar/Explosion_4k.jpg
            String url = "http://" + butketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String FileUpload(MultipartFile file) {
        //1,获取阿里云存储相关常量
        String endPoint= OSSValueUtils.END_POINT;
        String accessKeyId = OSSValueUtils.ACCESS_KEY_ID;
        String accessKeySecret = OSSValueUtils.ACCESS_KEY_SECRET;
        String bucketName = OSSValueUtils.BUCKET_NAME;
        String fileHost = OSSValueUtils.FILE_HOST;

        String uploadUrl=null;
        //2,判断bucket是否存在，如果存在进行存储，如果不存在创建实例
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        if (!ossClient.doesBucketExist(bucketName)) {
            Bucket bucket = ossClient.createBucket(bucketName);
            //设置权限为公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }else{
            //获取上传文件流
            try {
                InputStream inputStream = file.getInputStream();
                //创建日期路径
                String filePath = new DateTime().toString("yyyy/MM/dd");
                log.info(filePath);
                //文件扩展名 uuid.扩展名
                //String original = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();//uuid文件名
                String fileName=uuid+file.getOriginalFilename();
                log.info(fileName);

                //String fileType = original.substring(original.lastIndexOf("."));//.扩展名
                // String newName=fileName+fileType;
                //String fileUrl = fileHost + "/" + filePath + "/" + newName;
                String path=filePath + "/" + fileName;
                log.info(path);
                //文件上传
                ossClient.putObject(bucketName, path, inputStream);
                //文件关闭
                ossClient.shutdown();
                //获取上传地址
                uploadUrl="http://"+bucketName+"."+endPoint+"/"+path;
                log.info(uploadUrl);
                return uploadUrl;
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }
}
