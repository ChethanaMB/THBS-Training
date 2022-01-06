package com.bookapi.book.Controller.Auth;

import java.util.List;
import java.util.*;

import com.bookapi.book.response;
import com.bookapi.book.DB.UserRepo;
import com.bookapi.book.Models.AuthModel;
import com.bookapi.book.Models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthEmail {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
	UserRepo userRepo;

    @PostMapping("/register")
	public response doRegister(@RequestBody User user) {
        
        if(userRepo.count()>0){
            List<User> list=userRepo.findAll();
            for (User usr : list) {
                if(usr.getEmail().equals(user.getEmail())){
                    return new response(404,"User exits",user);
                }
            }
        }

        Random r=new Random();
        String s=String.format("%04d", r.nextInt(10000));
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail());
        user.setUid(UUID.randomUUID().toString());
        userRepo.insert(user);

        msg.setSubject("Authentication code");
        msg.setText("Auth code:"+s+" \n \t This code will xpire in 30sec");
        try{
            javaMailSender.send(msg);
            return new response(200,"registered succesfully",user);
        }
        catch(Exception e){
            return new response(404,"error",null);
        }

    }

    @PostMapping("/login")
	public response doLogin(@RequestBody AuthModel authModel) {
    if(userRepo.count() > 0){
        List<User> list=userRepo.findAll();
        for(User u : list){
            if(u.getEmail().equals(authModel.getEmail()) && u.getPassword().equals(authModel.getPassword())){
               return new response(200,"Logged succesfully",u);
            }
            else{
                return new response(404,"User not found",u);
            }
        }
    }   
        return new response(404,"no user in db",authModel);
    }
}
