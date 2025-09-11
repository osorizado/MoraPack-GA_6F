package com.morapack.ga;

import java.util.*;

public class Chromosome {
    List<String> route;   // Secuencia de vuelos
    double fitness;

    static Map<String, Double> tiemposVuelo = new HashMap<>();
    static Map<String, Integer> capacidadVuelo = new HashMap<>();
    static int capacidadAlmacen = 800;
    static Random rand = new Random();

    static {
        // Intercontinentales
        tiemposVuelo.put("SPIM-JFK", 1.0); capacidadVuelo.put("SPIM-JFK", 300);
        tiemposVuelo.put("SPIM-MAD", 1.0); capacidadVuelo.put("SPIM-MAD", 350);
        tiemposVuelo.put("SPIM-FRA", 1.0); capacidadVuelo.put("SPIM-FRA", 250);
        tiemposVuelo.put("SPIM-DXB", 1.0); capacidadVuelo.put("SPIM-DXB", 300);
        tiemposVuelo.put("EBCI-JFK", 1.0); capacidadVuelo.put("EBCI-JFK", 300);
        tiemposVuelo.put("UBBB-PEK", 1.0); capacidadVuelo.put("UBBB-PEK", 400);

        // Regionales
        tiemposVuelo.put("JFK-LAX", 0.5); capacidadVuelo.put("JFK-LAX", 250);
        tiemposVuelo.put("JFK-YYZ", 0.5); capacidadVuelo.put("JFK-YYZ", 200);
        tiemposVuelo.put("MAD-AMS", 0.5); capacidadVuelo.put("MAD-AMS", 250);
        tiemposVuelo.put("MAD-FRA", 0.5); capacidadVuelo.put("MAD-FRA", 300);
        tiemposVuelo.put("FRA-DXB", 0.5); capacidadVuelo.put("FRA-DXB", 200);
        tiemposVuelo.put("FRA-AMS", 0.5); capacidadVuelo.put("FRA-AMS", 250);
        tiemposVuelo.put("DXB-BOM", 0.5); capacidadVuelo.put("DXB-BOM", 300);
        tiemposVuelo.put("PEK-HND", 0.5); capacidadVuelo.put("PEK-HND", 250);
        tiemposVuelo.put("AMS-LHR", 0.5); capacidadVuelo.put("AMS-LHR", 200);
    }

    public Chromosome(List<String> route) {
        this.route = new ArrayList<>(route);
    }

    // Evaluar fitness con reglas MoraPack
    public void evaluate() {
        double totalTiempo = 0.0;
        double penalizacionTiempo = 0.0, penalizacionAlmacen = 0.0, penalizacionVuelo = 0.0;

        int cargaTotal = 0;
        boolean intercontinental = false;

        for (int i = 0; i < route.size(); i++) {
            String vuelo = route.get(i);
            double t = tiemposVuelo.getOrDefault(vuelo, 1.0);
            totalTiempo += t;

            if (t == 1.0) intercontinental = true;

            // Penalización por conexión en aeropuerto (2h ~ 0.1d)
            if (i > 0) totalTiempo += 0.1;

            // Carga aleatoria en este vuelo (100–500)
            int cargaVuelo = 100 + rand.nextInt(401);
            cargaTotal += cargaVuelo;

            int capMax = capacidadVuelo.getOrDefault(vuelo, 200);
            if (cargaVuelo > capMax) {
                double exceso = (double)(cargaVuelo - capMax) / capMax;
                penalizacionVuelo += exceso * 20;
            }
        }

        // Penalización SLA
        if (intercontinental && totalTiempo > 3.0) {
            penalizacionTiempo = (totalTiempo - 3.0) * 15;
        } else if (!intercontinental && totalTiempo > 2.0) {
            penalizacionTiempo = (totalTiempo - 2.0) * 15;
        }

        // Penalización por almacén
        if (cargaTotal > capacidadAlmacen) {
            double exceso = (double)(cargaTotal - capacidadAlmacen) / capacidadAlmacen;
            penalizacionAlmacen = exceso * 30;
        }

        this.fitness = 100 - penalizacionTiempo - penalizacionAlmacen - penalizacionVuelo;
        if (this.fitness < 0) this.fitness = 0;
    }

    @Override
    public String toString() {
        return "Route: " + route + " | Fitness: " + String.format("%.2f", fitness);
    }
}
