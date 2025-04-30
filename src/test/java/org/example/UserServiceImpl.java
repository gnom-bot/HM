package org.example;

import org.example.UserDao;
import org.example.User;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(User user) {
        // Дополнительная бизнес-логика может быть здесь
        userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        // Дополнительная бизнес-логика может быть здесь
        return userDao.findById(id);
    }

    @Override
    public void updateUser(User user) {
        // Дополнительная бизнес-логика может быть здесь
        userDao.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        // Дополнительная бизнес-логика может быть здесь
        userDao.delete(id);
    }
}