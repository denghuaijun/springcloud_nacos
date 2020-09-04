package com.taikang.business.service.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dhj
 * @Description
 * @Date Create in 17:11 2019/9/3
 */
@Slf4j
@Component
public class MongoDBTemplateService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 使用tempalte保存操作
     * @param collectionName
     * @param params
     */
    public void save(String collectionName, Map<String,Object> params){
        mongoTemplate.save(params,collectionName);
    }

    /**
     * 根据入参条件精确查询
     * @param queryMap
     * @param collectionName
     */
    public Map<String,Object> accurateQuery(Map<String,Object> queryMap,String collectionName){
        Criteria criteria =null;
        for (Map.Entry<String,Object> entry : queryMap.entrySet()){
            if (criteria == null){
                criteria = Criteria.where(entry.getKey()).is(entry.getValue());
            }else {
                criteria.and(entry.getKey()).is(entry.getValue());
            }
        }
        Query query = new Query(criteria);
        Map resultMap = mongoTemplate.findOne(query, Map.class, collectionName);

       return resultMap;
    }
}
