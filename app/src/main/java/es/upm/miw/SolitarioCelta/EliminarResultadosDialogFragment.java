package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Raquel on 21/10/17.
 */

public class EliminarResultadosDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final MejoresResultados activity = (MejoresResultados) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle(R.string.txtDialogoEliminarResultadosTitulo)
                .setMessage(R.string.txtDialogoEliminarResultadosPregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoFinalAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.eliminarResultados();
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoFinalNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                );

        return builder.create();
    }
}
