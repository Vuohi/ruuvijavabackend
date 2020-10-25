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
public class AverageDataConverter implements DynamoDBTypeConverter<String, AverageData> {
    
    @Override
    public String convert(AverageData data) {
        
        String averages = null;
        try {
            if (data != null) {
                averages = String.format("{\"friendlyname\": \"%s\", \"temperature\": \"%s\", \"humidity\": \"%s\", \"timestamp\": %s}",
                        data.getFriendlyname(), String.valueOf(data.getTemperature()), String.valueOf(data.getHumidity()), data.getTimestamp());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return averages;
    }
    
    @Override
    public AverageData unconvert(String s) {
        AverageData averageData = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode dataOfAverage = mapper.readTree(s);
            averageData = mapper.convertValue(dataOfAverage, AverageData.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AverageDataConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return averageData;
    }
}
