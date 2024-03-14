package com.example.quizapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quizapp2.room.QuizAppEntity;
import com.example.quizapp2.room.QuizAppRepository;

public class MainActivity extends AppCompatActivity {

    private QuizAppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button gallery = findViewById(R.id.button);
        Button quiz = findViewById(R.id.button2);

        repository = new QuizAppRepository(getApplication());

        repository.getAllImages().observe(this, repo -> {
            if(repo.isEmpty()) {
                repository.insert(new QuizAppEntity(String.valueOf(R.drawable.haaland), "Haaland"));
                repository.insert(new QuizAppEntity(String.valueOf(R.drawable.hojlund), "Hojlund"));
                repository.insert(new QuizAppEntity(String.valueOf(R.drawable.messi), "Messi"));
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
                try {
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("main", e.toString());
                }
            }
        });
    }
}