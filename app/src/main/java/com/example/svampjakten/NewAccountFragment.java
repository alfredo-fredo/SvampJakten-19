package com.example.svampjakten;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewAccountFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    EditText email;
    EditText password;
    EditText passwordRepeat;

    Button button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("myTag", "Created");
        return inflater.inflate(R.layout.fragment_new_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.create_email);

        password = view.findViewById(R.id.create_password);

        passwordRepeat = view.findViewById(R.id.create_password_repeat);

        button = view.findViewById(R.id.create_button);

        firebaseAuth = FirebaseAuth.getInstance();

        Log.d("myTag", "New account view created.");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("myTag", "You pressed the buttonen");

                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String passRepeat = passwordRepeat.getText().toString();

                if(mail != null && pass != null && passRepeat != null){
                    if(pass.equals(passRepeat)){
                        if(pass.length() >= 6 && pass.length() <= 20){

                        firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    View view = getActivity().findViewById(R.id.include_center_fragment);

                                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);

                                    animation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            getActivity().findViewById(R.id.include_center_fragment).setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "User created!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });

                                    view.startAnimation(animation);

                                } else{
                                    Toast.makeText(getContext(), "Failed to create account..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



                        } else {
                            Toast.makeText(getContext(), "Password 6 - 20 letters.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Password doesn't match.", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(getContext(), "One or more field is empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
