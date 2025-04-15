package com.example.userservice.controller;

import com.example.userservice.dtos.LoginRequestDto;
import com.example.userservice.dtos.LogoutRequestDto;
import com.example.userservice.dtos.SignUpRequestDto;
import com.example.userservice.dtos.UserDto;
import com.example.userservice.exceptions.UnauthorizedException;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.userservice.models.Token;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user =  userService.signUp(
                signUpRequestDto.getName(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword()
                );
        //convert this user job to userDto
        return UserDto.from(user);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto) throws UserNotFoundException, UnauthorizedException {
        return userService.login(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        userService.logout(logoutRequestDto.getTokenValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable  String tokenValue){
        User user = userService.validateToken(tokenValue);
        System.out.println("roles" + user.getRoles());
        return UserDto.from(user);
    }
}
