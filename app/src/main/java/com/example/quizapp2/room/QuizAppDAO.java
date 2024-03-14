package com.example.quizapp2.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizAppDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(QuizAppEntity imageAndUri);

    @Query("DELETE FROM images_table")
    void deleteImage(QuizAppEntity image);

    @Query("SELECT * FROM images_table")
    LiveData<List<QuizAppEntity>> getAllImages();





}
