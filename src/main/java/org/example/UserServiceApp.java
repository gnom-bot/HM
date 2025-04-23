package org.example;
import java.util.Scanner;

public class UserServiceApp {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Найти пользователя по ID");
            System.out.println("3. Обновить пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("5. Выйти");
            System.out.print("Введите номер действия: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createUser(scanner, userDao);
                    break;
                case "2":
                    findUser(scanner, userDao);
                    break;
                case "3":
                    updateUser(scanner, userDao);
                    break;
                case "4":
                    deleteUser(scanner, userDao);
                    break;
                case "5":
                    userDao.closeSessionFactory();
                    System.out.println("Приложение завершено.");
                    return;
                default:
                    System.out.println("Некорректный ввод. Пожалуйста, попробуйте еще раз.");
            }
        }
    }

    private static void createUser(Scanner scanner, UserDao userDao) {
        System.out.print("Введите имя: ");
        String firstName = scanner.nextLine();
        System.out.print("Введите фамилию: ");
        String lastName = scanner.nextLine();
        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        User newUser = new User(firstName, lastName, email);
        userDao.save(newUser);
        System.out.println("Пользователь успешно создан с ID: " + newUser.getId());
    }

    private static void findUser(Scanner scanner, UserDao userDao) {
        System.out.print("Введите ID пользователя для поиска: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            User user = userDao.findById(id);
            if (user != null) {
                System.out.println("Найден пользователь: " + user);
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный формат ID.");
        }
    }

    private static void updateUser(Scanner scanner, UserDao userDao) {
        System.out.print("Введите ID пользователя для обновления: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            User existingUser = userDao.findById(id);
            if (existingUser != null) {
                System.out.print("Введите новое имя (" + existingUser.getFirstName() + "): ");
                String firstName = scanner.nextLine();
                System.out.print("Введите новую фамилию (" + existingUser.getLastName() + "): ");
                String lastName = scanner.nextLine();
                System.out.print("Введите новый email (" + existingUser.getEmail() + "): ");
                String email = scanner.nextLine();

                existingUser.setFirstName(firstName);
                existingUser.setLastName(lastName);
                existingUser.setEmail(email);

                userDao.update(existingUser);
                System.out.println("Пользователь с ID " + id + " успешно обновлен.");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный формат ID.");
        }
    }

    private static void deleteUser(Scanner scanner, UserDao userDao) {
        System.out.print("Введите ID пользователя для удаления: ");
        try {
            Long id = Long.parseLong(scanner.nextLine());
            userDao.delete(id);
            System.out.println("Пользователь с ID " + id + " успешно удален.");
        } catch (NumberFormatException e) {
            System.out.println("Некорректный формат ID.");
        }
    }
}