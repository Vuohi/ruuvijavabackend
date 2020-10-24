/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import lombok.Data;

/**
 *
 * @author katri
 */
@Data
public class MeasurementData {
    
    private Double temperature;
    
    private Double humidity;
    
    private int pressure;
    
    private String timestamp;
    
    private String friendlyname;
    
    
    
}
