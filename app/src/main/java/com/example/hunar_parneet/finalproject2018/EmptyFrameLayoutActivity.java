package com.example.hunar_parneet.finalproject2018;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class EmptyFrameLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_frame_layout);

        //get bundle back:
        Bundle infoToPass = getIntent().getExtras(); //this is bundle from line 65, FragmentExample.java

        //repeat from tablet section:


        FoodCalFragment newFragment = new FoodCalFragment();
        newFragment.iAmTablet = false;
        newFragment.setArguments(infoToPass); //give information to bundle

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftrans = fm.beginTransaction();
        ftrans.replace(R.id.empty_frame, newFragment); //load a fragment into the framelayout
        ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
        ftrans.commit(); //actually load it
    }
}

