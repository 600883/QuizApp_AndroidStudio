package com.example.quizapp2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
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

    @SuppressLint("Range")
    public static String getFilenameToTextView(Context context, Uri uri) {
        if (uri == null) return "";
        String filename = null;

        // Check if the URI is a content URI
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                Log.e("FileUtils", "Failed to get filename from URI", e);
            }
        } else if (uri.getScheme().equals("file")) {
            filename = new File(uri.getPath()).getName();
        }

        if (filename != null) {
            int dotIndex = filename.lastIndexOf(".");
            if (dotIndex != -1) {
                filename = filename.substring(0, dotIndex);
            }
        }
            return filename != null ? filename : "";
        }
    }
