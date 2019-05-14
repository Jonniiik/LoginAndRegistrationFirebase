package com.eugeneponomarev.loginandregistration;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inizi);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new UserFragment();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

    }
}
