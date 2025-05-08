package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleInputHandler {

    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public ConsoleInputHandler(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void handleConsoleInput() {
        System.out.println("Консольный ввод данных пользователя:");

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Выйти");
            System.out.print("Ваш выбор: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createUserFromConsole();
                    break;
                case "2":
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Некорректный выбор.");
            }
        }
    }

    private void createUserFromConsole() {
        System.out.print("Введите имя: ");
        String firstName = scanner.nextLine();

        System.out.print("Введите фамилию: ");
        String lastName = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        CreateUserRequestDto requestDto = new CreateUserRequestDto();
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setEmail(email);

        userService.createUser(requestDto);
        System.out.println("Пользователь успешно создан.");
    }
}