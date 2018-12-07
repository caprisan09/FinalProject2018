package com.example.hunar_parneet.finalproject2018;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.hunar_parneet.finalproject2018.SearchedFoodDatabase.ACTIVITY_NAME;

public class FoodNutritionActivity extends AppCompatActivity {

    ListView searchedFoodList;
    ProgressBar progressBar;
    Button searchButton,favFoodButton, deleteSearchButton;
    EditText foodSearch;
    public ArrayList<String> mySelectedFood;
    public SearchFoodAdapter searchFoodAdapter;
    public Context ctx = this;
    public Cursor cursor;
    public SearchedFoodDatabase mySearchFoodDatabase;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrition);


        mySelectedFood = new ArrayList<>();
        searchFoodAdapter = new SearchFoodAdapter(ctx);
        mySearchFoodDatabase = new SearchedFoodDatabase(ctx);
        db = mySearchFoodDatabase.getWritableDatabase();

        searchButton=(Button)findViewById(R.id.search);
        deleteSearchButton=(Button)findViewById(R.id.deleteSearch);
        foodSearch=(EditText)findViewById(R.id.foodSearch);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);
        searchedFoodList= (ListView)findViewById(R.id.selectedFood);

        runQuery();
        searchedFoodList.setAdapter(searchFoodAdapter);
        searchFoodAdapter.notifyDataSetChanged();

        deleteSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySelectedFood.clear();
                db.delete(SearchedFoodDatabase.TABLE_NAME, null, null);
                runQuery();
                searchFoodAdapter.notifyDataSetChanged();
            }
        });

        mylistView = findViewById(R.id.listView);
        myprogressBar = findViewById(R.id.progressBar);
        mybutton= findViewById(R.id.button);
        myeditText= findViewById(R.id.editText);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selection = foodSearch.getText().toString();
                setDatabaseInfo();
                Bundle infoToSend = new Bundle();
                infoToSend.putString("myfood", selection);
                Intent i1 = new Intent(FoodNutritionActivity.this, DataFromWebsite.class);
                i1.putExtras(infoToSend);
                startActivityForResult(i1, 50);

                    }
                });
        favFoodButton=(Button)findViewById(R.id.favFood);
        favFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(FoodNutritionActivity.this, ListOfFav.class);
                Log.i(ACTIVITY_NAME, "fav list button is clicked");
                // startActivity(i2);
                startActivityForResult(i2, 50);
                    }
                });
    }
       public void setDatabaseInfo(){

           String searchedFood = foodSearch.getText().toString();
           ContentValues newRow = new ContentValues();
           newRow.put(SearchedFoodDatabase.KEY_MESSAGE, searchedFood);//all columns have a value
           //ready to insert into database:
           db.insert(SearchedFoodDatabase.TABLE_NAME, "ReplacementValue", newRow);

           mySelectedFood.add(searchedFood);
           cursor = db.query(SearchedFoodDatabase.TABLE_NAME, new String[] {SearchedFoodDatabase.KEY_Id, SearchedFoodDatabase.KEY_MESSAGE},
                   null, null, null,null, null, null );
           searchFoodAdapter.notifyDataSetChanged();
           foodSearch.setText("");
       }
       public void runQuery()
       {
           cursor = db.query(SearchedFoodDatabase.TABLE_NAME, new String[] {SearchedFoodDatabase.KEY_Id, SearchedFoodDatabase.KEY_MESSAGE},
                   null, null, null,null, null, null );
           if(cursor!=null){
               cursor.moveToFirst();

               Log.i(ACTIVITY_NAME, "Cursor's column count = "+cursor.getColumnCount());
               for(int i = 0; i <cursor.getColumnCount(); i++)
                   Log.i(ACTIVITY_NAME, "Cursor column: = "+cursor.getColumnName(i));

               for(int i = 0; i < cursor.getCount(); i++)
               {
                   String na = cursor.getString(cursor.getColumnIndex(SearchedFoodDatabase.KEY_MESSAGE));

                   mySelectedFood.add(na);
                   cursor.moveToNext();  //read next row

                   Log.i(ACTIVITY_NAME, "Message: "+ na);
               }
           }
       }
       private class SearchFoodAdapter extends ArrayAdapter<String> {

           public SearchFoodAdapter(Context ctx){
               super(ctx,0);
           }


           @Override
           public int getCount() {
               return mySelectedFood.size();
           }

           @Override
           public String getItem(int position) {
               return mySelectedFood.get(position);
           }

           @Override
           public View getView(int position, View oldView, ViewGroup parent){
               LayoutInflater inflater = FoodNutritionActivity.this.getLayoutInflater();

               View result = inflater.inflate(R.layout.searched_food_list, null);

               TextView message = result.findViewById(R.id.searched_food);

               message.setText(getItem(position));
               return result;
           }

           @Override
           public long getItemId(int position) {
               return position;
           }
       }
}