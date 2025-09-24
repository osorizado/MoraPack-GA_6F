package com.morapack.ga;

public class Vuelo {
    public final int id;
    public final String origen;
    public final String destino;
    public final int salidaMin;   // minutos desde 00:00
    public final int llegadaMin;  // minutos desde 00:00
    public int capacidad;         // mutable
    public final double horasDuracion;
    public final boolean esContinental;

    public Vuelo(int id, String origen, String destino, int salidaMin, int llegadaMin,
                 int capacidad, int duracion, boolean esContinental) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.salidaMin = salidaMin;
        this.llegadaMin = llegadaMin;
        this.capacidad = capacidad;
        this.horasDuracion = duracion / 60.0;
        this.esContinental = esContinental;
    }
}
