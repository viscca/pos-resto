package com.pos.restokasir.db_sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class C_DB_Setting extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Db_Setting";
    public static final String TOKEN_TABLE_NAME = "ConfigSTR";
    public static final String TOKEN_COLUMN_ID = "Kunci";
    public static final String TOKEN_COLUMN_NAME = "isi";

    public C_DB_Setting(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TOKEN_TABLE_NAME + " (" +
                TOKEN_COLUMN_ID + " TEXT PRIMARY KEY, " +
                TOKEN_COLUMN_NAME + " TEXT "+ ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TOKEN_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void add(String Key, String Isi) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();
        sqLiteDatabase.delete(TOKEN_TABLE_NAME, TOKEN_COLUMN_ID + "=?",
                new String[]{Key});
        if(!Isi.equals("")) {
            ContentValues values = new ContentValues();
            values.put(TOKEN_COLUMN_ID, Key);
            values.put(TOKEN_COLUMN_NAME, Isi);
            //inserting new row
            sqLiteDatabase.insert(TOKEN_TABLE_NAME, null, values);
        }
        sqLiteDatabase.close();
    }

    public String get_Key(String Key) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TOKEN_TABLE_NAME, new String[] { TOKEN_COLUMN_NAME },
                TOKEN_COLUMN_ID + "=?",
                new String[] { Key },
                null, null, null, null);
        return cursor.moveToFirst() ? cursor.getString(0) : "";
    }

    public void delete() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(TOKEN_TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }

    //get the all notes
    @SuppressLint("Range")
    public String select() {
        // select all query
        String token = "";
        String select_query= "SELECT * FROM " + TOKEN_TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                token = cursor.getString(cursor.getColumnIndex(TOKEN_COLUMN_NAME));
            } while (cursor.moveToNext());
        }
        return token;
    }
}