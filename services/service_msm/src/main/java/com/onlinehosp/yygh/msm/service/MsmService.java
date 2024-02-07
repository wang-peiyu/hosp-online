package com.onlinehosp.yygh.msm.service;

import com.onlinehosp.yygh.vo.msm.MsmVo;

public interface MsmService {
    boolean send(String phone, String code);
    boolean send(MsmVo msmVo);
}
