package com.onlinehosp.yygh_hosp.controller;


import com.onlinehosp.yygh.common.exception.YyghException;
import com.onlinehosp.yygh.common.result.Result;
import com.onlinehosp.yygh.common.utils.MD5;
import com.onlinehosp.yygh.model.hosp.HospitalSet;
import com.onlinehosp.yygh.vo.hosp.HospitalSetQueryVo;
import com.onlinehosp.yygh_hosp.service.HospitalSetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Api(tags="医院核心管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
//@CrossOrigin //此注解允许跨域访问请求
public class HospitalSetController {

    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;
//http://localhost:8201/admin/hosp/hospitalSet/findAll

    @ApiOperation(value = "查找所有医院信息")
    @GetMapping("/findAll")
    public Result findAllHospitalSet()
    {
        List<HospitalSet> list=new ArrayList<>();

        try{
            list=hospitalSetService.list();
        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }

        return Result.ok(list);
    }

    @ApiOperation(value = "跟据医院id进行删除操作")
    @DeleteMapping("/{id}")
    public Result removeHospSet(@PathVariable Long id)
    {

        boolean result;

        try{
             result=hospitalSetService.removeById(id);

        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }


        if(result)
        {
            System.out.println(Result.ok());
            return Result.ok();
        }else {
            System.out.println(Result.fail());
            return Result.fail();
        }
    }

    //条件查询带分页
    @PostMapping("/findHospSetPage/{current}/{limit}")
    public Result findHospSetPage (@PathVariable long current,
                                   @PathVariable long limit,
                                   @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo)
    {


        //设置分页条件
        Page<HospitalSet> page=new Page<>(current,limit);

        Page<HospitalSet> result=new Page<>();

        //创建复杂查询对象
        QueryWrapper<HospitalSet> queryWrapper=new QueryWrapper<>();

        String hosname=hospitalSetQueryVo.getHosname();
        String hoscode=hospitalSetQueryVo.getHoscode();

        //判断hosname和hoscode是否为空
        if(!StringUtils.isEmpty(hosname))
        {
            queryWrapper.like("hosname",hosname);
        }

        if(!StringUtils.isEmpty(hoscode))
        {
            queryWrapper.like("hoscode",hoscode);
        }

        try{
            //调用分页查询方法
            result=hospitalSetService.page(page,queryWrapper);
        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }

        return Result.ok(result);
    }

    //添加医院信息
    @PostMapping("/saveHospSet")
    public Result saveHospSet(@RequestBody HospitalSet hospitalSet)
    {


        //设置状态码1为可用，0为不可
        hospitalSet.setStatus(1);

        //设置匹配密钥，当前时间加上随机数
        Random rm=new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+rm.nextInt(1000)));

        boolean result;

        try{
            result=hospitalSetService.save(hospitalSet);
        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }

        if(result)
        {
            return Result.ok();
        }else
        {
            return Result.fail();
        }
    }

    //跟据id获取医院信息
    @GetMapping("/getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id)
    {
        HospitalSet hospitalSet=new HospitalSet();

        try{
            hospitalSet=hospitalSetService.getById(id);
        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }


        return Result.ok(hospitalSet);
    }

    //修改医院信息
    @PostMapping("/updateHospitalSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet)
    {
        boolean result;

        try{
            result=hospitalSetService.updateById(hospitalSet);
        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }

        if(result)
        {
            return Result.ok();
        }else
        {
            return Result.fail();
        }
    }

    //批量删除医院信息
    @DeleteMapping("/batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> list)
    {

        boolean result;

        try{
            result=hospitalSetService.removeByIds(list);
        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }

        if(result)
        {
            return Result.ok();
        }else
        {
            return Result.fail();
        }
    }


    @GetMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        boolean result;
        System.out.println(id);
        System.out.println(status);
        try{
            HospitalSet hospitalSet=hospitalSetService.getById(id);
            hospitalSet.setStatus(status);
            result=hospitalSetService.updateById(hospitalSet);

        }catch (Exception e)
        {
            throw new YyghException("失败",201);
        }

        if(result)
        {
            return Result.ok();
        }else
        {
            return Result.fail();
        }

    }


}
