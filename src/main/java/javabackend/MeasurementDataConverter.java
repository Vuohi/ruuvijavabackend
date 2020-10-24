/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author katri
 */
public class MeasurementDataConverter implements DynamoDBTypeConverter<String, MeasurementData> {

    @Override
    public String convert(MeasurementData data) {
        String measurements = null;
        try {
            if (data != null) {
                measurements = String.format("{\"temperature\": %s, \"humidity\": %s, \"pressure\": %s, \"timestamp\": %s, \"friendlyname\": \"%s\"}",
                        String.valueOf(data.getTemperature()), String.valueOf(data.getHumidity()), String.valueOf(data.getPressure()), data.getTimestamp(), data.getFriendlyname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return measurements;
    }

    @Override
    public MeasurementData unconvert(String s) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode dataOfMeasurement = mapper.readTree(s);
            MeasurementData measurementData = mapper.convertValue(dataOfMeasurement, MeasurementData.class);
            return measurementData;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MeasurementDataConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
