package com.phoenix.blocker.controller;

import com.phoenix.blocker.dao.Person;
import com.phoenix.blocker.service.FirebaseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class UserController {

    @Autowired
    private FirebaseServices firebaseServices;

    @GetMapping("/getUserDetails")
    public Person getUserDetails(@RequestHeader String name) throws InterruptedException, ExecutionException {
        return firebaseServices.getUserDetails(name);
    }

    @PostMapping("/createUser")
    public String createNewUser(@RequestBody Person person) throws InterruptedException, ExecutionException {
        return firebaseServices.saveUserDetails(person);
    }

    @PutMapping("/updateUser")
    public String updateUser(@RequestBody Person person) {
        return "Updated user "+person.getName();
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestHeader String name) throws InterruptedException, ExecutionException {
        return firebaseServices.deleteUser(name);
    }
}
