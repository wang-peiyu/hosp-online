package com.onlinehosp.yygh_hosp.service;

import com.onlinehosp.yygh.model.hosp.Department;
import com.onlinehosp.yygh.vo.hosp.DepartmentQueryVo;
import com.onlinehosp.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void saveDepartment(Map<String, Object> paramMap);

    Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> findDeptTree(String hoscode);

    String getDepName(String hoscode, String depcode);

    Department getDepartment(String hoscode, String depcode);
}
