/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author katri
 */
@Data
public class Tag {
    
    private String name;
    
    private String mac;
    
    private String friendlyname;
    
    private String englishname;
    
    private Alert low;
    
    private Alert high;
    
    @JsonProperty("alerts")
    private void unpackNested(Map<String,Object> alerts) {
        ObjectMapper mapper = new ObjectMapper(); 
        JsonNode lowProperties = mapper.convertValue(alerts.get("low"), JsonNode.class);
        JsonNode highProperties = mapper.convertValue(alerts.get("high"), JsonNode.class);
               
        Alert alert = mapper.convertValue(lowProperties, Alert.class);
        this.low = alert;
        alert = mapper.convertValue(highProperties, Alert.class);
        this.high = alert;
    }
    
}
