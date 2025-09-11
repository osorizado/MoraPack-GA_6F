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

    // Crossover de un punto
    public Chromosome crossover(Chromosome p1, Chromosome p2) {
        int point = rand.nextInt(p1.route.size());
        List<String> childRoute = new ArrayList<>(p1.route.subList(0, point));
        for (String gene : p2.route) {
            if (!childRoute.contains(gene)) {
                childRoute.add(gene);
            }
        }
        Chromosome child = new Chromosome(childRoute);
        child.evaluate();
        return child;
    }

    // Mutación: intercambiar dos vuelos
    public void mutate(Chromosome c, double mutationRate) {
        if (rand.nextDouble() < mutationRate) {
            int i = rand.nextInt(c.route.size());
            int j = rand.nextInt(c.route.size());
            Collections.swap(c.route, i, j);
            c.evaluate();
        }
    }
}

