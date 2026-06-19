package com.azen.tasktrackingapp.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse
{
    private String token;
    private Long userId;
    private String username;
}