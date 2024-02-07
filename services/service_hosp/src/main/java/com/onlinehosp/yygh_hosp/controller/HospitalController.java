package com.onlinehosp.yygh_hosp.controller;


import com.onlinehosp.yygh.common.result.Result;
import com.onlinehosp.yygh.model.hosp.Hospital;
import com.onlinehosp.yygh.vo.hosp.HospitalQueryVo;
import com.onlinehosp.yygh_hosp.service.HospitalService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/hospital")
//@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;


    //医院列表分页查询
    @GetMapping("/list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo)
    {
        System.out.println(hospitalQueryVo);
        Page<Hospital> pageModle=hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        return Result.ok(pageModle);
    }
    @ApiOperation(value = "更新医院的上下线状态")
    @GetMapping("/updateHospStatus/{id}/{status}")
    public Result updateHospStatus(@PathVariable String id,
                                   @PathVariable Integer status)
    {
        Hospital hospital=hospitalService.updateStatusById(id,status);
        return Result.ok();
    }
    @ApiOperation(value = "医院详情信息")
    @GetMapping("/showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id){
        Map<String,Object> hospital= hospitalService.getHospById(id);
        return Result.ok(hospital);
    }




}
