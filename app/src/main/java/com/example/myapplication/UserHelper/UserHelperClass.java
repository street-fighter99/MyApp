package com.example.myapplication.UserHelper;

public class UserHelperClass {
    String name, phone, bloodgroup, places,user;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String phone, String bloodgroup, String places, String user) {
        this.name = name;
        this.phone = phone;
        this.bloodgroup = bloodgroup;
        this.places = places;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }
}

