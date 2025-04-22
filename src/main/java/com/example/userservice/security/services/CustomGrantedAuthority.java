package com.example.userservice.security.services;

import com.example.userservice.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


public class CustomGrantedAuthority implements GrantedAuthority {
    private Role role;
    public CustomGrantedAuthority(Role role){
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return null;
    }
}
