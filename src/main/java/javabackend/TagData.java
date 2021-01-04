/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author katri
 */
@Data
public class TagData {

    
    private String tagName;
    
    private String mac;
    
    private String friendlyName;
    
    private String englishName;
   
    @DynamoDBTypeConverted(converter = AlertConverter.class)
    private Alert low;
    
    @DynamoDBTypeConverted(converter = AlertConverter.class)
    private Alert high;
    
//    @JsonProperty("alerts")
//    private void unpackNested(Map<String,Object> alerts) {
//        System.out.println("muunnetaan...");
//        ObjectMapper mapper = new ObjectMapper(); 
//        JsonNode lowProperties = mapper.convertValue(alerts.get("low"), JsonNode.class);
//        JsonNode highProperties = mapper.convertValue(alerts.get("high"), JsonNode.class);
//               
//        Alert alert = mapper.convertValue(lowProperties, Alert.class);
//        this.low = alert;
//        alert = mapper.convertValue(highProperties, Alert.class);
//        this.high = alert;
//    }
    
}
