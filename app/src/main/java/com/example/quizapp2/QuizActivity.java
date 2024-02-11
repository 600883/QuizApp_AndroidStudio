package com.example.quizapp2;


import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ImageView imageView;
    Button option1, option2, option3;
    private List<Uri> imageList;
    private String correctAnswer;
    private int currentImageIndex;
    private int numCorrects;
    private int totTries;
    private TextView resultStats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // this code retrieves the list of imageUri's that was passed from the galleryActivity using an Intent.
        Intent intent = getIntent();
        imageList = (List<Uri>) intent.getSerializableExtra("imageList");

        assert imageList != null;
        Collections.shuffle(imageList);

        imageView = findViewById(R.id.imageView2);
        option1 = findViewById(R.id.button6);
        option2 = findViewById(R.id.button7);
        option3 = findViewById(R.id.button8);

        resultStats = findViewById(R.id.resultatView);


        imageView.setImageURI(imageList.get(0));


        option1.setText(FileUtils.getFileNameFromUri(imageList.get(0)));
        option2.setText(FileUtils.getFileNameFromUri(imageList.get(1)));
        option3.setText(FileUtils.getFileNameFromUri(imageList.get(2)));

        resultStats.setText(numCorrects + "/" + totTries);


        correctAnswer = FileUtils.getFileNameFromUri(imageList.get(0));


        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(option1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(option2);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAnswer(option3);
            }
        });

        currentImageIndex = 0;

    }

    /*
    this method handles the delay after a correct option is selected and calls the
    updateQuiz after 500 milliseconds
     */
    private void handleAnswer(Button button) {
        if(button.getText().toString().equals(correctAnswer)) {
            button.setBackgroundColor(Color.GREEN);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentImageIndex++;
                    updateQuiz();
                    numCorrects++;
                    totTries++;
                    resultStats.setText(numCorrects + "/" + totTries);
                }
            }, 500);
        } else {
            button.setBackgroundColor(Color.RED);
            totTries++;
            resultStats.setText(numCorrects + "/" + totTries);
        }
    }


    /*
    silly celebration animation that appears when the user has finished the quiz
     */
    private void blinkBackground() {
        ValueAnimator animator = ValueAnimator.ofArgb(Color.YELLOW, Color.GREEN);
        animator.setDuration(500); // Set duration for each color transition
        animator.setRepeatCount(10); // Repeat the animation 10 times (or any desired number)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                getWindow().getDecorView().setBackgroundColor(color);
            }
        });
        animator.start();
    }

    private void updateQuiz() {
        /*
        sets the color to purple each time the updateQuiz is called, after the buttons is clicked
         */
        option1.setBackgroundColor(Color.parseColor("#6200EE"));
        option2.setBackgroundColor(Color.parseColor("#6200EE"));
        option3.setBackgroundColor(Color.parseColor("#6200EE"));

        // this keeps the quiz active, as long as there are more images to guess
        if (currentImageIndex < imageList.size()) {
            imageView.setImageURI(imageList.get(currentImageIndex));

            // Set the filename of the current image as the correct answer
            correctAnswer = FileUtils.getFileNameFromUri(imageList.get(currentImageIndex));

            // Get all image names except the correct one
            List<String> options = new ArrayList<>();
            for(int i = 0; i < imageList.size(); i++) {
                if(i != currentImageIndex) {
                    options.add(FileUtils.getFileNameFromUri(imageList.get(i)));
                }
            }

            // Shuffle the options and add the correct answer to a random position
            Collections.shuffle(options);
            int correctIndex = new Random().nextInt(3);
            options.add(correctIndex, correctAnswer);

            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));

        } else {
            // if the currentImage index is equal to the imagelist.size(), in practice, this means when all the images is
            // guessed
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_LONG).show();
            blinkBackground();
            Button quitButton = findViewById(R.id.quit);
            quitButton.setVisibility(View.VISIBLE);
            quitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}