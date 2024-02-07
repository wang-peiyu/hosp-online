package com.onlinehosp.yygh.cmn.service;

import com.onlinehosp.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse response) throws IOException;

    void importDictData(MultipartFile file) throws IOException;

    String getDictName(String dictCode, String value);

    List<Dict> findByDictCode(String dictCode);
}
