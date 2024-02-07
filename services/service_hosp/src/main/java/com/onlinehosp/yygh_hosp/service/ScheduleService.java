package com.onlinehosp.yygh_hosp.service;

import com.onlinehosp.yygh.model.hosp.Schedule;
import com.onlinehosp.yygh.vo.hosp.ScheduleOrderVo;
import com.onlinehosp.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> paramMap);

    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    Map<String, Object> getResultSchedule(long page, long limit, String hoscode, String depcode);

    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);

    //获取可预约的排班数据
    Map<String,Object> getBookingScheduleRule(int page,int limit,String hoscode,String depcode);

    Schedule getScheduleId(String scheduleId);

    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    void update(Schedule schedule);
}
