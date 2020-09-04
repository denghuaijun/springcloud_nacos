package com.taikang.business.service.feignclient;

import com.taikang.business.service.histrix.FeginClientErrorServiceImpl;
import com.taikang.business.service.model.UploadFileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author dhj
 * @Description
 * @Date Create in 18:13 2019/8/24
 */
@FeignClient(name = "service-provider",fallback = FeginClientErrorServiceImpl.class)
public interface FeignClientService {

    @RequestMapping(value = "/service/provider/test/feign/{name}",method = RequestMethod.GET)
    public String testFeign(@PathVariable(value = "name") String name);

    @RequestMapping(value = "/service/provider/test/feign/post",method = RequestMethod.POST)
    String testPostFeign(@RequestBody String name);

    @RequestMapping(value = "/service/provider/upload",method = RequestMethod.POST)
    public String upload(@RequestBody UploadFileDTO fileDTO);
}
