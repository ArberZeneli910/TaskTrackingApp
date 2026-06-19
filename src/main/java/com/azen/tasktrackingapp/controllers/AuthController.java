package com.azen.tasktrackingapp.controllers;

import com.azen.tasktrackingapp.dtos.LoginRequest;
import com.azen.tasktrackingapp.dtos.LoginResponse;
import com.azen.tasktrackingapp.security.CustomUserDetails;
import com.azen.tasktrackingapp.security.CustomUserDetailsService;
import com.azen.tasktrackingapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request)
    {
        try
        {
            CustomUserDetails userDetails =
                    (CustomUserDetails) userDetailsService.loadUserByUsername(request.getUsername());

            if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword()))
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new LoginResponse(token, userDetails.getId(), userDetails.getUsername()));

        } catch (UsernameNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}