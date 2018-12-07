package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FoodCalFragment extends android.support.v4.app.Fragment {

        private final String ACTIVITY_NAME = "FoodCalFragment";
    TextView totalCal, avgCal, maxCal, minCal;

    Button  backButton;

    ListOfFav previousActivity;
        public boolean iAmTablet;
        public EmptyFrameLayoutActivity parent;

    public FoodCalFragment(){}


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            final View screen = inflater.inflate(R.layout.display_calculation, container, false);
            totalCal = (TextView)screen.findViewById(R.id.totalCal);
            avgCal = (TextView)screen.findViewById(R.id.avgCal);
            maxCal = (TextView)screen.findViewById(R.id.maxCal);
            minCal= (TextView) screen.findViewById(R.id. minCal);
            backButton=(Button)screen.findViewById(R.id.back);
            Bundle infoToPass = getArguments(); //returns the arguments set before

            totalCal.setText("Total Calories: "+infoToPass.getDouble("TotalCals"));
            avgCal.setText("Average Calories: "+infoToPass.getDouble("AvgCals"));
            maxCal.setText("Maximum Calories: "+infoToPass.getDouble("MaxCals"));
            minCal.setText("Minimum Calories: "+infoToPass.getDouble("MinCals"));


            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(ACTIVITY_NAME, "Delete button on fragment is clicked");


                     if (iAmTablet) {

                        getActivity().getSupportFragmentManager().beginTransaction().remove(FoodCalFragment.this).commit();


                    }
                    else {
                        Log.i(ACTIVITY_NAME, "Delete the message button clicked");
                        Intent resultIntent = new Intent();
                        getActivity().setResult(Activity.RESULT_OK, resultIntent);
                        getActivity().finish();
                    }
                }
            });

            return screen;

        }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(iAmTablet)
            previousActivity = (ListOfFav) context;
    }

}
