package com.example.tride;

import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.annotations.SerializedName;

public class post {
    @SerializedName("body")
    private Number fromTime;

    public post(Number fromTime) {
        this.fromTime = fromTime;
    }
}
