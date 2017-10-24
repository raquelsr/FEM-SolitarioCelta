package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import es.upm.miw.SolitarioCelta.models.Partida;
import es.upm.miw.SolitarioCelta.models.Resultado;

public class MainActivity extends Activity {

    JuegoCelta juego;
    private static final String GRID_KEY = "GRID_KEY";
    private static final String FICHAS_KEY = "FICHAS_KEY";
    private static final String CRONOMETRO_KEY = "CRONOMETRO_KEY";

    private static final String LOG_TAG = "MiW";
    private static final String ficheroPartidasGuardadas = "Partidas_Guardadas";
    private static final String ficheroResultados = "Resultados";

    TextView tv_nfichas;
    Chronometer cronometro;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = new JuegoCelta();
        cronometro = (Chronometer) findViewById(R.id.cronometro);


        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getParcelable("Partida") != null) {
                Partida partida = getIntent().getExtras().getParcelable("Partida");
                if (partida != null){
                    juego.deserializaTablero(partida.getEstadoPartida());
                    juego.setNumeroFichas(partida.getNumero_piezas());

                    String[] ms = partida.getCronometroTxt().split(":");
                    long baseTime = SystemClock.elapsedRealtime() - (Integer.valueOf(ms[0]) * 60000 + Integer.valueOf(ms[1]) * 1000);
                    cronometro.setBase(baseTime);
                }
            }
        }

        if (savedInstanceState != null) {
            juego.setNumeroFichas(savedInstanceState.getInt(FICHAS_KEY));
            cronometro.setBase(savedInstanceState.getLong(CRONOMETRO_KEY));
        }

        mostrarTablero();
        actualizarNumeroFichas();
        cronometro.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int color;
        int colorLetra;
        int tamaño;

        String prefColor = pref.getString(getResources().getString(R.string.keyColor), getResources().getString(R.string.defaultColor));
        String prefTamaño = pref.getString(getResources().getString(R.string.keyTamaño), getResources().getString(R.string.defaultTamaño));

        color = Color.WHITE;
        colorLetra = Color.BLACK;

        if (prefColor.equals("Azul")) {
            color = Color.CYAN;
            colorLetra = Color.WHITE;
        } else if (prefColor.equals("Verde")) {
            color = Color.GREEN;
            colorLetra = Color.BLACK;
        } else if (prefColor.equals("Rojo")) {
            color = Color.RED;
            colorLetra = Color.WHITE;
        } else if (prefColor.equals("Amarillo")) {
            color = Color.YELLOW;
            colorLetra = Color.BLACK;
        } else if (prefColor.equals("Rosa")) {
            color = Color.MAGENTA;
            colorLetra = Color.WHITE;
        } else if (prefColor.equals("Blanco")) {
            color = Color.WHITE;
            colorLetra = Color.BLACK;
        }

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_main);
        layout.setBackgroundColor(color);

        LinearLayout layout1 = (LinearLayout) findViewById(R.id.layout_main_1);
        layout1.setBackgroundColor(color);

        LinearLayout layout2 = (LinearLayout) findViewById(R.id.layout_main_2);
        layout2.setBackgroundColor(color);

        tv_nfichas.setTextColor(colorLetra);
        cronometro.setTextColor(colorLetra);


        if (prefTamaño.equals("Grande")) {
            tamaño = 24;
        } else if (prefTamaño.equals("Pequeño")) {
            tamaño = 10;
        } else {
            tamaño = 16;
        }

        tv_nfichas.setTextSize(tamaño);
        cronometro.setTextSize(tamaño);

    }

    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     *
     * @param v Vista de la ficha pulsada
     */

    public void fichaPulsada(View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna

        juego.jugar(i, j);
        actualizarNumeroFichas();

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
     *
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
     *
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
        actualizarNumeroFichas();
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
                DialogFragment reiniciarDialog = new ReiniciarDialogFragment();
                reiniciarDialog.show(getFragmentManager(), "reiniciar");
                Log.i(LOG_TAG, "Reiniciar partida. ");
                return true;
            case R.id.opcGuardarPartida:
                DialogFragment guardarDialog = new GuardarPartidaDialogFragment();
                guardarDialog.show(getFragmentManager(), "guardarPartida");
                return true;
            case R.id.opcRecuperarPartida:
                if (juego.numeroFichas() != 32) {
                    DialogFragment recuperarDialog = new RecuperarPartidaDialogFragment();
                    recuperarDialog.show(getFragmentManager(), "recuperar");
                } else {
                    mostrarPartidasGuardadas();
                }
                return true;
            case R.id.opcMejoresResultados:
                mostrarResultados();
                return true;
            default:
                Toast.makeText(
                        this,
                        getString(R.string.txtSinImplementar),
                        Toast.LENGTH_SHORT
                ).show();
        }
        return true;
    }

    public void guardarPartida() {
        try {
            FileOutputStream fos = openFileOutput(ficheroPartidasGuardadas, Context.MODE_APPEND);

            String cadenaJuego = juego.serializaTablero();

            Calendar date = Calendar.getInstance();
            String fecha = String.valueOf(date.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(date.get(Calendar.MONTH) + 1) + "/" + String.valueOf(date.get(Calendar.YEAR));
            String minutos = (date.get(Calendar.MINUTE)) < 10
                    ? "0".concat(String.valueOf((date.get(Calendar.MINUTE))))
                    : String.valueOf(date.get(Calendar.MINUTE));

            String hora = String.valueOf(date.get(Calendar.HOUR_OF_DAY)) + ":" + minutos;

            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
            String jugador = preferencias.getString(
                    getResources().getString(R.string.keyNombreJugador),
                    getResources().getString(R.string.defaultNombreJugador)
            );

            fos.write(jugador.getBytes());
            fos.write(';');
            fos.write(cadenaJuego.getBytes());
            fos.write(';');
            fos.write(String.valueOf(juego.numeroFichas()).getBytes());
            fos.write(';');
            fos.write(fecha.getBytes());
            fos.write(';');
            fos.write(hora.getBytes());
            fos.write(';');
            fos.write(cronometro.getText().toString().getBytes());
            fos.write(';');
            fos.write(String.valueOf(cronometro.getBase()).getBytes());
            fos.write('\n');

            fos.close();
            Toast.makeText(this, "La partida ha sido guardada con éxito.", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Partida guardada.");
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
    }

    public void mostrarPartidasGuardadas() {

        ArrayList<Partida> partidas = new ArrayList<>();
        try {

            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(ficheroPartidasGuardadas)));
            String linea = fin.readLine();
            while (linea != null) {

                String[] datosJuego = linea.split(";");
                Partida partida = new Partida(datosJuego[0], datosJuego[1], Integer.valueOf(datosJuego[2]), datosJuego[3], datosJuego[4], datosJuego[5], datosJuego[6]);
                partidas.add(partida);
                linea = fin.readLine();
            }
            fin.close();

            Intent i = new Intent(this, PartidasGuardadas.class);
            i.putParcelableArrayListExtra("partidas", partidas);
            startActivity(i);
            Log.i(LOG_TAG, "Listado de partidas guardadas.");

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No existen partidas guardadas áun.", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "No existen partidas guardadas.");
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
    }

    public void guardarResultado() {
        try {
            FileOutputStream fos = openFileOutput(ficheroResultados, Context.MODE_APPEND);

            Calendar date = Calendar.getInstance();
            String fecha = String.valueOf(date.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(date.get(Calendar.MONTH) + 1) + "/" + String.valueOf(date.get(Calendar.YEAR));
            String minutos = (date.get(Calendar.MINUTE)) < 10
                    ? "0".concat(String.valueOf((date.get(Calendar.MINUTE))))
                    : String.valueOf(date.get(Calendar.MINUTE));

            String hora = String.valueOf(date.get(Calendar.HOUR_OF_DAY)) + ":" + minutos;

            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
            String jugador = preferencias.getString(
                    getResources().getString(R.string.keyNombreJugador),
                    getResources().getString(R.string.defaultNombreJugador)
            );

            fos.write(jugador.getBytes());
            fos.write(';');
            fos.write(fecha.getBytes());
            fos.write(';');
            fos.write(hora.getBytes());
            fos.write(';');
            fos.write(String.valueOf(juego.numeroFichas()).getBytes());
            fos.write(';');
            fos.write(cronometro.getText().toString().getBytes());
            fos.write('\n');

            fos.close();
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
    }

    public void mostrarResultados() {

        ArrayList<Resultado> resultados = new ArrayList<>();
        try {

            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(ficheroResultados)));
            String linea = fin.readLine();
            while (linea != null) {

                String[] datosResultado = linea.split(";");
                Resultado resultado = new Resultado(datosResultado[0], datosResultado[1], datosResultado[2], Integer.valueOf(datosResultado[3]), datosResultado[4]);
                resultados.add(resultado);

                linea = fin.readLine();
            }
            fin.close();

            Collections.sort(resultados);
            Intent i = new Intent(this, MejoresResultados.class);
            i.putParcelableArrayListExtra("resultados", resultados);
            startActivity(i);

            Log.i(LOG_TAG, "Listado de resultados guardados.");

        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No existen resultados guardados aún.", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "No existen resultados guardados.");
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            e.printStackTrace();
        }
    }

    public void actualizarNumeroFichas() {
        tv_nfichas = (TextView) findViewById(R.id.tv_numeroFichas);
        tv_nfichas.setText(getResources().getString(R.string.txtNumeroFichas) + (String.valueOf(juego.numeroFichas())));
    }

    public void actualizarCronometro(long tiempo) {
        cronometro.setBase(tiempo);
        cronometro.start();
    }
}
