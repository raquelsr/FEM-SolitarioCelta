package es.upm.miw.SolitarioCelta.models;

import android.provider.BaseColumns;

/**
 * Created by Raquel on 19/10/17.
 */

public class ResultadoContract {

    private ResultadoContract(){}

    public static abstract class tablaResultados implements BaseColumns {
        static final String TABLE_NAME  = "resultados";

        static final String COL_ID = _ID;
        static final String COL_JUGADOR = "jugador";
        static final String COL_FECHA = "fecha";
        static final String COL_HORA = "hora";
        static final String COL_NUMEROFICHAS = "numero_fichas";
        static final String COL_TIEMPO = "tiempo";
    }
}
