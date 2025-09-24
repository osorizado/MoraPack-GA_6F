// ✅ Main.java
package com.morapack.ga;

public class Main {
    public static void main(String[] args) {
        DataLoader.loadAeropuertos("data/aeropuertos.txt");
        DataLoader.loadVuelos("data/vuelos.txt");
        DataLoader.loadPedidos("data/pedidos.txt");

        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.run(DataLoader.vuelos, DataLoader.pedidos, DataLoader.aeropuertos);
    }
}