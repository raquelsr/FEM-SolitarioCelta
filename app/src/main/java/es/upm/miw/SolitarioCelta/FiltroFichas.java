package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FiltroFichas extends Activity {

    private static final String key_nfichas= "NFICHAS";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_fichas);
    }

    public void aceptarNFichas (View v){
        EditText edit_nfichas = (EditText) findViewById(R.id.edita_nfichas);
        String nfichas = edit_nfichas.getText().toString();
        Intent resultados = new Intent(this, MejoresResultados.class);
        resultados.putExtra(key_nfichas, nfichas);
        startActivity(resultados);
    }
}
