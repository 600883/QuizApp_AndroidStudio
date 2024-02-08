package com.example.quizapp2;

import android.net.Uri;
import android.util.Log;

import java.io.File;

public class FileUtils {

    public static String getFileNameFromUri(Uri uri) {
        String filename = null;
        if(uri != null) {
            String path = uri.getPath();
            Log.d("info", path);
            if(path != null) {
                filename = new File(path).getName();
                int dotIndex = filename.lastIndexOf(".");
                if (dotIndex != -1) {
                   filename =  filename.substring(0, dotIndex);
                }
            }
        }

        return filename != null ? filename : "";
    }
}
