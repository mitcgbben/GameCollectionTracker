package com.example.bennettmitchell_final;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.bennettmitchell_final.model.ImageHandler;

public class Status implements GameID, Parcelable {
    private int id;
    private String name;
    private byte[] icon;
    public Status(String name){
        this.name = name;
    }

    public Status(String name, int id, byte[] icon){
        this.name = name;
        this.id = id;
        this.icon = icon;
    }
    public Status(){
        this.name = "";
        this.id = -1;
        this.icon = null;
    }

    protected Status(Parcel in) {
        id = in.readInt();
        name = in.readString();
        icon = in.createByteArray();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public int getID(){return this.id;}
    public void setID(int id){this.id = id;}

    public byte[] getIcon(){return icon;}
    public void setIcon(byte[] icon){this.icon = icon;}
    public void setIcon(Bitmap icon){this.setIcon(ImageHandler.convertBitmap(icon));}
    public void setIcon(String url){this.setIcon(ImageHandler.getImageFromWeb(url));}
    public Bitmap getDisplayIcon(){return ImageHandler.convertBitmap(icon);}

    @Override
    public String toString(){
        return this.name + " ID: " + this.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.readByteArray(icon);
    }
}
