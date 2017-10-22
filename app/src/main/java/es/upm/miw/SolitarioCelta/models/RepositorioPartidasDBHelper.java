package es.upm.miw.SolitarioCelta.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static es.upm.miw.SolitarioCelta.models.PartidaContract.tablaPartidas;

/**
 * Created by Raquel on 21/10/17.
 */

public class RepositorioPartidasDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = tablaPartidas.TABLE_NAME + ".db";
    private static final int DB_VERSION = 1;

    public RepositorioPartidasDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE " + tablaPartidas.TABLE_NAME + " (" +
                tablaPartidas.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tablaPartidas.COL_JUGADOR + " TEXT, " +
                tablaPartidas.COL_FECHA + " TEXT, " +
                tablaPartidas.COL_HORA + " TEXT, " +
                tablaPartidas.COL_NUMEROFICHAS + " INTEGER, " +
                tablaPartidas.COL_ESTADO + " TEXT, " +
                tablaPartidas.COL_CRONOMETROTXT + " TEXT, " +
                tablaPartidas.COL_CRONOMETROBASE + " TEXT " + " )";
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteSQL = "DROP TABLE IF EXISTS " + tablaPartidas.TABLE_NAME;
        db.execSQL(deleteSQL);
        onCreate(db);
    }

    public long add(String jugador, String fecha, String hora, int numeroFichas, String estado, String cronometroTxt, String cronometroBase) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(tablaPartidas.COL_JUGADOR, jugador);
        valores.put(tablaPartidas.COL_FECHA, fecha);
        valores.put(tablaPartidas.COL_HORA, hora);
        valores.put(tablaPartidas.COL_NUMEROFICHAS, numeroFichas);
        valores.put(tablaPartidas.COL_ESTADO, estado);
        valores.put(tablaPartidas.COL_CRONOMETROTXT, cronometroTxt);
        valores.put(tablaPartidas.COL_CRONOMETROBASE, cronometroBase);

        return db.insert(tablaPartidas.TABLE_NAME, null, valores);
    }


    public ArrayList<Partida> getAll() {

        String consultaSQL = "SELECT * FROM " + tablaPartidas.TABLE_NAME;
        ArrayList<Partida> listPartidas = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(consultaSQL, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Partida partida = new Partida(
                        cursor.getInt(cursor.getColumnIndex(tablaPartidas.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_JUGADOR)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_FECHA)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_HORA)),
                        cursor.getInt(cursor.getColumnIndex(tablaPartidas.COL_NUMEROFICHAS)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_ESTADO)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_CRONOMETROTXT)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_CRONOMETROBASE))
                );
                listPartidas.add(partida);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return listPartidas;
    }

    public Partida get(int id) {

        String consultaSQL = "SELECT * FROM " + tablaPartidas.TABLE_NAME + " WHERE _ID=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(consultaSQL, null);
        Partida partida = new Partida();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                partida = new Partida(
                        cursor.getInt(cursor.getColumnIndex(tablaPartidas.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_JUGADOR)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_FECHA)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_HORA)),
                        cursor.getInt(cursor.getColumnIndex(tablaPartidas.COL_NUMEROFICHAS)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_ESTADO)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_CRONOMETROTXT)),
                        cursor.getString(cursor.getColumnIndex(tablaPartidas.COL_CRONOMETROBASE))
                );
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return partida;
    }

    public void deleteAll (){
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSQL = "DELETE FROM " + tablaPartidas.TABLE_NAME;
        db.execSQL(deleteSQL);
    }
}
