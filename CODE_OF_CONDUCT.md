package com.example.svampjakten;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfilePicture extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private static final int REQUEST_IMAGE_UPLOAD = 222;

    public ImageView profilePicture;
    public Button takePicture, uploadPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        profilePicture = findViewById(R.id.profile_image_view);
        takePicture = findViewById(R.id.take_picture_button);
        uploadPicture = findViewById(R.id.upload_picture_button);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera();
            }

        });

        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchGallery();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = null;
        // Take picture from camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            // upload picture from gallery
        } else if (requestCode == REQUEST_IMAGE_UPLOAD) {
            if (data != null) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Configure byte output stream.
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        // Compress the image.
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,70, bytes);
        int dimension = getSquareCropDimensForBitmap(imageBitmap);
        Bitmap croppedBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
        profilePicture.setImageBitmap(croppedBitmap);
        encodeBitmapAndSaveToFirebase(croppedBitmap);
        finish();
        Intent toProfile = new Intent(this, MainActivity.class);
        startActivity(toProfile);

    }

    private void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(this.getPackageManager()) != null);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

    }

    private void onLaunchGallery(){
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select file"), 222);

    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap){
        // save image to firebase.

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("profile_picture");
        ref.setValue(imageEncoded);
    }

    public int getSquareCropDimensForBitmap(Bitmap bitmap) {

        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }
}
