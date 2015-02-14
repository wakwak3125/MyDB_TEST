package com.manturf.timeline;

import android.provider.BaseColumns;

/**
 * Created by RyoSakaguchi on 15/02/11.
 */
public class EventsContract {

    public EventsContract() {
    }

    public static abstract class Events implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String EVENT_ID = "_id";
        public static final String EVENT_TITLE = "title";
        public static final String EVENT_PLACE = "place";
        public static final String USER_OCCUPATION = "occupation";
        public static final String EVENT_DATE = "date";
        public static final String EVENT_TIME = "time";

        public static final String CREATE_TABLE
                = "create table if not exists " + TABLE_NAME + " (" +
                EVENT_ID +" integer primary key autoincrement," +
                EVENT_TITLE + " text unique," +
                EVENT_PLACE + " text)";
        public static final String INIT_TABLE
                = "insert into " + TABLE_NAME + "(" + EVENT_TITLE + "," +  EVENT_PLACE + ") " +
                        "values ('IT系飲み会', '渋谷'), ('土木系ガチ飲み', '馬橋')";
        public static final String DROP_TABLE
                = "drop table if exists" + TABLE_NAME;
    }
}
