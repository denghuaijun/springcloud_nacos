package com.taikang.business.service.controller;

import com.alibaba.fastjson.JSONObject;
import com.taikang.business.service.mongo.MongoDBTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
 * @author dhj
 * @Description 测试mongodb各种操作姿势
 * @Date Create in 17:42 2019/9/3
 */

@RestController
@Slf4j
public class TestMongoDBController {


     @Autowired
     private MongoDBTemplateService mongoDBTemplateService;

     @RequestMapping("/test/mongo/save")
     public String saveMongoDB(){

         Map<String,Object> saveObject = new HashMap<>();
         saveObject.put("name","测试");
         saveObject.put("age",18);
         saveObject.put("sex","男");

         mongoDBTemplateService.save("user",saveObject);
         Map<String,Object> querMap = new HashMap<>();
         querMap.put("name","测试");
         Map<String, Object> map = mongoDBTemplateService.accurateQuery(querMap, "user");
         String result = JSONObject.toJSONString(map);

         return result;
     }
}
