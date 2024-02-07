package com.onlinehosp.yygh_hosp.service;

import com.onlinehosp.yygh.model.hosp.HospitalSet;
import com.onlinehosp.yygh.vo.order.SignInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hospCode);

    SignInfoVo getSignInfoVo(String hoscode);
}
