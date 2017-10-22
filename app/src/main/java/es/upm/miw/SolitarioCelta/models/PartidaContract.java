package es.upm.miw.SolitarioCelta.models;

import android.provider.BaseColumns;

/**
 * Created by Raquel on 21/10/17.
 */

public class PartidaContract {

    private PartidaContract(){}

    public static abstract class tablaPartidas implements BaseColumns {
        static final String TABLE_NAME  = "partidas";

        static final String COL_ID = _ID;
        static final String COL_JUGADOR = "jugador";
        static final String COL_FECHA = "fecha";
        static final String COL_HORA = "hora";
        static final String COL_NUMEROFICHAS = "numero_fichas";
        static final String COL_ESTADO = "estado";
        static final String COL_CRONOMETROTXT = "cronometroTxt";
        static final String COL_CRONOMETROBASE = "cronometroBase";
    }
}
