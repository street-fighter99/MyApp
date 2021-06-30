package com.example.myapplication.UserHelper;

public class ServiceHelper {
    String name;
    String service;
    String phone;
    String place;
    String user;

    public ServiceHelper() {
    }

    public ServiceHelper(String name, String service, String phone, String place, String user) {
        this.name = name;
        this.service = service;
        this.phone = phone;
        this.place = place;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
