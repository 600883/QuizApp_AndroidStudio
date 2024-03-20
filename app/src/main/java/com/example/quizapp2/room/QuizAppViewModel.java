package com.example.quizapp2.room;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;

public class QuizAppViewModel extends AndroidViewModel {

    private QuizAppRepository repository;

    private MutableLiveData<Boolean> shouldSortByName = new MutableLiveData<>(false);

    private MutableLiveData<List<Long>> selectedImageIds = new MutableLiveData<>();

    private LiveData<List<QuizAppEntity>> quizImages = Transformations.switchMap(selectedImageIds, ids -> {
        return repository.getImagesByIds(ids);
    });

    private LiveData<List<QuizAppEntity>> allImages;

    public QuizAppViewModel(Application application) {
        super(application);
        repository = new QuizAppRepository(application);
        allImages = repository.getAllImages();
    }

    public void setSelectedImageIds(List<Long> imageIds) {
        selectedImageIds.setValue(imageIds);
    }

    public LiveData<List<QuizAppEntity>> getQuizImages() {
        return quizImages;
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

    public LiveData<List<String>> getAllImageNamesExcept(String correctImageName) {
        return repository.getAllImageNamesExcept(correctImageName);
    }


}
