package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        User user = new User();
        user.setFirstName(createUserRequestDto.getFirstName());
        user.setLastName(createUserRequestDto.getLastName());
        user.setEmail(createUserRequestDto.getEmail());
        User savedUser = userRepository.save(user);
        return toUserResponseDto(savedUser);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));
        return toUserResponseDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto updateUser(Long id, UpdateUserRequestDto updateUserRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + id + " не найден"));

        if (updateUserRequestDto.getFirstName() != null) {
            existingUser.setFirstName(updateUserRequestDto.getFirstName());
        }
        if (updateUserRequestDto.getLastName() != null) {
            existingUser.setLastName(updateUserRequestDto.getLastName());
        }
        if (updateUserRequestDto.getEmail() != null) {
            existingUser.setEmail(updateUserRequestDto.getEmail());
        }

        User updatedUser = userRepository.save(existingUser);
        return toUserResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("Пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }

    private UserResponseDto toUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}