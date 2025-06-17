/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa2tsp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Zulharizi
 */
public class realWorldApplication {
    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);

        // Step 1: Ask for number of nodes
        System.out.print("Enter number of nodes (locations): ");
        int n = input.nextInt();

        // Step 2: Get coordinates for each node
        City[] cities = new City[n];
        for (int i = 0; i < n; i++) {
            System.out.printf("Enter x and y coordinates for location %d (separated by space): ", i);
            double x = input.nextDouble();
            double y = input.nextDouble();
            cities[i] = new City(x, y);
        }

        // Step 3: Ask for starting point
        int start;
        while (true) {
            System.out.print("Select starting location ( Location 0 to " + (n - 1) + "): ");
            start = input.nextInt();
            if (start >= 0 && start < n) break;
            System.out.println("Invalid index. Try again.");
        }

        // Step 4: Solve TSP using Nearest Neighbor
        long startTime = System.nanoTime();
        List<Integer> tour = tspNearestNeighbor(cities, start);
        long endTime = System.nanoTime();

        // Step 5: Show results
        double distance = totalDistance(tour, cities);
        double timeMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("\nNearest Neighbor Tour: " + tour);
        System.out.printf("Total Distance: %.2f\n", distance);
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
    
    public static List<Integer> tspNearestNeighbor (City[] cities, int start) {
        
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

        // Return to start
        tour.add(start);
        return tour;
    } 
    
    public static double totalDistance (List<Integer> path, City[] cities) {
        
        double sum = 0;
        
        for (int i = 0; i < path.size() - 1; i++) {
            
            City a = cities[path.get(i)];
            City b = cities[path.get(i + 1)];
            sum += a.distanceTo(b);
        }
        return sum;
    }
    
}
