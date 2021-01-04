/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author katri
 */
@Service
public class UserInfoService {
    
    @Autowired
    private DynamoDBConfig config;
    
    private DynamoDBMapper getMapper() {
        AmazonDynamoDB client = this.config.amazonDynamoDB();
        return this.config.dynamoDBMapper(client, this.config.dynamoDBMapperConfig());
    }
    
    public UserInfo getByUser(String user) {
        DynamoDBMapper mapper = this.getMapper();
        
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(user));
        eav.put(":val2", new AttributeValue().withS("userdata"));
        
        DynamoDBQueryExpression<UserInfo> queryExpression = new DynamoDBQueryExpression<UserInfo>()
                .withKeyConditionExpression("Person = :val1 and Timestamp_Tagname = :val2")
                .withExpressionAttributeValues(eav);
        
        List<UserInfo> response = mapper.query(UserInfo.class, queryExpression);
        
        if (response.isEmpty()) {
            return null;
        }
        
        return response.get(0);
    }
    
    public void save(UserInfo userinfo) {
        DynamoDBMapper mapper = this.getMapper();
        mapper.save(userinfo);
    }
    
    public UserInfo updateUser(UserInfo userInfo) {
        DynamoDBMapper mapper = this.getMapper();
        mapper.save(userInfo, DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES.config());
        return userInfo;
    }
    
}
