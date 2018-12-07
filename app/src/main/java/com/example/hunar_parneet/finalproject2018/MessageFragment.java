package com.example.hunar_parneet.finalproject2018;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MessageFragment extends Fragment {
    /**
     *
     */
    private final String ACTIVITY_NAME = "MessageFragment";

    /**
     *
     */
    private Button backBtn;
    /**
     *
     */
    private ListView busList;

    /**
     *
     */
    public MessageFragment() {
    }

    /**
     *
     */
    public OcTranspoActivity octa;
    /**
     *
     */
    public boolean iAmTablet;
    /**
     *
     */
    public EmptyFrameLayoutActivity parent;
    /**
     *
     */
    public BusInfoQuery busInfoQuery;
    /**
     *
     */
    public List<RouteDetail> myBusList;
    /**
     *
     */
    public MyListItemsAdapter listAdapter;
    /**
     *
     */
    public int stopNumber;
    /**
     *
     */
    private int busNumber;


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        busInfoQuery = new BusInfoQuery();

        myBusList = new ArrayList<>();

        if (iAmTablet) {
            listAdapter = new MyListItemsAdapter(octa);

        } else {
            listAdapter = new MyListItemsAdapter(getContext());

        }
        final Bundle infoToPass = getArguments(); //returns the arguments set before
        stopNumber = infoToPass.getInt("stopNumber");
        Log.i(ACTIVITY_NAME, "Stop Number: " + stopNumber);
        final View screen = inflater.inflate(R.layout.activity_list_items, container, false);
        backBtn = screen.findViewById(R.id.back_Btn);
        busList = screen.findViewById(R.id.bus_list);

        busInfoQuery.execute();

        busList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        Log.i(ACTIVITY_NAME, "I am in the message detail activity");

        backBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "Delete button on fragment is clicked");


                if (iAmTablet) {
                    octa.refreshView();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(MessageFragment.this).commit();


                } else {
                    Log.i(ACTIVITY_NAME, "Back button clicked");
                    Intent resultIntent = new Intent();
                    //resultIntent.putExtra("ID", idPassed);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                }
            }
        });
        busList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                busNumber = Integer.valueOf(listAdapter.getItem(position).getBusNumber());

                Bundle info = new Bundle();
                Log.i(ACTIVITY_NAME, "List item was clicked");
                info.putInt("stopNumber", stopNumber);
                info.putInt("busNumber", busNumber);
                info.putInt("ListItem", 1);
                Log.i(ACTIVITY_NAME, "The stop number: " + stopNumber);
                Log.i(ACTIVITY_NAME, "The bus number: " + busNumber);

                Intent nextPage = new Intent();
                nextPage.putExtras(info); //send info
                getActivity().setResult(Activity.RESULT_OK, nextPage);
                getActivity().finish();
            }


        });
        return screen;

    }

    /**
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (iAmTablet)
            octa = (OcTranspoActivity) context;
    }

    /**
     *
     */
    private class MyListItemsAdapter extends ArrayAdapter<RouteDetail> {

        /**
         * @param ctx
         */
        public MyListItemsAdapter(Context ctx) {
            super(ctx, 0);

        }

        /**
         * @return
         */
        @Override
        public int getCount() {
            return myBusList.size();
        }

        /**
         * @param position
         * @return
         */
        @Override
        public RouteDetail getItem(int position) {

            return myBusList.get(position);

        }

        /**
         * @param position
         * @param oldView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View oldView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View result = inflater.inflate(R.layout.list_row, null);

            TextView busNum = result.findViewById(R.id.list_text);
            TextView route = result.findViewById(R.id.description);

            busNum.setText("" + getItem(position).getBusNumber());
            route.setText(getItem(position).getRouteHeading());
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
    private class BusInfoQuery extends AsyncTask<String, Integer, String> {


        /**
         *
         */
        private int busNumber;
        /**
         *
         */
        private int count = 0;
        /**
         *
         */
        private String busInfo = "";
        /**
         *
         */
        private String routeInfo = "";
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
                url = new URL("https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=" + stopNumber);
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
                int loopcount = 0;
                while (xpp.next() != XmlPullParser.END_DOCUMENT) {

                    Log.i(ACTIVITY_NAME, "I enter the while loop");
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        Log.i(ACTIVITY_NAME, "I am in first IF condition");
                        String name = xpp.getName();
                        Log.i(ACTIVITY_NAME, "Name: " + name);
                        if (name.equals("RouteNo")) {
                            count++;
                            if (xpp.next() == XmlPullParser.TEXT) {
                                busInfo = xpp.getText();
                                Log.i(ACTIVITY_NAME, "The bus number is: " + busInfo);
                            }
                        }
                        if (name.equals("RouteHeading")) {
                            if (xpp.next() == XmlPullParser.TEXT) {
                                routeInfo = xpp.getText();
                            }
                        }
                    }
                    if (xpp.getEventType() == XmlPullParser.END_TAG) {
                        String endtag = xpp.getName();

                        if (endtag.equals("Route")) {
                            loopcount++;
                            myBusList.add(new RouteDetail(Integer.parseInt(busInfo), routeInfo));
                            Log.i(ACTIVITY_NAME, "The records added: " + loopcount);

                        }
                    }
                }
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


        }

        /**
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            Log.i(ACTIVITY_NAME, "I am in onPostExecute");
            listAdapter.notifyDataSetChanged();

        }


    }


}

