package com.example.quizapp2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.quizapp2.room.Converters;
import com.example.quizapp2.room.QuizAppEntity;
import com.example.quizapp2.room.QuizAppRepository;
import com.example.quizapp2.room.QuizAppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.Manifest;

public class GalleryActivity extends AppCompatActivity {

    private static int PERMISSION_REQUEST_READ_MEDIA_IMAGES = 101;

    ActivityResultLauncher<Intent> resultLauncher;
    List<Uri> images = new ArrayList<>();

    private QuizAppViewModel quizAppViewModel;

    ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        FloatingActionButton add = findViewById(R.id.floatingActionButton2);
        Button backButton = findViewById(R.id.button4);
        Button sort = findViewById(R.id.button5);

        // set up the imageAdapter first
        imageAdapter = new ImageAdapter(this);

        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);

        //checkAndRequestImagePermission();

        // Initialize the viewmodel
        quizAppViewModel = new ViewModelProvider(this).get(QuizAppViewModel.class);


        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();

                    if (intent != null) {
                        Uri pickedImage = intent.getData();
                        if (pickedImage != null) {

                            String displayImageName = FileUtils.getFilenameToTextView(GalleryActivity.this, pickedImage);
                            TextView imageText = findViewById(R.id.textView3);

                            imageText.setText(displayImageName);
                            String imageURI = Converters.uriToString(pickedImage);
                            QuizAppEntity image = new QuizAppEntity(0, imageURI, imageText.getText().toString());
                            quizAppViewModel.insert(image);
                        }
                    }
                }
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            updateImageAdapter();
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestReadMediaImagesPermission();
            }
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortList();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QuizAppEntity image = (QuizAppEntity) imageAdapter.getItem(position);
                int imageID = image.getId();
                quizAppViewModel.deleteImageWithID(imageID);
                imageAdapter.notifyDataSetChanged();

            }
        });

    }

        private void updateImageAdapter() {
            quizAppViewModel.getAllImages().observe(this, quizAppEntities -> {
                imageAdapter.setList(quizAppEntities);
                imageAdapter.notifyDataSetChanged();
            });
        }

        private void pickImage() {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");

            resultLauncher.launch(intent);
        }

        private void sortList() {
            quizAppViewModel.getAllImages().observe(this, images -> {
                if(!images.isEmpty()) {
                    // sorting the list using a comparator
                    Collections.sort(images, new Comparator<QuizAppEntity>() {
                        @Override
                        public int compare(QuizAppEntity img1, QuizAppEntity img2) {
                            return img1.getName().compareTo(img2.getName());
                        }
                    });
                    // remember to update the adapter after changes in the db, so the viewmodel can update the views
                    updateImageAdapter();
                };
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
