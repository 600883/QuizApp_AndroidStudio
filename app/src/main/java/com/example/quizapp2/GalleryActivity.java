package com.example.quizapp2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
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

public class GalleryActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> resultLauncher;
    List<Uri> images = new ArrayList<>();

    private QuizAppViewModel quizAppViewModel;

    ImageAdapter imageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // set up the imageAdapter first
        imageAdapter = new ImageAdapter(this);

        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);

        // Initialize the viewmodel
        quizAppViewModel = new ViewModelProvider(this).get(QuizAppViewModel.class);

        // Observe the livedata
        quizAppViewModel.getAllImages().observe(this, new Observer<List<QuizAppEntity>>() {
            @Override
            public void onChanged(@Nullable final List<QuizAppEntity> quizAppEntities) {
                imageAdapter.setList(quizAppEntities);
                Log.e("galleryAct", "Updated the image adapters list");
            }
        });



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
                            QuizAppEntity image = new QuizAppEntity(imageURI, imageText.getText().toString());
                            quizAppViewModel.insert(image);
                        }
                    }
                }
            }
        });


        FloatingActionButton add = findViewById(R.id.floatingActionButton2);
        Button backButton = findViewById(R.id.button4);
        Button sort = findViewById(R.id.button5);
        Button start = findViewById(R.id.button3);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortImagesFromAZ(images);
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
        private void pickImage() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");

            resultLauncher.launch(intent);
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




        /*
        gridView = findViewById(R.id.gridView);


        //adds the default images to the gallery
        images.add(Uri.parse("android.resource://com.example.quizapp2/drawable/haaland"));
        images.add(Uri.parse("android.resource://com.example.quizapp2/drawable/odegaard"));
        images.add(Uri.parse("android.resource://com.example.quizapp2/drawable/messi"));


        FloatingActionButton add = findViewById(R.id.floatingActionButton2);
        Button backButton = findViewById(R.id.button4);
        Button sort = findViewById(R.id.button5);
        Button start = findViewById(R.id.button3);


        /*
            this method is for displaying images and their associated named in a "GridView". A grid view has gridItem
            which represents an image and a text
            @param this, refers to the current context, usually an activity or a fragment

            getView, is called by the Gridview to get the view for each item at the specified position
         */
        /*
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


        // sets the imageArrayAdapter as the adapter for a gridview
        // An adapter is responsible for managing the data and creating views for each item in the gridview
        gridView.setAdapter(imageArrayAdapter);


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {

                    // this method is called when the activity launched by the GetContent() contract returns a result
                    @Override
                    public void onActivityResult(Uri uri) {
                        Log.d("ss", uri.toString());
                        if (uri != null) {
                            images.add(uri);

                            imageArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });

        /*
            onClickListener for the add button, the listener calls the pickImage(), when the button is clicked
         */
        /*
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


        // functionality for removing a image when clicking on the image
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri clickedImage = images.get(position);
                images.remove(clickedImage);
                imageArrayAdapter.notifyDataSetChanged();
            }
        });


        /*
            onClickListener for the start quiz button
            Intent.putExtra(...) adds an extra to the intent, which contains the images
         */
        /*
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, QuizActivity.class);
                intent.putExtra("imageList", (Serializable) images);
                startActivity(intent);
            }
        });
    }

    /*
        This method creates an intent to launch an activity for selecting images from the device's storage
        Intent.setType(...) specifies the type of the content to select
        The resultLauncher launches the activity with the intent, allowing the users to select one or more images
     */
        /*
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");

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

         */

}
