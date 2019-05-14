package com.eugeneponomarev.loginandregistration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eugeneponomarev.loginandregistration.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmailLogin;
    EditText editTextPasswordLogin;

    Button buttonLogin;

    TextView textViewSingUp;
    TextView textViewForgotPassword;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    List<User> users;

    @Override
    protected void onStart() {
        super.onStart();

        users = new ArrayList<>();
        users.clear();

        firebaseInitial();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.background_login_2);

        initialComponents();
        clickButton();
    }

    private void firebaseInitial() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, InitialActivity.class));
            finish();
        }
    }

    private void initialComponents() {
        textViewSingUp = (TextView) findViewById(R.id.textViewSingUp);
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        editTextEmailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        auth = FirebaseAuth.getInstance();
    }

    private void clickButton() {
        textViewSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editEmail = editTextEmailLogin.getText().toString();
                String editPassword = editTextPasswordLogin.getText().toString();

                if (TextUtils.isEmpty(editEmail) || TextUtils.isEmpty(editPassword)) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(editEmail, editPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, InitialActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Mistake!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
