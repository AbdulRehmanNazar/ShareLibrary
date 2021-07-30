package com.ceries.sharingutil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abdul.sharinglib.ShareUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int FILE_REQUEST_CODE = 123;
    private Button shareButton, imagePicker, filePickerBtn, shareFileBtn;
    private EditText textBox, subjectTextBox;
    private Uri imageUri = null, fileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subjectTextBox = findViewById(R.id.subjectTextBox);
        textBox = findViewById(R.id.textBox);
        shareFileBtn = findViewById(R.id.shareFileBtn);
        shareFileBtn.setOnClickListener(view -> {
            if (fileUri == null) {
                Toast.makeText(MainActivity.this, "Please select file to share", Toast.LENGTH_SHORT).show();
                return;
            }
            String subjectText = "";
            if (subjectTextBox.getText().toString().length() > 0) {
                subjectText = subjectTextBox.getText().toString();
            }
            if (textBox.getText().toString().length() == 0) {
                Toast.makeText(MainActivity.this, "Please enter sharing text", Toast.LENGTH_SHORT).show();
                return;
            }
            ShareUtil.shareFile(MainActivity.this, subjectText, textBox.getText().toString(), fileUri);
        });
        imagePicker = findViewById(R.id.imagePickBtn);
        imagePicker.setOnClickListener(view -> {
            ImagePicker.Companion.with(MainActivity.this)
                    .galleryOnly()
                    .start();
        });

        filePickerBtn = findViewById(R.id.filePickBtn);
        filePickerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission(true)
                    .setShowImages(false)
                    .setShowVideos(false)
                    .setShowAudios(true)
                    .setShowFiles(true)
                    .enableImageCapture(false)
                    .setMaxSelection(1)
                    .setSkipZeroSizeFiles(true)
                    .build());
            startActivityForResult(intent, FILE_REQUEST_CODE);
        });
        shareButton = findViewById(R.id.shareBtn);
        shareButton.setOnClickListener(view -> {
            String subjectText = "";
            if (subjectTextBox.getText().toString().length() > 0) {
                subjectText = subjectTextBox.getText().toString();
            }
            if (textBox.getText().toString().length() == 0) {
                Toast.makeText(MainActivity.this, "Please enter sharing text", Toast.LENGTH_SHORT).show();
                return;
            }
            ShareUtil.share(MainActivity.this, subjectText, textBox.getText().toString(), imageUri);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            if (files.size() > 0) {
                fileUri = files.get(0).getUri();
                Toast.makeText(MainActivity.this, "File is selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    imageUri = data.getData();
                    Toast.makeText(MainActivity.this, "Image is selected", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}