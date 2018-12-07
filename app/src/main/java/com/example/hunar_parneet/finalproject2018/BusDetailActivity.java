package com.example.hunar_parneet.finalproject2018;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BusDetailActivity extends AppCompatActivity {
    /**
     *
     */
    public final static String ACTIVITY_NAME = "BusDetailActivity";
    public ArrayList<Double> averageDelay;
    public ArrayList<Double> myStatistics;
    public ArrayList<Double> statResults;
    /**
     *
     */
    private TimeDatabaseHelper avgAdjTime;
    /**
     *
     */
    private List<BusDetail> detailBusList;
    /**
     *
     */
    private DetailBusInfoQuery detailBusInfoQuery;
    /**
     *
     */
    public DetailBusListItemsAdapter detailBusAdapter;
    /**
     *
     */
    private int busNumber;
    /**
     *
     */
    private int stopNumber;
    /**
     *
     */
    private Context ctx = this;
    /**
     *
     */
    public ListView detailList;
    /**
     *
     */
    public ProgressBar pBar;
    private Cursor cursor;
    public ContentValues newRow;
    /**
     *
     */
    private SQLiteDatabase db;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_list);
        avgAdjTime = new TimeDatabaseHelper(ctx);
        newRow = new ContentValues();
        db = avgAdjTime.getWritableDatabase();
        averageDelay = new ArrayList<>();
        pBar = findViewById(R.id.pbar);
        pBar.setVisibility(View.VISIBLE);

        detailBusInfoQuery = new DetailBusInfoQuery();
        detailBusAdapter = new DetailBusListItemsAdapter(ctx);
        detailBusList = new ArrayList<>();
        myStatistics = new ArrayList<>();
        statResults=new ArrayList<>();

        detailList = findViewById(R.id.detail_view);

        busNumber = getIntent().getExtras().getInt("busNumber");
        stopNumber = getIntent().getExtras().getInt("stopNumber");

        detailBusInfoQuery.execute();

        detailList.setAdapter(detailBusAdapter);
        detailBusAdapter.notifyDataSetChanged();
    }

    public void populateStatList() {
        if (!averageDelay.isEmpty()) {

            Log.i(ACTIVITY_NAME, "The size of average delay is: "+averageDelay.size());
            myStatistics.clear();
            for (int i=0; i<averageDelay.size(); i++) {
                if (!statResults.isEmpty()) {
                    myStatistics.add(statResults.get(i));
                } else {
                    myStatistics.add(averageDelay.get(i));
                }
            }
            statResults.clear();

            for (int i=0; i<averageDelay.size(); i++) {
                double calculate = 0.0;
                if (!myStatistics.isEmpty()) {
                    calculate = myStatistics.get(i) + averageDelay.get(i) / 2;
                    Log.i(ACTIVITY_NAME, "The calculate is: " + calculate);
                }
                statResults.add(calculate);
            }
        }else{
            Log.i(ACTIVITY_NAME, "The average delay list is empty, with size: "+averageDelay.size());
        }
    }

    public void runQuery() {
        Log.i(ACTIVITY_NAME, "I am before cursor");
        int count = 0;
        //Log.i(ACTIVITY_NAME, "the position parameter is: "+position);
        cursor = db.query(TimeDatabaseHelper.TABLE_NAME, new String[]{TimeDatabaseHelper.COL_ID, TimeDatabaseHelper.COL_BUS_NUM,
                TimeDatabaseHelper.COL_ADJUSTED_TIME}, null, null, null, null, null, null);
        double adjTime = 0.0;
        if (cursor != null) {
            cursor.moveToFirst();
            Log.i(ACTIVITY_NAME, "The number of rows in database: " + cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {

                int busNum = cursor.getInt(cursor.getColumnIndex(TimeDatabaseHelper.COL_BUS_NUM));
                Log.i(ACTIVITY_NAME, "The bus number from database: " + busNum);
                //            long rowID = cursor.getLong(cursor.getColumnIndex(TimeDatabaseHelper.COL_ID));
                adjTime = cursor.getDouble(cursor.getColumnIndex(TimeDatabaseHelper.COL_ADJUSTED_TIME));
                Log.i(ACTIVITY_NAME, "The avg adjusted time from database: " + adjTime);
                if (!averageDelay.isEmpty()) {
                    adjTime = (adjTime + averageDelay.get(i)) / 2;
                } else {
                    adjTime = adjTime;
                }
                Log.i(ACTIVITY_NAME, "The adjusted time is: " + adjTime);
                averageDelay.add(adjTime);
                cursor.moveToNext();  //read next row
            }
            Log.i(ACTIVITY_NAME, "The count is: " + count);

        }

    }

    /**
     *
     */
    class DetailBusInfoQuery extends AsyncTask<String, Integer, String> {

        /**
         *
         */
        private int count = 0;
        /**
         *
         */
        private int route;
        /**
         *
         */
        private String busInfo = "";
        /**
         *
         */
        private String tripDest = "";
        /**
         *
         */
        private String startTime = "";
        /**
         *
         */
        private double gpsSpeed;
        /**
         *
         */
        private double longitude;
        /**
         *
         */
        private double latitude;
        /**
         *
         */
        private String adjustedTime = "";
        /**
         *
         */
        private XmlPullParser xpp;
        /**
         *
         */
        private static final String ACTIVITY_NAME = "InfoQuery";

        /**
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                Log.i(ACTIVITY_NAME, "The stop number: " + stopNumber);
                Log.i(ACTIVITY_NAME, "The bus number: " + busNumber);
                url = new URL("https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=" + stopNumber + "&routeNo=" + busNumber);
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                // Starts the query
                Log.i(ACTIVITY_NAME, "I am before connect()");
                conn.connect();

                xpp = Xml.newPullParser();
                xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xpp.setInput(conn.getInputStream(), "UTF-8");

                Log.i(ACTIVITY_NAME, "I am outside of while loop");
                int count = 0;
                while (xpp.next() != XmlPullParser.END_DOCUMENT) {

                    Log.i(ACTIVITY_NAME, "I enter the while loop");

                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "I am in first IF condition");
                        String name = xpp.getName();
                        Log.i(ACTIVITY_NAME, "Name: " + name);

                        if (name.equals("RouteNo")) {
                            if (xpp.next() == XmlPullParser.TEXT) {
                                route = Integer.parseInt(xpp.getText());
                                publishProgress(10);
                            }
                        }
                        if (name.equals("TripDestination")) {
                            if (xpp.next() == XmlPullParser.TEXT) {
                                count++;
                                tripDest = xpp.getText();
                                publishProgress(20);
                            }
                        }
                        if (name.equals("TripStartTime")) {

                            if (xpp.next() == XmlPullParser.TEXT) {
                                startTime = xpp.getText();
                                publishProgress(30);
                            }
                        }
                        if (name.equals("AdjustedScheduleTime")) {

                            if (xpp.next() == XmlPullParser.TEXT) {
                                adjustedTime = xpp.getText();
                                publishProgress(40);
                            }
                        }
                        if (name.equals("GPSSpeed")) {

                            if (xpp.next() == XmlPullParser.TEXT) {
                                gpsSpeed = Double.parseDouble(xpp.getText());
                                publishProgress(50);
                            }
                        }
                        if (name.equals("Latitude")) {

                            if (xpp.next() == XmlPullParser.TEXT) {
                                latitude = Double.parseDouble(xpp.getText());
                                publishProgress(60);
                            }
                        }
                        if (name.equals("Longitude")) {

                            if (xpp.next() == XmlPullParser.TEXT) {
                                longitude = Double.parseDouble(xpp.getText());
                                publishProgress(70);
                            }
                        }
                    }
                    if (xpp.getEventType() == XmlPullParser.END_TAG) {
                        count=0;
                        String endtag = xpp.getName();
                        if (endtag.equals("Trip")) {
                            count++;
                           // db.delete(TimeDatabaseHelper.TABLE_NAME, null, null);

                            Log.i(ACTIVITY_NAME, "I have deleted all the rows");
                            detailBusList.add(new BusDetail(route, gpsSpeed, latitude, longitude, tripDest, startTime, adjustedTime));
                            /*newRow.put("KEY_ADJUSTED_TIME", adjustedTime);//all columns have a value
                            newRow.put("KEY_BUS_NUM", route);
                            Log.i(ACTIVITY_NAME, "I am going to insert a row");
                            db.insert(TimeDatabaseHelper.TABLE_NAME, "ReplacementValue", newRow);
                            runQuery();*/
                            averageDelay.add(Double.parseDouble(adjustedTime));

                            publishProgress(90);
                        }

                        Log.i(ACTIVITY_NAME, "The end tag is: " + endtag);
                    }
                }
                Log.i(ACTIVITY_NAME, "The size of average delay is: "+averageDelay.size());
                populateStatList();
                Log.i(ACTIVITY_NAME, "The loop count is: " + count);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return busInfo;
        }

        /**
         * @param value
         */
        @Override
        protected void onProgressUpdate(Integer... value) {
            pBar.setVisibility(View.VISIBLE);
            pBar.setProgress(value[0]);

        }

        /**
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            averageDelay.clear();
            detailBusAdapter.notifyDataSetChanged();
            Log.i(ACTIVITY_NAME, "I am in onPostExecute");
            pBar.setVisibility(View.INVISIBLE);
        }


    }

    /**
     *
     */
    class DetailBusListItemsAdapter extends ArrayAdapter<BusDetail> {

        /**
         * @param ctx
         */
        public DetailBusListItemsAdapter(Context ctx) {
            super(ctx, 0);

        }

        /**
         * @return
         */
        @Override
        public int getCount() {
            return detailBusList.size();
        }

        /**
         * @param position
         * @return
         */
        @Override
        public BusDetail getItem(int position) {

            return detailBusList.get(position);

        }

        /**
         * @param position
         * @param oldView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View oldView, ViewGroup parent) {
            LayoutInflater inflater = BusDetailActivity.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.list_bus_detail, null);

            TextView startTime = result.findViewById(R.id.start_time);
            TextView adjustedTime = result.findViewById(R.id.adjusted_time);
            TextView longitude = result.findViewById(R.id.longitude);
            TextView latitude = result.findViewById(R.id.latitude);
            TextView gpsSpeed = result.findViewById(R.id.gps_speed);
            TextView tripDest = result.findViewById(R.id.trip_dest);
            TextView bus = result.findViewById(R.id.bus_number);
            TextView avgAdjTime = result.findViewById(R.id.avg_adj_time);

            bus.setText("Bus Number: " + getItem(position).getBusNumber());
            startTime.setText("Start time: " + getItem(position).getTripStartTime());
            adjustedTime.setText("Adjusted Time: " + getItem(position).getAdjustedScheduledTime());
            longitude.setText("Longitude: " + getItem(position).getLongitude());
            latitude.setText("Latitude: " + getItem(position).getLatitude());
            gpsSpeed.setText("GPS speed: " + getItem(position).getGpsSpeed());
            tripDest.setText("Trip Destination: " + getItem(position).getTripDestination());

            avgAdjTime.setText("Avg Adjusted Time: " + statResults.get(position));


            return result;
        }

        /**
         * @param position
         * @return
         */
        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    /**
     *
     */
    class TimeDatabaseHelper extends SQLiteOpenHelper {

        /**
         *
         */
        private static final String ACTIVITY_NAME = "TimeDatabaseHelper";
        /**
         *
         */
        private static final String DATABASE_NAME = "AdjustedTime";
        /**
         *
         */
        private static final int VERSION_NUM = 1;
        /**
         *
         */
        public static final String TABLE_NAME = "BUSTIMESTAT";
        /**
         *
         */
        public static final String COL_ADJUSTED_TIME = "KEY_ADJUSTED_TIME";
        /**
         *
         */
        public static final String COL_BUS_NUM = "KEY_BUS_NUM";
        /**
         *
         */
        public static final String COL_ID = "KEY_ID";

        /**
         * @param ctx
         */

        public TimeDatabaseHelper(Context ctx) {

            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        /**
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME +
                    " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_BUS_NUM + " int, " + COL_ADJUSTED_TIME + " double);");
            Log.i(ACTIVITY_NAME, "Calling onCreate");
        }

        /**
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //delete current table
            onCreate(db);
            Log.i(ACTIVITY_NAME, "Calling onCreate, oldVersion = " + oldVersion + " newVersion = " + newVersion);
        }
    }


}

