package com.userservice.models;

import jakarta.persistence.Entity;

@Entity(name = "users")
public class User extends BaseModel{
    private String username;
    private String password;
    private String email;
}
