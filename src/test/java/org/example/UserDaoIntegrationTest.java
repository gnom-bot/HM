package org.example;

import org.example.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    private SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()

                .applySetting("hibernate.connection.url", postgreSQLContainer.getJdbcUrl())
                .applySetting("hibernate.connection.username", postgreSQLContainer.getUsername())
                .applySetting("hibernate.connection.password", postgreSQLContainer.getPassword())
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop") // Создавать и удалять схему для каждого теста
                .applySetting("hibernate.show_sql", "true")
                .applySetting("hibernate.format_sql", "true")

                .build();
        MetadataSources sources = new MetadataSources(registry);
        sources.addAnnotatedClass(User.class); // Регистрируем нашу сущность
        sessionFactory = sources.buildMetadata().buildSessionFactory();


        try {
            userDao = new UserDao();
            // Внедряем SessionFactory, созданный для тестов
            java.lang.reflect.Field sessionFactoryField = UserDao.class.getDeclaredField("sessionFactory");
            sessionFactoryField.setAccessible(true);
            sessionFactoryField.set(userDao, sessionFactory);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Ошибка внедрения SessionFactory в UserDao", e);
        }
    }

    @AfterAll
    static void tearDownAll() {
        if (postgreSQLContainer.isRunning()) {
            postgreSQLContainer.stop();
        }
    }

    @Test
    void save_shouldPersistUser() {
        User user = new User("Test", "User", "test@example.com");
        userDao.save(user);
        assertNotNull(user.getId());

        try (var session = sessionFactory.openSession()) {
            User retrievedUser = session.get(User.class, user.getId());
            assertEquals("Test", retrievedUser.getFirstName());
            assertEquals("User", retrievedUser.getLastName());
            assertEquals("test@example.com", retrievedUser.getEmail());
        }
    }

    @Test
    void findById_shouldReturnExistingUser() {
        User user = new User("Find", "User", "find@example.com");
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction(); // Начинаем транзакцию
            session.persist(user);
            session.getTransaction().commit(); // Фиксируем транзакцию
        }
        User retrievedUser = userDao.findById(user.getId());
        assertNotNull(retrievedUser);
        assertEquals("Find", retrievedUser.getFirstName());
    }
    @Test
    void findById_shouldReturnNullIfUserNotFound() {
        User retrievedUser = userDao.findById(999L);
        assertNull(retrievedUser);
    }

    @Test
    void update_shouldUpdateExistingUser() {
        User user = new User("Update", "User", "update@example.com");
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction(); // Начинаем транзакцию
            session.persist(user);
            session.getTransaction().commit(); // Фиксируем транзакцию
        }
        user.setFirstName("Updated");
        userDao.update(user);

        try (var session = sessionFactory.openSession()) {
            User updatedUser = session.get(User.class, user.getId());
            assertEquals("Updated", updatedUser.getFirstName());
        }
    }

    @Test
    void delete_shouldRemoveExistingUser() {
        User user = new User("Delete", "User", "delete@example.com");
        try (var session = sessionFactory.openSession()) {
            session.beginTransaction(); // Начинаем транзакцию
            session.persist(user);
            session.getTransaction().commit();
        }

        userDao.delete(user.getId());

        try (var session = sessionFactory.openSession()) {
            User deletedUser = session.get(User.class, user.getId());
            assertNull(deletedUser);
        }
    }
}