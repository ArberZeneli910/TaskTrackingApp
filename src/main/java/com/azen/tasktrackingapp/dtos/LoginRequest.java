package com.azen.tasktrackingapp.dtos;

import lombok.Data;

@Data
public class LoginRequest
{
    private String username;
    private String password;
}