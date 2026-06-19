package com.azen.tasktrackingapp.services;

import com.azen.tasktrackingapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService
{
    User createUser(User user);
    User getUserById(Long id);
    Page<User> getAllUsers(Pageable pageable);
}
