package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class RecuperarPartidaDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.txtDialogoRecuperarTitulo))
                .setMessage(getString(R.string.txtDialogoRecuperarPregunta))
                .setPositiveButton(
                        getString(R.string.txtDialogoAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                main.mostrarPartidasGuardadas();
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
