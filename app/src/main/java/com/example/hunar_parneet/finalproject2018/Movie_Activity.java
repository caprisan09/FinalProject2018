package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Movie_Activity extends Activity {

    ListView listView;
    ProgressBar progressBar;
    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        //---LISTVIEW---
        listView = (ListView)findViewById(R.id.ListView);

        //--PROGRESSBAR--
        progressBar = (ProgressBar)findViewById(R.id.ProgressBar);

        //---BUTTON---
        button=(Button)findViewById(R.id.Button);

        //EDITTEXT
        editText = (EditText)findViewById(R.id.EditText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = "Button is Clicked";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context,text,duration);
                toast.show();

                /*Snackbar.make(progressBar,"String to show",
                Snackbar.LENGTH_LONG).show();*/



            }
        });

    }
}
