package com.morapack.ga;

import java.util.*;

public class Chromosome {
    List<String> route;   // Secuencia de vuelos
    double fitness;

    static Map<String, Double> tiemposVuelo = DataLoader.tiemposVuelo;
    static Map<String, Integer> capacidadVuelo = DataLoader.capacidadVuelo;
    static int capacidadAlmacen = 800;
    static Random rand = new Random();



    public Chromosome(List<String> route) {
        this.route = new ArrayList<>(route);
    }

    public void evaluate() {
        double totalTiempo = 0.0;
        double penalizacionTiempo = 0.0, penalizacionAlmacen = 0.0, penalizacionVuelo = 0.0;

        int cargaTotal = 0;

        for (int i = 0; i < route.size(); i++) {
            String vuelo = route.get(i);
            double t = tiemposVuelo.getOrDefault(vuelo, 1.0);
            totalTiempo += t;

            if (i > 0) totalTiempo += 0.1; // conexión

            int cargaVuelo = 100 + rand.nextInt(401);
            cargaTotal += cargaVuelo;

            int capMax = capacidadVuelo.getOrDefault(vuelo, 200);
            if (cargaVuelo > capMax) {
                double exceso = (cargaVuelo - capMax) / (double) capMax;
                penalizacionVuelo += exceso; // ya no *20
            }
        }

        // SLA: en vez de 3 o 2, pon límite relativo al # vuelos
        double limite = 2.0 + route.size() * 0.5;
        if (totalTiempo > limite) {
            penalizacionTiempo = (totalTiempo - limite);
        }

        if (cargaTotal > capacidadAlmacen) {
            double exceso = (cargaTotal - capacidadAlmacen) / (double) capacidadAlmacen;
            penalizacionAlmacen = exceso;
        }

        double penalizacionTotal = penalizacionTiempo + penalizacionAlmacen + penalizacionVuelo;
        double score = 100.0 / (1.0 + penalizacionTotal / 10.0);
        this.fitness = Math.max(0, score);

    }


    @Override
    public String toString() {
        return "Route: " + route + " | Fitness: " + String.format("%.2f", fitness);
    }
}
