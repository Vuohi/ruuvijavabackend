/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author katri
 */
public class AlertConverter implements DynamoDBTypeConverter<String, Alert>{
    
    @Override
    public String convert(Alert data) {
        
        String alert = null;
        try {
            if (data != null) {
                alert = String.format("{\"activated\": \"%s\", \"triggered\": \"%s\", \"value\": \"%s\"}",
                        String.valueOf(data.isActivated()), String.valueOf(data.isTriggered()), String.valueOf(data.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alert;
    }
    
    @Override
    public Alert unconvert(String s) {
        Alert alert = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode dataOfAlert = mapper.readTree(s);
            alert = mapper.convertValue(dataOfAlert, Alert.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AverageDataConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return alert;
    }
}
