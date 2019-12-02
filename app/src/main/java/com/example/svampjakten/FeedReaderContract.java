package com.example.svampjakten;

import android.provider.BaseColumns;

public class FeedReaderContract {

    private FeedReaderContract(){
        //empty
    }

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Darkmode";
        public static final String DARKMODE = "darkMode";
    }

}
