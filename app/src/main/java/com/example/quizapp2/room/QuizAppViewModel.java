package com.example.quizapp2.room;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.quizapp2.FileUtils;
import com.example.quizapp2.QuizActivity;
import com.example.quizapp2.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class QuizAppViewModel extends AndroidViewModel {

    private QuizAppRepository repository;
    private QuizActivity quizActivity;
    private MutableLiveData<Integer> correctAnswers = new MutableLiveData<>(0);
    private MutableLiveData<Integer> attempts = new MutableLiveData<>(0);
    private MutableLiveData<QuizAppEntity> currentImage = new MutableLiveData<>();
    private MutableLiveData<List<QuizAppEntity>> pickedImages = new MutableLiveData<>();
    private MutableLiveData<Boolean> isQuizFinished = new MutableLiveData<>();
    private LiveData<List<QuizAppEntity>> allImages;
    private List<QuizAppEntity> shuffledImages;
    private int currentIndex = 0;

    Iterator quizIterator;

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

    // Method to initialize or restore the quiz state
    public void setupQuiz(List<QuizAppEntity> images) {
        if (shuffledImages == null) {
            shuffledImages = images;
            Collections.shuffle(shuffledImages);
            currentIndex = 0;
            currentImage.setValue(shuffledImages.get(currentIndex));
        } else {
            // If quizAppEntityList is not null, it means the ViewModel already has state
            // (e.g. after a configuration change), so simply restore it
            currentImage.setValue(shuffledImages.get(currentIndex));
        }
    }

    public void loadNextImage() {
        // Ensure there are images to show
        if (shuffledImages != null && currentIndex < shuffledImages.size()) {
            currentImage.setValue(shuffledImages.get(currentIndex));

            currentIndex++;
            quizActivity.nextQuestion();

        } else {
            isQuizFinished.setValue(true);
        }
    }


    // Add default images
    private void insertDefaultImages() {
        repository.insert(new QuizAppEntity(R.drawable.odegaard, null, "odegaard"));
        repository.insert(new QuizAppEntity(R.drawable.haaland, null, "haaland"));
        repository.insert(new QuizAppEntity(R.drawable.messi, null, "messi"));

    }

    public MutableLiveData<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public LiveData<Boolean> getIsQuizFinished() {
        return isQuizFinished;
    }

    public MutableLiveData<Integer> getAttempts() {
        return attempts;
    }

    public MutableLiveData<QuizAppEntity> getCurrentImage() {
        return currentImage;
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
