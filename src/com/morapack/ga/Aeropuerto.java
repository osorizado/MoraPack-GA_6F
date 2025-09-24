package com.morapack.ga;
import java.util.TreeMap;

public class Aeropuerto {
    public int id;
    public String codigo;
    public int capacidad;
    public String continente;
    public TreeMap<Integer, Integer> ocupacionPorMinuto = new TreeMap<>();

    public Aeropuerto(int id, String codigo, int capacidad, String continente) {
        this.id = id;
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.continente = continente;
    }
}
