package com.onlinehosp.yygh.cmn.controller;

import com.onlinehosp.yygh.cmn.service.DictService;
import com.onlinehosp.yygh.common.result.Result;
import com.onlinehosp.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value="数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;
    //导入数据字典
    @PostMapping("importData")
    public Result  importData(MultipartFile file) throws IOException {
      dictService.importDictData(file);
      return Result.ok();
    }
    @ApiOperation(value="根据数据id获取子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id)
    {
         List<Dict> list=  dictService.findChildData(id);
         return Result.ok(list);
    }
    //导出数据字典接口
    @GetMapping("exportData")
            public void exportDict(HttpServletResponse response) throws IOException {
        dictService.exportDictData(response);

    }
    //跟据dictcode和value查询
    @GetMapping("/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value)
    {
        String dictName=dictService.getDictName(dictCode,value);
        return dictName;
    }

    //跟据value查询
    @GetMapping("/getName/{value}")
    public String getName(@PathVariable String value)
    {
        String dictName=dictService.getDictName("",value);
        return dictName;
    }
    @ApiOperation(value = "跟据code查询子节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list= dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

}
