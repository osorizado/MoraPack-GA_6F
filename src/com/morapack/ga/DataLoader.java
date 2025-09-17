package com.morapack.ga;
import java.io.*;
import java.util.*;

public class DataLoader {
    public static List<String> vuelosDisponibles = new ArrayList<>();
    public static Map<String, Double> tiemposVuelo = new HashMap<>();
    public static Map<String, Integer> capacidadVuelo = new HashMap<>();
    // ✅ Ahora
    public static List<Pedido> pedidos = new ArrayList<>();

    static String[] hubs = {"SPIM", "EBCI", "UBBB"};
    public static void loadVuelos(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue; // prevenir errores

                String origen = parts[0].trim();
                String destino = parts[1].trim();
                String timeStr = parts[2].trim();

                double tiempo = 0.0;
                try {
                    if (timeStr.contains(":")) {
                        String[] hm = timeStr.split(":");
                        int horas = Integer.parseInt(hm[0].trim().replaceAll("[^0-9]", ""));   // solo dígitos
                        int minutos = Integer.parseInt(hm[1].trim().replaceAll("[^0-9]", "")); // solo dígitos
                        tiempo = horas + minutos / 60.0;
                    } else {
                        tiempo = Double.parseDouble(timeStr.replaceAll("[^0-9.]", "")); // también limpiar basura
                    }
                } catch (NumberFormatException e) {
                    System.err.println("⚠️ Error parseando tiempo en línea: " + line);
                    continue; // saltar esta línea y seguir
                }

                int capacidad = Integer.parseInt(parts[3].trim().replaceAll("[^0-9]", ""));

                String vuelo = origen + "-" + destino;

                Chromosome.tiemposVuelo.put(vuelo, tiempo);
                Chromosome.capacidadVuelo.put(vuelo, capacidad);
                vuelosDisponibles.add(vuelo);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo vuelos: " + e.getMessage());
        }
    }



    // Leer pedidos.txt: formato -> origen,destino,cantidad
    public static void loadPedidos(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Random rand = new Random();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;
                String id = parts[0].trim();
                String destino = parts[1].trim();
                int cantidad = Integer.parseInt(parts[2].trim());

                // Asignar un hub origen aleatorio
                String hubOrigen = hubs[rand.nextInt(hubs.length)];

                pedidos.add(new Pedido(id, destino, cantidad, hubOrigen));
            }
        } catch (IOException e) {
            System.err.println("Error cargando pedidos: " + e.getMessage());
        }
    }

    // Leer aeropuertos.txt (opcional)
    public static List<String> loadAeropuertos(String file) {
        List<String> aeropuertos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                aeropuertos.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error cargando aeropuertos: " + e.getMessage());
        }
        return aeropuertos;
    }
}
