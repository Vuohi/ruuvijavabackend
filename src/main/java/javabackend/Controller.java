/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;


import java.time.Instant;
import java.util.List;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public List<List<Measurement>> getMeasurements(@PathVariable String user) {
        String end = String.valueOf(Instant.now().getEpochSecond());
        String beginning = String.valueOf(Instant.now().getEpochSecond() - 86400);
        return this.measurementService.arrangeByTag(this.measurementService.getByUserAndTimestamp(user, beginning, end));
        
    }
    
    @PostMapping("/measurements/{user}")
    public ResponseEntity<List<List<Measurement>>> getTimePeriod(@PathVariable String user, @RequestBody TimePeriod timePeriod) {
        DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
        String beginning = String.valueOf(parser.parseDateTime(timePeriod.getBeginning()).getMillis());
        String end = String.valueOf(parser.parseDateTime(timePeriod.getEnd()).getMillis());
        if (Long.parseLong(end) - Long.parseLong(beginning) > 1814400000) {
            System.out.println(Long.parseLong(end));
            System.out.println(Long.parseLong(beginning));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(this.measurementService.arrangeByTag(this.measurementService.getByUserAndTimestamp(user, beginning, end)), HttpStatus.OK);
        
    }
    
    @PostMapping("/measurements/{user}/add")
    public void addMeasurement(@RequestBody List<Measurement> measurements) {
        for (Measurement measurement : measurements) {
            this.measurementService.save(measurement);
        }
    }
    
    @GetMapping("/measurements/{user}/latest")
    public List<Measurement> getLatestMeasurement(@PathVariable String user) {
        return this.measurementService.getLatestMeasurements(user);
    }
    
    
    @PostMapping("/querydata")
    public GoogleResponse getQueryData(@RequestBody GoogleRequest req) {
        return this.measurementService.getQueryData(req);
    }
    
}
