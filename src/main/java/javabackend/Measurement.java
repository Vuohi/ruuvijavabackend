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
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Data;

/**
 *
 * @author katri
 */
@Data
@DynamoDBTable(tableName = "RuuviMeasurements")
public class Measurement {
    
    @DynamoDBHashKey(attributeName="Person")
    private String user;
    
    @DynamoDBRangeKey(attributeName="Timestamp_Tagname")
    private String timestamp_tag;
    
    @DynamoDBTypeConverted(converter = MeasurementDataConverter.class)
    @DynamoDBAttribute(attributeName="Data")
    private MeasurementData data;
    
    @DynamoDBAttribute(attributeName="MeasurementDate")
    private String dateOfMeasurement;
    
}
