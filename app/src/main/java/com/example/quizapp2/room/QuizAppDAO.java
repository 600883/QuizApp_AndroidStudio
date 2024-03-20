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

    @Query("DELETE FROM images_table WHERE id = :id")
    void deleteImage(long id);

    @Query("SELECT * FROM images_table")
    LiveData<List<QuizAppEntity>> getAllImages();

    @Query("SELECT * FROM images_table ORDER BY name ASC")
    LiveData<List<QuizAppEntity>> getAllImagesSortedByName();

    @Query("SELECT * FROM images_table WHERE id IN (:imageIds)")
    LiveData<List<QuizAppEntity>> getImagesByIds(List<Long> imageIds);

    @Query("SELECT name FROM images_table WHERE name != :correctImageName")
    LiveData<List<String>> getAllImageNamesExcept(String correctImageName);

}
