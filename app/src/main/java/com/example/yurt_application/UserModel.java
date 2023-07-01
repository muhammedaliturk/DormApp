package com.example.yurt_application;

public class UserModel {
    private String name,surname,email,password,id,kurumid,onay,imageUrl,kat,oda;




    public UserModel(String name, String surname, String email, String password, String id, String kurumid, String onay, String imageUrl, String kat, String oda) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.id = id;
        this.kurumid = kurumid;
        this.onay = onay;
        this.imageUrl = imageUrl;
        this.kat=kat;
        this.oda=oda;
    }
    public String getKat() {
        return kat;
    }

    public void setKat(String kat) {
        this.kat = kat;
    }

    public String getOda() {
        return oda;
    }

    public void setOda(String oda) {
        this.oda = oda;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getOnay() {
        return onay;
    }

    public void setOnay(String onay) {
        this.onay = onay;
    }
    public String getKurumid() {
        return kurumid;
    }

    public void setKurumid(String kurumid) {
        this.kurumid = kurumid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
