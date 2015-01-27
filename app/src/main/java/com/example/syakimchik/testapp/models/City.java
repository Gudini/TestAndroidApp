package com.example.syakimchik.testapp.models;

import com.example.syakimchik.testapp.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * Created by syakimchik on 1/27/2015.
 */
public class City {

    @SerializedName("id")
    private long mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("image")
    private String mImageUrl;

    public String getName(){
        return mName;
    }

    public String getImageUrl(){
        return Constants.HOST+mImageUrl;
    }
}
