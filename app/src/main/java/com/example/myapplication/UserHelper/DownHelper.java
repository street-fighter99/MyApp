package com.example.myapplication.UserHelper;

public class DownHelper {
    String Name,Description,imgUrl;

    public DownHelper() { }

    public DownHelper(String name, String description, String imgUrl) {
        Name = name;
        Description = description;
        this.imgUrl = imgUrl;
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
