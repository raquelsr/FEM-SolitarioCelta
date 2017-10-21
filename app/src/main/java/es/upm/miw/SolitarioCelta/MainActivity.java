package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import es.upm.miw.SolitarioCelta.models.RepositorioResultadoDBHelper;

public class MainActivity extends Activity {

	JuegoCelta juego;
    private final String GRID_KEY = "GRID_KEY";
    private final String LOG_TAG = "MiW";
    private static final String fichero = "PartidaGuardada";

    RepositorioResultadoDBHelper db;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = new JuegoCelta();
        mostrarTablero();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String jugador = preferencias.getString(
                getResources().getString(R.string.keyNombreJugador),
                getResources().getString(R.string.defaultNombreJugador)
        );
    }


    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     * @param v Vista de la ficha pulsada
     */
    public void fichaPulsada(View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna

        juego.jugar(i, j);

        mostrarTablero();
        if (juego.juegoTerminado()) {
            guardarResultado();
            new AlertDialogFragment().show(getFragmentManager(), "ALERT_DIALOG");
        }
    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        String strRId;
        String prefijoIdentificador = getPackageName() + ":id/p"; // formato: package:type/entry
        int idBoton;

        for (int i = 0; i < JuegoCelta.TAMANIO; i++)
            for (int j = 0; j < JuegoCelta.TAMANIO; j++) {
                strRId = prefijoIdentificador + Integer.toString(i) + Integer.toString(j);
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = (RadioButton) findViewById(idBoton);
                    button.setChecked(juego.obtenerFicha(i, j) == JuegoCelta.FICHA);
                }
            }
    }

    /**
     * Guarda el estado del tablero (serializado)
     * @param outState Bundle para almacenar el estado del juego
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(GRID_KEY, juego.serializaTablero());
        super.onSaveInstanceState(outState);
    }

    /**
     * Recupera el estado del juego
     * @param savedInstanceState Bundle con el estado del juego almacenado
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(GRID_KEY);
        juego.deserializaTablero(grid);
        mostrarTablero();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, SCeltaPrefs.class));
                return true;
            case R.id.opcAcercaDe:
                startActivity(new Intent(this, AcercaDe.class));
                return true;
            case R.id.opcReiniciarPartida:
                DialogFragment reiniciarDialog = (DialogFragment) new ReiniciarDialogFragment();
                reiniciarDialog.show(getFragmentManager(), "reiniciar");
                Log.i(LOG_TAG, "Reiniciar partida");
                return true;
            case R.id.opcGuardarPartida:
                guardarPartida();
                return true;
            case R.id.opcRecuperarPartida:
                if (juego.numeroFichas()!=32){
                    DialogFragment recuperarDialog = (DialogFragment) new RecuperarPartidaDialogFragment();
                    recuperarDialog.show(getFragmentManager(), "recuperar");
                } else {
                    recuperarPartida();
                }
                return true;
            case R.id.opcMejoresResultados:
                mostrarMejoresResultados();
                return true;

            // TODO!!! resto opciones

            default:
                Toast.makeText(
                        this,
                        getString(R.string.txtSinImplementar),
                        Toast.LENGTH_SHORT
                ).show();
        }
        return true;
    }

    public void guardarPartida(){
        try {
            String cadenaJuego = juego.serializaTablero();
            FileOutputStream fos = openFileOutput(fichero, Context.MODE_PRIVATE);
            fos.write(cadenaJuego.getBytes());
            fos.write('\n');
            fos.close();
            Toast.makeText(this, "La partida ha sido guardada con éxito.", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Partida guardada");
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
    }

    public void recuperarPartida(){
        try {
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(fichero)));
            String linea = fin.readLine();
            if (linea != null) {
                juego.deserializaTablero(linea);
                mostrarTablero();
            }
            fin.close();
            Toast.makeText(this, "La partida ha sido recuperada.", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Partida recuperada");
        } catch (FileNotFoundException e){
            Toast.makeText(this, "No existen partidas guardadas.", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "No existen partidas guardadas.");
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
    }

    public void guardarResultado(){
        db = new RepositorioResultadoDBHelper(getApplicationContext());

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        String jugador = preferencias.getString(
                getResources().getString(R.string.keyNombreJugador),
                getResources().getString(R.string.defaultNombreJugador)
        );

        Calendar date = Calendar.getInstance();
        String fecha = String.valueOf(date.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(date.get(Calendar.MONTH)+1) + "/" + String.valueOf(date.get(Calendar.YEAR));
        String minutos = (date.get(Calendar.MINUTE)) < 10
                ? "0".concat(String.valueOf((date.get(Calendar.MINUTE))))
                :  String.valueOf(date.get(Calendar.MINUTE));

        String hora = String.valueOf(date.get(Calendar.HOUR_OF_DAY)) + ":" + minutos;

        long id = db.add(jugador, fecha, hora, juego.numeroFichas());
        Log.i("MiW", String.valueOf(id));
    }

    public void mostrarMejoresResultados(){
        Intent intent = new Intent(this, MejoresResultados.class);
        startActivity(intent);
    }
}
