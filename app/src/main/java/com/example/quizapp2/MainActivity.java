package com.example.quizapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
        setContentView(R.layout.activity_main);

        Button gallery = findViewById(R.id.button);
        Button quiz = findViewById(R.id.button2);

        // Initialize the viewmodel so i can use it to call method on the repo
        quizAppViewModel = new ViewModelProvider(this).get(QuizAppViewModel.class);

        repository = new QuizAppRepository(getApplication());

        repository.getAllImages().observe(this, repo -> {
            if(repo.isEmpty()) {
                repository.insert(new QuizAppEntity(FileUtils.getResourceName(this, R.drawable.haaland), "Haaland"));
                repository.insert(new QuizAppEntity(FileUtils.getResourceName(this, R.drawable.hojlund), "Hojlund"));
                repository.insert(new QuizAppEntity(FileUtils.getResourceName(this, R.drawable.messi), "Messi"));
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("main", e.toString());
                }
            }
        });

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numberOfImages = getRepositoryImageCount();
                if (numberOfImages >= 3) {
                    try {
                        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.d("main", "Error starting quiz", e);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You must have at least 3 photos in the gallery", Toast.LENGTH_SHORT).show();
                }
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