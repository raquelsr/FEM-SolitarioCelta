package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.adapters.PartidasAdapter;
import es.upm.miw.SolitarioCelta.models.Partida;
import es.upm.miw.SolitarioCelta.models.RepositorioPartidasDBHelper;

public class PartidasGuardadas extends Activity {

    private static final String LOG_TAG = "MiW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_guardadas);
        mostrarPartidasGuardadas();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_partidas, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcEliminarResultados:
                dialogPartidas();
        }
        return true;
    }


    public void mostrarPartidasGuardadas(){
        final ListView lista = (ListView) findViewById(R.id.list_partidas);

        RepositorioPartidasDBHelper db = new RepositorioPartidasDBHelper(getApplicationContext());
        final ArrayList<Partida> partidas = db.getAll();

        if (partidas.isEmpty()){
            Toast.makeText(this, "No hay ninguna partida guardada aún.", Toast.LENGTH_SHORT).show();
        }

        PartidasAdapter adapter = new PartidasAdapter(getApplicationContext(), partidas);
        lista.setAdapter(adapter);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_partida = partidas.get(position).getId();
                recuperarPartida(id_partida);
                Log.i(LOG_TAG,  "Partida recuperada: id " + position);
                Toast.makeText(getApplicationContext(), "La partida ha sido recuperada.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void recuperarPartida(int id){
        RepositorioPartidasDBHelper db = new RepositorioPartidasDBHelper(getApplicationContext());
        Partida partida = db.get(id);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Partida", partida);
        startActivity(intent);
    }


    public void dialogPartidas(){
        DialogFragment dialog = new EliminarPartidasDialogFragment();
        dialog.show(getFragmentManager(), "eliminarPartidas");
    }

    public void eliminarPartidas(){
        RepositorioPartidasDBHelper db = new RepositorioPartidasDBHelper(getApplicationContext());
        db.deleteAll();
        mostrarPartidasGuardadas();
        Toast.makeText(this, "Se han eliminado todos las partidas correctamente.", Toast.LENGTH_SHORT).show();
    }


}
