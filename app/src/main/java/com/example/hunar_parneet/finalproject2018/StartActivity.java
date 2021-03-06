package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private Button start;
    protected static final String ACTIVITY_NAME = " >> >> in StartActivity";
    private FrameLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.i(ACTIVITY_NAME, "Method In onCreate() << <<");

        Toolbar lab8_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(lab8_toolbar);



        start = (Button)findViewById(R.id.Button1);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(StartActivity.this,ActivityListNews.class);
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast;
        View view;


        switch(item.getItemId()){

            case R.id.action_bus:
                Intent bus_page = new Intent(StartActivity.this, OcTranspoActivity.class);
                startActivity(bus_page);
                break;
            case  R.id.action_movie:

                Intent movie_page = new Intent(StartActivity.this, Movie_Activity.class);
                startActivity(movie_page);
                break;

            case R.id.action_food:
                Intent food_page = new Intent(StartActivity.this, FoodNutritionActivity.class);
                startActivity(food_page);
                break;

            case R.id.about:
                CharSequence text = "Version 1.0 CBC NEWS William Bernal !";//
                int duration = Toast.LENGTH_SHORT;

                toast = Toast.makeText(getApplicationContext(), text, duration);
                view = toast.getView();
                view.setBackgroundColor(Color.GREEN);
                toast.show();
                break;

            case R.id.action_read:
                text = "Saved articles available: ";//
                duration = Toast.LENGTH_LONG;
                Intent i = new Intent(StartActivity.this,SavedNews.class);
                startActivity(i);
                toast = Toast.makeText(getApplicationContext(), text, duration);
                view = toast.getView();
                view.setBackgroundColor(Color.GREEN);
                toast.show();



                break;
        }
        return true;


    }



}
