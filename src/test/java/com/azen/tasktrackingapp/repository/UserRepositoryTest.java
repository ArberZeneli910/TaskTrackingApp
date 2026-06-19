package com.azen.tasktrackingapp.repository;

import com.azen.tasktrackingapp.models.User;
import com.azen.tasktrackingapp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest
{
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser()
    {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldFindUserById()
    {
        User user = new User();
        user.setUsername("janedoe");
        user.setEmail("jane@example.com");
        user.setPassword("password456");
        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("jane@example.com");
    }
}