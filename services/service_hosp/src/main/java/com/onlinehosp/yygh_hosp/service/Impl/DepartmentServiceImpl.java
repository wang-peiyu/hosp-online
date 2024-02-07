package com.onlinehosp.yygh_hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.onlinehosp.yygh.model.hosp.Department;
import com.onlinehosp.yygh.vo.hosp.DepartmentQueryVo;
import com.onlinehosp.yygh.vo.hosp.DepartmentVo;
import com.onlinehosp.yygh_hosp.repository.DepartmentRepository;
import com.onlinehosp.yygh_hosp.service.DepartmentService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    //上传科室
    @Override
    public void saveDepartment(Map<String, Object> paramMap) {
        String paramMapString= JSONObject.toJSONString(paramMap);
        Department department=JSONObject.parseObject(paramMapString,Department.class);

        Department departmentExist=departmentRepository
                .getDepartmentByHoscodeAndDepcode(department.getHoscode(),department.getDepcode());


        if(departmentExist!=null){//如果已经存在就修改
            department.setCreateTime(departmentExist.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }else {//如果不存在就新增
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }

    }

    @Override
    public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        //创建pageable，设置当前页和每页记录数
        Pageable pageable= PageRequest.of(page-1,limit);

        Department department=new Department();
        BeanUtils.copyProperties(departmentQueryVo,department);
        department.setIsDeleted(0);

        //创建example对象
        ExampleMatcher matcher=ExampleMatcher.matching().
                withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        Example<Department> example= Example.of(department,matcher);



        Page<Department> all= departmentRepository.findAll(example,pageable);
        return all;
    }

    //删除科室接口
    @Override
    public void remove(String hoscode, String depcode) {
        Department departmentExist=
                departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);

        if(departmentExist!=null){
            departmentRepository.deleteById(departmentExist.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {




            List<DepartmentVo> result=new ArrayList<>();

            //根据医院编号查询所有科室
            Department departmentQuery=new Department();
            departmentQuery.setHoscode(hoscode);
            Example example=Example.of(departmentQuery);

            //所有科室列表
            List<Department> departmentList=departmentRepository.findAll(example);

            //跟据大科室获取其下面的子科室
            Map<String,List<Department>> departmentMap=
                    departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));

            //遍历map集合
            for(Map.Entry<String,List<Department>> entry:departmentMap.entrySet()){
                //获取大科室编号
                String bigcode=entry.getKey();

                //大科室其下的所有小科室
                List<Department> departmentList1=entry.getValue();

                //封装大科室
                DepartmentVo departmentVo=new DepartmentVo();
                departmentVo.setDepcode(bigcode);
                departmentVo.setDepname(departmentList1.get(0).getBigname());


                //封装小科室(将department类型变为departmentVo类型)
                List<DepartmentVo> children=new ArrayList<>();
                for (Department department:departmentList1){
                    DepartmentVo departmentVo1=new DepartmentVo();
                    departmentVo1.setDepcode(department.getDepcode());
                    departmentVo1.setDepname(department.getDepname());
                    children.add(departmentVo1);
                }
                departmentVo.setChildren(children);
                result.add(departmentVo);
            }


            return result;

    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department=  departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
        if (department!=null){
            return department.getDepname();
        }
        return null;
    }

    @Override
    public Department getDepartment(String hoscode, String depcode) {
        return departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode,depcode);
    }
}
