package com.example.bennettmitchell_final;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// create a sqlite helper class to interact with the database
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gaming.db";

    public DBHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DROP_STATUS);
        db.execSQL(SQL_DROP_PLATFORM);
        db.execSQL(SQL_DROP_GAMES);
        db.execSQL(SQL_CREATE_STATUS);
        db.execSQL(SQL_CREATE_PLATFORM);
        db.execSQL(SQL_CREATE_GAMES);
        Log.i("database", "Database Created");
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
                    Database.PlatformTable.CN_NAME + "TEXT NOT NULL," +
                    Database.PlatformTable.CN_ICON + "BLOB) ;";
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