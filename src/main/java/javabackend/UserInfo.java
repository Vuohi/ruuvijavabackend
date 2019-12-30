/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author katri
 */
@Data
@DynamoDBTable(tableName = "java-spring-test")
public class UserInfo {
    
    @DynamoDBHashKey
    private String owner;
    
    @DynamoDBAttribute
    private Integer ranking;
    
    @DynamoDBAttribute
    private List<String> data;
    
}
