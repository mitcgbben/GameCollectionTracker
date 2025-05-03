package com.example.bennettmitchell_final.model;

import android.content.Context;
import android.provider.BaseColumns;

// oh no
public final class Database {

    // just has information on the database
    private Database(Context context){} // plink

    public enum Tables{
        GAMES,
        PLATFORMS,
        STATUSES
    }
    // define how the tables will look
    public static class GamesTable implements BaseColumns{
        public static final String TABLE_NAME = "Games";
        public static final String CN_TITLE= "gameTitle";
        public static final String CN_PLATFORMID= "platformID";
        public static final String CN_STATUSID = "statusID";
        public static final String CN_DESC = "description";
//        public static final String CN_BOXARTLINK = "boxArtLink";
        public static final String CN_BOXART = "boxArt";
        public static final String CN_DEV = "developer";
        public static final String CN_PUB = "publisher";
        public static final String CN_RELEASE = "releaseDate";
        public static final String CN_USERNOTES = "userNotes";
    }

    public static class StatusTable implements BaseColumns{
        public static final String TABLE_NAME = "Statuses";
        public static final String CN_NAME= "statusName";
        public static final String CN_ICON= "statusIcon";
    }

    public static class PlatformTable implements BaseColumns{
        public static final String TABLE_NAME = "Platforms";}
        /*
        public static final String CN_NAME= "platformName";
        public static final String CN_ICON= "platformIcon";
    }
     */




}


