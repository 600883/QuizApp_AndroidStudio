package com.example.quizapp2.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {QuizAppEntity.class}, version = 2, exportSchema = false)
public abstract class QuizAppRoomDatabase extends RoomDatabase {
    public abstract QuizAppDAO quizAppDAO();

    private static volatile QuizAppRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static QuizAppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuizAppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, QuizAppRoomDatabase.class, "image-database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
