package com.eduardo.dm_t2_2.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sql extends SQLiteOpenHelper {

    private static final String database = "viajes";
    private static final int VERSION = 1;
    private static final String  tViajes = "CREATE TABLE VIAJES (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "NOMBRE TEXT NOT NULL," +
            "DESTINO TEXT NOT NULL," +
            "STATUS TEXT NOT NULL," +
            "FECHA TEXT NOT NULL," +
            "DISPONIBLES TEXT NOT NULL," +
            "DETALLES TEXT NOT NULL," +
            "IMAGEN TEXT NOT NULL);";

    public sql(Context context){
        super(context, database, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tViajes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS VIAJES");
            db.execSQL(tViajes);
        }
    }
}
