package com.onlinehosp.yygh_hosp.service.Impl;

import com.onlinehosp.yygh.common.exception.YyghException;
import com.onlinehosp.yygh.common.result.ResultCodeEnum;
import com.onlinehosp.yygh.model.hosp.HospitalSet;
import com.onlinehosp.yygh.vo.order.SignInfoVo;
import com.onlinehosp.yygh_hosp.mapper.HospitalSetMapper;
import com.onlinehosp.yygh_hosp.service.HospitalSetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Override
    public String getSignKey(String hospCode) {
        QueryWrapper<HospitalSet> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("hoscode",hospCode);

        HospitalSet hospitalSet=baseMapper.selectOne(queryWrapper);
        return hospitalSet.getSignKey();
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;

    }
}
