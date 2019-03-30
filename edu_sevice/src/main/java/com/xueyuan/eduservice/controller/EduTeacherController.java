package com.xueyuan.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xueyuan.eduservice.entity.EduTeacher;
import com.xueyuan.eduservice.entity.QueryTeacher;
import com.xueyuan.eduservice.service.EduTeacherService;
import com.xueyuan.result.R;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author jesse941223
 * @since 2019-03-19
 */
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin   //解决跨域问题
public class EduTeacherController {

    @Autowired
    EduTeacherService eduTeacherService;

   /* @GetMapping("list")
    public  List<EduTeacher> list(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return list;


    }*/
//1 查询所有
    @GetMapping("list")
    public R list() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items", list);
        //返回统一格式数据

    }
// 2. 删除 ，通过id
    @DeleteMapping("{id}")
    public R removeById(@PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        if (flag) {
            return R.ok();
        } else {
          return   R.error();

       }
    }
//4.分页查询
    @GetMapping("{page}/{limit}")
    public R getPageList(  @ApiParam(name = "page", value = "当前页码", required = true)
                            @PathVariable Long page,

                        @ApiParam(name = "limit", value = "每页记录数", required = true)
                            @PathVariable Long limit){


            Page<EduTeacher> pageParam = new Page<>(page, limit);

        eduTeacherService.page(pageParam, null);
            List<EduTeacher> records = pageParam.getRecords();
            long total = pageParam.getTotal();

            return  R.ok().data("total", total).data("rows", records);
        }
        //多条件查询带分页
    @GetMapping("pageList/{page}/{limit}")
    public R  getPageCondition(@PathVariable Long page,
                               @PathVariable Long limit,
                                QueryTeacher queryTeacher){
        //
        Page<EduTeacher>pageTeacher=new Page<>(page,limit);
      //在service中自己创建条件，做操作
        eduTeacherService.pageList(pageTeacher,queryTeacher);

        List<EduTeacher> records = pageTeacher.getRecords();
        long total = pageTeacher.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }
    //5.添加讲师
    @PostMapping("save")
    public R addTeacher( @RequestBody EduTeacher eduTeacher){
         boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return  R.ok();
        }else {
            return R.error();
        }


    }

    //登录提交方法的方法
@PostMapping("login")
   public R login(){
    //{"code":20000,"data":{"token":"admin"}}
        return R.ok().data("token","admin");
   }
    //登录提交方法的方法
@PostMapping("logout")
   public R logout(){
    //{"code":20000,"data":{"token":"admin"}}
        return R.ok().data("token","null");
   }
   @GetMapping("info")
   public R info(){
        return  R.ok().data("roles","admin").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
      // {"code":20000,"data":{"roles":["admin"],"name":"admin","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
   }



    //6.根据id查询
    @GetMapping("{id}")
    public R getTeacherById(@PathVariable String id){
        final EduTeacher eduTeacherServiceById = eduTeacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacherServiceById);
    }
    // 7. 修改讲师信息
    @PostMapping("updateTeacher/{id}")
    public R  updateTeacherById(@ PathVariable String id,
                                @RequestBody EduTeacher eduTeacher){
        eduTeacher.setId(id);
         boolean updateById = eduTeacherService.updateById(eduTeacher);
        if (updateById) {
            return R.ok();
        }else {
            return R.error();
        }
    }


}