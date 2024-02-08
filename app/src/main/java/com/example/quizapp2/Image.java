package com.example.quizapp2;

import android.net.Uri;

public class Image {

    private String name;
    private Uri uri;

    public Image(String name, Uri uri){
        this.uri = uri;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

}