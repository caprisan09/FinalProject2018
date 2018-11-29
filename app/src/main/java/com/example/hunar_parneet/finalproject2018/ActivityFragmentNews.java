package com.example.hunar_parneet.finalproject2018;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivityFragmentNews extends Fragment {

    private static final String TAG = "ActivityFragmentNews";
    private static final String KEY_NEWS = "news";

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private newsReader reader;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String cbcNewsURL ="https://www.cbc.ca/cmlink/rss-world";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (internetServiceAvailable()) {
            Log.v("HTTP", "INternet is not available");

            if (savedInstanceState != null){
                ArrayList<NewStory> newNews = savedInstanceState.getParcelableArrayList(KEY_NEWS);
                ListNewsCBC.get(getActivity()).setArticles(newNews);
            } else {
                new showWebPage().execute();
            }

        } else {
            Toast.makeText(getActivity(), "Internet is not available verify your conection", Toast.LENGTH_SHORT).show();
            Log.v("HTTP", "Internet is not available");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.activity_fragment_news,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reader = new newsReader();


        //networkAvailable

        //
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }

            void refreshItems(){
                if(internetServiceAvailable()) {
                    new showWebPage().execute();// Load items
                }
                // Load complete
                onItemsLoadComplete();
            }
            void onItemsLoadComplete(){
                updateUI();
                // Update the adapter and notify data set changed
                // Stop refresh animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        updateUI();
        return view;
    }

    @SuppressLint("LongLogTag")
    public boolean internetServiceAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        Log.d("internetServiceAvailable", "network not available");
        return false;
    } // isNetwork

    private class showWebPage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            ListNewsCBC.get(getActivity()).clearArticles();
            try {
                downloadUrl(cbcNewsURL);
                return "Web page retrieved";
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i("onPostExecute", result);
            updateUI();
        }
    } // showWebPage

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private void downloadUrl(String cbcURLNews) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(cbcURLNews);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.i("HTTP: downloadUrl: ", "Conecting...: " + response);
            is = conn.getInputStream();

            reader.parseXML(getActivity(),is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    } //dowloadURL

    private class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView newsTextSummary;
        private ImageView newsImage;
        private NewStory newStory;

        //private Resources res;

        public CardHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_single_news, parent, false));
            itemView.setOnClickListener(this);

            newsTextSummary = (TextView) itemView.findViewById(R.id.news_text_view);
            newsImage = (ImageView) itemView.findViewById(R.id.image_view);
        }

        @Override
        public void onClick(View view){
            Intent intent = ArticleDetailActivity.newIntent(getActivity(), newStory.getId());
            startActivity(intent);
        }

        public void bind(NewStory newStory){
            this.newStory = newStory;
            updateImage();
            updateHeadline();
        }


        private void updateHeadline() {
            String news = newStory.getHeadline(); //get the headline
            newsTextSummary.setText(news); //set the headline
        }


        private void updateImage() {

            String imageName = newStory.getImageNews(); //get the image url

            Picasso.with(getContext())
                    .load(imageName)
                    .error(R.drawable.errorimage)
                    .into(newsImage);
        }
    }

    private class CardAdapter extends RecyclerView.Adapter<CardHolder>{
        private List<NewStory> mNewStories;

        public CardAdapter(List<NewStory> newStories){
            mNewStories = newStories;
        }

        public void setArticles(List<NewStory> newStories){
            mNewStories = newStories;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new CardHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position){
            NewStory newStory = mNewStories.get(position);
            holder.bind(newStory);
        }

        @Override
        public int getItemCount(){
            return mNewStories.size();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ListNewsCBC listNewsCBC = ListNewsCBC.get(getActivity());
        List<NewStory> newStories = listNewsCBC.getArticles();

        if (cardAdapter == null) {
            cardAdapter = new CardAdapter(newStories);
            recyclerView.setAdapter(cardAdapter);
        } else {
            cardAdapter.setArticles(newStories);
        }
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putParcelableArrayList(KEY_NEWS,ListNewsCBC.get(getActivity()).getArticles());
    }
}
