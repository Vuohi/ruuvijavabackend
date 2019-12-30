/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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
public class MeasurementService {
    
    @Autowired
    private DynamoDBConfig config;
    
    
    private DynamoDBMapper getMapper() {
        AmazonDynamoDB client = this.config.amazonDynamoDB();
        return this.config.dynamoDBMapper(client, this.config.dynamoDBMapperConfig());
    }
    
    public List<Measurement> getByUserAndTimestamp(String user, String beginning, String end) {
        beginning += user.substring(0, 2);
        beginning += "0";
        
        end += user.substring(0, 2);
        end += "9";
        
        DynamoDBMapper mapper = this.getMapper();
        
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(user));
        eav.put(":val2", new AttributeValue().withS(beginning));
        eav.put(":val3", new AttributeValue().withS(end));
        
        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()
                .withKeyConditionExpression("Person = :val1 and Timestamp_Tagname between :val2 and :val3")
                .withExpressionAttributeValues(eav);
        
        List<Measurement> response = mapper.query(Measurement.class, queryExpression);
        
        return response;
        
    }
    
    public Measurement getLatestMeasurement(String user) {
        DynamoDBMapper mapper = this.getMapper();
        
    }
    
}
