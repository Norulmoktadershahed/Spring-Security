package com.shahed.SpringSecEx.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "user_info")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Column(unique = true, nullable = false, length = 50)
    private String userName;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Override
    public String toString() {
        return "Users{" + "Id=" + Id + ", userName='" + userName + '\'' + ", password='" + password + '\'' + '}';
    }
}
