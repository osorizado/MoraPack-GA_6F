package com.morapack.ga;

import java.util.*;

public class Population {
    private List<Chromosome> chromosomes;
    private int populationSize;
    private Random rand = new Random();

    public Population(int populationSize) {
        this.populationSize = populationSize;
        this.chromosomes = new ArrayList<>();
    }

    public Population() {
        this.chromosomes = new ArrayList<>();
    }

    // Inicializar población aleatoria
    public void initialize(List<String> vuelosDisponibles, int routeLength) {
        for (int i = 0; i < populationSize; i++) {
            List<String> shuffled = new ArrayList<>(vuelosDisponibles);
            Collections.shuffle(shuffled, rand);

            List<String> route = new ArrayList<>(shuffled.subList(0, routeLength));
            Chromosome c = new Chromosome(route);
            c.evaluate();
            chromosomes.add(c);
        }
    }

    // Métodos que faltaban
    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public Chromosome getBestChromosome() {
        return Collections.max(chromosomes, Comparator.comparingDouble(c -> c.fitness));
    }

    // ===== Operadores genéticos =====

    public Chromosome tournamentSelection(List<Chromosome> population) {
        Chromosome a = population.get(rand.nextInt(population.size()));
        Chromosome b = population.get(rand.nextInt(population.size()));
        return (a.fitness > b.fitness) ? a : b;
    }

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

    public void mutate(Chromosome c, double mutationRate) {
        if (rand.nextDouble() < mutationRate) {
            int i = rand.nextInt(c.route.size());
            int j = rand.nextInt(c.route.size());
            Collections.swap(c.route, i, j);
            c.evaluate();
        }
    }
}
