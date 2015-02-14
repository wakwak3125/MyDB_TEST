package com.manturf.timeline;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String TAG = MainActivity.class.getSimpleName();

    private SimpleCursorAdapter cursorAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventsDbHelper eventsDbHelper = new EventsDbHelper(this);
        final SQLiteDatabase db = eventsDbHelper.getWritableDatabase();
        Log.i("CREATE_TABLE", EventsContract.Events.CREATE_TABLE);
        db.execSQL(EventsContract.Events.CREATE_TABLE);

        try {
            db.execSQL(EventsContract.Events.INIT_TABLE);
        } catch (SQLiteConstraintException constraint) {
            Log.i(TAG, "テーブルの初期化処理" + String.valueOf(constraint));
        } finally {
            Toast.makeText(this, "読み込み終了", Toast.LENGTH_SHORT).show();
        }

        /*db.execSQL(EventsContract.Events.INIT_TABLE);*/


        // adapter の設定
        String[] from = {
                EventsContract.Events.EVENT_TITLE,
                EventsContract.Events.EVENT_PLACE,
        };

        int[] to = {
                android.R.id.text1,
                android.R.id.text2
        };
        cursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                null,
                from,
                to,
                0
        );
        // ListViewの取得
        listView = (ListView) findViewById(R.id.list1);
        listView.setAdapter(cursorAdapter);

        //追加ボタンの処理
        Button add_button = (Button) findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //INSERT
                ContentValues values = new ContentValues();
                values.put(EventsContract.Events.EVENT_TITLE, "オカマ系ガチムチのみ@二丁目");
                values.put(EventsContract.Events.EVENT_PLACE, "新宿");
                getContentResolver().insert(EventContentProvider.CONTENT_URI, values);
            }
        });
        //削除ボタンの処理
        Button del_button = (Button) findViewById(R.id.del_button);
        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(
                        EventsContract.Events.TABLE_NAME,
                        EventsContract.Events.EVENT_PLACE + " = ?",
                        new String[]{"新宿"}
                );
                getContentResolver().notifyChange(EventContentProvider.CONTENT_URI, null);
            }
        });
        // (1)Loaderの初期化
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // (2)非同期処理を投げる
        String[] projection = {
                EventsContract.Events.EVENT_ID,
                EventsContract.Events.EVENT_TITLE,
                EventsContract.Events.EVENT_PLACE
            /*EventsContract.Events.EVENT_PLACE,*/
        };
        return new CursorLoader(
                this,
                EventContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                null

        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // (3)結果をcursorAdapterに反映する
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

    }
}
