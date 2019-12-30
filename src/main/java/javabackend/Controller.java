/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author katri
 */
@RestController
public class Controller {
    
    @Autowired
    private MeasurementService measurementService;
    
    
    
       
    @GetMapping("/measurements")
    @ResponseBody
    public Iterable<Measurement> getMeasurements(@RequestParam String user) {
        return this.measurementService.getByUserAndTimestamp(user, "1577653208", "1577654708");
        
    }
    
    @PostMapping("/measurements")
    public Iterable<Measurement> getTimePeriod(@RequestParam DateTime beginning, @RequestParam DateTime end) {
        
    }
   
//    @GetMapping("/measurements/{id}")
//    public List<Measurement> getMeasurementsByTag(@PathVariable Long id) {
//        
//        
//    }
//    
    @GetMapping("/last")
    public Measurement getLatestMeasurement() {
        
    }
//    
//    
//    @PostMapping("/queryData")
//    public Measurement getQueryData() {
//        
//    }
    
}
