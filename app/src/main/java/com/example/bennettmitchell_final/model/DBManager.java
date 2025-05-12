package com.example.bennettmitchell_final.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bennettmitchell_final.Game;
import com.example.bennettmitchell_final.GameID;
import com.example.bennettmitchell_final.Platform;
import com.example.bennettmitchell_final.R;
import com.example.bennettmitchell_final.Status;

import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private Context context;
    private static DBHelper dbHelper;
    private static final String NOINFO = "INFORMATION NOT PROVIDED";

    private static SQLiteDatabase writeableDB;
    private static SQLiteDatabase readableDB;

    // define these important aspects to interact with the database
    /*
    public DBManager(Context context){
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }
    */
    public static void initDB(Context context){
        dbHelper = new DBHelper(context);
        writeableDB = dbHelper.getWritableDatabase();
        readableDB = dbHelper.getReadableDatabase();
        addDefaults();
    }
    public static void setWriteableDB(SQLiteDatabase w){writeableDB = w;}
    public static void setReadableDB(SQLiteDatabase r){readableDB = r;}
    //  its as shrimple as that
    public static long insertGame(String title, String releaseDate, byte[] boxArt, String developer,
                           String publisher, String description, String userNotes,
                           int gameStatus, int platformID){
        // database in write mode
//       SQLiteDatabase db = dbHelper.getWritableDatabase();



       // map of values to insert
        ContentValues values = new ContentValues();
        values.put(Database.GamesTable.CN_TITLE, title);
        values.put(Database.GamesTable.CN_RELEASE, releaseDate);
        values.put(Database.GamesTable.CN_BOXART, boxArt);
        values.put(Database.GamesTable.CN_DEV, developer);
        values.put(Database.GamesTable.CN_PUB, publisher);
        values.put(Database.GamesTable.CN_DESC, description);
        values.put(Database.GamesTable.CN_USERNOTES, userNotes);
        values.put(Database.GamesTable.CN_STATUSID, gameStatus);
        values.put(Database.GamesTable.CN_PLATFORMID, platformID);

        // inserts the new row and returns the new row id
        long newRow = writeableDB.insert(Database.GamesTable.TABLE_NAME, null, values); // first time using a long lmao
        return newRow;
    }

    // just insert the game name for testing
    public static long insertGame(String title){
        return insertGame(title, "", null, NOINFO, NOINFO, NOINFO, NOINFO, 0, 0);
    } // cat thumbs up emoji

    // insert a game object //
    public static long insertGame(Game g){
        byte[] boxArt = g.getBoxArt();
        return insertGame(g.getTitle(), g.getReleaseDate(), boxArt, g.getDeveloper(), g.getPublisher(), g.getDescription(), g.getUserNotes(), g.getGameStatus().getID(), g.getPlatform().getID());
    }



    public static long updateGame(Game g){

        ContentValues values = new ContentValues();
        values.put(Database.GamesTable.CN_TITLE, g.getTitle());
        values.put(Database.GamesTable.CN_RELEASE, g.getReleaseDate());
        values.put(Database.GamesTable.CN_DEV, g.getDeveloper());
        values.put(Database.GamesTable.CN_PUB, g.getPublisher());
        values.put(Database.GamesTable.CN_DESC, g.getDescription());
        values.put(Database.GamesTable.CN_STATUSID, g.getGameStatus().getID());
        values.put(Database.GamesTable.CN_PLATFORMID, g.getPlatform().getID());
        values.put(Database.GamesTable.CN_BOXART, g.getBoxArt());
        values.put(Database.GamesTable.CN_USERNOTES, g.getUserNotes());

        String selection = Database.GamesTable._ID + " LIKE ?";
        String[] args = {Integer.toString(g.getGameID())};

        return writeableDB.update(Database.GamesTable.TABLE_NAME, values, selection, args);
    }
    public static long updateGameShort(){
        return 1;
    }
    // get one specific game
    public static Game getGame(int gameID){
//        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] cols = null;
        String selection = Database.GamesTable._ID + " LIKE ?";
        String[] args = {Integer.toString(gameID)};

        Cursor cursor = readableDB.query(Database.GamesTable.TABLE_NAME, cols, selection, args, null, null, null);

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
            int statusID = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_STATUSID));
            int platformID = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_PLATFORMID));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable._ID));
//            Log.d("Database", Integer.toString(id));

            Status status = (Status) getGameIdentifier(Database.Tables.STATUSES, statusID);
            Status platform = (Status) getGameIdentifier(Database.Tables.PLATFORMS, platformID);

            game = new Game(title, releaseDate, boxArt, developer, publisher, description, notes, status, platform, id);
        }
        else{
            game = null;
        }
        cursor.close();
        return game;
    }


    public static List<Game> getGames(){
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = null;
        //String[]
        String sortOrder = Database.GamesTable.CN_TITLE + " ASC";
        // SELECT * FROM Games; essentially
        Cursor cursor = readableDB.query(Database.GamesTable.TABLE_NAME, null, null, null, null, null, sortOrder);
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
            int statusID = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_STATUSID));
            int platformID = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable.CN_PLATFORMID));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.GamesTable._ID));
            Log.d("Database", Integer.toString(id));

            if (statusID < 0){
                statusID = 0;
            }
            if (platformID < 0){
                platformID = 0;
            }
            Status status = (Status) getGameIdentifier(Database.Tables.STATUSES, statusID);
            Status platform = (Status) getGameIdentifier(Database.Tables.PLATFORMS, platformID);

            Game game = new Game(title, releaseDate, boxArt, developer, publisher, description, notes, status, platform, id);
            games.add(game);
            // gaming gaming gaming gaming gaming :3
        }

        cursor.close();
        return games;
                // query is: table to query, columns to return (null is *), columns for where clause, values for where clause, dont group rows, dont filter by row groups, sort order
    }

    public static int deleteGame(int gameID){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = Database.GamesTable._ID + " LIKE ?";
        String[] selectArg = {Integer.toString(gameID)};
        int deletedRows = writeableDB.delete(Database.GamesTable.TABLE_NAME, selection, selectArg);
        Log.i("Database", gameID + " Deleted items: " + deletedRows);
//        db.close();
        return deletedRows;
    }

    /// Statuses and Platforms ///
    public static List<GameID> getGameIDs(Database.Tables dest){
        String tableName = (dest == Database.Tables.PLATFORMS) ? Database.PlatformTable.TABLE_NAME : Database.StatusTable.TABLE_NAME;

        Cursor cursor = readableDB.query(tableName, null, null, null, null, null, null);

        ArrayList<GameID> statuses = new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.StatusTable.CN_NAME));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.StatusTable._ID));
            byte[]  icon = cursor.getBlob(cursor.getColumnIndexOrThrow(Database.StatusTable.CN_ICON));
            Status s = new Status(name, id, icon);
            statuses.add(s); // :3
        }
        cursor.close();
        return statuses;
    }

    public static void addGameID(Database.Tables dest, GameID p){
        // it feels like this line came to me in a manic episode lmao
        String tableName = (dest == Database.Tables.PLATFORMS) ? Database.PlatformTable.TABLE_NAME : Database.StatusTable.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(Database.StatusTable.CN_NAME, p.getName());
        values.put(Database.StatusTable.CN_ICON, p.getIcon());

        long newRow = writeableDB.insert(tableName, null, values);
        p.setID((int) newRow);
    }
    public static void removeGameID(Database.Tables dest, int id){
        String tableName = (dest == Database.Tables.PLATFORMS) ? Database.PlatformTable.TABLE_NAME : Database.StatusTable.TABLE_NAME;
    }

    public static int findGameID(ArrayList<GameID> list, GameID subject){
        int index = -1;
        for (int i = 0; i < list.size(); i++){
            if (list.get(i).getID() == subject.getID()){
                index = i;
                break;
            }
        }
        Log.d("plink",Integer.toString(index));
        return index;
    }
    public static GameID getGameIdentifier(Database.Tables dest, int id){
        // :3
        id = Math.max(id, 0);
        String tableName = (dest == Database.Tables.PLATFORMS) ? Database.PlatformTable.TABLE_NAME : Database.StatusTable.TABLE_NAME;
        String selection = Database.StatusTable._ID + " LIKE ?";
        String[] args = {Integer.toString(id)};

        Cursor cursor = readableDB.query(tableName, null, selection, args, null, null, null);
        cursor.moveToNext();
        Status s;
        try {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Database.StatusTable.CN_NAME)); // its 1 am but the different between Database.StatusTable and Database.PlatformTable is literally just Database.?.TABLE_NAME
            int idID = cursor.getInt(cursor.getColumnIndexOrThrow(Database.StatusTable._ID));                   // i should sleep but the grind never stops!!!!!
            byte[] icon = cursor.getBlob(cursor.getColumnIndexOrThrow(Database.StatusTable.CN_ICON));
            s = new Status(name, id, icon);
        }
        // if there are no game ids in the results
        catch(CursorIndexOutOfBoundsException e){
            s = new Status();
        }

        cursor.close();
        return s;
    }

    // test
    @TestOnly
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

    public static void addDefaults(){
        List<GameID> status = getGameIDs(Database.Tables.STATUSES);
        if (status.isEmpty()) {
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

    }
     public static void reset(){
         SQLiteDatabase db = dbHelper.getReadableDatabase();
         dbHelper.onCreate(db);
         addDefaults();
     }
     public static void close(){
        dbHelper.close();
     }
}
