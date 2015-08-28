package com.example.user.comicsheroes;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by User on 27.08.2015.
 */

public class DatabaseHeroes extends SQLiteOpenHelper implements BaseColumns{

    private static final String DATABASE_NAME = "databaseheroes.db";
    public static final String DATABASE_TABLE = "heroes";
    private static final int DATABASE_VERSION = 1;
    public static final String HERO_NAME_COLUMN = "hero_name";
    public static final String URL_COLUMN = "url";
    public static final String DESCRIPTION_COLUMN = "description";
    //  private static final int ID_COLUMN = 0;
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + HERO_NAME_COLUMN
            + " text, " + URL_COLUMN + " text, " + DESCRIPTION_COLUMN
            + " text);";

    DatabaseHeroes(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DatabaseHeroes(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }
    public DatabaseHeroes(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}
