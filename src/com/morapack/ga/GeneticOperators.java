package com.morapack.ga;
import java.util.*;
public class GeneticOperators {
    private Random rand = new Random();

    // Selección por torneo
    public Chromosome tournamentSelection(List<Chromosome> population) {
        Chromosome a = population.get(rand.nextInt(population.size()));
        Chromosome b = population.get(rand.nextInt(population.size()));
        return (a.fitness > b.fitness) ? a : b;
    }

    // Crossover de un punto (con objetos Vuelo)
    public Chromosome crossover(Chromosome p1, Chromosome p2,
                                Pedido pedido, Map<String, Aeropuerto> aeropuertos,
                                Map<Integer, Integer> capRest) {
        int point = rand.nextInt(p1.route.size());
        List<Vuelo> childRoute = new ArrayList<>(p1.route.subList(0, point));
        for (Vuelo v : p2.route) {
            if (!childRoute.contains(v)) {
                childRoute.add(v);
            }
        }
        Chromosome child = new Chromosome(childRoute);
        child.evaluate(pedido, aeropuertos, capRest);
        return child;
    }

    // Mutación: intercambiar vuelos
    public void mutate(Chromosome c, double mutationRate,
                       Pedido pedido, Map<String, Aeropuerto> aeropuertos,
                       Map<Integer, Integer> capRest) {
        if (rand.nextDouble() < mutationRate && c.route.size() > 1) {
            int i = rand.nextInt(c.route.size());
            int j = rand.nextInt(c.route.size());
            Collections.swap(c.route, i, j);
            c.evaluate(pedido, aeropuertos, capRest);
        }
    }
}

