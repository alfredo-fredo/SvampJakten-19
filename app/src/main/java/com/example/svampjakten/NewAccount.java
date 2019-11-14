package com.example.svampjakten;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewAccount extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private Button create;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        email = findViewById(R.id.email_);
        password = findViewById(R.id.password_);
        repeatPassword = findViewById(R.id.repeatPassword_);
        create = findViewById(R.id.create);

        firebaseAuth = FirebaseAuth.getInstance();


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(NewAccount.this, "Registred succesful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(NewAccount.this, LoginFragment.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(NewAccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));
        }else {
            Toast.makeText(this,"U Didn't signed in",Toast.LENGTH_LONG).show();
        }
    }
}
