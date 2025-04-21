package com.example.userservice.services;

import com.example.userservice.exceptions.UnauthorizedException;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImp implements UserService{
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;
    public UserServiceImp(UserRepository userRepository,TokenRepository tokenRepository,BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User signUp(String name, String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            //redirect to login page
            return optionalUser.get();
        }
        User user = new  User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(new ArrayList<>());
        return userRepository.save(user);
    }

    @Override
    public Token login(String email, String password) throws UserNotFoundException, UnauthorizedException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            //redirect to signup page
            throw  new UserNotFoundException("User with email: "+email+" not found");
        }
        //if user is present,check if the password matches
        User user = optionalUser.get();
        if(passwordEncoder.matches(password, user.getPassword())){
            //login successful,Create a token
            Token token = new Token();
            token.setUser(user);
            token.setValue(RandomStringUtils.randomAlphanumeric(120));
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH,30);
            Date dateAfter30Days = calendar.getTime();
            token.setExpiryDate(dateAfter30Days);
            return tokenRepository.save(token);
        }
        //loginfailed
        throw new UnauthorizedException("Login Failed");

    }

    @Override
    public void logout(String tokenValue) {
        Optional<Token> optionalToken = tokenRepository.findByValue(tokenValue);
        if(optionalToken.isEmpty()){
            throw  new RuntimeException("Token invalid.");
        }
        Token token = optionalToken.get();
        token.setIsDeleted(true);
        tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) {
        //check if token is present in DB. and the expiryDate is greater than currentDate
        Date currentDate = new Date();
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryDateGreaterThan(tokenValue,false,currentDate);
        //token invalid
        return optionalToken.map(Token::getUser).orElse(null);

    }
}
