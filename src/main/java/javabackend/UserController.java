/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javabackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author katri
 */
@RestController
public class UserController {
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PostMapping("/sign-up")
    public void signUp(@RequestBody UserInfo user) {
        
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setInfoTag("userdata");
        
        this.userInfoService.save(user);
        
    }
    
    
}
