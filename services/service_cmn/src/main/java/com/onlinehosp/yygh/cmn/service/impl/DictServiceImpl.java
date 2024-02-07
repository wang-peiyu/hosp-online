package com.onlinehosp.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.onlinehosp.yygh.cmn.listener.DictListener;
import com.onlinehosp.yygh.cmn.mapper.DictMapper;
import com.onlinehosp.yygh.cmn.service.DictService;
import com.onlinehosp.yygh.model.cmn.Dict;
import org.springframework.cache.annotation.CacheEvict;
import com.onlinehosp.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.annotation.Cacheable;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Cacheable(value="dict",keyGenerator="keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
   //向list集合每个dict对象中设置hasChildren值
        for(Dict dict : dictList)
        {
          Long dictId=dict.getId();
         boolean isChild= this.isChildren(dictId);
         dict.setHasChildren(isChild);
        }
        return dictList;
    }

    @Override
    public void exportDictData(HttpServletResponse response) throws IOException {
        //设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName= "dict";
        response.setHeader("Content-disposition","attachment;filename="+fileName+".xlsx");
        //查询数据库
        List<Dict> dictList=baseMapper.selectList(null);
        List<DictEeVo> dictVoList=new ArrayList<>();
        for(Dict dict:dictList)
        {
             DictEeVo dictEeVo=new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictVoList.add(dictEeVo);
        }
        //调用方法进行写操作
        EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet(("dict")).doWrite(dictVoList);
    }

    @Override
    @CacheEvict(value="dict",allEntries = true)
    public void importDictData(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
    }

    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCde为空，使用value查询
        if(StringUtils.isEmpty(dictCode)){
            QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("value",value);
            return baseMapper.selectOne(queryWrapper).getName();
        }else {
            Dict dict=getDictByDictCode(dictCode);
            Long id = dict.getId();
            //跟据id和value查询
            QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("value",value);
            queryWrapper.eq("parent_id",id);
            return baseMapper.selectOne(queryWrapper).getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //跟据dictcode获取对应的id
        Dict dict=getDictByDictCode(dictCode);
        Long id=dict.getId();
        List<Dict> list=findChildData(id);
        return list;
    }

    public Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(queryWrapper);
        return dict;
    }

    private boolean isChildren(Long id)
    {
        QueryWrapper<Dict> wrapper=new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count=baseMapper.selectCount(wrapper);
        return count>0;
    }
}
