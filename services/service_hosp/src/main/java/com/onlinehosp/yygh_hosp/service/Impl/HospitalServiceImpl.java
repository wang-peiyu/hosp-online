package com.onlinehosp.yygh_hosp.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.onlinehosp.yygh.cmn.client.DictFeignClient;
import com.onlinehosp.yygh.model.hosp.Hospital;
import com.onlinehosp.yygh.vo.hosp.HospitalQueryVo;
import com.onlinehosp.yygh_hosp.repository.HospitalRepository;
import com.onlinehosp.yygh_hosp.service.HospitalService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {


    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数map集合转换成对象
        String mapString = JSONObject.toJSONString(paramMap);

        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        //判断是否存在数据
        Hospital hospitalExist= hospitalRepository.getHospitalByHoscode(hospital.getHoscode());

        if(hospitalExist!=null){//如果已经存在就修改
            hospital.setStatus(hospital.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {//如果不存在就新增
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }


    }

    @Override
    public Hospital getHospitalByCode(String hospCode) {
        Hospital hospital=hospitalRepository.getHospitalByHoscode(hospCode);
        return hospital;
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable= PageRequest.of(page-1,limit);

        //对象转换
        Hospital hospital=new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        hospital.setIsDeleted(0);

        //创建匹配条件
        ExampleMatcher matcher=ExampleMatcher.matching().
                withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        //创建对象
        Example<Hospital> example=Example.of(hospital,matcher);
        //调用方法实现查询

        Page<Hospital> pages=hospitalRepository.findAll(example,pageable);
        System.out.println(pages.getContent());

        //通过调用service_cmn查询医院的等级,然后添加到pages中每个hospital中
        pages.getContent().stream().forEach(item->{
            this.setHospitalHosType(item);
        });

        return pages;
    }

    @Override
    public Hospital updateStatusById(String id, Integer status) {
        Hospital hospital=hospitalRepository.findById(id).get();

        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());

        hospitalRepository.save(hospital);
        return hospital;
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Hospital hospital=this.setHospitalHosType(hospitalRepository.findById(id).get());
        Map<String,Object> map=new HashMap<>();
        map.put("hospital",hospital);
        map.put("bookingRule",hospital.getBookingRule());
        return map;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital= hospitalRepository.getHospitalByHoscode(hoscode);
        if(hospital!=null){
            return hospital.getHosname();
        }
        return null;
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.getHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> getHospByHosCode(String hoscode) {
        Hospital hospital=this.getHospitalByCode(hoscode);
        String id=hospital.getId();
        Map<String,Object> map =this.getHospById(id);
        return map;
    }

    private Hospital setHospitalHosType(Hospital item) {
        //查询医院等级
        String type= dictFeignClient.getName("Hostype",item.getHostype());

        //查询医院省市地区
        String province=dictFeignClient.getName(item.getProvinceCode());
        String city=dictFeignClient.getName(item.getCityCode());
        String district=dictFeignClient.getName(item.getDistrictCode());

        item.getParam().put("hostypeString",type);
        item.getParam().put("fullAddress",province+city+district);

        return item;
    }
}
