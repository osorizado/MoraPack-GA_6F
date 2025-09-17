// ✅ Main.java
package com.morapack.ga;

public class Main {
    public static void main(String[] args) {
        DataLoader.loadVuelos("data/vuelos.txt");
        DataLoader.loadPedidos("data/pedidos.txt");

        GeneticAlgorithm ga = new GeneticAlgorithm();

        System.out.println("\n=== Procesando pedidos ===");
        ga.runWithPedidos(DataLoader.vuelosDisponibles, 5, DataLoader.pedidos);
    }
}