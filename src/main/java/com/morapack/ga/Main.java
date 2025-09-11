// ✅ Main.java
package main.java.com.morapack.ga;
import com.morapack.ga.GeneticAlgorithm;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<String> vuelos = Arrays.asList(
                "SPIM-JFK", "SPIM-MAD", "SPIM-FRA", "SPIM-DXB",
                "EBCI-JFK", "UBBB-PEK",
                "JFK-LAX", "JFK-YYZ",
                "MAD-AMS", "MAD-FRA",
                "FRA-DXB", "FRA-AMS",
                "DXB-BOM", "PEK-HND", "AMS-LHR"
        );

        GeneticAlgorithm ga = new GeneticAlgorithm();

        // Escenario 1: Operación día a día
        System.out.println("\n=== Escenario 1: Día a día ===");
        ga.run(vuelos, 3);

        // Escenario 2: Simulación semanal
        System.out.println("\n=== Escenario 2: Simulación semanal ===");
        ga.run(vuelos, 5);

        // Escenario 3: Hasta colapso
        System.out.println("\n=== Escenario 3: Hasta colapso ===");
        ga.run(vuelos, 7);
    }
}