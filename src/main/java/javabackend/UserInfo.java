/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

/**
 *
 * @author katri
 */
@Data
@DynamoDBTable(tableName = "RuuviMeasurements")
public class UserInfo {
    
    @DynamoDBHashKey(attributeName="Person")
    private String username;
    
    @DynamoDBRangeKey(attributeName="Timestamp_Tagname")
    private String infoTag;
    
    @DynamoDBAttribute(attributeName="Data")
    private String password;
    
    @DynamoDBAttribute(attributeName="NumberOfTags")
    private int numberOfTags;
    
}
