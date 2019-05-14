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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextUsernameRegistration;
    EditText editTextEmailRegistration;
    EditText editTextPasswordRegistration;

    Button buttonRegistration;
    Button buttonBack;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setBackgroundDrawableResource(R.drawable.background_login_2);


        initialComponents();
        clickButton();
    }

    private void initialComponents() {
        editTextUsernameRegistration = (EditText) findViewById(R.id.editTextUsernameRegistration);
        editTextEmailRegistration = (EditText) findViewById(R.id.editTextEmailRegistration);
        editTextPasswordRegistration = (EditText) findViewById(R.id.editTextPasswordRegistration);

        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        auth = FirebaseAuth.getInstance();
    }

    private void clickButton() {
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editUsername = editTextUsernameRegistration.getText().toString();
                String editEmail = editTextEmailRegistration.getText().toString();
                String editPassword = editTextPasswordRegistration.getText().toString();

                if (TextUtils.isEmpty(editUsername) || TextUtils.isEmpty(editEmail) || TextUtils.isEmpty(editPassword)) {
                    Toast.makeText(RegistrationActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (editPassword.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "min 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    registerUsers(editUsername, editEmail, editPassword);
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    private void registerUsers(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username.toLowerCase());
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegistrationActivity.this, InitialActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegistrationActivity.this, "You can't register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
