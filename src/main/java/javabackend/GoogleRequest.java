/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author katri
 */
@Data
public class GoogleRequest {
    
    private String room;
    private String value;
    private boolean allRequiredParamsPresent;
    
    @SuppressWarnings("unchecked")
    @JsonProperty("queryResult")
    private void unpackNested(Map<String,Object> queryResult) {
        Map<String, String> parameters = (Map<String,String>) queryResult.get("parameters"); 
        this.room = parameters.get("room");
        this.value = parameters.get("value");
        this.allRequiredParamsPresent = (boolean)queryResult.get("allRequiredParamsPresent");
    }
}
