package com.example.quizapp2.room;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class QuizAppViewModel extends AndroidViewModel {

    private QuizAppRepository repository;

    private MutableLiveData<Boolean> shouldSortByName = new MutableLiveData<>(false);

    private LiveData<List<QuizAppEntity>> allImages;

    public QuizAppViewModel(Application application) {
        super(application);
        repository = new QuizAppRepository(application);
        allImages = repository.getAllImages();
    }

   public LiveData<List<QuizAppEntity>> getAllImages() {
        return allImages;
    }

    public void insert(QuizAppEntity image) {
        repository.insert(image);
    }

    public void deleteImageWithID(long id) {
        repository.delete(id);
    }


}
