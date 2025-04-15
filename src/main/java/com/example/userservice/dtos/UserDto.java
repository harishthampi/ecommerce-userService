package com.example.userservice.dtos;

import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private List<String> roles;

    public static UserDto from(User user){ //need to allow the other class to call this function without creating an object so use static keyword

        if(user == null){
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(new ArrayList<>());
        for(Role role : user.getRoles()){
            System.out.println("roles " +user.getRoles());
            userDto.getRoles().add(role.getValue());
        }
        return userDto;
    }
}
