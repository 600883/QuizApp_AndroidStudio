package com.example.quizapp2.room;

import android.net.Uri;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static Uri fromString(String uri) {
        return uri == null ? null : Uri.parse(uri);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }
}
