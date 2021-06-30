package com.example.myapplication.UserHelper;

public class DownHelper {
    String Name,Description,imgUrl,user;

    public DownHelper() {
    }

    public DownHelper(String name, String description, String imgUrl, String user) {
        Name = name;
        Description = description;
        this.imgUrl = imgUrl;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
