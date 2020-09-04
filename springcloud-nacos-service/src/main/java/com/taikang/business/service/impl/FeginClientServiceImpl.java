package com.taikang.business.service.impl;

import com.taikang.business.service.feignclient.FeignClientService;
import com.taikang.business.service.model.UploadFileDTO;
import org.springframework.stereotype.Service;

/**
 * @author dhj
 * @Description
 * @Date Create in 18:16 2019/8/24
 */
@Service
public class FeginClientServiceImpl implements FeignClientService {
    @Override
    public String testFeign(String name) {

        return "测试feignClient调用机制name="+name;
    }

    @Override
    public String testPostFeign(String name) {
        return  "测试feignClient调用机制Post..name="+name;
    }

    @Override
    public String upload(UploadFileDTO fileDTO) {
        return null;
    }
}
