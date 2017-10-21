package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.models.RepositorioResultadoDBHelper;
import es.upm.miw.SolitarioCelta.models.Resultado;

public class MejoresResultados extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejores_resultados);

        mostrarResultados();

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

    public void mostrarResultados(){
        ListView lista = (ListView) findViewById(R.id.list_resultados);

        RepositorioResultadoDBHelper db = new RepositorioResultadoDBHelper(getApplicationContext());
        ArrayList<Resultado> resultados = db.getBest();

        ResultadosAdapter adapter = new ResultadosAdapter(getApplicationContext(), resultados);
        lista.setAdapter(adapter);
    }

    public void dialogResultados(){
        DialogFragment dialog = (DialogFragment) new EliminarResultadosDialogFragment();
        dialog.show(getFragmentManager(), "eliminarResultados");
    }

    public void eliminarResultados(){
        RepositorioResultadoDBHelper db = new RepositorioResultadoDBHelper(getApplicationContext());
        db.deleteScore();
        mostrarResultados();
        Toast.makeText(this, "Resultados eliminados correctamente.", Toast.LENGTH_SHORT).show();
    }
}
