package com.taikang.business.api.controller;


import com.taikang.business.service.feignclient.FeignClientService;
import com.taikang.business.service.model.UploadFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class Api {

    @Autowired
    private FeignClientService feignClientService;

    @Autowired
    private RestTemplate restTemplate;

    /**
    *@author dhj
    *@Description restTemplate远程调用get请求
    *@Date : 12:17 2019/8/26
    */
    @RequestMapping(value = "/api/nacos/{name}",method = RequestMethod.GET)
    public  String test(@PathVariable String name){
        System.out.println("进入客户端消费者===");
        return restTemplate.getForObject("http://service-provider/nacos/"+name,String.class);
    }

    /**
    *@author dhj
    *@Description  feignClient远程调用get请求模拟
    *@Date : 12:17 2019/8/26
    */
    @RequestMapping(value = "/api/nacos/feign/{name}",method = RequestMethod.GET)
    public  String testFeign(@PathVariable(value = "name") String name){
        System.out.println("测试feign调用机制消费端开始----");
       return feignClientService.testFeign(name);
    }

    /**
     *@author dhj
     *@Description  feignClient远程调用post请求模拟
     *@Date : 12:17 2019/8/26
     */
    @RequestMapping(value = "/api/test/post/feign",method = RequestMethod.POST)
    public  String testPostFeign(@RequestBody String name){
        System.out.println("测试feign调用机制消费端开始----");
        return feignClientService.testPostFeign(name);
    }

    /**
    *@author dhj
    *@Description 实现文件上传功能并返回相应的存储路径
    *@Date : 10:00 2019/10/18
    */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String upload(@RequestBody MultipartFile file) throws IOException {
        UploadFileDTO fileDTO = new UploadFileDTO();
        byte[] bytes = file.getBytes();
        String originalFilename = file.getOriginalFilename();//全文件名
        String fileName = file.getName();
        String contentType = file.getContentType();
        fileDTO.setBytes(bytes);
        fileDTO.setContentType(contentType);
        fileDTO.setFileName(fileName);
        fileDTO.setOriginalFilename(originalFilename);
        feignClientService.upload(fileDTO);

        return "";
    }

}
