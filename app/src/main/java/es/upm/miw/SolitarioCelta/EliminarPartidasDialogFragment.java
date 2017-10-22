package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Raquel on 21/10/17.
 */

public class EliminarPartidasDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final PartidasGuardadas activity = (PartidasGuardadas) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle(R.string.txtDialogoEliminarPartidasTitulo)
                .setMessage(R.string.txtDialogoEliminarPartidasPregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.eliminarPartidas();
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                );

        return builder.create();
    }
}
