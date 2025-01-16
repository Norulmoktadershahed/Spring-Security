package com.shahed.SpringSecEx.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_info")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String userName;
    private String password;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Users{" + "Id=" + Id + ", userName='" + userName + '\'' + ", password='" + password + '\'' + '}';
    }
}
