package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.adapters.ResultadosAdapter;
import es.upm.miw.SolitarioCelta.models.RepositorioResultadoDBHelper;
import es.upm.miw.SolitarioCelta.models.Resultado;

public class MejoresResultados extends Activity {

    private static final String key_nfichas = "NFICHAS";
    private static final String key_jugador = "JUGADOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejores_resultados);
        String nfichas = "";
        String jugador = "";

        if (getIntent().getExtras()!=null){
            if (getIntent().getExtras().getString(key_nfichas)!=null){
                nfichas= getIntent().getExtras().getString(key_nfichas);
            }
            if (getIntent().getExtras().getString(key_jugador)!=null){
                jugador = getIntent().getExtras().getString(key_jugador);
            }
        }
        mostrarResultados(nfichas,jugador);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_resultados, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcEliminarResultados:
             dialogResultados();
        }
        return true;
    }

    public void mostrarResultados (String nfichas, String jugador){
        ListView lista = (ListView) findViewById(R.id.list_resultados);

        RepositorioResultadoDBHelper db = new RepositorioResultadoDBHelper(getApplicationContext());
        ArrayList<Resultado> resultados = db.getMejoresResultados(nfichas, jugador);

        ResultadosAdapter adapter = new ResultadosAdapter(getApplicationContext(), resultados);
        lista.setAdapter(adapter);
    }

    public void dialogResultados(){
        DialogFragment dialog = new EliminarResultadosDialogFragment();
        dialog.show(getFragmentManager(), "eliminarResultados");
    }

    public void eliminarResultados(){
        RepositorioResultadoDBHelper db = new RepositorioResultadoDBHelper(getApplicationContext());
        db.deleteScore();
        mostrarResultados("","");
        Toast.makeText(this, "Resultados eliminados correctamente.", Toast.LENGTH_SHORT).show();
    }

    public void mostrarDialogFichas(View v){
        Intent i = new Intent (this, FiltroFichas.class);
        startActivity(i);
    }

    public void mostrarDialogJugador(View v){
        Intent i = new Intent (this, FiltroJugador.class);
        startActivity(i);
    }

    public void mostrarResultadosSinFiltro(View v){
        mostrarResultados("","");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
