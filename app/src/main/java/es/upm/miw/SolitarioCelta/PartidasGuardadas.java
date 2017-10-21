package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.models.Partida;
import es.upm.miw.SolitarioCelta.models.RepositorioPartidasDBHelper;
import es.upm.miw.SolitarioCelta.models.RepositorioResultadoDBHelper;
import es.upm.miw.SolitarioCelta.models.Resultado;

public class PartidasGuardadas extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_guardadas);

        mostrarPartidasGuardadas();
    }


    public void mostrarPartidasGuardadas(){
        final ListView lista = (ListView) findViewById(R.id.list_partidas);

        RepositorioPartidasDBHelper db = new RepositorioPartidasDBHelper(getApplicationContext());
        final ArrayList<Partida> partidas = db.getAll();

        PartidasAdapter adapter = new PartidasAdapter(getApplicationContext(), partidas);
        lista.setAdapter(adapter);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MiW",  "Partida pulsada con id" + position);
                Toast.makeText(getApplicationContext(), "Partida recuperada", Toast.LENGTH_SHORT).show();
                int id_partida = partidas.get(position).getId();
                recuperarPartida(id_partida);
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


}
