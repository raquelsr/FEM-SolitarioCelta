package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

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
                        getString(R.string.txtDialogoFinalAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                main.juego.reiniciar();
                                main.mostrarTablero();
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalNegativo),
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