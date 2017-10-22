package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import es.upm.miw.SolitarioCelta.models.Partida;
import es.upm.miw.SolitarioCelta.models.RepositorioPartidasDBHelper;
import es.upm.miw.SolitarioCelta.models.RepositorioResultadoDBHelper;

public class MainActivity extends Activity {

	JuegoCelta juego;
    private final String GRID_KEY = "GRID_KEY";
    private final String FICHAS_KEY = "FICHAS_KEY";
    private final String CRONOMETRO_KEY = "CRONOMETRO_KEY";

    private final String LOG_TAG = "MiW";
    private static final String fichero = "PartidaGuardada";

    RepositorioResultadoDBHelper db_resultados;
    RepositorioPartidasDBHelper db_partidas;

    TextView tv_nfichas;
    Chronometer cronometro;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = new JuegoCelta();
        cronometro = (Chronometer) findViewById(R.id.cronometro);

        if (getIntent().getExtras()!=null){
            Partida partida = getIntent().getExtras().getParcelable("Partida");
            juego.deserializaTablero(partida.getEstadoPartida());
            juego.setNumeroFichas(partida.getNumero_piezas());
            cronometro.setBase(Long.valueOf(partida.getCronometroBase()));
        }

        if (savedInstanceState!=null){
            juego.setNumeroFichas(savedInstanceState.getInt(FICHAS_KEY));
            cronometro.setBase(savedInstanceState.getLong(CRONOMETRO_KEY));
        }
        mostrarTablero();
        actualizarInformacion();
        cronometro.start();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int color;
        int tamaño;

        System.out.println("WHITEEEEE " + Color.WHITE);
        String prefColor = pref.getString(getResources().getString(R.string.keyColor), null);
        String prefTamaño = pref.getString(getResources().getString(R.string.keyTamaño), null);


        if (prefColor.equals("Azul")) {
            color = Color.BLUE;
        } else if (prefColor.equals("Verde")){
            color = Color.GREEN;
        } else if (prefColor.equals("Rojo")){
            color = Color.RED;
        } else {
            color = Color.WHITE;
        }

        if (prefTamaño.equals("Grande")){
            tamaño = 24;
        } else if (prefTamaño.equals("Pequeño")){
            tamaño = 10;
        } else {
            tamaño = 16;
        }

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_main);
        layout.setBackgroundColor(color);

        tv_nfichas.setTextSize(tamaño);
        cronometro.setTextSize(tamaño);

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
        actualizarInformacion();

        mostrarTablero();
        if (juego.juegoTerminado()) {
            cronometro = (Chronometer) findViewById(R.id.cronometro);
            cronometro.stop();
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
        outState.putInt(FICHAS_KEY, juego.numeroFichas());
        outState.putLong(CRONOMETRO_KEY, cronometro.getBase());
        super.onSaveInstanceState(outState);
    }

    /**
     * Recupera el estado del juego
     * @param savedInstanceState Bundle con el estado del juego almacenado
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(GRID_KEY);
        int nfichas = savedInstanceState.getInt(FICHAS_KEY);
        long cronometroBase = savedInstanceState.getLong(CRONOMETRO_KEY);
        juego.deserializaTablero(grid);
        juego.setNumeroFichas(nfichas);

        mostrarTablero();
        actualizarInformacion();
        cronometro.setBase(cronometroBase);
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
                guardarPartidaBBDD();
                return true;
            case R.id.opcRecuperarPartida:
                if (juego.numeroFichas()!=32){
                    DialogFragment recuperarDialog = (DialogFragment) new RecuperarPartidaDialogFragment();
                    recuperarDialog.show(getFragmentManager(), "recuperar");
                } else {
                    //recuperarPartida();
                    mostrarPartidasGuardadas();
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

    public void guardarPartidaBBDD(){
        db_partidas = new RepositorioPartidasDBHelper(getApplicationContext());

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

        String estado = juego.serializaTablero();

        long id = db_partidas.add(jugador, fecha, hora, juego.numeroFichas(), estado, cronometro.getText().toString(), String.valueOf(cronometro.getBase()));
        Log.i("MiW", "PartidaGuardada2" + String.valueOf(id));
        Toast.makeText(this, "La partida ha sido guardada con éxito.", Toast.LENGTH_SHORT).show();
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
        db_resultados = new RepositorioResultadoDBHelper(getApplicationContext());

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

        long id = db_resultados.add(jugador, fecha, hora, juego.numeroFichas(), cronometro.getText().toString());
        Log.i("MiW", String.valueOf(id));
    }

    public void mostrarPartidasGuardadas(){
        Intent intent = new Intent(this, PartidasGuardadas.class);
        startActivity(intent);
    }

    public void mostrarMejoresResultados(){
        Intent intent = new Intent(this, MejoresResultados.class);
        startActivity(intent);
    }

    public void actualizarInformacion(){
        tv_nfichas = (TextView) findViewById(R.id.tv_numeroFichas);
        tv_nfichas.setText("Número de fichas: " + (String.valueOf(juego.numeroFichas())));
        cronometro.start();
    }
}
