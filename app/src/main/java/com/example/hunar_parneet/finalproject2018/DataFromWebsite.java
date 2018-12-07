package com.example.hunar_parneet.finalproject2018;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.hunar_parneet.finalproject2018.SearchedFoodDatabase.ACTIVITY_NAME;


public class DataFromWebsite extends AppCompatActivity {

    TextView searchedFood, calories, fatContent;
    EditText caloriesFromDb,fatContentFromDb;
    Button addToFavButton, backButton;
    public static final String ACTIVITY_NAME = "DataFromWebsite";
    String myfood, mycalories, myfatContent;
    private SQLiteDatabase db;
    public List<FoodDetail> myfavoriteList;
    public FavFoodDatabase favFoodDatabase;
    Context ctx =this;
    public Cursor cursor;


    private ArrayList<String> favFoodList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.data_from_website);
        myfavoriteList = new ArrayList<>();
        favFoodDatabase = new FavFoodDatabase(ctx);


        db =  favFoodDatabase.getWritableDatabase();

        myfood = getIntent().getExtras().getString("myfood");

        Log.i(ACTIVITY_NAME, "You seleted: "+myfood);

        searchedFood = (TextView)findViewById(R.id.searchedFood);
        calories = (TextView)findViewById(R.id.calories);
        fatContent = (TextView)findViewById(R.id.fatContent);
      /*  caloriesFromDb = (EditText)findViewById(R.id.showCalories);
        fatContentFromDb= (EditText)findViewById(R.id.showFatContent);*/

      addToFavButton=(Button)findViewById(R.id.addToFav);
        backButton=(Button)findViewById(R.id.back);


        addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatabaseInfo();
                myfavoriteList.add(new FoodDetail(myfood, mycalories, myfatContent));
               // Intent i3 = new Intent(DataFromWebsite.this, ListOfFav.class);
               // startActivity(i3);
               // startActivityForResult(i3, 50);
                //Log.i(ACTIVITY_NAME, getString(R.string.ReturnToStart));
                Toast.makeText(ctx, "This food is added to fav foods", Toast.LENGTH_LONG).show();



            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(DataFromWebsite.this, FoodNutritionActivity.class);
                //startActivity(i4);
                //startActivityForResult(i4, 50);
                //Log.i(ACTIVITY_NAME, getString(R.string.ReturnToStart));
                setResult(Activity.RESULT_OK, i4);
                finish();



            }
        });

        JSONQuery query = new JSONQuery();
        query.execute();





    }
    public void setDatabaseInfo(){

        //String searchedFood = foodSearch.getText().toString();
        ContentValues newRow = new ContentValues();
        newRow.put(FavFoodDatabase.KEY_FOOD, myfood);//all columns have a value
        newRow.put(FavFoodDatabase.KEY_CALORIE, mycalories);
        newRow.put(FavFoodDatabase.KEY_FAT, myfatContent);
        //ready to insert into database:
        db.insert(FavFoodDatabase.TABLE_NAME, "ReplacementValue", newRow);

     //   mySelectedFood.add(searchedFood);
        cursor = db.query(FavFoodDatabase.TABLE_NAME, new String[] {FavFoodDatabase.KEY_Id, FavFoodDatabase.KEY_FOOD,
                        FavFoodDatabase.KEY_CALORIE, FavFoodDatabase.KEY_FAT},null, null, null,null, null, null );
      //  searchFoodAdapter.notifyDataSetChanged();
     //   foodSearch.setText("");
    }

class JSONQuery extends AsyncTask<String, String, String>{
        /*String calories = "";
        String fatContent = "";*/



        public String doInBackground(String...args){

        try {
            //connect to Server:
            URL url = new URL("https://api.edamam.com/api/food-database/parser?app_id=5a96e98b&app_key=4ca6fa5b0cac3e06497ceed3c3b5f64c&ingr="+myfood);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream response = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
                while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            String result = sb.toString();
            JSONObject root = new JSONObject(result); //jObject is the root object
           myfood= root.getString("text");
          // publishProgress(...value:30);
            Log.i("Food name ", myfood);


      //  Log.i(ACTIVITY_NAME, "I made the root object");
            JSONArray parsedArray = root.getJSONArray("parsed"); //get variable "parsed" from root, which is an array
            Log.i(ACTIVITY_NAME, "I parsed the array");
            JSONObject parsed = parsedArray.getJSONObject(0);
            JSONObject food =parsed.getJSONObject("food"); //get first element of array
            JSONObject nutrients =food.getJSONObject("nutrients");


            //Log.i("Icon is:", icon);


            mycalories = nutrients.getString( "ENERC_KCAL"); // get "ENERC_KCAL" from nutrients
           myfatContent = nutrients.getString( "FAT"); // get "Fat" from nutrients


            Log.i(ACTIVITY_NAME, "Calories: "+mycalories);
            Log.i(ACTIVITY_NAME, "Fat: "+myfatContent);

            String value = "";


              /*  JSONObject wind = root.getJSONObject("wind"); //get variable "wind" from root
                    String speed = wind.getString("speed"); //get variable "speed" from wind
                    Log.i("Speed is:", speed);*/
        }
        catch(Exception e)
        {
            Log.i("Exception", e.getMessage());
        }

        return "";
    }


        public void onProgressUpdate(String ... args) //update your GUI
        {
            //setText()
            //setImage()
        }


        public void onPostExecute(String result)  // doInBackground has finished
        {
            searchedFood.setText(myfood + " has the following Calories and fat-content");
            calories.setText("The calories are "+ mycalories);
            fatContent.setText("The fat- content is "+ myfatContent);
        }

    }
}


