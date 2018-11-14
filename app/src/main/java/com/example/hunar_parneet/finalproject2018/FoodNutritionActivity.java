package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FoodNutritionActivity extends Activity {

    ListView listView;
    ProgressBar progressBar;
    Button button;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);

        listView = (ListView)findViewById(R.id.listView);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View v){
                Context context = getApplicationContext();
                CharSequence text = "Button is clicked";
                    int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
        }


        });
}


}
