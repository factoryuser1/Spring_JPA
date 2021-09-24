package com.example.demo.Model;

public class Person {

    private Long id;
    private String firstName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Number getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
