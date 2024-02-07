package com.onlinehosp.yygh_hosp.controller;


import com.onlinehosp.yygh.common.result.Result;
import com.onlinehosp.yygh.vo.hosp.DepartmentVo;
import com.onlinehosp.yygh_hosp.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hosp/department")
//@CrossOrigin
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value="查询所有科室列表")
    @GetMapping("/getDepList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode)
    {

        List<DepartmentVo> list=departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }


}
