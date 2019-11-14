package com.example.svampjakten;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    EditText LoginMail;
    EditText LoginPass;
    Button Login;
    Button LoginFacebook;
    Button Register;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        LoginMail = getActivity().findViewById(R.id.Signin_Email);
        LoginPass = getActivity().findViewById(R.id.Signin_Password);
        Login = getActivity().findViewById(R.id.Button_Login);
        LoginFacebook = getActivity().findViewById(R.id.Button_LoginFACEBOOK);
        Register = getActivity().findViewById(R.id.Button_NewAccount);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

}
