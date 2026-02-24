package com.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailEventDto{
    private String to;
    private String from;
    private String subject;
    private String message;
}
