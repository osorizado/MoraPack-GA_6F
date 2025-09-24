package com.morapack.ga;

import java.util.*;

public class Chromosome {
    List<Vuelo> route;
    double fitness;

    public Chromosome(List<Vuelo> route) {
        this.route = new ArrayList<>(route);
    }

    // Evaluación con todas las reglas que ya cumple ACO
    public void evaluate(Pedido pedido, Map<String, Aeropuerto> aeropuertos, Map<Integer, Integer> capRest) {
        double horasTotales = 0.0;
        double penalizacion = 0.0;

        if (route.isEmpty()) {
            this.fitness = 0;
            return;
        }

        Aeropuerto destinoAp = aeropuertos.get(pedido.destino);
        if (destinoAp == null) {
            this.fitness = 0;
            return;
        }

        String actual = pedido.hubOrigen;
        Set<String> visitados = new HashSet<>();
        visitados.add(actual);

        int horaActual = pedido.hora * 60 + pedido.minuto; // minuto del día
        int diaActual = pedido.dia;

        for (Vuelo v : route) {
            // verificar conectividad
            if (!v.origen.equals(actual)) {
                penalizacion += 10.0; // salto inválido
            }
            actual = v.destino;

            // verificar conexión mínima de 1h
            if (v.salidaMin < horaActual + 60) {
                penalizacion += 5.0;
            }

            // actualizar tiempo total
            horasTotales += v.horasDuracion;
            horaActual = v.llegadaMin;

            // capacidad del vuelo
            int cap = capRest.getOrDefault(v.id, v.capacidad);
            if (cap <= 0) penalizacion += 10.0;

            // evitar ciclos
            if (visitados.contains(actual)) penalizacion += 2.0;
            visitados.add(actual);
        }

        // +2h por aduanas en destino
        horasTotales += 2.0;

        // SLA dinámico
        String hubRegion = aeropuertos.get(pedido.hubOrigen).continente;
        String destRegion = destinoAp.continente;
        double sla = hubRegion.equals(destRegion) ? 48.0 : 72.0;

        if (horasTotales > sla) penalizacion += (horasTotales - sla);

        // penalización almacén destino lleno
        if (destinoAp.capacidad <= 0) penalizacion += 10.0;

        this.fitness = Math.max(0, 100.0 / (1.0 + penalizacion));
    }

    @Override
    public String toString() {
        return "Ruta=" + route + " | Fitness=" + String.format("%.2f", fitness);
    }
}
