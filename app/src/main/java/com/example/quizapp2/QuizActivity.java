package com.example.quizapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quizapp2.R;
import com.example.quizapp2.room.QuizAppEntity;
import com.example.quizapp2.room.QuizAppViewModel;
import com.example.quizapp2.room.QuizgameViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    static QuizgameViewModel quizGameViewModel;

    Button button;
    Button button2;
    Button button3;

    TextView answerView;

    ImageView imageView;

    QuizAppEntity image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Set layout based on orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_quiz_landscape);
        } else {
            setContentView(R.layout.activity_quiz);
        }

        //Associate the ViewModel with the activity
        quizGameViewModel = new ViewModelProvider(this).get(QuizgameViewModel.class);

        button = findViewById(R.id.button6);
        button2 = findViewById(R.id.button7);
        button3 = findViewById(R.id.button8);
        answerView = findViewById(R.id.resultatView);
        imageView = findViewById(R.id.imageView2);


        //Sets initial score and updated score when it changes
        quizGameViewModel.getScore().observe(this, score -> {
            if (score != null) {
                //TextView scoreView = findViewById(R.id.scoreText);
                //scoreView.setText("Your Score: " + score);
            }
        });

        //Sets initial rounds played and updated rounds played when it changes
        quizGameViewModel.getRoundsplayed().observe(this, roundsPlayed -> {
            if (roundsPlayed != null) {
                //TextView rounds = findViewById(R.id.rounds);
                String roundText = "Round: " + roundsPlayed;
                //rounds.setText(roundText);
            }
        });


        //will only start the quiz once i have access to the objects
        quizGameViewModel.getAllImages().observe(this, images -> {

            //Logic for initial state of the game
            play();

            //The buttons will now call the checkAnswer and playAgain methods when clicked
            setButtonClickListeners(images);
        });


    }


    public void setButtonClickListeners(List<QuizAppEntity> images) {

        button.setOnClickListener(v ->
        {
            checkAnswer(button);
            playAgain(images);
        });

        button2.setOnClickListener(v ->
        {
            checkAnswer(button2);
            playAgain(images);
        });

        button3.setOnClickListener(v ->
        {
            checkAnswer(button3);
            playAgain(images);
        });
    }

    public void checkAnswer(Button button) {


        MutableLiveData<Integer> score = quizGameViewModel.getScore();

        MutableLiveData<QuizAppEntity> currentImage = quizGameViewModel.getPickedImage();

        image = currentImage.getValue();

        if (button.getText().equals(image.getName())) {
            answerView.setText("Correct Answer!");

            int currentScore = score.getValue() != null ? score.getValue() : 0;


            score.setValue(currentScore + 1);


        } else {
            answerView.setText("The correct answer was: " + image.getName());
        }
    }

    public void playAgain(List<QuizAppEntity> images) {


        PickRandomImages(images);

        MutableLiveData<QuizAppEntity> dogPicked = quizGameViewModel.getPickedImage();

        QuizAppEntity dog = dogPicked.getValue();

        MutableLiveData<Integer> roundsPlayed = quizGameViewModel.getRoundsplayed();

        int roudsPlayedValue = roundsPlayed.getValue();


        roundsPlayed.setValue(roudsPlayedValue + 1);



        if (image.getImageResId() != 0) {
            Glide.with(this).load(image.getImageResId()).into(imageView);
        } else if (image.getUri() != null) {
            Glide.with(this).load((image.getUri())).into(imageView);
        } else {
            Log.d("quiz", "No resource id or Uri");
        }


        //Update the buttons
        fillButtonOptionsList(images);
        addTextToButtons();

    }


    public static void fillButtonOptionsList(List<QuizAppEntity> images) {


        List<String> buttonOptions = quizGameViewModel.getButtonOptions().getValue();
        //buttonOptions.clear();

        MutableLiveData<QuizAppEntity> currentImage = quizGameViewModel.getPickedImage();

        QuizAppEntity image = currentImage.getValue();

        Random random = new Random();


        while (buttonOptions.size() != 2) {
            int randomIndex = random.nextInt(images.size());
            QuizAppEntity randomImage = images.get(randomIndex);

            if (randomImage != image && !buttonOptions.contains(randomImage.getName())) {
                String imageText = randomImage.getName();
                buttonOptions.add(imageText);
            }
        }

    }

    public void addTextToButtons() {

        MutableLiveData<QuizAppEntity> currentImage = quizGameViewModel.getPickedImage();

        QuizAppEntity image = currentImage.getValue();

        List<String> buttonOptions = quizGameViewModel.getButtonOptions().getValue();

        List<String> options = new ArrayList<>();

        options.add(image.getName());
        options.add(buttonOptions.get(0));
        options.add(buttonOptions.get(1));

        Collections.shuffle(options);

        button.setText(options.get(0));
        button2.setText(options.get(1));
        button3.setText(options.get(2));
    }

    public static void PickRandomImages(List<QuizAppEntity> images) { //Sets the value of dogPicked

        QuizAppEntity image;
        if (images == null || images.isEmpty()) {
            return;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(images.size());

        image = images.get(randomIndex);

        MutableLiveData<QuizAppEntity> currentImage = new MutableLiveData<>(image);

        quizGameViewModel.setImagePicked(currentImage);

    }

    public void play() {

        MutableLiveData<QuizAppEntity> currentImage = quizGameViewModel.getPickedImage();

        QuizAppEntity image = currentImage.getValue();

        if (image.getImageResId() != 0) {

            Glide.with(this).load(image.getImageResId()).into(imageView);
        } else if (image.getUri() != null) {
            Glide.with(this).load(image.getUri()).into(imageView);
        } else {
            Log.d("test", "No image URI or imageResource");
        }

        addTextToButtons();

    }


}