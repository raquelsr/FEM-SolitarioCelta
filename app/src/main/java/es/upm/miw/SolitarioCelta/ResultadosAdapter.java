package es.upm.miw.SolitarioCelta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.models.Resultado;

/**
 * Created by Raquel on 20/10/17.
 */

public class ResultadosAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Resultado> resultados;

    public ResultadosAdapter(Context context, ArrayList<Resultado> resultados) {
        super(context, R.layout.adapter_resultados, resultados);
        this.context = context;
        this.resultados = resultados;
        setNotifyOnChange(true);
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_resultados, null);
        }

        Resultado resultado = resultados.get(position);
        if (resultado != null) {
            TextView tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            tv_id.setText(String.valueOf(resultado.getId()));

            TextView tv_jugador = (TextView) convertView.findViewById(R.id.tv_jugador);
            tv_jugador.setText(resultado.getJugador());

            TextView tv_fichas = (TextView) convertView.findViewById(R.id.tv_fichas);
            tv_fichas.setText(String.valueOf(resultado.getNumero_piezas()));
        }

        return convertView;
    }
}
