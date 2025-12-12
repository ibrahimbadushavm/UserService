package com.userservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Session extends BaseModel {
    @Column(columnDefinition = "TEXT")
    private String token;
    @ManyToOne
    private User user;
    private SessionStatus status;
    private Date expiringAt;
}
