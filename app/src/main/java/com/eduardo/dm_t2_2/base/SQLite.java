package com.eduardo.dm_t2_2.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.util.Log;

import java.util.ArrayList;

public class SQLite {

    private sql sql;
    private SQLiteDatabase database;

    public SQLite(Context context){
        sql = new sql(context);
    }

    public void abrirBase(){

        Log.i("sqlite", "Se abre conexion con la BD" + sql.getDatabaseName());
        database = sql.getWritableDatabase();

    }

    public void cerrarBase(){
        Log.i("sqlite", "Se cierra conexion con la BD" + sql.getDatabaseName());
        sql.close();
    }

    public boolean addViaje(int ID, String destino, String status, String nombreV, String fecha, String disponible, String detalles, String imagen){
        ContentValues cv = new ContentValues();
        cv.put("ID", ID);
        cv.put("NOMBRE", nombreV);
        cv.put("DESTINO", destino);
        cv.put("STATUS", status);
        cv.put("FECHA", fecha);
        cv.put("DISPONIBLES", disponible);
        cv.put("DETALLES", detalles);
        cv.put("IMAGEN", imagen);

        return (database.insert("VIAJES", null, cv) != -1) ? true : false;
    }

    public Cursor getRegistros(){
        return database.rawQuery("SELECT * FROM VIAJES WHERE STATUS = 'Activo'", null);
    }
    public Cursor getRegistrosInactivos(){
        return database.rawQuery("SELECT * FROM VIAJES WHERE STATUS = 'Inactivo'", null);
    }

    public ArrayList<String> getViajes(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        String item = "";

        if(cursor.moveToFirst()){
            do{
                item += "ID: [" + cursor.getString(0) + "]\r\n";
                item += "NOMBRE: [" + cursor.getString(1) + "]\r\n";
                item += "DESTINO: [" + cursor.getString(2) + "]\r\n";
                item += "STATUS: [" + cursor.getString(3) + "]\r\n";
                item += "FECHA: [" + cursor.getString(4) + "]\r\n";
                item += "DISPONIBLES: [" + cursor.getString(5) + "]\r\n";
                item += "DETALLES: [" + cursor.getString(6) + "]\r\n";
                ListData.add(item);
                item = "";
            }while(cursor.moveToNext());
        }

        return  ListData;
    }

    public ArrayList<String> getImagenes(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        String item = "";

        if(cursor.moveToFirst()){
            do{
                ListData.add(cursor.getString(7));
                item = "";
            }while(cursor.moveToNext());
        }

        return  ListData;
    }
    public ArrayList<String> getIds(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        String item = "";

        if(cursor.moveToFirst()){
            do{
                ListData.add(cursor.getString(0));
                item = "";
            }while(cursor.moveToNext());
        }

        return  ListData;
    }

    public ArrayList<String> getID(Cursor cursor){
        ArrayList<String> ListData = new ArrayList<>();
        String item = "";

        if(cursor.moveToFirst()){
            do{
                item += "ID: [" + cursor.getString(0) + "]\r\n";
                ListData.add(item);
                item = "";
            }while(cursor.moveToNext());
        }

        return  ListData;
    }

    public String updateRegistroViaje(int ID, String nombre, String destino, String status, String fecha, String disponibles, String detalles, String imagen){
        ContentValues cv = new ContentValues();
        cv.put("ID", ID);
        cv.put("NOMBRE", nombre);
        cv.put("DESTINO", destino);
        cv.put("STATUS", status);
        cv.put("FECHA", fecha);
        cv.put("DISPONIBLES", disponibles);
        cv.put("DETALLES", detalles);
        cv.put("IMAGEN", imagen);

        int valor = database.update("VIAJES", cv, "ID = " + ID, null);

        if(valor == 1){
            return "VIAJE actualizado";
        }else{
            return "Error en la actualización";
        }
    }

    public Cursor getValor(int id){
        return database.rawQuery("SELECT * FROM VIAJES WHERE ID = " + id, null);
    }
    public String updateStatus(int ID, String status){
        ContentValues cv = new ContentValues();
        cv.put("ID", ID);
        cv.put("STATUS", status);
        int valor = database.update("VIAJES", cv, "ID = " + ID, null);

        if(valor == 1){
            return "STATUS actualizado";
        }else{
            return "Error en la actualización";
        }
    }

    public int eliminar(Editable id){
        return database.delete("VIAJES", "ID = "+id, null);
    }

}
