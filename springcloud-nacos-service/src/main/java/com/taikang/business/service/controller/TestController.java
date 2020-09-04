package com.taikang.business.service.controller;


import com.taikang.business.service.feignclient.FeignClientService;
import com.taikang.business.service.model.UploadFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


/**
 * 使用springCloud原生注解@RefreshScope实现配置自动更新
 */
@RestController
public class TestController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Value("${db_url}")
    private String dbUrl;

    @Value("${redis.prefix}")
    private String redisPrefix;

    @Autowired
    private FeignClientService feignClientService;

    /**
     * 使用springCloud原生注解Refresh动态配置
     * @return
     */

    @RequestMapping("/get")
    public  boolean get(){
        System.out.println(useLocalCache);
        return useLocalCache;
    }

    /**
     * 生产提供者API接口（测试restTemPlate）
     */
    @RequestMapping(value = "/nacos/{name}",method = RequestMethod.GET)
    public String hello(@PathVariable String name){
        System.out.println("进入客户端生产提供者===");
        return "Hello Nacose Provider----:"+name;
    }

    /**
     * 测试nacos配置管理
     */
     @RequestMapping(value = "/nacos/test/")
     public String testConfig(){
         System.out.println("redis前缀："+redisPrefix);
         return dbUrl;
     }

     /**
     *@author dhj
     *@Description 测试feignclient进行rpc调用
     *@Date : 18:46 2019/8/24
     */

     @RequestMapping(value = "/test/feign/{name}",method = RequestMethod.GET)
    public String test(@PathVariable(value = "name") String name){
         System.out.println("进入生产提供者--namme---"+name);
        return feignClientService.testFeign(name);
     }

    @RequestMapping(value = "/test/feign/post",method = RequestMethod.POST)
    public String testPost(@RequestBody String name){
        System.out.println("进入生产提供者--namme---"+name);
        return feignClientService.testPostFeign(name);
    }
    /**
     * 文件上传测试
    *@author dhj
    *@Description
    *@Date : 14:00 2019/10/18
    */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String upload(@RequestBody UploadFileDTO fileDTO){
         return feignClientService.upload(fileDTO);
    }
}
