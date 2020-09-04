package com.taikang.business.service.histrix;

import com.taikang.business.service.feignclient.FeignClientService;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dhj
 * @Description
 * @Date Create in 12:06 2019/8/26
 */
@ComponentScan
public class FeginClientErrorServiceImpl implements FeignClientService {
    @Override
    public String testFeign(String name) {
        return "返回异常！";
    }

    @Override
    public String testPostFeign(String name) {
        return "error";
    }
}
