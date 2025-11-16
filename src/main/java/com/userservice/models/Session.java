package com.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Session extends BaseModel {
    private String token;
    @ManyToOne
    private User user;
}
