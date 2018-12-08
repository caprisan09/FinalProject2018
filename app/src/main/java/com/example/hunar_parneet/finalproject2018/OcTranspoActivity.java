package com.example.hunar_parneet.finalproject2018;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *This activity is the home page for searching the oc transpo bus stop for route information
 */
public class OcTranspoActivity extends AppCompatActivity {
    /**
     *It takes the stop number as input from user
     */
    private EditText editText;
    /**
     *This is a search button when clicked find all the route information and display it
     */
    private Button button;

    /**
     *This array adapter is used to display the list of previous searches by the user
     */
    public StopListItemsAdapter messageAdapter;
    /**
     *This list stores the information parsed from the xml data
     */
    public List<MessageResult> myArrayList;

    /**
     *This variable is used for displaying toast in the activity
     */
    private Toast toast;
    /**
     *This variable stores the current context
     */
    private Context ctx;
    /**
     *This is the duration for which the Toast will be displayed;
     */
    private int duration = Toast.LENGTH_LONG;
    /**
     *This is the message that is displayed as a Toast
     */
    private CharSequence text = "Searching for the bus route";
    /**
     *This stores name of the current activity and is used during Log information
     */
    private static final String ACTIVITY_NAME = "OcTranspoActivity";
    /**
     *This variable stores the stop number entered by the user
     */
    public int stopNumber;
    /**
     *This is the database used to store the stop numbers so that they can be displayed as previous searches
     */
    public OctranspoDatabaseHelper dbopener;
    /**
     *This variable is used to run queries on the database
     */
    private SQLiteDatabase db;
    /**
     *This variable is used to declare the object for the MessageResult class
     */
    public MessageResult messageAndID;
    /**
     *This is the declaration for the Cursor
     */
    public Cursor cursor;
    /**
     *This variable is used to get the row id from the database
     */
    public long databaseID;
    /**
     *This variable is prepare the list of all stops in the order they are searched
     */
    public ArrayList<String> busesList;
    /**
     *This list is used to display the searches in reverse order so that recent search is on the top
     */
    public ArrayList<String> displayList;
    /**
     *This button is used to delete the stop number search history
     */
    public Button deleteHistoryBtn;
    /**
     *This variable is used to decide if the user is on phone or tablet
     */
    public boolean ifFrameExist;
    /**
     *This object is used to start a fragment activity
     */
    private MessageFragment newFragment;
    /**
     *This object manages different fragments
     */
    private FragmentManager fm;
    /**
     *This object is used to start and finish fragment transactions
     */
    private FragmentTransaction ftrans;


    /**
     *This constructor initializes the objects so that they are not null
     */
    public OcTranspoActivity(){
        ctx =this;
        myArrayList = new ArrayList<>();
        busesList = new ArrayList<>();
        displayList = new ArrayList<>();
        dbopener = new OctranspoDatabaseHelper(ctx);


    }

    /**
     *This method is used to enter information into the database
     */
    public void setDatabaseInfo(){

        String stopEntry = editText.getText().toString();
        ContentValues newRow = new ContentValues();
        newRow.put("KEY_STOP_NUMBER", editText.getText().toString());//all columns have a value
        //ready to insert into database:
        databaseID = db.insert(OctranspoDatabaseHelper.TABLE_NAME, "ReplacementValue", newRow);
        messageAndID = new MessageResult(databaseID, stopEntry);
        myArrayList.add(messageAndID);
        cursor = db.query(OctranspoDatabaseHelper.TABLE_NAME, new String[] {OctranspoDatabaseHelper.COL_ID, OctranspoDatabaseHelper.COL_STOP_NUMBER},
                null, null, null,null, null, null );
        messageAdapter.notifyDataSetChanged();
        editText.setText("");
    }

    /**
     *This method is used to query the existing database to get desired action
     */
    public void runQuery()
    {
        cursor = db.query(OctranspoDatabaseHelper.TABLE_NAME, new String[] {OctranspoDatabaseHelper.COL_ID, OctranspoDatabaseHelper.COL_STOP_NUMBER},
                null, null, null,null, null, null );
        if(cursor!=null){
            cursor.moveToFirst();
            for(int i = 0; i < cursor.getCount(); i++)
            {
                String na = cursor.getString(cursor.getColumnIndex(OctranspoDatabaseHelper.COL_STOP_NUMBER));
                databaseID = cursor.getLong(cursor.getColumnIndex(OctranspoDatabaseHelper.COL_ID));

                messageAndID = new MessageResult(databaseID, na);
                myArrayList.add(messageAndID);
                cursor.moveToNext();  //read next row
            }}}

    /**
     *This is the first method called when the activity starts
     * @param savedInstanceState - this is the bundle with intent
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oc_transpo_activity);
        FrameLayout fLayout = findViewById(R.id.frame_layout);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ifFrameExist = fLayout != null;
        deleteHistoryBtn = findViewById(R.id.clear_Btn);
        deleteHistoryBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *This statements in this method are executed when delete history button is clicked by the user
              * @param v - this is the view
             */
            @Override
            public void onClick(View v) {
                deleteMessage();
            }
        });
        db = dbopener.getWritableDatabase();

        /*Initialize the custom adapter for reading the list array*/
        messageAdapter = new StopListItemsAdapter(this);
        /*create reference to the components of the xml file*/
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        ListView listView = findViewById(R.id.list);

        /*inflate the list view from the custom adapter class*/
        listView.setAdapter(messageAdapter);

        /*when button is clicked, show a toast and snack bar to the user on the same window*/
        button.setOnClickListener(new View.OnClickListener() {
            /**
             *This statements in this method are executed when search button is clicked by the user
             * @param v - this is the view
             */
            @Override
            public void onClick(View v) {

                toast = Toast.makeText(ctx, text, duration);
                toast.show();
                Snackbar.make(button,"I am snackbar", Snackbar.LENGTH_LONG).show();
                stopNumber = Integer.parseInt(editText.getText().toString());
                setDatabaseInfo();

                Bundle infoToPass = new Bundle();
                Log.i(ACTIVITY_NAME, "List item was clicked");
                infoToPass.putInt("stopNumber", stopNumber);


                if(ifFrameExist) {

                    // copy this section in EmptyFragmentWindow
                    newFragment = new MessageFragment();
                    newFragment.iAmTablet = true;
                    newFragment.setArguments( infoToPass ); //give information to bundle

                    fm = getSupportFragmentManager();
                    ftrans = fm.beginTransaction();
                    ftrans.replace(R.id.frame_layout, newFragment); //load a fragment into the framelayout
                    ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
                    ftrans.commit(); //actually load it

                    //end of section to copy
                }
                else //on a phone
                {
                    Log.i(ACTIVITY_NAME, "I am on phone");
                    //go to new window:
                    Intent nextPage = new Intent(OcTranspoActivity.this, EmptyFrameLayoutActivity.class);
                    nextPage.putExtras(infoToPass); //send info
                    startActivityForResult(nextPage, 67);
                }



            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OcTranspoActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.dialog_message) //Add a dialog message to strings.xml
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                Intent resultIntent = new Intent( );
                                resultIntent.putExtra("Response", "Here is my response");
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        })
                        .show();
                Bundle infoToPass = new Bundle();
                Log.i(ACTIVITY_NAME, "List item was clicked");
                stopNumber = Integer.parseInt(messageAdapter.getItem(position));

                infoToPass.putInt("stopNumber", stopNumber);

                if(ifFrameExist) {
                    Log.i(ACTIVITY_NAME, "I am a tablet");
                    // copy this section in EmptyFragmentWindow

                    newFragment = new MessageFragment();
                    newFragment.iAmTablet = true;
                    newFragment.setArguments( infoToPass ); //give information to bundle
                    fm = getSupportFragmentManager();
                    ftrans = fm.beginTransaction();
                    ftrans.replace(R.id.frame_layout, newFragment); //load a fragment into the framelayout
                    ftrans.addToBackStack("name doesn't matter"); //changes the back button behaviour
                    ftrans.commit(); //actually load it

                    //end of section to copy
                }
                else //on a phone
                {
                    Log.i(ACTIVITY_NAME, "I am on phone");
                    //go to new window:
                    Intent nextPage = new Intent(OcTranspoActivity.this, EmptyFrameLayoutActivity.class);
                    nextPage.putExtras(infoToPass); //send info
                    startActivityForResult(nextPage, 67);
                }

            }
        });
        runQuery();
        if(!myArrayList.isEmpty()) {
            for (int i = myArrayList.size(); i > 0; i--) {
                displayList.add(myArrayList.get(i - 1).getMsg());
            }
            /*update list view when array list is changed*/
            messageAdapter.notifyDataSetChanged();
        }

    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==Activity.RESULT_OK) {
            if (data.getExtras().getInt("ListItem") == 1) {
                Intent busDetail = new Intent(OcTranspoActivity.this, BusDetailActivity.class);
                busDetail.putExtras(data);
                startActivity(busDetail);

            } else {
                Log.i(ACTIVITY_NAME, "Came back from the bus display list");
                refreshView();
            }
        }
    }

    /**
     *
     */
    public void refreshView(){
        displayList.clear();

        if(!myArrayList.isEmpty()) {
            for (int i = myArrayList.size(); i > 0; i--) {
                displayList.add(myArrayList.get(i - 1).getMsg());
            }
        }
        messageAdapter.notifyDataSetChanged();

    }

    /**
     *
     */
    public void deleteMessage(){

        /*Delete the selected row*/
        db.delete(OctranspoDatabaseHelper.TABLE_NAME, null, null );

        Log.i(ACTIVITY_NAME, "I am in the deleteMessage function");
        /*update your array list for both message and database id*/

        myArrayList.clear();
        displayList.clear();
        runQuery();
        messageAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;


    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.movies:
                Intent movies_page = new Intent(OcTranspoActivity.this, Movie_Activity.class);
                startActivity(movies_page);
                break;

            case R.id.news:
                Intent news_page = new Intent(OcTranspoActivity.this, StartActivity.class);
                startActivity(news_page);
                break;
            case R.id.bus:
                Intent bus_page = new Intent(OcTranspoActivity.this, OcTranspoActivity.class);
                startActivity(bus_page);
                break;
            case R.id.food:
                Intent food_page = new Intent(OcTranspoActivity.this, FoodNutritionActivity.class);
                startActivity(food_page);
                break;
            case R.id.help:
                AlertDialog.Builder about = new AlertDialog.Builder(OcTranspoActivity.this);
                about.setMessage(R.string.author_name).setTitle(R.string.app_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                duration = Toast.LENGTH_LONG;
                                Toast.makeText(ctx,"Welcome to my app", duration)
                                        .show();
                            }
                        })
                        .show();
                break;


        }

        return true;
    }

    /**
     *
     */
    /*this inner class create a custom adapter which reads the array list and updates List view in the base class*/
    private class StopListItemsAdapter extends ArrayAdapter<String> {

        /**
         *
          * @param ctx
         */
        public StopListItemsAdapter(Context ctx){
            super(ctx,0);
        }

        /**
         *
         * @return
         */
        @Override
        public int getCount() {
            return displayList.size();
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public String getItem(int position) {
            return displayList.get(position);
        }

        /**
         *
         * @param position
         * @param oldView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View oldView, ViewGroup parent){
            LayoutInflater inflater = OcTranspoActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.list_row, null);

            TextView message = result.findViewById(R.id.list_text);
            message.setText(getItem(position));
            return result;
        }

        /**
         *
         * @param position
         * @return
         */
        @Override
        public long getItemId(int position) {
            return position;
        }
    }


}
