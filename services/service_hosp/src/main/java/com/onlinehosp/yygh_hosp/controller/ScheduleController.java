package com.onlinehosp.yygh_hosp.controller;


import com.onlinehosp.yygh.common.result.Result;
import com.onlinehosp.yygh.model.hosp.Schedule;
import com.onlinehosp.yygh_hosp.service.ScheduleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hosp/schedule")
//@CrossOrigin //此注解允许跨域访问请求
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;


    @ApiOperation(value = "查询排版规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getSchedule(@PathVariable long page,
                              @PathVariable long limit,
                              @PathVariable String hoscode,
                              @PathVariable String depcode){
        Map<String,Object> map=  scheduleService.getResultSchedule(page,limit,hoscode,depcode);
        return Result.ok(map);
    }
    @ApiOperation(value = "查询排班仔细内容")
    @GetMapping("getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate){
        List<Schedule> list= scheduleService.getScheduleDetail(hoscode,depcode,workDate);
        return Result.ok(list);
    }


}
