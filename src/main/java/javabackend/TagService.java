/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.time.LocalDate;
import java.util.ArrayList;
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
public class TagService {
    
    @Autowired
    private DynamoDBConfig config;
    
    @Autowired
    private UserInfoService userInfoService;
    
    private DynamoDBMapper getMapper() {
        AmazonDynamoDB client = this.config.amazonDynamoDB();
        return this.config.dynamoDBMapper(client, this.config.dynamoDBMapperConfig());
    }
    
    public List<TagData> getTagsByUsername(String username) {      
        List<TagData> tags = new ArrayList<>();        
        
        for (Tag tag : this.fetchTagsFromDynamo(username)) {
            tags.add(tag.getData());
        }
        System.out.println(tags);
        return tags;
    }
    
//    public JsonNode updateTags(TagData tag, String username) throws JsonProcessingException {
//        String tagData = this.getBucketContent(username);
//        List<TagData> tags = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode tagJson = mapper.readTree(tagData);
//        System.out.println(tagJson);
//        Iterator<JsonNode> iterator = tagJson.get("tags").iterator();
//        while (iterator.hasNext()) {
//            JsonNode jsonNode = iterator.next();
//            TagData currentTag = mapper.convertValue(jsonNode, TagData.class);
//            if (tag.getMac().equals(currentTag.getMac())) {
//                currentTag = tag;
//            }
//            tags.add(currentTag);
//        }
//        
//        JsonNode updatedTags = mapper.convertValue(tags, JsonNode.class);
//        System.out.println(updatedTags);
//        ObjectNode mutible = (ObjectNode)tagJson;
//        mutible.put("tags", updatedTags);
//        tagJson = (JsonNode)mutible;
//        System.out.println(tagJson);
//        tagData = mapper.writeValueAsString(tagJson);
//        System.out.println(tagData);
//        this.setBucketContent(tagData, username);
//        return tagJson;
//        
//    }
    
//    public String getBucketContent(String username) {
//        String bucketName = "ruuvibucket-" + username;
//        String key = "taglist-" + username + ".json";       
//        AmazonS3 S3Client = this.config.amazonS3();
//        
//        String tagData = "";
//               
//        try {
//            tagData = StreamUtils.copyToString(S3Client.getObject(bucketName, key).getObjectContent(), StandardCharsets.UTF_8);
//        }            
//        catch (IOException ex) {
//            Logger.getLogger(TagService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return tagData;
//    }
//    
//    public void setBucketContent(String tagData, String username) {
//        String bucketName = "ruuvibucket-" + username;
//        String key = "taglist-" + username + ".json";       
//        AmazonS3 S3Client = this.config.amazonS3();
//        
//        S3Client.putObject(bucketName, key, tagData);
//    }
    
    public List<Tag> fetchTagsFromDynamo(String username) {
        String beg = username.substring(0, 3) + "1";
        String end = username.substring(0, 3) + "9";
        
        DynamoDBMapper mapper = this.getMapper();
        
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(username));
        eav.put(":val2", new AttributeValue().withS(beg));
        eav.put(":val3", new AttributeValue().withS(end));
        
        DynamoDBQueryExpression<Tag> queryExpression = new DynamoDBQueryExpression<Tag>()
                .withKeyConditionExpression("Person = :val1 and Timestamp_Tagname between :val2 and :val3")
                .withExpressionAttributeValues(eav);
           
        return mapper.query(Tag.class, queryExpression);     
    }
    
    public TagData updateTagData(TagData tagData, String username) {
        
        DynamoDBMapper mapper = this.getMapper();
        Tag tag = new Tag();
        tag.setUsername(username);
        tag.setTagID(tagData.getTagName());
        tag.setData(tagData);
        
        mapper.save(tag, SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES.config());
        
        return tagData; 
        
    }
    
    public TagData addTag(TagData tagData, String username) {
        
        DynamoDBMapper mapper = this.getMapper();
        
        UserInfo user = this.userInfoService.getByUser(username);
        String tagID = username.substring(0, 3) + (user.getNumberOfTags() + 1);
        tagData.setTagName(tagID);
        user.setNumberOfTags(user.getNumberOfTags() + 1);
        
        Tag newTag = new Tag();
        newTag.setUsername(username);
        newTag.setTagID(tagID);
        newTag.setData(tagData);
        newTag.setDate(LocalDate.now().toString());
        
        mapper.save(newTag);
        this.userInfoService.updateUser(user);
        
        return newTag.getData();
    }
}
