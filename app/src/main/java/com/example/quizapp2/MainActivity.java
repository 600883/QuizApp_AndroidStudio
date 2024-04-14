package com.example.quizapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quizapp2.room.QuizAppEntity;
import com.example.quizapp2.room.QuizAppRepository;
import com.example.quizapp2.room.QuizAppViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private QuizAppRepository repository;
    private QuizAppViewModel quizAppViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_landscape_xml);
        } else {
            setContentView(R.layout.activity_main);
        }

        Button gallery = findViewById(R.id.button);
        Button quiz = findViewById(R.id.button2);

        // Initialize the viewmodel so i can use it to call method on the repo
        quizAppViewModel = new ViewModelProvider(this).get(QuizAppViewModel.class);

       // Observe the livedata from ViewModel
        quizAppViewModel.getAllImages().observe(this, images -> {
            if (images != null && images.size() >= 3) {
                quiz.setEnabled(true); // Enables the quiz button if there are 3 or more photos
            } else {
                quiz.setEnabled(false);
                Toast.makeText(this,"You must have 3 or more photos in the gallery to start the quiz", Toast.LENGTH_SHORT).show();
            }
        });


        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz.isEnabled()) {
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    startActivity(intent);
                }
            }
        });

       gallery.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
               startActivity(intent);
           }
       });
    }

    // this didnt work beacuse quizappviewmodel was null when i tried to call getAllImages(), thats
    // why the app crashed. i have to initialize the quizappviewmodel before using it.
    // IT WORKS NOW
    private int getRepositoryImageCount() {
        List<QuizAppEntity> images = quizAppViewModel.getAllImages().getValue();

        if(images != null) {
            System.out.println(images.size());
            return images.size();
        }
            return 0;

    }
}