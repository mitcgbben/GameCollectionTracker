package com.example.bennettmitchell_final;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.bennettmitchell_final.model.DBManager;
import com.example.bennettmitchell_final.model.Database;
import com.example.bennettmitchell_final.model.ImageHandler;

public class Game implements Parcelable {

    private String title;
    private String releaseDate;
    private byte[] boxArt;
//    private Bitmap boxArtDisplay;
    private String developer;
    private String publisher;
    private String description;
    private String userNotes;
    private Status gameStatus;
//    private int gameStatus;
    private Status platform;
    private int gameID;

    public Game(String title, String releaseDate, byte[] boxArt, String developer,
                String publisher, String description, String userNotes,
                Status gameStatus, Status platformID, int gameID){
        this.setTitle(title);
        this.setReleaseDate(releaseDate);
        this.setBoxArt(boxArt);
        this.setDeveloper(developer);
        this.setPublisher(publisher);
        this.setDescription(description);
        this.setUserNotes(userNotes);
        this.setGameStatus(gameStatus);
        this.setPlatform(platformID);
        this.setGameID(gameID);
    }
    public Game(String title){
        this(title, "01/01/2000", null, "NOINFO", "NOINFO", "NOINFO", "", new Status(), new Status(), 0);
    }


    protected Game(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        boxArt = in.createByteArray();
        developer = in.readString();
        publisher = in.readString();
        description = in.readString();
        userNotes = in.readString();
        gameStatus = (Status) DBManager.getGameIdentifier(Database.Tables.STATUSES, in.readInt());
        platform = (Status) DBManager.getGameIdentifier(Database.Tables.PLATFORMS, in.readInt());
        gameID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeByteArray(boxArt);
        dest.writeString(developer);
        dest.writeString(publisher);
        dest.writeString(description);
        dest.writeString(userNotes);
        dest.writeInt(gameStatus.getID());
        dest.writeInt(platform.getID());
        dest.writeInt(gameID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public String toString(){
        String msg = "Title: " + title + "( " + getYear() + ") from: " + publisher + " Status: " + getGameStatus().getName() + " | Platform: " + getPlatform().getName();
        return msg;
    }


    // load image from url
    // TODO: This currently does not work and requires running in background
    // NetworkOnMainThreadException

    // allow to set image to bitmap
    public void setBoxArt(Bitmap bitmap){
        this.setBoxArt(ImageHandler.convertBitmap(bitmap));
    }




    public Bitmap getBoxArtDisplay(){
        return ImageHandler.convertBitmap(getBoxArt());
    }
    public String getYear(){
        String[] dateArray = releaseDate.split("/");
        try{
            return dateArray[2];
        }
        catch (IndexOutOfBoundsException e){
            return "2000";
        }
    }
    // a a
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getReleaseDate() {return releaseDate;}
    public void setReleaseDate(String releaseDate) {this.releaseDate = releaseDate;}
    public byte[] getBoxArt() {return boxArt;}
    public void setBoxArt(byte[] boxArt) {this.boxArt = boxArt;}
    public String getDeveloper() {return developer;}
    public void setDeveloper(String developer) {this.developer = developer;}
    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getUserNotes() {return userNotes;}
    public void setUserNotes(String userNotes) {this.userNotes = userNotes;}
    public Status getGameStatus() {return gameStatus;}
    public void setGameStatus(Status gameStatus) {this.gameStatus = gameStatus;}
    public Status getPlatform() {return platform;}
    public void setPlatform(Status platformID) {this.platform = platformID;}
    public int getGameID(){return gameID;}
    public void setGameID(int gameID){this.gameID = gameID;}
//    public Drawable getBoxArtDisplay(){return boxArtDisplay;}
//    public void setBoxArtDisplay(Drawable d){this.boxArtDisplay = d;}
}
