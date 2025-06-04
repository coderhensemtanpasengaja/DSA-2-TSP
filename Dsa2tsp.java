/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.dsa2tsp;

/**
 *
 * @author Zulharizi
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Dsa2tsp {
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

    public static City[] generateCities(int n) {
        City[] cities = new City[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            cities[i] = new City(rand.nextDouble() * 100, rand.nextDouble() * 100);
        }
        return cities;
    }

    public static void printCitiesAndDistances(City[] cities) {
        System.out.println("List of Cities (index: x, y):");
        for (int i = 0; i < cities.length; i++) {
            System.out.printf("City %d: (%.2f, %.2f)\n", i, cities[i].x, cities[i].y);
        }

        System.out.println("\nDistance Matrix:");
        for (int i = 0; i < cities.length; i++) {
            for (int j = 0; j < cities.length; j++) {
                double d = cities[i].distanceTo(cities[j]);
                System.out.printf("%8.2f ", d);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static List<Integer> tspBruteForce(City[] cities) {
        List<Integer> cityIndices = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            cityIndices.add(i);
        }

        double minDistance = Double.MAX_VALUE;
        List<Integer> bestTour = new ArrayList<>();

        for (List<Integer> perm : permutations(cityIndices)) {
            double dist = totalDistance(perm, cities);
            if (dist < minDistance) {
                minDistance = dist;
                bestTour = new ArrayList<>(perm);
            }
        }

        bestTour.add(bestTour.get(0));
        return bestTour;
    }

    public static List<Integer> tspNearestNeighbor(City[] cities) {
        int n = cities.length;
        boolean[] visited = new boolean[n];
        List<Integer> tour = new ArrayList<>();
        int current = 0;
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

        tour.add(tour.get(0));
        return tour;
    }

    public static double totalDistance(List<Integer> path, City[] cities) {
        double sum = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            sum += cities[path.get(i)].distanceTo(cities[path.get(i + 1)]);
        }
        return sum;
    }

    public static List<List<Integer>> permutations(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        permuteHelper(list, 0, result);
        return result;
    }

    private static void permuteHelper(List<Integer> list, int start, List<List<Integer>> result) {
        if (start == list.size() - 1) {
            result.add(new ArrayList<>(list));
        } else {
            for (int i = start; i < list.size(); i++) {
                Collections.swap(list, i, start);
                permuteHelper(list, start + 1, result);
                Collections.swap(list, i, start);
            }
        }
    }

    public static void printTour(String method, List<Integer> tour, double distance, double timeMs) {
        System.out.printf("%s Tour: %s\n", method, tour);
        System.out.printf("%s Distance: %.2f | Time: %.3f ms\n\n", method, distance, timeMs);
    }

    public static void main(String[] args) {
        int[] cityCounts = {5, 6, 7, 8, 10, 20};

        for (int n : cityCounts) {
            System.out.println("========== " + n + " Cities ==========");
            City[] cities = generateCities(n);
            printCitiesAndDistances(cities);

            if (n <= 10) {
                long start = System.nanoTime();
                List<Integer> bruteTour = tspBruteForce(cities);
                long end = System.nanoTime();
                double time = (end - start) / 1_000_000.0;
                double dist = totalDistance(bruteTour, cities);
                printTour("Brute-force", bruteTour, dist, time);
            }

            long startNN = System.nanoTime();
            List<Integer> nnTour = tspNearestNeighbor(cities);
            long endNN = System.nanoTime();
            double timeNN = (endNN - startNN) / 1_000_000.0;
            double distNN = totalDistance(nnTour, cities);
            printTour("Nearest Neighbor", nnTour, distNN, timeNN);
        }
    }
}

