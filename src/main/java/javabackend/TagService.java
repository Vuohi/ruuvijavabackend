/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
public class TagService {
    
    @Autowired
    private DynamoDBConfig config;
    
    public List<Tag> getTagsByUsername(String username) throws JsonProcessingException {      
        List<Tag> tags = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();        
        String tagData = this.getBucketContent(username);

        JsonNode tagJson = mapper.readTree(tagData);
        Iterator<JsonNode> iterator = tagJson.get("tags").iterator();
        while (iterator.hasNext()) {
            JsonNode jsonNode = iterator.next();
            Tag tag = mapper.convertValue(jsonNode, Tag.class);
            tags.add(tag);
        }

        return tags;
    }
    
    public JsonNode updateTags(Tag tag, String username) throws JsonProcessingException {
        String tagData = this.getBucketContent(username);
        List<Tag> tags = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tagJson = mapper.readTree(tagData);
        System.out.println(tagJson);
        Iterator<JsonNode> iterator = tagJson.get("tags").iterator();
        while (iterator.hasNext()) {
            System.out.println("ihan vaan käyn läpi näitä tagien tietoja...");
            JsonNode jsonNode = iterator.next();
            Tag currentTag = mapper.convertValue(jsonNode, Tag.class);
            if (tag.getMac().equals(currentTag.getMac())) {
                currentTag = tag;
            }
            tags.add(currentTag);
        }
        
        JsonNode updatedTags = mapper.convertValue(tags, JsonNode.class);
        System.out.println(updatedTags);
        ObjectNode mutible = (ObjectNode)tagJson;
        mutible.put("tags", updatedTags);
        tagJson = (JsonNode)mutible;
        System.out.println(tagJson);
        tagData = mapper.writeValueAsString(tagJson);
        System.out.println(tagData);
        this.setBucketContent(tagData, username);
        return tagJson;
        
    }
    
    public String getBucketContent(String username) {
        String bucketName = "ruuvibucket-" + username;
        String key = "taglist-" + username + ".json";       
        AmazonS3 S3Client = this.config.amazonS3();
        
        String tagData = "";
               
        try {
            tagData = StreamUtils.copyToString(S3Client.getObject(bucketName, key).getObjectContent(), StandardCharsets.UTF_8);
        }            
        catch (IOException ex) {
            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tagData;
    }
    
    public void setBucketContent(String tagData, String username) {
        String bucketName = "ruuvibucket-" + username;
        String key = "taglist-" + username + ".json";       
        AmazonS3 S3Client = this.config.amazonS3();
        
        S3Client.putObject(bucketName, key, tagData);
    }
}
