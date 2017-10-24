package es.upm.miw.SolitarioCelta.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import es.upm.miw.SolitarioCelta.R;
import es.upm.miw.SolitarioCelta.models.Resultado;


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

            TextView tv_posicion = (TextView) convertView.findViewById(R.id.tv_posicion);
            tv_posicion.setText(String.valueOf(position+1));

            if (position == 0){
                tv_posicion.setBackgroundColor(Color.YELLOW);
                tv_posicion.setTextSize(18);
            } else if (position == 1){
                tv_posicion.setBackgroundColor(Color.LTGRAY);
                tv_posicion.setTextSize(18);
            } else if (position == 2){
                tv_posicion.setBackgroundColor(Color.GRAY);
                tv_posicion.setTextSize(18);
            }

            TextView tv_jugador = (TextView) convertView.findViewById(R.id.tv_jugador);
            tv_jugador.setText(resultado.getJugador());

            TextView tv_fichas = (TextView) convertView.findViewById(R.id.tv_fichas);
            tv_fichas.setText(String.valueOf(resultado.getNumero_piezas()));

            TextView tv_tiempo = (TextView) convertView.findViewById(R.id.tv_tiempo);
            tv_tiempo.setText(resultado.getTiempo());
        }

        return convertView;
    }
}
