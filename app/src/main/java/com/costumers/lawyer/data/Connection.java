package com.costumers.lawyer.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stiven on 2/29/2016.
 */
public class Connection extends SQLiteOpenHelper {

    private  static final String DB_NAME="ClientsLawyers.sqlite";
    private static final  int DB_CHEMA_VERSION=3;

    public Connection(Context context) {
        super(context, DB_NAME, null, DB_CHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseManager.CREATE_TABLE_PERSONS);
        db.execSQL(DataBaseManager.CREATE_TABLE_FILTER);
        long i=0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseManager.TABLE_NAME_PERSONS);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseManager.TABLE_NAME_FILTER);
        onCreate(db);
    }



}
