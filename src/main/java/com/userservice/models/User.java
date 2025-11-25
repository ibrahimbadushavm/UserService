package com.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseModel{
    private String userName;
    private String password;
    private String email;
    @OneToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
