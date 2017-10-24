package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FiltroJugador extends Activity {

    private static final String key_jugador= "JUGADOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_jugador);
    }

    public void aceptarJugador (View v){
        EditText edit_jugador = (EditText) findViewById(R.id.edita_jugador);
        String jugador = edit_jugador.getText().toString();
        Intent resultados = new Intent(this, MejoresResultados.class);
        resultados.putExtra(key_jugador, jugador);
        startActivity(resultados);
    }
}
