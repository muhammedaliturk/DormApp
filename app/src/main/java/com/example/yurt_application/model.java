package com.example.yurt_application;

public class model {
    String email,name,surname,onay;

    public model() {
    }

    public model(String email, String name, String surname, String onay) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.onay = onay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOnay() {
        return onay;
    }

    public void setOnay(String onay) {
        this.onay = onay;
    }
}
