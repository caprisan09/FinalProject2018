package com.example.hunar_parneet.finalproject2018;


import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class ListNewsCBC {


    private static ListNewsCBC sListNewsCBC;
    private ArrayList<NewStory> mNewStories;


    public static ListNewsCBC get(Context context) {
        if (sListNewsCBC == null) {
            sListNewsCBC = new ListNewsCBC(context);
        }
        return sListNewsCBC;
    }


    private ListNewsCBC(Context context){
        this.mNewStories = new ArrayList<NewStory>();

    }

    public void clearArticles() {
        this.mNewStories = new ArrayList<NewStory>();
    }

    public NewStory getArticle (UUID id) {
        for (NewStory newStory : mNewStories) {
            if (newStory.getId().equals(id)) {
                return newStory;
            }
        }
        return null;
    }

    public ArrayList<NewStory> getArticles(){
        return mNewStories;
    }

    public void setArticles(ArrayList<NewStory> newNews){
        this.mNewStories = newNews;
    }
}