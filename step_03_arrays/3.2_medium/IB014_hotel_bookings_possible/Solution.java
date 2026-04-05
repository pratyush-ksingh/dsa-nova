/**
 * Problem: Hotel Bookings Possible
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given arrive[], depart[], and K rooms, determine if all hotel bookings
 * can be accommodated without exceeding K rooms at any point in time.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    /**
     * For each booking, count how many other bookings overlap with it.
     * Two bookings i and j overlap if arrive[i] <= depart[j] AND arrive[j] <= depart[i].
     * If any booking has more than K simultaneous overlapping bookings, return false.
     */
    static class BruteForce {
        public boolean hotel(int[] arrive, int[] depart, int K) {
            int n = arrive.length;
            for (int i = 0; i < n; i++) {
                int count = 0;
                for (int j = 0; j < n; j++) {
                    if (arrive[j] <= depart[i] && arrive[i] <= depart[j]) {
                        count++;
                    }
                }
                if (count > K) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Sweep Line (Combined Event List)
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Create +1 events for arrivals and -1 events for departures.
     * Sort all events by time, breaking ties by processing departures first
     * (so a departing guest frees a room before a new guest checks in on the same day).
     * Track current occupancy; return false if it ever exceeds K.
     */
    static class Optimal {
        public boolean hotel(int[] arrive, int[] depart, int K) {
            int n = arrive.length;
            int[][] events = new int[2 * n][2];
            for (int i = 0; i < n; i++) {
                events[i][0] = arrive[i];
                events[i][1] = 1;        // arrival
                events[n + i][0] = depart[i];
                events[n + i][1] = -1;   // departure
            }
            // Sort by time; on tie, departure (-1) < arrival (1) so depart first
            Arrays.sort(events, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

            int currentRooms = 0;
            for (int[] event : events) {
                currentRooms += event[1];
                if (currentRooms > K) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Two Sorted Arrays + Two Pointers
    // Time: O(n log n)  |  Space: O(1) extra (sorts in-place)
    // ============================================================
    /**
     * Sort arrive[] and depart[] independently. Use two pointers to
     * simulate the sweep without building an extra event array.
     * Advance the arrival pointer when the next event is an arrival;
     * otherwise advance the departure pointer to free a room.
     */
    static class Best {
        public boolean hotel(int[] arrive, int[] depart, int K) {
            int[] arr = arrive.clone();
            int[] dep = depart.clone();
            Arrays.sort(arr);
            Arrays.sort(dep);
            int n = arr.length;
            int currentRooms = 0;
            int i = 0, j = 0;
            while (i < n) {
                if (arr[i] <= dep[j]) {
                    currentRooms++;
                    if (currentRooms > K) return false;
                    i++;
                } else {
                    currentRooms--;
                    j++;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Hotel Bookings Possible ===");

        int[] arrive1 = {1, 3, 5};
        int[] depart1 = {2, 6, 8};
        int K1 = 1;
        System.out.println("Test 1: arrive=[1,3,5], depart=[2,6,8], K=1");
        System.out.println("Brute:   " + new BruteForce().hotel(arrive1, depart1, K1));   // false
        System.out.println("Optimal: " + new Optimal().hotel(arrive1, depart1, K1));       // false
        System.out.println("Best:    " + new Best().hotel(arrive1, depart1, K1));          // false

        int[] arrive2 = {1, 2, 3};
        int[] depart2 = {2, 3, 4};
        int K2 = 2;
        System.out.println("\nTest 2: arrive=[1,2,3], depart=[2,3,4], K=2");
        System.out.println("Brute:   " + new BruteForce().hotel(arrive2, depart2, K2));   // true
        System.out.println("Optimal: " + new Optimal().hotel(arrive2, depart2, K2));       // true
        System.out.println("Best:    " + new Best().hotel(arrive2, depart2, K2));          // true
    }
}
