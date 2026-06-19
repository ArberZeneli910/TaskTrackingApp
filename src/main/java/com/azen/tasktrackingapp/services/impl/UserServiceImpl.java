package com.azen.tasktrackingapp.services.impl;

import com.azen.tasktrackingapp.exceptions.ResourceNotFoundException;
import com.azen.tasktrackingapp.models.User;
import com.azen.tasktrackingapp.repositories.UserRepository;
import com.azen.tasktrackingapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable)
    {
        return userRepository.findAll(pageable);
    }
}
