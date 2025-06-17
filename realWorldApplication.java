/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa2tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class realWorldApplication {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // Welcome message
        System.out.println("\n=============================================");
        System.out.println("  Welcome to the J&T Delivery Route Optimizer");
        System.out.println("=============================================\n");

        // Step 1: Ask for number of delivery locations (excluding warehouse)
        System.out.print("Enter number of delivery locations for today: ");
        int n = input.nextInt();

        System.out.println("\nSystem Status: Loading delivery locations...");

        // Step 2: Get coordinates for each delivery location
        City[] cities = new City[n + 1];
        cities[0] = new City(0, 0); // Warehouse at (0, 0)

        for (int i = 1; i <= n; i++) {
            System.out.printf("Enter x and y coordinates for delivery location %d (separated by space): ", i);
            double x = input.nextDouble();
            double y = input.nextDouble();
            cities[i] = new City(x, y);
        }

        System.out.println("\nSystem Status: Calculating optimal route...\n");

        // Step 3: Solve TSP starting from warehouse (index 0)
        long startTime = System.nanoTime();
        List<Integer> tour = tspNearestNeighbor(cities, 0);
        long endTime = System.nanoTime();

        // Step 4: Show results
        double distance = totalDistance(tour, cities);
        double timeMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("Optimal Delivery Route:");
        for (int i = 0; i < tour.size(); i++) {
            int idx = tour.get(i);
            if (i == tour.size() - 1) {
                System.out.print("Warehouse (Return)");
            } else if (idx == 0) {
                System.out.print("Warehouse -> ");
            } else {
                System.out.print("Location " + idx + " -> ");
            }
        }
        System.out.println();

        System.out.printf("\nTotal Distance: %.2f km\n", distance);
        System.out.printf("Execution Time: %.3f ms\n", timeMs);

        input.close();
    }

    static class City {
        double x, y;
        City(double x, double y) {
            this.x = x;
            this.y = y;
        }
        double distanceTo(City other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    public static List<Integer> tspNearestNeighbor(City[] cities, int start) {
        int n = cities.length;
        boolean[] visited = new boolean[n];
        List<Integer> tour = new ArrayList<>();
        int current = start;
        tour.add(current);
        visited[current] = true;

        for (int i = 1; i < n; i++) {
            int next = -1;
            double minDist = Double.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!visited[j]) {
                    double d = cities[current].distanceTo(cities[j]);
                    if (d < minDist) {
                        minDist = d;
                        next = j;
                    }
                }
            }
            tour.add(next);
            visited[next] = true;
            current = next;
        }
        tour.add(start); // Return to warehouse
        return tour;
    }

    public static double totalDistance(List<Integer> tour, City[] cities) {
        double total = 0;
        for (int i = 0; i < tour.size() - 1; i++) {
            City from = cities[tour.get(i)];
            City to = cities[tour.get(i + 1)];
            total += from.distanceTo(to);
        }
        return total;
    }
}
