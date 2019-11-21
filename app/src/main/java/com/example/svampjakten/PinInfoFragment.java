package com.example.svampjakten;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PinInfoFragment extends Fragment {

    /*

    !!! Den här är inte klar än, men funkar. Ska snyggas till och tänkas över lite design.

     */

    private String name;
    private String comment;
    private Bitmap photo;

    TextView nameText;
    TextView commentText;
    ImageView photoImageView;

    public PinInfoFragment(String name, String comment, Bitmap photo) {
        this.name = name;
        this.comment = comment;
        if(photo != null){
            this.photo = photo;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameText = getActivity().findViewById(R.id.userPinName);
        commentText = getActivity().findViewById(R.id.userPinComment);
        photoImageView = getActivity().findViewById(R.id.userPinPhoto);

        nameText.setText(name);
        commentText.setText(comment);
        if(photo != null){
            photoImageView.setImageBitmap(photo);
        }

    }
}
