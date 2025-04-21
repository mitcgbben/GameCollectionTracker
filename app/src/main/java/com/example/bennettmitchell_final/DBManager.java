package com.example.bennettmitchell_final;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private Context context;
    private DBHelper dbHelper;
    private static final String NOINFO = "INFORMATION NOT PROVIDED";
    // define these important aspects to interact with the database
    public DBManager(Context context){
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    //  its as shrimple as that
    public long insertGame(String title, String releaseDate, byte[] boxArt, String developer,
                           String publisher, String description, String userNotes,
                           int gameStatus, int platformID){
        // database in write mode
       SQLiteDatabase db = dbHelper.getWritableDatabase();

       // TODO FORMAT DATES
        String releaseDateF = "1/1/2000";

       // map of values to insert
        ContentValues values = new ContentValues();
        values.put(Database.GamesTable.CN_TITLE, title);
        values.put(Database.GamesTable.CN_RELEASE, releaseDateF);
        values.put(Database.GamesTable.CN_BOXART, boxArt);
        values.put(Database.GamesTable.CN_DEV, developer);
        values.put(Database.GamesTable.CN_PUB, publisher);
        values.put(Database.GamesTable.CN_DESC, description);
        values.put(Database.GamesTable.CN_USERNOTES, userNotes);
        values.put(Database.GamesTable.CN_STATUSID, gameStatus);
        values.put(Database.GamesTable.CN_PLATFORMID, platformID);

        // inserts the new row and returns the new row id
        long newRow = db.insert(Database.GamesTable.TABLE_NAME, null, values); // first time using a long lmao
        db.close(); // close to not use too much data
        return newRow;
    }

    // just insert the game name for testing
    public long insertGame(String title){
        return insertGame(title, "", null, NOINFO, NOINFO, NOINFO, NOINFO, 0, 0);
    } // cat thumbs up emoji

    // insert a game object //
    public long insertGame(Game g){
        byte[] boxArt = g.getBoxArt();
        return insertGame(g.getTitle(), g.getReleaseDate(), boxArt, g.getDeveloper(), g.getPublisher(), g.getDescription(), g.getUserNotes(), g.getGameStatus(), g.getPlatformID());
    }



    public long updateGame(Game g){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Database.GamesTable.CN_TITLE, g.getTitle());
        values.put(Database.GamesTable.CN_RELEASE, g.getTitle());
        values.put(Database.GamesTable.CN_DEV, g.getDeveloper());
        values.put(Database.GamesTable.CN_PUB, g.getPublisher());
        values.put(Database.GamesTable.CN_DESC, g.getDescription());
        values.put(Database.GamesTable.CN_STATUSID, g.getGameStatus());
        values.put(Database.GamesTable.CN_PLATFORMID, g.getPlatformID());
        values.put(Database.GamesTable.CN_BOXART, g.getBoxArt());



        String selection = Database.GamesTable._ID + " LIKE ?";
        String[] args = {Integer.toString(g.getGameID())};

        return db.update(Database.GamesTable.TABLE_NAME, values, selection, args);
    }
    // TODO : udpate status or user notes
    public long updateGameShort(){
        return 1;
    }
    // get one specific game
    public Game getGame(int gameID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] cols = null;
        String selection = Database.GamesTable._ID + " LIKE ?";
        String[] args = {Integer.toString(gameID)};

        Cursor cursor = db.query(Database.GamesTable.TABLE_NAME, cols, selection, args, null, null, null);

        Game game;
        // check to see if there is a result
        if (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_TITLE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_RELEASE));
            byte[] boxArt = cursor.getBlob(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_BOXART));
            String developer = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_DEV));
            String publisher = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_PUB));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_DESC));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_USERNOTES));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_STATUSID));
            int platform = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_PLATFORMID));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable._ID));
//            Log.d("Database", Integer.toString(id));


            game = new Game(title, releaseDate, boxArt, developer, publisher, description, notes, status, platform, id);
        }
        else{
            game = null;
        }
        cursor.close();
        return game;
    }


    public List<Game> getGames(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = null;
        //String[]
        // SELECT * FROM Games; essentially
        Cursor cursor = db.query(Database.GamesTable.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Game> games = new ArrayList<>();

        // loop through the
        while (cursor.moveToNext()){
            // thisll be long oh no
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_TITLE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_RELEASE));
            byte[] boxArt = cursor.getBlob(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_BOXART));
            String developer = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_DEV));
            String publisher = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_PUB));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_DESC));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_USERNOTES));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_STATUSID));
            int platform = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_PLATFORMID));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable._ID));
            Log.d("Database", Integer.toString(id));

            Game game = new Game(title, releaseDate, boxArt, developer, publisher, description, notes, status, platform, id);
            games.add(game);
            // gaming gaming gaming gaming gaming :3
        }

        cursor.close();
        return games;
                // query is: table to query, columns to return (null is *), columns for where clause, values for where clause, dont group rows, dont filter by row groups, sort order
    }

    public int deleteGame(int gameID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = Database.GamesTable._ID + " LIKE ?";
        String[] selectArg = {Integer.toString(gameID)};
        int deletedRows = db.delete(Database.GamesTable.TABLE_NAME, selection, selectArg);
        Log.i("Database", gameID + " Deleted items: " + deletedRows);
        db.close();
        return deletedRows;
    }

    // test
     private void listTables(){
         SQLiteDatabase db = dbHelper.getReadableDatabase();
         String selection = "type = ?";
         String[] col = {"name"};
         String[] select = {"table"};

         Cursor cursor = db.query("sqlite_master", col, selection, select, null, null, null);

         while(cursor.moveToNext()){
             String a = cursor.getString(cursor.getColumnIndexOrThrow("name"));
             Log.i("Database","Table: " + a);
         }
         cursor.close();
     }

     public void reset(){
         SQLiteDatabase db = dbHelper.getReadableDatabase();
         dbHelper.onCreate(db);
     }
     public void close(){
        dbHelper.close();
     }
}
