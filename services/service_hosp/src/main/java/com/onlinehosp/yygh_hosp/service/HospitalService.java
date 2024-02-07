package com.onlinehosp.yygh_hosp.service;

import com.onlinehosp.yygh.model.hosp.Hospital;
import com.onlinehosp.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    //上传医院的接口
    void save(Map<String, Object> paramMap);

    Hospital getHospitalByCode(String hospCode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    Hospital updateStatusById(String id, Integer status);

    Map<String, Object> getHospById(String id);

    String getHospName(String hoscode);

    List<Hospital> findByHosname(String hosname);

    Map<String, Object> getHospByHosCode(String hoscode);
}
