package com.example.quizapp2.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class QuizAppRepository {

    private QuizAppDAO quizAppDAO;

    private LiveData<List<QuizAppEntity>> allImages;

    public LiveData<List<QuizAppEntity>> getAllImages() {
        return allImages;
    }

    public QuizAppRepository(Application application) {
        QuizAppRoomDatabase db = QuizAppRoomDatabase.getDatabase(application);
        quizAppDAO = db.quizAppDAO();
        allImages = quizAppDAO.getAllImages();
    }

    public void insert(QuizAppEntity image) {
        QuizAppRoomDatabase.databaseWriteExecutor.execute(() -> {
            quizAppDAO.insert(image);
        });
    }

    void delete(long id) {
        QuizAppRoomDatabase.databaseWriteExecutor.execute(() -> {
            quizAppDAO.deleteImage(id);
        });
    }



}
