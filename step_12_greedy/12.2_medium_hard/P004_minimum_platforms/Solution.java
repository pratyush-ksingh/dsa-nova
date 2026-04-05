/**
 * Problem: Minimum Platforms (GeeksForGeeks)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given arrival and departure times of trains, find the minimum number
 * of platforms needed so no train has to wait.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (Check All Trains Against Each Other)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteForce {
    public static int solve(int[] arrival, int[] departure) {
        int n = arrival.length;
        int maxPlatforms = 0;

        for (int i = 0; i < n; i++) {
            int count = 1;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if (arrival[j] <= departure[i] && departure[j] >= arrival[i]) {
                        count++;
                    }
                }
            }
            maxPlatforms = Math.max(maxPlatforms, count);
        }
        return maxPlatforms;
    }
}

// ============================================================
// Approach 2: Optimal (Sort + Two Pointer Sweep)
// Time: O(n log n) | Space: O(1) if in-place sort
// ============================================================
class Optimal {
    public static int solve(int[] arrival, int[] departure) {
        int n = arrival.length;
        int[] arr = arrival.clone();
        int[] dep = departure.clone();

        Arrays.sort(arr);
        Arrays.sort(dep);

        int platforms = 0, maxPlatforms = 0;
        int i = 0, j = 0;

        while (i < n) {
            if (arr[i] <= dep[j]) {
                platforms++;
                maxPlatforms = Math.max(maxPlatforms, platforms);
                i++;
            } else {
                platforms--;
                j++;
            }
        }
        return maxPlatforms;
    }
}

// ============================================================
// Approach 3: Best (Event-Based Sweep Line)
// Time: O(n log n) | Space: O(n)
// ============================================================
class Best {
    public static int solve(int[] arrival, int[] departure) {
        int n = arrival.length;
        int[][] events = new int[2 * n][2];

        for (int i = 0; i < n; i++) {
            events[2 * i] = new int[]{arrival[i], 1};      // arrival
            events[2 * i + 1] = new int[]{departure[i], -1}; // departure
        }

        // Sort by time; if same time, process departures (-1) before arrivals (1)
        // Actually for this problem: if arrival == departure of another,
        // they DO overlap, so process arrival first (+1 before -1)
        Arrays.sort(events, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return a[1] - b[1]; // departure (-1) before arrival (1) at same time
                                // BUT the standard GFG problem says arr[i] <= dep[j]
                                // means overlap, so we process arrival first.
                                // Using arrival first: change to b[1] - a[1]
        });

        // For GFG standard: if train arrives at time T and another departs at T,
        // they need separate platforms. So arrival should be processed first.
        // Re-sort: at same time, arrival (+1) before departure (-1).
        Arrays.sort(events, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            return b[1] - a[1]; // +1 (arrival) before -1 (departure)
        });

        int platforms = 0, maxPlatforms = 0;
        for (int[] event : events) {
            platforms += event[1];
            maxPlatforms = Math.max(maxPlatforms, platforms);
        }
        return maxPlatforms;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Minimum Platforms ===\n");

        Object[][] tests = {
            {new int[]{900, 940, 950, 1100, 1500, 1800},
             new int[]{910, 1200, 1120, 1130, 1900, 2000}, 3},
            {new int[]{900, 1100, 1235},
             new int[]{1000, 1200, 1240}, 1},
            {new int[]{100, 200, 300, 400},
             new int[]{150, 250, 350, 450}, 1},
            {new int[]{100, 100, 100},
             new int[]{200, 200, 200}, 3},
            {new int[]{900},
             new int[]{1000}, 1},
        };

        for (Object[] t : tests) {
            int[] arrival = (int[]) t[0];
            int[] departure = (int[]) t[1];
            int expected = (int) t[2];

            int b = BruteForce.solve(arrival.clone(), departure.clone());
            int o = Optimal.solve(arrival.clone(), departure.clone());
            int h = Best.solve(arrival.clone(), departure.clone());
            boolean pass = b == expected && o == expected && h == expected;

            System.out.println("Input:    arr=" + Arrays.toString(arrival)
                             + ", dep=" + Arrays.toString(departure));
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + h);
            System.out.println("Expected: " + expected);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
