package com.example.mapper;

import com.example.entity.User;
import com.example.model.UpdateUserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void updateFromRequest_ShouldUpdateFields_WhenRequestHasValues() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("originalUser")
                .email("original@example.com")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setUsername("updatedUser");
        request.setEmail("updated@example.com");

        // When
        User result = userMapper.updateFromRequest(user, request);

        // Then
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@example.com", result.getEmail());
        assertNotNull(result.getUpdatedAt());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getActive(), result.getActive());
        assertEquals(user.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void updateFromRequest_ShouldNotUpdateFields_WhenRequestHasNullValues() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("originalUser")
                .email("original@example.com")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setUsername(null);
        request.setEmail(null);

        // When
        User result = userMapper.updateFromRequest(user, request);

        // Then
        assertEquals("originalUser", result.getUsername());
        assertEquals("original@example.com", result.getEmail());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void updateFromRequest_ShouldUpdateOnlyProvidedFields_WhenRequestHasPartialValues() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("originalUser")
                .email("original@example.com")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setUsername("updatedUser");
        // email is null

        // When
        User result = userMapper.updateFromRequest(user, request);

        // Then
        assertEquals("updatedUser", result.getUsername());
        assertEquals("original@example.com", result.getEmail());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void updateActiveField_ShouldSetActiveFieldAndUpdatedAt_WhenCalled() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        // When
        User result = userMapper.updateActiveField(user, false);

        // Then
        assertFalse(result.getActive());
        assertNotNull(result.getUpdatedAt());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void updateActiveField_ShouldSetActiveFieldToTrue_WhenCalled() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .active(false)
                .createdAt(LocalDateTime.now())
                .build();

        // When
        User result = userMapper.updateActiveField(user, true);

        // Then
        assertTrue(result.getActive());
        assertNotNull(result.getUpdatedAt());
    }
}