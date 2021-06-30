package com.example.myapplication.Models;

public class ServiceModel {
    String name;
    String service;
    String phone;
    String place;
    String key;
    String user;

    public ServiceModel() {
    }

    public ServiceModel(String name, String service, String phone, String place, String key, String user) {
        this.name = name;
        this.service = service;
        this.phone = phone;
        this.place = place;
        this.key = key;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}

