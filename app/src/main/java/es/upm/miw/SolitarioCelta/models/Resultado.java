package es.upm.miw.SolitarioCelta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Raquel on 19/10/17.
 */

public class Resultado implements Parcelable {

    private int id;
    private String jugador;
    private String fecha;
    private String hora;
    private int numero_piezas;

    public Resultado(int id, String jugador, String fecha, String hora, int numero_piezas) {
        this.id = id;
        this.jugador = jugador;
        this.fecha = fecha;
        this.hora = hora;
        this.numero_piezas = numero_piezas;
    }

    public int getId() {
        return id;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public String getDia() {
        return fecha;
    }

    public void setDia(String dia) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.jugador);
        dest.writeString(this.fecha);
        dest.writeString(this.hora);
        dest.writeInt(this.numero_piezas);
    }

    protected Resultado(Parcel in) {
        this.id = in.readInt();
        this.jugador = in.readString();
        this.fecha = in.readString();
        this.hora = in.readString();
        this.numero_piezas = in.readInt();
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
}
