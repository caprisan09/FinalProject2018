package com.example.hunar_parneet.finalproject2018;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class NewStory implements Parcelable{


    private UUID uuid;
    private String headLine;
    private String ImageNews;
    private String newsURL;
    private String descriptionNews;

    @Override
    public int describeContents() {
        return 0;
    }


    public NewStory(String theHeadline, String theImageUrl, String theNewsUrl, String theDescription) {
        uuid = (UUID.randomUUID());
        headLine = theHeadline;
        ImageNews = theImageUrl;
        newsURL = theNewsUrl;
        descriptionNews = theDescription;

    }


    private NewStory(Parcel in){

        this.uuid = (UUID) in.readSerializable();
        this.headLine = in.readString();
        this.ImageNews = in.readString();
        this.newsURL = in.readString();
        this.descriptionNews = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(uuid);
        dest.writeString(headLine);
        dest.writeString(ImageNews);
        dest.writeString(newsURL);
        dest.writeString(descriptionNews);

    }
    public static final Creator<NewStory> CREATOR = new Creator<NewStory>() {

        @Override
        public NewStory createFromParcel(Parcel source) {
            return new NewStory(source);
        }

        @Override
        public NewStory[] newArray(int size) {
            return new NewStory[size];
        }
    };

    public UUID getId() {
        return uuid;
    }

    public String getHeadline() {
        return headLine;
    }

    public String getImageNews() {
        return ImageNews;
    }

    public String getNewsUrl() {
        return newsURL;
    }

    public String getDescription() {
        return descriptionNews;
    }

    @Override
    public String toString(){
        return headLine;
    }
}
