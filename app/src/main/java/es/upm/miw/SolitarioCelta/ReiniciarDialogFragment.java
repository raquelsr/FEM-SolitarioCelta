package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * Created by Raquel on 19/10/17.
 */

public class ReiniciarDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.txtDialogoReiniciarTitulo))
                .setMessage(getString(R.string.txtDialogoReiniciarPregunta))
                .setPositiveButton(
                        getString(R.string.txtDialogoAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                main.juego.reiniciar();
                                main.mostrarTablero();
                                main.actualizarNumeroFichas();
                                main.actualizarCronometro(SystemClock.elapsedRealtime());
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Acción opción No
                            }
                        }
                );

        return builder.create();
    }
}
