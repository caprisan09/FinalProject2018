package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.hunar_parneet.finalproject2018.SearchedFoodDatabase.ACTIVITY_NAME;

public class ListOfFav extends AppCompatActivity {

    Button calculate, deleteFav;
    ListView favFood;
    public FavFoodDatabase favFoodDatabase;
    Context ctx = this;
    public Cursor cursor;
    public SQLiteDatabase db;
    public List<FoodDetail> myFavFoodDetails;
    public FavFoodAdapter favFoodAdapter;
    CheckBox chkBox;
    public ArrayList<String> allCalories, allFood, allFatContent;
    public List<FoodDetail> myCalculationList;
    public boolean ifFrameExist;


    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.list_of_fav);
        favFoodDatabase = new FavFoodDatabase(ctx);
        myFavFoodDetails = new ArrayList<>();
        favFoodAdapter = new FavFoodAdapter(ctx);
        allCalories = new ArrayList<>();
        allFood = new ArrayList<>();
        allFatContent = new ArrayList<>();
        myCalculationList = new ArrayList<>();

        Log.i(ACTIVITY_NAME, "I am in ListOfFav activity");

        db = favFoodDatabase.getReadableDatabase();

       favFood = (ListView)findViewById(R.id.favList);
        calculate=(Button)findViewById(R.id. calculate);
       deleteFav=(Button)findViewById(R.id.deleteFav);
       runQuery();
       favFood.setAdapter(favFoodAdapter);
       favFoodAdapter.notifyDataSetChanged();
        FrameLayout fLayout = findViewById(R.id.frameLayout);
        ifFrameExist = fLayout != null;

        deleteFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = myCalculationList.size();
                Log.i(ACTIVITY_NAME, "The size of list is: "+myCalculationList.size());
                for(int i=0; i<size; i++) {
                    String foodName = myCalculationList.get(i).getFood();
                    Log.i(ACTIVITY_NAME, "The food name: "+foodName);
                    db.delete(FavFoodDatabase.TABLE_NAME, FavFoodDatabase.KEY_FOOD + " = ?", new String[]{foodName});
                }
                myFavFoodDetails.clear();
                runQuery();
                favFoodAdapter.notifyDataSetChanged();
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double maxCals=0;
                double minCals=0;
                double totalCals=0;
                double avgCals=0;
                int listSize=0;
                double [] cals=null;
                if(!myCalculationList.isEmpty()) {
                    listSize = myCalculationList.size();
                    cals = new double[listSize];
                }
                Log.i(ACTIVITY_NAME, "The size of list is: "+myCalculationList.size());
                Log.i(ACTIVITY_NAME, "The size of cals array is: "+ cals.length);
                for(int i=0; i<listSize; i++){

                    cals[i] = Double.valueOf(myCalculationList.get(i).getCalories());
                }
                /*calculate the maximum calories*/
                int count=0;
                for(int i=0; i<cals.length; i++){
                    for(int j=0; j<cals.length; j++){
                        if(cals[i]>=cals[j]){
                            count++;
                        }
                    }
                    Log.i(ACTIVITY_NAME, "The count is: "+count);
                    if(count==cals.length){
                        maxCals = cals[i];
                    }
                    count=0;
                }
                /*calculate the minimum of the calories*/
                count=0;
                for(int i=0; i<cals.length; i++){
                    for(int j=0; j<cals.length; j++){
                        if(cals[i]<=cals[j]){
                            count++;
                        }
                    }
                    Log.i(ACTIVITY_NAME, "The count is: "+count);
                    if(count==cals.length){
                        minCals = cals[i];
                    }
                    count=0;
                }
                /*calculate total calories*/
                for(int i=0; i<cals.length; i++){
                    totalCals+=cals[i];
                }
                /*calculate average calories*/
                for(int i=0; i<cals.length; i++){
                    int sum=0;
                    sum+=cals[i];
                    avgCals = sum/cals.length;

                }
                Bundle infoToPass = new Bundle();
                infoToPass.putDouble("AvgCals", avgCals);
                infoToPass.putDouble("MaxCals", maxCals);
                infoToPass.putDouble("MinCals", minCals);
                infoToPass.putDouble("TotalCals", totalCals);

                if(ifFrameExist) {
                    Log.i(ACTIVITY_NAME, "I am a tablet");
                    // copy this section in EmptyFragmentWindow
                    FoodCalFragment newFragment = new FoodCalFragment();

                    newFragment.iAmTablet = true;

                    newFragment.setArguments( infoToPass ); //give information to bundle
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ftrans = fm.beginTransaction();
                    ftrans.replace(R.id.frameLayout, newFragment); //load a fragment into the framelayout
                    ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
                    ftrans.commit(); //actually load it

                    //end of section to copy
                }
                else //on a phone
                {
                    Log.i(ACTIVITY_NAME, "I am on phone");
                    //go to new window:
                    Intent nextPage = new Intent(ListOfFav.this, EmptyFrameLayoutActivity.class);
                    nextPage.putExtras(infoToPass); //send info
                    startActivityForResult(nextPage, 97);
                }



            }
        });

    }
    public void runQuery()
    {
        cursor = db.query(FavFoodDatabase.TABLE_NAME, new String[] {FavFoodDatabase.KEY_Id, FavFoodDatabase.KEY_FOOD,
                FavFoodDatabase.KEY_CALORIE, FavFoodDatabase.KEY_FAT},null, null, null,null, null, null );
        if(cursor!=null){
            cursor.moveToFirst();

            Log.i(ACTIVITY_NAME, "Cursor's column count = "+cursor.getColumnCount());
            for(int i = 0; i <cursor.getColumnCount(); i++)
                Log.i(ACTIVITY_NAME, "Cursor column: = "+cursor.getColumnName(i));

            for(int i = 0; i < cursor.getCount(); i++)
            {
                String food = cursor.getString(cursor.getColumnIndex(FavFoodDatabase.KEY_FOOD));
                String calories = cursor.getString(cursor.getColumnIndex(FavFoodDatabase.KEY_CALORIE));
                String fatContent = cursor.getString(cursor.getColumnIndex(FavFoodDatabase.KEY_FAT));

                myFavFoodDetails.add(new FoodDetail(food, calories, fatContent));
                cursor.moveToNext();  //read next row

                Log.i(ACTIVITY_NAME, "Food: "+ food);
                Log.i(ACTIVITY_NAME, "Calories: "+ calories);
                Log.i(ACTIVITY_NAME, "Fat content: "+ fatContent);
            }
        }else{Log.i(ACTIVITY_NAME, "The cursor returned NULL");}
    }
    private class FavFoodAdapter extends ArrayAdapter<FoodDetail> {

        public FavFoodAdapter(Context ctx){
            super(ctx,0);
        }


        @Override
        public int getCount() {
            return myFavFoodDetails.size();
        }

        @Override
        public FoodDetail getItem(int position) {
            return myFavFoodDetails.get(position);
        }

        @Override
        public View getView(final int position, View oldView, ViewGroup parent){
            LayoutInflater inflater = ListOfFav.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.infl_fav_list, null);

            TextView myfood = result.findViewById(R.id.favFood);
            TextView  myCalories = result.findViewById(R.id.favFoodCalories);
            TextView myFatContent = result.findViewById(R.id.favFoodFat);

            myfood.setText("Food: "+getItem(position).getFood());
            myCalories.setText("Calories: "+getItem(position).getCalories());
            myFatContent.setText("Fat content: "+getItem(position).getFatContent());
            chkBox = result.findViewById(R.id.tag_me);
            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        long itemPos = getItemId(position);
                        String foodItem = getItem(position).getFood();
                        String foodCals = getItem(position).getCalories();
                        String foodFat = getItem(position).getFatContent();
                        myCalculationList.add(new FoodDetail(foodItem, foodCals, foodFat));
                        Log.i(ACTIVITY_NAME, "The food tagged is: "+foodItem);
                    }

                }});

            return result;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

}
