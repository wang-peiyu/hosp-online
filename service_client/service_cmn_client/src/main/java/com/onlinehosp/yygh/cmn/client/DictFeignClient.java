package com.onlinehosp.yygh.cmn.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//表明被调用的接口名
@FeignClient("service-cmn")
@Repository
public interface DictFeignClient {

    //跟据dictcode和value查询
    @GetMapping("admin/cmn/dict/getName/{dictCode}/{value}")
    public String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value);

    //跟据value查询
    @GetMapping("admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);

}
