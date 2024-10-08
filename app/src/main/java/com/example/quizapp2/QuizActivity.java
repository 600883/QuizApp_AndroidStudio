package com.example.quizapp2;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quizapp2.room.QuizAppEntity;
import com.example.quizapp2.room.QuizAppViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button option1, option2, option3;

    private Iterator<QuizAppEntity> quizIterator;

    private QuizAppEntity currentImage;

    private String correctAnswer;

    private QuizAppViewModel quizAppViewModel;

    TextView textView;

    ImageAdapter imageAdapter;

    private static int PERMISSION_REQUEST_READ_MEDIA_IMAGES = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_quiz_landscape);
        } else {
            setContentView(R.layout.activity_quiz);
        }

        imageAdapter = new ImageAdapter(this);

        imageView = findViewById(R.id.imageView2);
        option1 = findViewById(R.id.button6);
        option2 = findViewById(R.id.button7);
        option3 = findViewById(R.id.button8);

        //textView = findViewById(R.id.resultatView);

        // Initialize the ViewModel
        quizAppViewModel = new ViewModelProvider(this).get(QuizAppViewModel.class);

        quizAppViewModel.getAllImages().observe(this, images -> {
            setupQuiz(images);
        });

        /*
        // Only fetch and set up the quiz if it's a fresh start, check if savedInstanceState is null
        if (savedInstanceState == null) {
            // Fetch all images and set up the quiz
            quizAppViewModel.getAllImages().observe(this, images -> {
                // Make sure to check for null or empty lists
                if (images != null && !images.isEmpty()) {
                    setupQuiz(images);
                }
            });
        }

         */



        quizAppViewModel.getCurrentImage().observe(this, new Observer<QuizAppEntity>() {
            @Override
            public void onChanged(QuizAppEntity quizAppEntity) {
                displayImage(quizAppEntity);
            }
        });

        quizAppViewModel.getCorrectAnswers().observe(this, numberCorrects -> {
            updateScoreDisplay(numberCorrects, quizAppViewModel.getAttempts().getValue());
        });

        quizAppViewModel.getAttempts().observe(this, attempts -> {
            updateScoreDisplay(quizAppViewModel.getCorrectAnswers().getValue(), attempts);
        });






        /*
        quizAppViewModel.getIsQuizFinished().observe(this, isFinished -> {
            if (isFinished) {
                finishQuiz();
            }
        });

         */

        checkAnswers();
    }


    private void setupQuiz(List<QuizAppEntity> images) {
        // Shuffle the images to randomize the order
        Collections.shuffle(images);

        // Convert the list to an iterator to let the quiz know which photo to show next
        quizIterator = images.iterator();


        //quizAppViewModel.loadNextImage();
        nextQuestion();

    }

    private void updateScoreDisplay(Integer correctAnswers, Integer attempts) {
        if (correctAnswers == null || attempts == null) return;

        String scoreText = correctAnswers + "/"+ attempts;
        TextView scoreView = findViewById(R.id.resultatView);
        scoreView.setText(scoreText);
    }

    public void nextQuestion() {
        resetButtonColorsAndEnable();
        if(quizIterator.hasNext()) {
            currentImage = quizIterator.next();

            //Display the image from the current quizAppEntity
            displayImage(currentImage);

            correctAnswer = currentImage.getName();

            prepareOptions();

        } else {
            finishQuiz();
        }
    }

    private void prepareOptions() {
        // Store the LiveData object in a final reference so that it can be accessed from within the onChanged method
        final LiveData<List<String>> liveData = quizAppViewModel.getAllImageNamesExcept(correctAnswer);
        liveData.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> options) {
                //Collections.shuffle(options);

                while (options.size() > 2) {
                    options.remove(options.size() - 1);
                }

                int correctIndex = new Random().nextInt(3);
                options.add(correctIndex, correctAnswer);

                option1.setText(options.get(0));
                option2.setText(options.get(1));
                option3.setText(options.get(2));

                // After populating the buttons, you no longer need updates for this LiveData,
                // so i remove the observer to prevent future updates from triggering this code.
                liveData.removeObserver(this);
            }
        });
    }


    private void checkAnswers() {
        MutableLiveData<Integer> score = quizAppViewModel.getCorrectAnswers();
        View.OnClickListener answerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;

                Integer currentAttempts = quizAppViewModel.getAttempts().getValue();
                if(currentAttempts != null) {
                    quizAppViewModel.getAttempts().setValue(currentAttempts + 1);
                }

                if (clickedButton.getText().toString().equals(correctAnswer)) {
                    clickedButton.setBackgroundColor(Color.GREEN);

                    Integer currentScore = quizAppViewModel.getCorrectAnswers().getValue();
                    if(currentScore != null) {
                        quizAppViewModel.getCorrectAnswers().setValue(currentScore + 1);
                    }

                    loadNextQuestionWithDelay();

                } else {
                    clickedButton.setBackgroundColor(Color.RED);
                    loadNextQuestionWithDelay();

                }
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
            }

        };
        option1.setOnClickListener(answerClickListener);
        option2.setOnClickListener(answerClickListener);
        option3.setOnClickListener(answerClickListener);
    }

    private void loadNextQuestionWithDelay() {
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetButtonColorsAndEnable();
                nextQuestion();
            }
        }, 500); // 500 ms delay before loading next question
    }

    // This method gets called each time loadNextQuestionWithDelay is run.
    // It resets the buttons color green/red to the default color(purple)
    // It ensures that the buttons is ready to be clicked in the next question
    private void resetButtonColorsAndEnable() {

        option1.setBackgroundColor(Color.parseColor("#6200EE"));
        option2.setBackgroundColor(Color.parseColor("#6200EE"));
        option3.setBackgroundColor(Color.parseColor("#6200EE"));

        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
    }


    // This method is responsible for showing the image in the quiz. It uses Glide to load it into
    // the imageView in the Activity
    private void displayImage(QuizAppEntity image) {

        if (image.getImageResId() != 0) {
            Glide.with(this).load(image.getImageResId()).into(imageView);
        } else if (image.getUri() != null) {
            Glide.with(this).load((image.getUri())).into(imageView);
        } else {
            Log.d("quiz", "No resource id or Uri");
        }
    }

    // This method finishes and quits the quiz. First the user is shown a confirmation
    // that the quiz is finished. And then the app goes back to the main activity after 2 sec
    public void finishQuiz() {
        Toast.makeText(this,"You have finished the quiz!", Toast.LENGTH_LONG).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }


    private void updateImageAdapter() {
        quizAppViewModel.getAllImages().observe(this, images -> {
            imageAdapter.setList(images);
            imageAdapter.notifyDataSetChanged();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestReadMediaImagesPermission() {
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.READ_MEDIA_IMAGES},
                PERMISSION_REQUEST_READ_MEDIA_IMAGES);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_READ_MEDIA_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with accessing media images
                updateImageAdapter();
            } else {
                // Permission denied, inform the user of the necessary permissions
                Toast.makeText(this, "Permission required to access images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
