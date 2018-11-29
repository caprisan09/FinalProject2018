package com.example.hunar_parneet.finalproject2018;

/**
 * Author: Akanksha Malik
 * ID: 140901360
 * Created 2017-10-24
 */

//import statements
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import java.util.UUID;

public class FragmentNewsDetail extends Fragment {

    //Constants
    private static final String ARG_CARD_ID = "card_id";

    //variables
    private ImageView imageView;
    private TextView newsTextView;
    private Button moreButton;
    private NewStory mNewStory;

    /**
     * param cardId
     * return fragment
     */
    public static FragmentNewsDetail newInstance(UUID cardId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CARD_ID, cardId);
        FragmentNewsDetail fragment = new FragmentNewsDetail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

        UUID cardId = (UUID) getArguments().getSerializable(ARG_CARD_ID);
        mNewStory = ListNewsCBC.get(getActivity()).getArticle(cardId);
    }


    @Override
    public void onPause(){
        super.onPause();
        //ListNewsCBC.get(getActivity()).updateCard(mNewStory);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_single_news, container, false);

        setView(v);
        showDescription();
        updateImage();
        onClickMore();

        return v;
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

    /**
     * inflates the widgets
     * param view v
     */
    private void setView(View v) {
        newsTextView = (TextView) v.findViewById(R.id.news_text_view);
        imageView = (ImageView) v.findViewById(R.id.image_view);
        moreButton = (Button) v.findViewById(R.id.more_button);

    }

    private void onClickMore(){
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    Intent intent = ActivityNewsWebPage.newIntent(getActivity(), mNewStory.getNewsUrl());
                    startActivity(intent);
                    Log.v("HTTP", "Network available");

                } else {
                    Toast.makeText(getActivity(),"Not available",Toast.LENGTH_SHORT).show();
                    Log.v("HTTP", "Network NOT available");
                }

            }
        });
    }
    public boolean isNetworkAvailable() {
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

    /**
     * Sets the answer in the TextView when the button is pressed
     */
    private void showDescription() {
        String description = mNewStory.getDescription(); //get the paragraph
        newsTextView.setText(description); //set the paragraph
    }

    /**
     * Sets the new image in the ImageView when the user presses "Next Question"
     */
    private void updateImage() {
        String imageName = mNewStory.getImageNews(); //get the image url

        Picasso.with(getContext())
                .load(imageName)
                .error(R.drawable.errorimage)
                .into(imageView);
    }

}

