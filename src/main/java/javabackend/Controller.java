/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    
    
       
    @GetMapping("/measurements/{user}")
    @ResponseBody
    public Map<String, List<Measurement>> getMeasurements(@PathVariable String user) {
        return this.measurementService.arrangeByTag(this.measurementService.getByUserAndTimestamp(user, "1577689807", "1577693108"));
        
    }
    
    @PostMapping("/measurements/{user}")
    public Iterable<Measurement> getTimePeriod(@PathVariable String user, @RequestParam DateTime beginning, @RequestParam DateTime end) {
        
        return this.measurementService.getByUserAndTimestamp(user, beginning.toString(), end.toString());
    }
   
//    @GetMapping("/measurements/{id}")
//    public List<Measurement> getMeasurementsByTag(@PathVariable Long id) {
//        
//        
//    }
//    
//    @GetMapping("/last")
//    public Measurement getLatestMeasurement() {
//        
//    }
//    
//    
//    @PostMapping("/queryData")
//    public Measurement getQueryData() {
//        
//    }
    
}
