package com.morapack.ga;

public class Pedido {
    public String id;
    public String destino;
    public int cantidad;
    public String hubOrigen; // SPIM, EBCI o UBBB

    public Pedido(String id, String destino, int cantidad, String hubOrigen) {
        this.id = id;
        this.destino = destino;
        this.cantidad = cantidad;
        this.hubOrigen = hubOrigen;
    }
}
