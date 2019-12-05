package com.example.svampjakten;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePinFragment extends Fragment {

    double pinLatitude;
    double pinLongitude;


    CreatePinFragment(double pinLatitude, double pinLongitude){
        this.pinLatitude = pinLatitude;
        this.pinLongitude = pinLongitude;
    }

    //CreatedPinCallBack createdPinCallBack;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myDbRef;
    FirebaseUser firebaseUser;

    EditText placeNameText;
    EditText commentText;

    Button okButton;
    Button galleryButton;
    Button takePhotoButton;

    RatingBar ratingStars;

    Bitmap uploadPhoto;

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int TAKE_IMAGE_CODE = 27;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_INTENT = 2;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private StorageReference storage;
    private String key;

    static String PINS_DB_REF = "Pins";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_pin, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDbRef = firebaseDatabase.getReference(PINS_DB_REF);

        placeNameText = getView().findViewById(R.id.create_pin_place_name);
        commentText = getActivity().findViewById(R.id.create_pin_comment);
        galleryButton = getView().findViewById(R.id.create_pin_gallery);
        takePhotoButton = getView().findViewById(R.id.create_pin_add_photo);
        okButton = getView().findViewById(R.id.create_pin_OK);
        ratingStars = getView().findViewById(R.id.create_pin_ratingBar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });


        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(placeNameText.getText() != null && (ratingStars.getRating() > 0) && uploadPhoto != null)
                createPin(placeNameText.getText().toString(), ratingStars.getRating(), uploadPhoto);
            }
        });

        getActivity().findViewById(R.id.create_pin_add_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

                }
            }
        });

        getActivity().findViewById(R.id.create_pin_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.create_pin_layout).setVisibility(View.GONE);
            }
        });
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("Hej", "Pew pew");
        if (requestCode == GALLERY_INTENT) {
            Log.d("Hej", "Brap brap");
            if (resultCode == RESULT_OK) {
                uploadPhoto = (Bitmap) data.getExtras().get("data");
            }

        }
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                uploadPhoto = (Bitmap) data.getExtras().get("data");
            }
        }
    }
    private void handleUpload(Bitmap bitmap, String pinPushId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);

        final StorageReference reference = FirebaseStorage.getInstance().getReference().child("kamerabilder").child(pinPushId + ".jpeg");

        reference.putBytes(outputStream.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Pin created.", Toast.LENGTH_LONG).show();
                getActivity().findViewById(R.id.create_pin_layout).setVisibility(View.GONE);

                //createdPinCallBack.pinCreatedCallBack();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to create pin..", Toast.LENGTH_LONG).show();
                getActivity().findViewById(R.id.create_pin_layout).setVisibility(View.GONE);
            }
        });
    }

    private void createPin(String placeName, double starRating, final Bitmap bitmap){
        final String pushRef = myDbRef.push().getKey();
        myDbRef.child(pushRef).setValue(new Pin(new PinLocation(pinLatitude, pinLongitude), placeName, firebaseUser.getUid(), starRating)).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

            }

        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                handleUpload(bitmap, pushRef);
            }
        });
    }


}
