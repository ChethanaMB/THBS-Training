package com.bookapi.book.Controller.Auth;

import com.bookapi.book.DB.BookRepo;
import com.bookapi.book.DB.UserRepo;
import com.bookapi.book.Models.AuthBook;
import com.bookapi.book.Models.AuthModel;
import com.bookapi.book.Models.Book;
import com.bookapi.book.Models.User;
import com.bookapi.book.response;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired 
    JavaMailSender javaMailSender;

    @Autowired
    UserRepo userRepo;

    @Autowired 
    BookRepo bookRepo;

    @PostMapping("/register")// registration
    public response addUser(@RequestBody User user){
        if(userRepo.count()>0){
            List<User>list =userRepo.findAll();
            for (User usr : list) {
                if(usr.getEmail().equals(user.getEmail())){
                    return new response(400, "alread exit",user);
                } 
            }
        }

        Random r = new Random();
        String s = String.format("%04d", r.nextInt(1000));
        SimpleMailMessage msg= new SimpleMailMessage();
        msg.setTo(user.getEmail());
        user.setUid(UUID.randomUUID().toString());
        userRepo.insert(user);

        msg.setSubject("Authentiation code");
        msg.setText("hello i am here to check my program is running fine or not . thabku!, your code is" +s);
        try{
            javaMailSender.send(msg);
            return new response(200, "registered sucessfully", user);
        }catch(Exception e){
            return new response(404, "error",null);

        }
    }
    @PostMapping("/login") // login 
    public response userLogin(@RequestBody AuthModel authModel){
        if(userRepo.count()>0){
           List<User> list= userRepo.findAll();
           for (User u : list) {
               if(u.getEmail().equals(authModel.getEmail()) && u.getPassword().equals(authModel.getPassword())){
                   return new response(200, "login sucess", u);
               }  
           }
        }
        return new response(404, "no user in db", authModel);
    }

    @PostMapping("/addBook")// it will add book in database
    public response addBook(@RequestBody Book book){
        if(bookRepo.count()>0){
            List<Book>list =bookRepo.findAll();
            for (Book b : list) {
                if(b.getName().equals(book.getName())){
                    return new response(200, "alread exit",book);
                } 
            }
        }

        try{
            book.setId(UUID.randomUUID().toString());
            bookRepo.insert(book);
            return new response(200, "book inserted sucessfully", book);
        }catch(Exception e){
            return new response(404, "opps found some issue", e);
        }
    }

    @GetMapping("/getAllBook")// it will return all books details from data base
    public response getAllBook(){
        List<Book> list = bookRepo.findAll();     
        return new response(200, "match  found", list);
    }

    @PostMapping("/getBook")
    public response getBook(@RequestBody AuthBook authBook){
        if(bookRepo.count()>0){
            List<Book> list = bookRepo.findAll();
            for (Book bk : list) {
                if(bk.getName().equals(authBook.getName())){
                    return new response(200, "match found", bk);
                }   
            }
        }
        return new response(400, "match not found",authBook );
    }

    //deleting entry from data base
    @PostMapping("/deleteBook")
    public response deleteBook(@RequestBody Book book){
        List<Book> list =bookRepo.findAll();
        for (Book book2 : list) {
            if(book2.getId().equals(book.getId())){
                deleteBook(book);
                return new response(200, "book deleted sucessfully", book);
            }  
        }
        return new response(404, "match not found", null);
    }

    //updating entry of data base
    @PostMapping("/updateBook")
    public response updateBook(@RequestBody Book book){
        if(bookRepo.existsById(book.getId())){// it will check the id we enter is present in bookrepo or not
            Book bok = bookRepo.findById(book.getId()).get();//it will take id from  out entry and store in bok 
            bok.setName(book.getName());////here we set name from our entry to bok
            bok.setAuthor(book.getAuthor());// here we set author form our entry to bok
            bookRepo.save(bok);//here we sae bok in out bookrepo and it will update our data
            return new response(200,"editied sucessfully", bok);
        }
        return new response(404,"book not found", null);
    }
    
}