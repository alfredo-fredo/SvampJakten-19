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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePinFragment extends Fragment {


    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText Comment;
    Button OK;
    static final int REQUEST_TAKE_PHOTO = 1;

    private Button galleryBtn;




    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_INTENT = 2;
    private Uri mImageUri = null;
    private StorageReference mStorage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_pin, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mStorage = FirebaseStorage.getInstance().getReference();

        galleryBtn = (Button) getView().findViewById(R.id.gallery_btn);

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });



        /*database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");


        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        OK = getActivity().findViewById(R.id.create_pin_OK);
        Comment = getActivity().findViewById(R.id.create_pin_comment);



        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                myRef.child(currentuser).setValue(Comment.getText().toString());
                // Log.d("test",);
                getActivity().findViewById(R.id.create_pin_layout).setVisibility(View.GONE);






            }

        });


         */

        getActivity().findViewById(R.id.create_pin_add_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Uri fileUri =
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
               Uri uri = data.getData();
               StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

               filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       //Toast.makeText(CreatePinFragment.this,"Upload done", Toast.LENGTH_LONG).show();
                   }
               });

            }
        }
    }


}
