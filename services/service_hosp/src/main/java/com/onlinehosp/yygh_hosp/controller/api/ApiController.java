package com.onlinehosp.yygh_hosp.controller.api;


import com.onlinehosp.yygh.common.exception.YyghException;
import com.onlinehosp.yygh.common.helper.HttpRequestHelper;
import com.onlinehosp.yygh.common.result.Result;
import com.onlinehosp.yygh.common.result.ResultCodeEnum;
import com.onlinehosp.yygh.common.utils.MD5;
import com.onlinehosp.yygh.model.hosp.Department;
import com.onlinehosp.yygh.model.hosp.Hospital;
import com.onlinehosp.yygh.model.hosp.Schedule;
import com.onlinehosp.yygh.vo.hosp.DepartmentQueryVo;
import com.onlinehosp.yygh.vo.hosp.ScheduleQueryVo;
import com.onlinehosp.yygh_hosp.service.DepartmentService;
import com.onlinehosp.yygh_hosp.service.HospitalService;
import com.onlinehosp.yygh_hosp.service.HospitalSetService;
import com.onlinehosp.yygh_hosp.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //查询医院的接口
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital= hospitalService.getHospitalByCode((String)paramMap.get("hoscode"));
        return Result.ok(hospital);
    }


    //上传医院的接口
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request){
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //传输过程中"+"转换为了""，因此要转换回来
        String logoData=(String) paramMap.get("logoData");
        logoData=logoData.replaceAll(" ","+");
        paramMap.put("logoData",logoData);

        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        hospitalService.save(paramMap);
        return Result.ok();

    }

    //上传科室的接口
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.saveDepartment(paramMap);
        return Result.ok();
    }

    //查询科室的接口
    @PostMapping("/department/list")
    public Result findDepartment(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //医院编号
        String hoscode=(String) paramMap.get("hoscode");

        int page= StringUtils.isEmpty(paramMap.get("page"))?1:Integer.parseInt((String) paramMap.get("page"));
        int limit= StringUtils.isEmpty(paramMap.get("limit"))?1:Integer.parseInt((String) paramMap.get("limit"));

        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        DepartmentQueryVo departmentQueryVo=new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel= departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);
    }

    //删除科室的接口
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request){
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //医院编号
        String hoscode=(String) paramMap.get("hoscode");
        //科室编号
        String depcode=(String) paramMap.get("depcode");
        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        departmentService.remove(hoscode,depcode);
        return Result.ok();

    }

    //上传排班接口
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        // 获取传递过来的排版信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //判断签名是否一致
        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        scheduleService.save(paramMap);
        return Result.ok();

    }

    //查询排版接口
    @PostMapping("/schedule/list")
    public Result findSchedule(HttpServletRequest request){
        // 获取传递过来的排版信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //医院编号
        String hoscode=(String) paramMap.get("hoscode");
        //科室编号
        String depcode=(String) paramMap.get("depcode");
        int page= StringUtils.isEmpty(paramMap.get("page"))?1:Integer.parseInt((String) paramMap.get("page"));
        int limit= StringUtils.isEmpty(paramMap.get("limit"))?1:Integer.parseInt((String) paramMap.get("limit"));

        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        ScheduleQueryVo scheduleQueryVo=new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setHoscode(depcode);

        Page<Schedule> pageModel=scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //删除排版
    //删除科室的接口
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        // 获取传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //医院编号
        String hoscode=(String) paramMap.get("hoscode");
        //科室编号
        String hosScheduleId=(String) paramMap.get("hosScheduleId");
        if(!judgeSignKey(paramMap)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();

    }



    //公共方法判断signkey是否一致
    public boolean judgeSignKey(Map<String, Object> paramMap){
        //验证需要添加的医院signkey是否一致
        String hospSign =(String) paramMap.get("sign");

        String hospCode=(String) paramMap.get("hoscode");

        String signKey=hospitalSetService.getSignKey(hospCode);

        String signKeyMd5=MD5.encrypt(signKey);

        return hospSign.equals(signKeyMd5);
    }
    
}
