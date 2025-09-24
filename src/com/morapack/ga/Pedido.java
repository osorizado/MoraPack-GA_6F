package com.morapack.ga;

public class Pedido {
    public String id;
    public String destino;
    public int cantidad;
    public String hubOrigen;
    public int dia;     // día del mes
    public int hora;    // hora de creación del pedido
    public int minuto;  // minuto de creación

    public Pedido(String id, String destino, int cantidad, String hubOrigen, int dia, int hora, int minuto) {
        this.id = id;
        this.destino = destino;
        this.cantidad = cantidad;
        this.hubOrigen = hubOrigen;
        this.dia = dia;
        this.hora = hora;
        this.minuto = minuto;
    }
}
