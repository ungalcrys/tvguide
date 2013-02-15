package ro.ungalcrys.portro.db;

import java.util.ArrayList;

import ro.ungalcrys.portro.Channel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StationsOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "tv_channels";

    public static final String DB_NAME = "portro.db";

    // columns
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID
            + " int, " + COL_NAME + " TEXT);";

    // private boolean wasExisting;

    public StationsOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // wasExisting = false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        // wasExisting = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // public boolean wasExisting() {
    // return wasExisting;
    // }

    public void insert(ArrayList<Channel> channels) {
        SQLiteDatabase db = getWritableDatabase();
        for (Channel channel : channels) {
            ContentValues values = new ContentValues();
            values.put(COL_ID, channel.getId());
            values.put(COL_NAME, channel.getName());
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }

    public Channel[] select() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { COL_ID, COL_NAME }, null, null, null,
                null, null);
        cursor.moveToFirst();
        Channel[] channels = new Channel[cursor.getCount()];
        int i = 0;
        while (!cursor.isAfterLast()) {
            channels[i++] = new Channel(cursor.getInt(0), cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();
        return channels;
    }
}
