package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("John", "Doe", "john.doe@example.com");
        testUser.setId(1L);
    }

    @Test
    void createUser_shouldCallUserDaoSave() {
        userService.createUser(testUser);
        Mockito.verify(userDao, Mockito.times(1)).save(testUser);
    }

    @Test
    void getUserById_shouldCallUserDaoFindByIdAndReturnUser() {
        Mockito.when(userDao.findById(1L)).thenReturn(testUser);
        User result = userService.getUserById(1L);
        Assertions.assertEquals(testUser, result);
        Mockito.verify(userDao, Mockito.times(1)).findById(1L);
    }

    @Test
    void getUserById_shouldReturnNullIfUserNotFound() {
        Mockito.when(userDao.findById(ArgumentMatchers.anyLong())).thenReturn(null);
        User result = userService.getUserById(2L);
        Assertions.assertNull(result);
        Mockito.verify(userDao, Mockito.times(1)).findById(2L);
    }

    @Test
    void updateUser_shouldCallUserDaoUpdate() {
        userService.updateUser(testUser);
        Mockito.verify(userDao, Mockito.times(1)).update(testUser);
    }

    @Test
    void deleteUser_shouldCallUserDaoDelete() {
        userService.deleteUser(1L);
        Mockito.verify(userDao, Mockito.times(1)).delete(1L);
    }
}