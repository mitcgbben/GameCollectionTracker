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

public class Game implements Parcelable {

    private String title;
    private String releaseDate;
    private byte[] boxArt;
//    private Bitmap boxArtDisplay;
    private String developer;
    private String publisher;
    private String description;
    private String userNotes;
    private int gameStatus;
    private int platformID;
    private int gameID;

    public Game(String title, String releaseDate, byte[] boxArt, String developer,
                String publisher, String description, String userNotes,
                int gameStatus, int platformID, int gameID){
        this.setTitle(title);
        this.setReleaseDate(releaseDate);
        this.setBoxArt(boxArt);
        this.setDeveloper(developer);
        this.setPublisher(publisher);
        this.setDescription(description);
        this.setUserNotes(userNotes);
        this.setGameStatus(gameStatus);
        this.setPlatformID(platformID);
        this.setGameID(gameID);
    }
    public Game(String title){
        this(title, "01/01/2000", null, "NOINFO", "NOINFO", "NOINFO", "", 0, 0, 0);
    }


    protected Game(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        boxArt = in.createByteArray();
        developer = in.readString();
        publisher = in.readString();
        description = in.readString();
        userNotes = in.readString();
        gameStatus = in.readInt();
        platformID = in.readInt();
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
        dest.writeInt(gameStatus);
        dest.writeInt(platformID);
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
        String msg = "Title: " + title + "( " + getYear() + ") from: " + publisher;
        return msg;
    }


    // load image from url
    // TODO: This currently does not work and requires running in background
    // NetworkOnMainThreadException

    // allow to set image to bitmap
    public void setBoxArt(Bitmap bitmap){
        this.setBoxArt(convertBitmap(bitmap));
    }


    // Converts from bitmap to byte array
    private byte[] convertBitmap(Bitmap img){
        if (img != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
        else return null;
    }
    // Converts from byte array to bitmap
    private Bitmap convertBitmap(byte[] img){
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            return bitmap;
        }
        else return null;
    }

    public Bitmap getBoxArtDisplay(){
        return convertBitmap(getBoxArt());
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
    public int getGameStatus() {return gameStatus;}
    public void setGameStatus(int gameStatus) {this.gameStatus = gameStatus;}
    public int getPlatformID() {return platformID;}
    public void setPlatformID(int platformID) {this.platformID = platformID;}
    public int getGameID(){return gameID;}
    public void setGameID(int gameID){this.gameID = gameID;}
//    public Drawable getBoxArtDisplay(){return boxArtDisplay;}
//    public void setBoxArtDisplay(Drawable d){this.boxArtDisplay = d;}
}
