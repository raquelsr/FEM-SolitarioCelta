package es.upm.miw.SolitarioCelta.models;

/**
 * Created by Raquel on 19/10/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static es.upm.miw.SolitarioCelta.models.ResultadoContract.tablaResultados;

public class RepositorioResultadoDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = tablaResultados.TABLE_NAME + ".db";
    private static final int DB_VERSION = 1;

    public RepositorioResultadoDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSQL = "CREATE TABLE " + tablaResultados.TABLE_NAME + " (" +
                tablaResultados.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tablaResultados.COL_JUGADOR + " TEXT, " +
                tablaResultados.COL_FECHA + " TEXT, " +
                tablaResultados.COL_HORA + " TEXT, " +
                tablaResultados.COL_NUMEROFICHAS + " INTEGER, " +
                tablaResultados.COL_TIEMPO + " TEXT " + " )";
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String deleteSQL = "DROP TABLE IF EXISTS " + tablaResultados.TABLE_NAME;
        db.execSQL(deleteSQL);
        onCreate(db);
    }

    public long add(String jugador, String fecha, String hora, int numeroFichas, String tiempo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(tablaResultados.COL_JUGADOR, jugador);
        valores.put(tablaResultados.COL_FECHA, fecha);
        valores.put(tablaResultados.COL_HORA, hora);
        valores.put(tablaResultados.COL_NUMEROFICHAS, numeroFichas);
        valores.put(tablaResultados.COL_TIEMPO, tiempo);

        return db.insert(tablaResultados.TABLE_NAME, null, valores);
    }

    public ArrayList<Resultado> getMejoresResultados(String nfichas, String jugador) {

        String consultaSQL;
        if (!nfichas.equals("")) {
            consultaSQL = "SELECT * FROM " + tablaResultados.TABLE_NAME + " WHERE " + tablaResultados.COL_NUMEROFICHAS + " = " + nfichas
                    + " ORDER BY " + tablaResultados.COL_NUMEROFICHAS + " , " + tablaResultados.COL_TIEMPO;
        } else if (!jugador.equals("")) {
            consultaSQL = "SELECT * FROM " + tablaResultados.TABLE_NAME + " WHERE " + tablaResultados.COL_JUGADOR + " = \"" + jugador + "\""
                    + " ORDER BY " + tablaResultados.COL_NUMEROFICHAS + " , " + tablaResultados.COL_TIEMPO;
        } else {
            consultaSQL = "SELECT * FROM " + tablaResultados.TABLE_NAME + " ORDER BY " + tablaResultados.COL_NUMEROFICHAS + " , " + tablaResultados.COL_TIEMPO;
        }

        ArrayList<Resultado> listresultados = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(consultaSQL, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Resultado resultado = new Resultado(
                        cursor.getInt(cursor.getColumnIndex(tablaResultados.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(tablaResultados.COL_JUGADOR)),
                        cursor.getString(cursor.getColumnIndex(tablaResultados.COL_FECHA)),
                        cursor.getString(cursor.getColumnIndex(tablaResultados.COL_HORA)),
                        cursor.getInt(cursor.getColumnIndex(tablaResultados.COL_NUMEROFICHAS)),
                        cursor.getString(cursor.getColumnIndex(tablaResultados.COL_TIEMPO))
                );

                listresultados.add(resultado);
                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return listresultados;
    }

    public void deleteScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteSQL = "DELETE FROM " + tablaResultados.TABLE_NAME;
        db.execSQL(deleteSQL);
    }
}
