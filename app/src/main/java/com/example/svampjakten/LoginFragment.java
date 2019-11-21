package com.example.svampjakten;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText email;
    EditText password;

    TextView textView;

    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pin_info_animation));

        button = getActivity().findViewById(R.id.login_button);

        textView = getActivity().findViewById(R.id.login_createAccount);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = getActivity().findViewById(R.id.login_email);
                password = getActivity().findViewById(R.id.login_password);

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if(mail.length() < 1 && pass.length() < 1){
                    getActivity().findViewById(R.id.include_center_fragment).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shaking_failed));
                    Toast.makeText(getContext(), "Enter email and password.", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
                                View centerFrag = getActivity().findViewById(R.id.include_center_fragment);

                                getActivity().findViewById(R.id.main_layout).setVisibility(View.VISIBLE);

                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        getActivity().findViewById(R.id.include_center_fragment).setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "Login succesful!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });

                                centerFrag.startAnimation(animation);

                            } else{
                                Toast.makeText(getContext(), "Login details incorrect.", Toast.LENGTH_SHORT).show();
                                getActivity().findViewById(R.id.include_center_fragment).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shaking_failed));
                            }
                        }
                    });
                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Push", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.include_center_fragment, new NewAccountFragment()).commit();
            }
        });
    }
}
