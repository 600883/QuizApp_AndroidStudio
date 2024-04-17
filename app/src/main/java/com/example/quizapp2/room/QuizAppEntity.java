package com.example.quizapp2.room;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "images_table")
public class QuizAppEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "uri")
    private String uri;

    @ColumnInfo(name = "imageResId")
    private int imageResId;

    @ColumnInfo(name = "name")
    private String name;


    // Constructor for both URI and drawable resource ID
    // You need to determine how to utilize the uri and imageResId parameters
    public QuizAppEntity(int imageResId, String uri, String name) {
        this.imageResId = imageResId;
        this.uri = uri;
        this.name = name;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public boolean isDrawable() {
        return imageResId != 0;
    }

    public boolean isUri() {
        return uri != null && !uri.isEmpty();
    }


}
