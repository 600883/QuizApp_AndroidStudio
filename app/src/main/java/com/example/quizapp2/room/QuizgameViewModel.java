package com.example.quizapp2.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.quizapp2.R;
import com.example.quizapp2.room.QuizAppEntity;
import com.example.quizapp2.room.QuizAppRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizgameViewModel extends AndroidViewModel {

    private QuizAppRepository repository;

    /*
    private MutableLiveData<List<Long>> selectedImageIds = new MutableLiveData<>();

    private LiveData<List<QuizAppEntity>> quizImages = Transformations.switchMap(selectedImageIds, ids -> {
        return repository.getImagesByIds(ids);
    });

     */

    private MutableLiveData<Integer> correctAnswers = new MutableLiveData<>(0);
    private MutableLiveData<Integer> attempts = new MutableLiveData<>(1);
    private MutableLiveData<QuizAppEntity> pickedImage = new MutableLiveData<>();
    private MutableLiveData<List<QuizAppEntity>> options = new MutableLiveData<>();
    private Observer<List<QuizAppEntity>> observer;

    private LiveData<List<QuizAppEntity>> allImages;

    public QuizgameViewModel(Application application) {
        super(application);
        repository = new QuizAppRepository(application);
        allImages = repository.getAllImages();
        options.setValue(new ArrayList<>());
        //checkAndInsertDefaultImages();

        allImages.observeForever(observer = new Observer<List<QuizAppEntity>>() {
            @Override
            public void onChanged(List<QuizAppEntity> images) {
                if (images != null && !images.isEmpty()) {
                    setupQuiz(images);
                }
            }
        });


    }

    private void setupQuiz(List<QuizAppEntity> images) {
        // Shuffle the images to randomize the order
        Collections.shuffle(images);

        pickedImage.setValue(images.get(0));

    }

    public void setButtonOptions(List<QuizAppEntity> images) {
        int indexA = images.indexOf(pickedImage.getValue());

        int indexB, indexC;

        Random random = new Random();

        do {
            indexB = random.nextInt(images.size());
        } while (indexB == indexA);

        do {
            indexC = random.nextInt(images.size());
        } while (indexC == indexA );
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

    public MutableLiveData<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public MutableLiveData<Integer> getAttempts() {
        return attempts;
    }

    public MutableLiveData<QuizAppEntity> getPickedImage() {
        return pickedImage;
    }

    public void setPickedImage(MutableLiveData<QuizAppEntity> pickedImage) {
        this.pickedImage = pickedImage;
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