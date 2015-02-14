package com.manturf.timeline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by RyoSakaguchi on 15/02/11.
 */
public class EventsDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "EventList.db";
    public static final int DB_VERSION = 1;

    public EventsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("DBHelper","でーびーへるぱーのonCreateだよ");
        // create table
        db.execSQL(EventsContract.Events.CREATE_TABLE);
        // init table
        db.execSQL(EventsContract.Events.INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        // delete old table
        db.execSQL(EventsContract.Events.DROP_TABLE);
        // onCreate
        onCreate(db);

    }


}
