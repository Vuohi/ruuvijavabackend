/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 *
 * @author katri
 */
@Data
public class GoogleResponse {
    
    private String fulfillmentText;
    
    private JsonNode payload;
    
}
