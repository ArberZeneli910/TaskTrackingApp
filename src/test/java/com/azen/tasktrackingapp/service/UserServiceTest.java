package com.azen.tasktrackingapp.service;

import com.azen.tasktrackingapp.exceptions.ResourceNotFoundException;
import com.azen.tasktrackingapp.models.User;
import com.azen.tasktrackingapp.repositories.UserRepository;
import com.azen.tasktrackingapp.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUser()
    {
        User user = new User();
        user.setUsername("johndoe");
        user.setEmail("john@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertThat(result.getUsername()).isEqualTo("johndoe");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldReturnUserWhenFound()
    {
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getUsername()).isEqualTo("johndoe");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound()
    {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}