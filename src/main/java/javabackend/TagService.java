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
