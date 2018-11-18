package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
private Button buttonStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = (Button)findViewById(R.id.Button1);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(MainActivity.this,ListNewsActivity.class);
                //startActivity(i);
            Intent intent = new Intent(context, ListNewsActivity.class);
            intent.putExtra("source", webSite.getSources().get(position).getId());
            intent.putExtra("sortBy", webSite.getSources().get(position).getSortBysAvailable).get(0);
            context.startActivity(intent);



            }
        });

    }
}
