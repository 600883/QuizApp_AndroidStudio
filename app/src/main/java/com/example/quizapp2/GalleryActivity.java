package com.example.quizapp2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {


    GridView gridView;

    ArrayAdapter<Uri> imageArrayAdapter;

    ActivityResultLauncher<String> resultLauncher;

    List<Uri> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridView);


        //images.add(R.drawable.haaland);
        images.add(Uri.parse("android.resource://com.example.quizapp2/drawable/haaland"));
        images.add(Uri.parse("android.resource://com.example.quizapp2/drawable/odegaard"));
        images.add(Uri.parse("android.resource://com.example.quizapp2/drawable/messi"));



        FloatingActionButton add = findViewById(R.id.floatingActionButton2);
        Button backButton = findViewById(R.id.button4);
        Button sort = findViewById(R.id.button5);
        Button start = findViewById(R.id.button3);


        imageArrayAdapter = new ArrayAdapter<Uri>(this, R.layout.grid_item, images) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.grid_item, parent, false);
                }
                ImageView imageView = convertView.findViewById(R.id.imageView);
                TextView textView = convertView.findViewById(R.id.textView3);

                Uri imageUri = getItem(position);

                imageView.setImageURI(imageUri);

                String imageName = FileUtils.getFileNameFromUri(imageUri);
                textView.setText(imageName);


                return convertView;
            }
        };

        gridView.setAdapter(imageArrayAdapter);


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            images.add(uri);

                            imageArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sortImagesFromAZ(images);
                    imageArrayAdapter.notifyDataSetChanged();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri clickedImage = images.get(position);
                images.remove(clickedImage);
                imageArrayAdapter.notifyDataSetChanged();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, QuizActivity.class);
                intent.putExtra("imageList", (Serializable) images);
                startActivity(intent);
            }
        });

    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        resultLauncher.launch("image/*");
    }

    private static void sortImagesFromAZ(List<Uri> image) {

        Comparator<Uri> uriComparator = new Comparator<Uri>() {
            @Override
            public int compare(Uri o1, Uri o2) {
                return o1.toString().compareTo(o2.toString());
            }
        };
        Collections.sort(image, uriComparator);

    }

    private static void sortImagesFromZA(List<Uri> images) {
        Comparator<Uri> uriComparator = new Comparator<Uri>() {
            @Override
            public int compare(Uri o1, Uri o2) {
                return o2.toString().compareTo(o1.toString());
            }
        };
        Collections.sort(images,uriComparator);
    }
}
