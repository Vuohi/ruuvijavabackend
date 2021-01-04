/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author katri
 */
public class TagDataConverter implements DynamoDBTypeConverter<Map<String, AttributeValue>, TagData> {
    
    
    @Override
    public Map<String, AttributeValue> convert(TagData data) {
        
        ObjectMapper mapper = new ObjectMapper();
        
        Item item = new Item()
               .withString("englishName", data.getEnglishName())
               .withString("friendlyName", data.getFriendlyName())
               .withMap("high", mapper.convertValue(data.getHigh(), Map.class))
               .withMap("low", mapper.convertValue(data.getLow(), Map.class))
               .withString("mac", data.getMac())
               .withString("tagName", data.getTagName());

        return ItemUtils.toAttributeValues(item);
    }

    @Override
    public TagData unconvert(Map<String, AttributeValue> data) {
        TagData tagData = new TagData();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String item = ItemUtils.toItem(data).toJSON();
            JsonNode json = mapper.readTree(item);
            tagData = mapper.convertValue(json, TagData.class);
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(TagDataConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return tagData;
    }
    
    
    
}
