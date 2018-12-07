package com.example.hunar_parneet.finalproject2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayCalculation  extends AppCompatActivity {

    TextView totalCal, avgCal, maxCal, minCal;
    EditText calculatedTotalCal, calculatedAvgCal, calculatedMaxCal, calculatedMinCal;
    Button  backButton;


    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.display_calculation);

        totalCal = (TextView)findViewById(R.id.totalCal);
        avgCal = (TextView)findViewById(R.id.avgCal);
        maxCal = (TextView)findViewById(R.id.maxCal);
        minCal= (TextView) findViewById(R.id. minCal);
        backButton=(Button)findViewById(R.id.back);

        totalCal.setText("Total Calories: "+getIntent().getExtras().getDouble("TotalCals"));
        avgCal.setText("Average Calories: "+getIntent().getExtras().getDouble("AvgCals"));
        maxCal.setText("Maximum Calories: "+getIntent().getExtras().getDouble("MaxCals"));
        minCal.setText("Minimum Calories: "+getIntent().getExtras().getDouble("MinCals"));


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i6 = new Intent(DisplayCalculation.this, FoodNutritionActivity.class);
                //startActivity(i4);
                startActivityForResult(i6, 50);
                //Log.i(ACTIVITY_NAME, getString(R.string.ReturnToStart));



            }
        });
    }

}
