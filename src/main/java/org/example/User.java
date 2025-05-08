package org.example;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") // Рекомендуется явно указывать имя таблицы
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Добавьте эту аннотацию
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
}