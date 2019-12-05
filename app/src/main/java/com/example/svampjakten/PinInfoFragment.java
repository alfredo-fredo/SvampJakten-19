package com.example.svampjakten;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class PinInfoFragment extends Fragment {

    String placeName;
    String name;
    String comment;
    Uri photoURL;
    double ratings;

    PinInfoFragment(String placeName, String name, String comment, Uri photoURL, double ratings){
        this.placeName = placeName;
        this.name = name;
        this.comment = comment;
        this.photoURL = photoURL;
        this.ratings = ratings;
    }

    TextView placeNameTextView;
    TextView nameTextView;
    TextView commentTextView;
    ImageView photoImageView;
    TextView ratingsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeNameTextView = getActivity().findViewById(R.id.pin_info_name);
        nameTextView = getActivity().findViewById(R.id.pin_info_name_user);
        commentTextView = getActivity().findViewById(R.id.pin_info_user_comment);
        photoImageView = getActivity().findViewById(R.id.pin_info_image);
        ratingsTextView = getActivity().findViewById(R.id.pin_info_rating);

        placeNameTextView.setText(placeName);
        nameTextView.setText(name);
        commentTextView.setText(comment);
        Glide.with(this).load(photoURL).into(photoImageView);
        ratingsTextView.setText("* " + ratings);

        getActivity().findViewById(R.id.pin_info_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.pin_info_layout).setVisibility(View.GONE);
            }
        });

        getActivity().findViewById(R.id.pin_info_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
