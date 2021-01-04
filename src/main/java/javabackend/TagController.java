/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public List<TagData> getTags(@PathVariable String user) {
        return tagService.getTagsByUsername(user);
    }
    
    @PutMapping("/tags/{user}")
    public TagData updateTag(@PathVariable String user, @RequestBody TagData tagData) {
        return this.tagService.updateTagData(tagData, user);
    }
    
    @PostMapping("/tags/{user}")
    public TagData addTag(@PathVariable String user, @RequestBody TagData tagData) {
        return this.tagService.addTag(tagData, user);
    }
    
}
