package es.upm.miw.SolitarioCelta.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Raquel on 21/10/17.
 */

public class Partida implements Parcelable {

    private int id;
    private String jugador;
    private String fecha;
    private String hora;
    private int numero_piezas;
    private String estadoPartida;

    public Partida(){

    }

    public Partida(int id, String jugador, String fecha, String hora, int numero_piezas, String estadoPartida) {
        this.id = id;
        this.jugador = jugador;
        this.fecha = fecha;
        this.hora = hora;
        this.numero_piezas = numero_piezas;
        this.estadoPartida = estadoPartida;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(String estadoPartida) {
        this.estadoPartida = estadoPartida;
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
        dest.writeString(this.estadoPartida);
    }

    protected Partida(Parcel in) {
        this.id = in.readInt();
        this.jugador = in.readString();
        this.fecha = in.readString();
        this.hora = in.readString();
        this.numero_piezas = in.readInt();
        this.estadoPartida = in.readString();
    }

    public static final Creator<Partida> CREATOR = new Creator<Partida>() {
        @Override
        public Partida createFromParcel(Parcel source) {
            return new Partida(source);
        }

        @Override
        public Partida[] newArray(int size) {
            return new Partida[size];
        }
    };
}
