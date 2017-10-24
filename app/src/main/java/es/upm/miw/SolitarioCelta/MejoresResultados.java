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
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import es.upm.miw.SolitarioCelta.adapters.ResultadosAdapter;
import es.upm.miw.SolitarioCelta.models.Resultado;

public class MejoresResultados extends Activity {

    private static final String key_nfichas = "NFICHAS";
    private static final String key_jugador = "JUGADOR";
    private static final String ficheroResultados = "ficheroResultados";
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

        final ListView lista = (ListView) findViewById(R.id.list_resultados);
        final ArrayList<Resultado> resultadosAll;

        resultadosAll = recuperarResultados();

        ArrayList<Resultado> resultados = new ArrayList<>();

        if(resultadosAll.isEmpty()){
            Toast.makeText(this, "No existen resultados", Toast.LENGTH_SHORT).show();
        }

        if (!nfichas.equals("")){
            int fichas = Integer.valueOf(nfichas);
            for (Resultado r: resultadosAll){
                if (r.getNumero_piezas() == fichas){
                    resultados.add(r);
                }
            }
        } else if (!jugador.equals("") ) {
            for (Resultado r : resultadosAll){
                if (r.getJugador().equals(jugador)){
                    resultados.add(r);
                }
            }
        } else {
            resultados = resultadosAll;
        }

        Collections.sort(resultados);

        ResultadosAdapter adapter = new ResultadosAdapter(getApplicationContext(), resultados);
        lista.setAdapter(adapter);

    }

    public void dialogResultados(){
        DialogFragment dialog = new EliminarResultadosDialogFragment();
        dialog.show(getFragmentManager(), "eliminarResultados");
    }

    public void eliminarResultados(){
        try {
            FileOutputStream fos = openFileOutput(ficheroResultados, Context.MODE_PRIVATE);
            fos.close();
            Toast.makeText(this, "El fichero resultado ha sido borrado", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public ArrayList<Resultado> recuperarResultados(){

        ArrayList<Resultado> resultados = new ArrayList<>();
        try {

            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("ficheroResultados")));
            String linea = fin.readLine();
            while (linea != null) {

                String[] datosResultado = linea.split(";");
                Resultado resultado = new Resultado (datosResultado[0],datosResultado[1],datosResultado[2],Integer.valueOf(datosResultado[3]),datosResultado[4]);
                resultados.add(resultado);

                linea = fin.readLine();
            }
            fin.close();

            Log.i("MiW", "Listado de resultados guardadas.");

        } catch (FileNotFoundException e){
            Toast.makeText(this, "No existen resultados guardadas.", Toast.LENGTH_SHORT).show();
            Log.i("MiW", "No existen resultados guardadas.");
        } catch (Exception e) {
            Log.e("MiW", "ERROR: " + e);
            e.printStackTrace();
        }
        return resultados;
    }
}
