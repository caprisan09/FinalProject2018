package com.example.hunar_parneet.finalproject2018;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 *
 */
public class EmptyFrameLayoutActivity extends AppCompatActivity {

    /**
     *
     */
    public FragmentManager fm;
    /**
     *
     */
    private MessageFragment newFragment;
    /**
     *
     */
    private FragmentTransaction ftrans;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_frame_layout_acyivity);
        //get bundle back:
        Bundle infoToPass = getIntent().getExtras();
        //repeat from tablet section:

        newFragment = new MessageFragment();
        newFragment.iAmTablet = false;
        newFragment.setArguments(infoToPass); //give information to bundle
        fm = getSupportFragmentManager();
        ftrans = fm.beginTransaction();
        ftrans.replace(R.id.empty_frame, newFragment); //load a fragment into the framelayout
        ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
        ftrans.commit(); //actually load it


    }
}
