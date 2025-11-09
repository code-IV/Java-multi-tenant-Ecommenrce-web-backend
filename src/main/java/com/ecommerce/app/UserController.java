package com.ecommerce.app;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;

import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    @GetMapping("/api/users")
    @ResponseBody
    private List<User> getUser(){
        return userRepo.findAll();
    }
    
    @GetMapping("/api/user/{id}")
    @ResponseBody
    private ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user = userRepo.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/user/uid/{uid}")
    @ResponseBody
    private ResponseEntity<User> getUserByUID(@PathVariable UUID uid){
        Optional<User> user = userRepo.findByUid(uid);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/user")
    private ResponseEntity<User> createUser(@Valid @RequestBody User newUser, BindingResult bindingResult){
        System.out.println("Incoming User: " + newUser);
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> System.out.println(error));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newUser);
        }

        User savedUser = userRepo.save(newUser);
        System.out.println("Saved User: " + savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    @PatchMapping("/api/user/{id}")
    private ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody User user){
        Optional<User> existingUserOpt = userRepo.findById(id);
        if(existingUserOpt.isPresent()){
            User existingUser = existingUserOpt.get();
            if(user.getUname() != null){
                existingUser.setUname(user.getUname());
            }
            if(user.getEmail() != null){
                existingUser.setEmail(user.getEmail());
            }
            if(user.getPhoneNumber() != null){
                existingUser.setPhoneNumber(user.getPhoneNumber());
            }
            User updatedUser = userRepo.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
