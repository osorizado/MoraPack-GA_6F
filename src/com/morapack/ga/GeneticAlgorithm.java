package com.morapack.ga;
import java.util.*;
import java.io.*;
public class GeneticAlgorithm {
    int populationSize = 30;
    double crossoverRate = 0.8;
    double mutationRate = 0.2;
    int maxGenerations = 50;
    int routeLength = 5;

    public void run(List<Vuelo> vuelosDisponibles, List<Pedido> pedidos,
                    Map<String, Aeropuerto> aeropuertos) {

        Map<Integer,Integer> capRest = new HashMap<>();
        for (Vuelo v : vuelosDisponibles) capRest.put(v.id, v.capacidad);

        int totalSolicitados = 0, totalAsignados = 0, totalPendientes = 0;

        try (PrintWriter writer = new PrintWriter("plan_asignacion_GA.csv")) {
            writer.println("pedido_id,dia,hub_origen,destino,ruta,asignados,pendientes,fitness");

            for (Pedido p : pedidos) {
                totalSolicitados += p.cantidad;
                int restantes = p.cantidad;
                int asignadosTotal = 0;

                while (restantes > 0) {
                    Population pop = new Population(populationSize);
                    pop.initialize(vuelosDisponibles, routeLength, p, aeropuertos, capRest);

                    Chromosome best = pop.getBestChromosome();

                    // cuello de botella
                    int cuello = Integer.MAX_VALUE;
                    for (Vuelo v : best.route) {
                        cuello = Math.min(cuello, capRest.getOrDefault(v.id, v.capacidad));
                    }
                    if (cuello <= 0) break;

                    int asignados = Math.min(restantes, cuello);
                    restantes -= asignados;
                    asignadosTotal += asignados;

                    // actualizar capacidades
                    for (Vuelo v : best.route) {
                        capRest.put(v.id, capRest.get(v.id) - asignados);
                    }

                    writer.printf("%s,%d,%s,%s,\"%s\",%d,%d,%.2f%n",
                            p.id, p.dia, p.hubOrigen, p.destino,
                            best.route.toString(), asignados, restantes, best.fitness);

                    if (restantes == 0) break;
                }

                totalAsignados += asignadosTotal;
                totalPendientes += restantes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Resumen en consola
        System.out.println("\n=== Resumen GA ===");
        System.out.println("Pedidos totales: " + pedidos.size());
        System.out.println("Paquetes solicitados: " + totalSolicitados);
        System.out.println("Paquetes asignados: " + totalAsignados);
        System.out.println("Paquetes pendientes: " + totalPendientes);
    }
}