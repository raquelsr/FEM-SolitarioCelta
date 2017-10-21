package es.upm.miw.SolitarioCelta;

import android.content.Context;
import android.icu.text.MessagePattern;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.models.Partida;
import es.upm.miw.SolitarioCelta.models.Resultado;

/**
 * Created by Raquel on 21/10/17.
 */

public class PartidasAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Partida> partidas;

    public PartidasAdapter(Context context, ArrayList<Partida> partidas) {
        super(context, R.layout.adapter_partidas, partidas);
        this.context = context;
        this.partidas = partidas;
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
            convertView = inflater.inflate(R.layout.adapter_partidas, null);
        }

        Partida partida = partidas.get(position);
        if (partida != null) {

            TextView tv_jugador = (TextView) convertView.findViewById(R.id.tv_Partidajugador);
            tv_jugador.setText(partida.getJugador());

            TextView tv_fichas = (TextView) convertView.findViewById(R.id.tv_Partidafichas);
            tv_fichas.setText(String.valueOf(partida.getNumero_piezas()));

            TextView tv_fecha = (TextView) convertView.findViewById(R.id.tv_Partidafecha);
            tv_fecha.setText(partida.getFecha());

            TextView tv_hora = (TextView) convertView.findViewById(R.id.tv_Partidahora);
            tv_hora.setText(partida.getHora());
        }

        return convertView;
    }
}
