package com.tpe.controller;


import com.tpe.dto.UserRequestDTO;
import com.tpe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //localhost:8080/register + POST  + JSON body
    @RequestMapping("/register")
    @PostMapping
    public ResponseEntity<String> register(@Valid @RequestBody UserRequestDTO userRequestDTO){

        userService.saveUser(userRequestDTO);


        return new ResponseEntity<>("User is registered successfully.", HttpStatus.CREATED);
    }

}
