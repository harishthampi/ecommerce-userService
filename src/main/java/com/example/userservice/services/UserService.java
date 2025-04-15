package com.example.userservice.services;

import com.example.userservice.exceptions.UnauthorizedException;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;

public interface UserService {
    User signUp(String name, String email, String password);
    Token login(String email, String password) throws UserNotFoundException, UnauthorizedException;
    void logout(String tokenValue);
    User validateToken(String tokenValue);
}
