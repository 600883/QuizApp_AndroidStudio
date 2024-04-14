package com.example.quizapp2.room;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.quizapp2.FileUtils;
import com.example.quizapp2.R;

import java.util.ArrayList;
import java.util.List;

public class QuizAppViewModel extends AndroidViewModel {

    private QuizAppRepository repository;

    /*
    private MutableLiveData<List<Long>> selectedImageIds = new MutableLiveData<>();

    private LiveData<List<QuizAppEntity>> quizImages = Transformations.switchMap(selectedImageIds, ids -> {
        return repository.getImagesByIds(ids);
    });

     */

    private LiveData<List<QuizAppEntity>> allImages;

    public QuizAppViewModel(Application application) {
        super(application);
        repository = new QuizAppRepository(application);
        allImages = repository.getAllImages();
        checkAndInsertDefaultImages();
    }

    private void checkAndInsertDefaultImages() {
        // Trigger a one time check for inserting default images if the list is empty
        // Observe the livedata
        allImages.observeForever(new Observer<List<QuizAppEntity>>() {
            @Override
            public void onChanged(List<QuizAppEntity> images) {
                // As soon as the livedata is observed, remove the observer, to prevent it from
                // being run more than once
                allImages.removeObserver(this);

                if (images.isEmpty()) {
                    insertDefaultImages();
                }
            }
        });
    }

    // Add default images
    private void insertDefaultImages() {
        repository.insert(new QuizAppEntity(R.drawable.odegaard, null, "odegaard"));
        repository.insert(new QuizAppEntity(R.drawable.haaland, null, "haaland"));
        repository.insert(new QuizAppEntity(R.drawable.messi, null, "messi"));

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
