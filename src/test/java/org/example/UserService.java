package org.example;

import org.example.UserDao;
import org.example.User;

public interface UserService {
    void createUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
}