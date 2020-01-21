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
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

/**
 *
 * @author katri
 */
@Service
public class MeasurementService {
    
    @Autowired
    private DynamoDBConfig config;
    
    @Autowired
    private UserInfoService userInfoService;
    
    
    private DynamoDBMapper getMapper() {
        AmazonDynamoDB client = this.config.amazonDynamoDB();
        return this.config.dynamoDBMapper(client, this.config.dynamoDBMapperConfig());
    }
    
    public List<Measurement> getByUserAndTimestamp(String user, String beginning, String end) {
        beginning += user.substring(0, 3);
        beginning += "0";
        
        end += user.substring(0, 3);
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
    
    public List<List<Measurement>> arrangeByTag(List<Measurement> measurements) {
        Map<String, List<Measurement>> arrangedMap = new HashMap<>();
        for (Measurement measurement : measurements) {
            String tagname = measurement.getTimestamp_tag().substring(11);
            arrangedMap.putIfAbsent(tagname, new ArrayList<>());
            arrangedMap.get(tagname).add(measurement);           
        }
        List<List<Measurement>> arranged = new ArrayList<>(arrangedMap.values());
        return arranged; 
    }
    
    public List<Measurement> getLatestMeasurements(String username) {
        DynamoDBMapper mapper = this.getMapper();
        int tags = this.userInfoService.getByUser(username).getNumberOfTags();
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        timestamp += "_" + username.substring(0, 3) + "9";
        
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(username));
        eav.put(":val2", new AttributeValue().withS(timestamp));
        
        DynamoDBQueryExpression<Measurement> queryExpression = new DynamoDBQueryExpression<Measurement>()               
                .withKeyConditionExpression("Person = :val1 and Timestamp_Tagname < :val2")
                .withExpressionAttributeValues(eav);
        queryExpression.setScanIndexForward(false);
        queryExpression.setLimit(tags);
        List<Measurement> response = mapper.queryPage(Measurement.class, queryExpression).getResults();
        
        return response;
                
        
    }
    
    public GoogleResponse getQueryData(GoogleRequest req) {
        String needResponse = "";
        String response = "";
        
        List<Measurement> latest = this.getLatestMeasurements("squi");
        
        if (latest.isEmpty()) {
            needResponse = "false";
            response = "I can\'t communicate with the database right now";
        }
        
        if (!req.isAllRequiredParamsPresent()) {
            needResponse = "true";
            response = "Sorry, I didn\'t quite catch that";
        }
               
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> equivalences = new HashMap<>();
        
        String bucketName = "ruuvitag-ids";
        String key = "taglist-extra.json";       
        AmazonS3 S3Client = this.config.amazonS3();
        
        try {
            String tagData = StreamUtils.copyToString(S3Client.getObject(bucketName, key).getObjectContent(), StandardCharsets.UTF_8);
            JsonNode tagJson = mapper.readTree(tagData);
            Iterator<JsonNode> iterator = tagJson.get("tags").iterator();
            while (iterator.hasNext()) {
                JsonNode jsonNode = iterator.next();
                equivalences.put(jsonNode.get("englishname").textValue(), jsonNode.get("friendlyname").textValue());
            }
        } catch (IOException ex) {
            Logger.getLogger(MeasurementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        for (Measurement measurement : latest) {
            try {
                JsonNode measurementData = mapper.readTree(measurement.getData());
                String finnishName = equivalences.get(req.getRoom());
                if (finnishName.equals(measurementData.get("friendlyname").textValue())) {
                    needResponse = "false";
                    response = req.getRoom() + " " + req.getValue() + " is " + measurementData.get(req.getValue());                  
                }
            } catch (JsonProcessingException ex) {
                Logger.getLogger(MeasurementService.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }
        GoogleResponse res = new GoogleResponse();
        res.setFulfillmentText(response);
        try {
            res.setPayload(mapper.readTree("{\"google\": {\"expectUserResponse\":" + needResponse + ",\"richResponse\": {\"items\": [{\"simpleResponse\": {\"textToSpeech\": \"" + response + "\",\"displayText\": \"" + response + "\"}}]}}}"));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MeasurementService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    
    
}
