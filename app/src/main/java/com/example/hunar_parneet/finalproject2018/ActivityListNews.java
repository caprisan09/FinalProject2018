package com.example.hunar_parneet.finalproject2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class ActivityListNews extends AppCompatActivity{

    protected static final String ACTIVITY_NAME = " myToolbar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new ActivityFragmentNews();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }

}