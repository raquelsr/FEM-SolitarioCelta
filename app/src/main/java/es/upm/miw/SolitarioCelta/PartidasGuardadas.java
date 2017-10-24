package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.adapters.PartidasAdapter;
import es.upm.miw.SolitarioCelta.models.Partida;

public class PartidasGuardadas extends Activity {

    private static final String LOG_TAG = "MiW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_guardadas);
        if (getIntent().getExtras()!=null){
            mostrarPartidasGuardadas();
        }
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

        final ArrayList<Partida> partidas = getIntent().getExtras().getParcelableArrayList("partidas");

        if(partidas.isEmpty()){
            Toast.makeText(this, "No existen partidas", Toast.LENGTH_SHORT).show();
        }

        PartidasAdapter adapter = new PartidasAdapter(getApplicationContext(), partidas);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Partida partida = partidas.get(position);
                recuperarPartida(partida);
                Log.i(LOG_TAG,  "Partida recuperada: id " + position);
                Toast.makeText(getApplicationContext(), "La partida ha sido recuperada.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void recuperarPartida(Partida partida){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Partida", partida);
        startActivity(intent);
    }


    public void dialogPartidas(){
        DialogFragment dialog = new EliminarPartidasDialogFragment();
        dialog.show(getFragmentManager(), "eliminarPartidas");
    }

    public void eliminarPartidas() {
        try {
            FileOutputStream fos = openFileOutput("PartidaGuardada", Context.MODE_PRIVATE);
            fos.close();
            Toast.makeText(this, "El fichero ha sido borrado", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
