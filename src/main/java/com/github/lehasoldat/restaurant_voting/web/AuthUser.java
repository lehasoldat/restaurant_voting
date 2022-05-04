package com.github.lehasoldat.restaurant_voting.web;

import com.github.lehasoldat.restaurant_voting.model.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = "user")
public class AuthUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public AuthUser(User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
    }

}
