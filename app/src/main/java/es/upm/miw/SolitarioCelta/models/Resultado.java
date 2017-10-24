package es.upm.miw.SolitarioCelta.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.sql.Time;
import java.util.Date;

public class Resultado implements Parcelable, Comparable<Resultado> {

    private String jugador;
    private String fecha;
    private String hora;
    private int numero_piezas;
    private String tiempo;

    public Resultado(String jugador, String fecha, String hora, int numero_piezas, String tiempo) {
        this.jugador = jugador;
        this.fecha = fecha;
        this.hora = hora;
        this.numero_piezas = numero_piezas;
        this.tiempo = tiempo;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String dia) {
        this.fecha = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getNumero_piezas() {
        return numero_piezas;
    }

    public void setNumero_piezas(int numero_piezas) {
        this.numero_piezas = numero_piezas;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jugador);
        dest.writeString(this.fecha);
        dest.writeString(this.hora);
        dest.writeInt(this.numero_piezas);
        dest.writeString(this.tiempo);
    }

    protected Resultado(Parcel in) {
        this.jugador = in.readString();
        this.fecha = in.readString();
        this.hora = in.readString();
        this.numero_piezas = in.readInt();
        this.tiempo = in.readString();
    }

    public static final Creator<Resultado> CREATOR = new Creator<Resultado>() {
        @Override
        public Resultado createFromParcel(Parcel source) {
            return new Resultado(source);
        }

        @Override
        public Resultado[] newArray(int size) {
            return new Resultado[size];
        }
    };

    @Override
    public int compareTo(Resultado resultado) {
        int solf = Integer.compare(this.getNumero_piezas(), resultado.getNumero_piezas());
        if (solf != 0) {
            return solf;
        } else {
            String[] t1 = tiempo.split(":");
            int min1 = Integer.valueOf(t1[0]);
            int seg1 = Integer.valueOf(t1[1]);
            String[] t2 = resultado.tiempo.split(":");
            int min2 = Integer.valueOf(t2[0]);
            int seg2 = Integer.valueOf(t2[1]);
            int solm = Integer.compare(min1, min2);
            if (solm != 0) {
                return solm;
            } else {
                int sols = Integer.compare(seg1, seg2);
                return sols;
            }
        }
    }
}
