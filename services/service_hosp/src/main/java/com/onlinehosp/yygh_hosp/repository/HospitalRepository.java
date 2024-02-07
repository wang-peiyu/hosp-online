package com.onlinehosp.yygh_hosp.repository;

import com.onlinehosp.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {

    //判断是否存在
    Hospital getHospitalByHoscode(String hoscode);

    List<Hospital> getHospitalByHosnameLike(String hosname);
}
