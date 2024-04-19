package com.example.quizapp2.room;

import static com.example.quizapp2.QuizActivity.PickRandomImages;
import static com.example.quizapp2.QuizActivity.fillButtonOptionsList;
import static com.example.quizapp2.QuizActivity.PickRandomImages;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.quizapp2.room.QuizAppEntity;
import com.example.quizapp2.room.QuizAppRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizgameViewModel extends AndroidViewModel {
    private QuizAppRepository mRepository;

    private final LiveData<List<QuizAppEntity>> allImages;


    private MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private MutableLiveData<Integer> roundsplayed = new MutableLiveData<>(1);
    private MutableLiveData<QuizAppEntity> imagePicked = new MutableLiveData<>();

    private MutableLiveData<List<String>> buttonOptions = new MutableLiveData<>();

    private Observer<List<QuizAppEntity>> observer;

    public QuizgameViewModel(Application application) {
        super(application);
        mRepository = new QuizAppRepository(application);
        allImages = mRepository.getAllImages();

        buttonOptions.setValue(new ArrayList<>());


        //Will set the initial value for dogPicked and buttonOptions when i have access
        // to the dog objects from room database
        allImages.observeForever( observer = new Observer<List<QuizAppEntity>>() {
            @Override
            public void onChanged(List<QuizAppEntity> images) {
                if (images != null && !images.isEmpty()) {

                    PickRandomImages(images);
                    fillButtonOptionsList(images);
                }
            }
        });


    }


    public LiveData<List<QuizAppEntity>> getAllImages() {
        return allImages;
    }


    public MutableLiveData<Integer> getScore() {
        return score;
    }


    public MutableLiveData<Integer> getRoundsplayed() {
        return roundsplayed;
    }


    public LiveData<List<String>> getButtonOptions() {
        return buttonOptions;
    }


    public MutableLiveData<QuizAppEntity> getPickedImage() {
        return imagePicked;
    }


    public void setImagePicked(MutableLiveData<QuizAppEntity> pickedImage) {
        this.imagePicked = pickedImage;
    }


    //Removes the oberver to avoid memory leaks
    @Override
    protected void onCleared() {
        super.onCleared();
        allImages.removeObserver(observer);
    }
}