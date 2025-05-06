package com.example.bennettmitchell_final.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bennettmitchell_final.R;
import com.example.bennettmitchell_final.Status;

// create a sqlite helper class to interact with the database
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gaming.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DROP_GAMES);
        Log.i("Database","Dropped Games");
        db.execSQL(SQL_DROP_STATUS);
        Log.i("Database","Dropped Status");
        db.execSQL(SQL_DROP_PLATFORM);
        Log.i("Database","Dropped Platform");
        db.execSQL(SQL_CREATE_STATUS);
        Log.i("Database","Created Status");
        db.execSQL(SQL_CREATE_PLATFORM);
        Log.i("Database","Created Platform");
        db.execSQL(SQL_CREATE_GAMES);
        Log.i("Database","Created Games");
        Log.i("database", "Database Created");
    }

    private void addDefaults(){
        Status s = new Status("Not Played");
        // no icon
        DBManager.addGameID(Database.Tables.STATUSES, s);
        s = new Status("In Progress");
        s.setIcon(ImageHandler.getResource(R.drawable.played));
        DBManager.addGameID(Database.Tables.STATUSES, s);
        s = new Status("Beaten");
        s.setIcon(ImageHandler.getResource(R.drawable.beaten));
        DBManager.addGameID(Database.Tables.STATUSES, s);
        s = new Status("100% Complete");
        s.setIcon(ImageHandler.getResource(R.drawable.complete));
        DBManager.addGameID(Database.Tables.STATUSES, s);

        Status p = new Status("Nintendo Switch");
        p.setIcon(ImageHandler.getResource(R.drawable.ninswitch));
        DBManager.addGameID(Database.Tables.PLATFORMS, p);

        p = new Status("Steam");
        p.setIcon(ImageHandler.getResource(R.drawable.steam));
        DBManager.addGameID(Database.Tables.PLATFORMS, p);

        p = new Status("Playstation 5");
        p.setIcon(ImageHandler.getResource(R.drawable.ps5));
        DBManager.addGameID(Database.Tables.PLATFORMS, p);
        p = new Status("Xbox");
        p.setIcon(ImageHandler.getResource(R.drawable.xbox));
        DBManager.addGameID(Database.Tables.PLATFORMS, p);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("plink", "Database upgrade?");
    }


    // write sql code
    private static final String SQL_CREATE_STATUS =
            // create Status table
            "CREATE TABLE " + Database.StatusTable.TABLE_NAME + " (" +
                    Database.StatusTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Database.StatusTable.CN_NAME + " TEXT NOT NULL," +
                    Database.StatusTable.CN_ICON + " BLOB); ";
                    // create platform table
    private static final String SQL_CREATE_PLATFORM =
                    "CREATE TABLE " + Database.PlatformTable.TABLE_NAME + " (" +
                    Database.PlatformTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Database.StatusTable.CN_NAME + " TEXT NOT NULL," +
                    Database.StatusTable.CN_ICON + " BLOB) ;";
    private static final String SQL_CREATE_GAMES =
                    // create actual games table
                    "CREATE TABLE " + Database.GamesTable.TABLE_NAME + " (" +
                    Database.GamesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Database.GamesTable.CN_TITLE + " TEXT," +
                    Database.GamesTable.CN_STATUSID + " INT," +
                    Database.GamesTable.CN_PLATFORMID + " INT, " +
                    Database.GamesTable.CN_DESC + " TEXT," +
                    Database.GamesTable.CN_BOXART + " BLOB," +
                    Database.GamesTable.CN_DEV + " TEXT," +
                    Database.GamesTable.CN_PUB + " TEXT," +
                    Database.GamesTable.CN_RELEASE + " TEXT," +
                    Database.GamesTable.CN_USERNOTES + " TEXT," +
                    "FOREIGN KEY (" + Database.GamesTable.CN_STATUSID + ") REFERENCES " + Database.StatusTable.TABLE_NAME + " (" + Database.StatusTable._ID + "), " +
                    "FOREIGN KEY (" + Database.GamesTable.CN_PLATFORMID + ") REFERENCES " + Database.PlatformTable.TABLE_NAME + " (" + Database.PlatformTable._ID + ") " +
                    ");";
    private static final String SQL_DROP_STATUS = "DROP TABLE IF EXISTS " + Database.StatusTable.TABLE_NAME + "; ";
    private static final String SQL_DROP_PLATFORM = "DROP TABLE IF EXISTS " + Database.PlatformTable.TABLE_NAME + "; ";
    private static final String SQL_DROP_GAMES = "DROP TABLE IF EXISTS " + Database.GamesTable.TABLE_NAME + "; ";
}