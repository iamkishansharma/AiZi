package com.heycode.aizi.models;

import androidx.annotation.Keep;

@Keep
public class UserDataModel {
    public String name, dob, email, imageUrl;

    public UserDataModel(String name, String dob, String email, String imageUrl) {

        this.name = name;
        this.dob = dob;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public UserDataModel() {
        //This is needed for getting data
    }
}
