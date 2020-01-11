/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;


import com.amazonaws.util.DateUtils;
import java.time.Instant;
import java.util.List;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public List<List<Measurement>> getMeasurements(@PathVariable String user) {
        String end = String.valueOf(Instant.now().getEpochSecond());
        String beginning = String.valueOf(Instant.now().getEpochSecond() - 86400);
        return this.measurementService.arrangeByTag(this.measurementService.getByUserAndTimestamp(user, beginning, end));
        
    }
    
    @PostMapping("/measurements/{user}")
    public List<List<Measurement>> getTimePeriod(@PathVariable String user, @RequestBody TimePeriod timePeriod) {
        DateTimeFormatter parser = ISODateTimeFormat.dateHour();
        String beginning = String.valueOf(parser.parseDateTime(timePeriod.getBeginning()).getMillis());
        String end = String.valueOf(parser.parseDateTime(timePeriod.getEnd()).getMillis());
        
        return this.measurementService.arrangeByTag(this.measurementService.getByUserAndTimestamp(user, beginning, end));
        
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
