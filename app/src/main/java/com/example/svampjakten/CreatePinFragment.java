package com.example.svampjakten;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePinFragment extends Fragment {

    static final int REQUEST_TAKE_PHOTO = 1;


    private static final int CAMERA_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
        Log.d("Hej", "Pew pew");
        if (requestCode == CAMERA_REQUEST_CODE) {
            Log.d("Hej", "Brap brap");
            if (resultCode == RESULT_OK) {
                Log.d("Hej" , "Bilden har skickats BRAP BRAP");
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
            }
        }
    }

}
