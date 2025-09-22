package com.morapack.ga;
import java.util.*;
import java.io.*;
public class GeneticAlgorithm {
    int populationSize = 30;
    double crossoverRate = 0.8;
    double mutationRate = 0.2;
    int maxGenerations = 50;

    /*public void run(List<String> vuelosDisponibles, int routeLength) {
        Population population = new Population(populationSize);
        population.initialize(vuelosDisponibles, routeLength);

        Chromosome best = population.getBestChromosome();

        //CSV Writer
        try (PrintWriter writer = new PrintWriter(new File("evolution_log.csv"))) {
            writer.println("Gen,BestRoute,BestFitness,Avg,Worst");

            for (int gen = 0; gen <= maxGenerations; gen++) {
                List<Chromosome> newGen = new ArrayList<>();

                while (newGen.size() < populationSize) {
                    Chromosome p1 = population.tournamentSelection(population.getChromosomes());
                    Chromosome p2 = population.tournamentSelection(population.getChromosomes());

                    Chromosome child;
                    if (Math.random() < crossoverRate) {
                        child = population.crossover(p1, p2);
                    } else {
                        child = new Chromosome(new ArrayList<>(p1.route));
                        child.evaluate();
                    }

                    population.mutate(child, mutationRate);
                    newGen.add(child);
                }

                population = new Population(populationSize);
                population.getChromosomes().addAll(newGen);

                Chromosome bestNow = population.getBestChromosome();
                if (bestNow.fitness > best.fitness) {
                    best = bestNow;
                }

                // KPIs
                double avg = population.getChromosomes().stream().mapToDouble(c -> c.fitness).average().orElse(0);
                double worst = population.getChromosomes().stream().mapToDouble(c -> c.fitness).min().orElse(0);

                // Consola con detalle
                System.out.printf("Gen %d -> Best: %s | Avg: %.2f | Worst: %.2f%n",
                        gen, bestNow.toString(), avg, worst);

                // CSV export
                writer.printf("%d,\"%s\",%.2f,%.2f,%.2f%n",
                        gen, bestNow.route.toString(), bestNow.fitness, avg, worst);

                // Visualización ASCII en consola (barra de mejor fitness)
                int barLength = (int) bestNow.fitness;
                String bar = "#".repeat(Math.max(0, barLength / 2)); // escala: 1 char = 2 puntos
                System.out.printf("Fitness bar [%s] %.2f%n", bar, bestNow.fitness);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir CSV: " + e.getMessage());
        }

        System.out.println("\n=== Mejor solución encontrada ===");
        System.out.println(best);
    }*/
    public void runWithPedidos(List<String> vuelosDisponibles, int routeLength, List<Pedido> pedidos) {
        String outputFile = "plan_asignacion.csv";
        int totalSolicitados = 0, totalAsignados = 0, totalPendientes = 0;
        int pedidosConAsignacion = 0;

        try (PrintWriter writer = new PrintWriter(new File(outputFile))) {
            writer.println("pedido_id,dia,hub_origen,destino,ruta,paquetes_asignados,paquetes_pendientes");

            for (Pedido p : pedidos) {
                totalSolicitados += p.cantidad;
                int restantes = p.cantidad;
                int asignadosTotal = 0;

                // === Filtrar vuelos solo del día del pedido ===
                List<String> vuelosDelDia = vuelosDisponibles.stream()
                        .filter(v -> v.endsWith("@D" + String.format("%02d", p.dia)))
                        .toList();

                if (vuelosDelDia.isEmpty()) {
                    totalPendientes += restantes;
                    continue;
                }

                // === Mientras queden paquetes por asignar ===
                while (restantes > 0) {
                    Population population = new Population(populationSize);
                    population.initialize(vuelosDelDia, routeLength);

                    Chromosome best = population.getBestChromosome();
                    String ruta = String.join(" | ", best.route);

                    // Capacidad mínima de la ruta
                    int capacidadRuta = Integer.MAX_VALUE;
                    for (String vuelo : best.route) {
                        int cap = DataLoader.capacidadVuelo.getOrDefault(vuelo, 200);
                        capacidadRuta = Math.min(capacidadRuta, cap);
                    }

                    if (capacidadRuta <= 0) break;

                    // Asignar fragmento del pedido
                    int asignados = Math.min(restantes, capacidadRuta);
                    restantes -= asignados;
                    asignadosTotal += asignados;

                    if (asignados > 0) {
                        pedidosConAsignacion++;
                        writer.printf("%s,%d,%s,%s,\"%s\",%d,%d%n",
                                p.id, p.dia, p.hubOrigen, p.destino, ruta, asignados, restantes);
                    }

                    // Seguridad: evitar bucles infinitos
                    if (asignados == 0) break;
                }

                totalAsignados += asignadosTotal;
                totalPendientes += restantes;
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo plan_asignacion.csv: " + e.getMessage());
        }

        // === Resumen en consola ===
        System.out.println("\n=== Resumen de planificación (GA) ===");
        System.out.println("Órdenes totales: " + pedidos.size());
        System.out.println("Órdenes con asignación: " + pedidosConAsignacion);
        System.out.println("Paquetes solicitados: " + totalSolicitados);
        System.out.println("Paquetes asignados: " + totalAsignados);
        System.out.println("Paquetes pendientes: " + totalPendientes);
        System.out.println("Plan escrito en: " + new File(outputFile).getAbsolutePath());
    }




}