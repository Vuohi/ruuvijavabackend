/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TagController {
    
    @Autowired
    private TagService tagService;
    
    @GetMapping("/tags/{user}")
    public List<Tag> getTags(@PathVariable String user) throws JsonProcessingException {
        return tagService.getTagsByUsername(user);
    }
    
    @PostMapping("/tags/{user}")
    public JsonNode addTag(@PathVariable String user, @RequestBody Tag tag) throws JsonProcessingException {
        return this.tagService.updateTags(tag, user);
    }
}
