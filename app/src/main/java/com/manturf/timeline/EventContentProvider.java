package com.manturf.timeline;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by RyoSakaguchi on 15/02/11.
 */
public class EventContentProvider extends ContentProvider {

    private static final String AUTHORITY =
            "com.manturf.timeline.eventcontentprovider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + EventsContract.Events.TABLE_NAME);
    private EventsDbHelper eventsDbHelper;


    // UriMatcher
    /*
    処理 -> URI
    select -> table EVENTS
    insert -> table
    update -> row EVENT_ITEM
    delete -> row
     */

    private static final int EVENTS = 1;
    private static final int EVENT_ITEM = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, EventsContract.Events.TABLE_NAME, EVENTS);
        uriMatcher.addURI(AUTHORITY, EventsContract.Events.TABLE_NAME + "/#", EVENT_ITEM);
    }


    @Override
    public boolean onCreate() {
        eventsDbHelper = new EventsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) != EVENTS) {
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }
        SQLiteDatabase db = eventsDbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                EventsContract.Events.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != EVENTS) {
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }
        SQLiteDatabase db = eventsDbHelper.getWritableDatabase();
        long newId = db.insert(
                EventsContract.Events.TABLE_NAME,
                null,
                values

        );
        Uri newUri = ContentUris.withAppendedId(EventContentProvider.CONTENT_URI, newId);
        getContext().getContentResolver().notifyChange(uri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) != EVENT_ITEM) {
            throw new IllegalArgumentException("Unknown URI:" + uri);
        }
        SQLiteDatabase db = eventsDbHelper.getWritableDatabase();
        int count = db.delete(
                EventsContract.Events.TABLE_NAME,
                selection,
                selectionArgs
        );
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
