/**
 * Problem: Page Faults in LRU
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a sequence of page references and a cache of fixed capacity,
 * simulate LRU page replacement and return the total number of page faults.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Brute Force (LinkedList as ordered cache, linear search)
// Time: O(n * c) | Space: O(c)
// ============================================================
class BruteForce {
    public static int solve(int[] pages, int capacity) {
        LinkedList<Integer> cache = new LinkedList<>();
        int faults = 0;

        for (int page : pages) {
            if (cache.contains(page)) {
                // Hit: move to front (most recently used)
                cache.remove((Integer) page);
                cache.addFirst(page);
            } else {
                // Miss: page fault
                faults++;
                if (cache.size() == capacity) {
                    cache.removeLast(); // evict LRU
                }
                cache.addFirst(page);
            }
        }
        return faults;
    }
}

// ============================================================
// Approach 2: Optimal (HashSet + Deque for O(1) lookup)
// Time: O(n * c) worst case due to deque remove, O(n) for lookup | Space: O(c)
// ============================================================
class Optimal {
    public static int solve(int[] pages, int capacity) {
        Set<Integer> cacheSet = new HashSet<>();
        Deque<Integer> cacheDeque = new ArrayDeque<>();
        int faults = 0;

        for (int page : pages) {
            if (cacheSet.contains(page)) {
                // Hit: move to front
                cacheDeque.remove(page); // O(c) but avoids O(c) search separately
                cacheDeque.addFirst(page);
            } else {
                // Miss: page fault
                faults++;
                if (cacheDeque.size() == capacity) {
                    int lru = cacheDeque.removeLast();
                    cacheSet.remove(lru);
                }
                cacheDeque.addFirst(page);
                cacheSet.add(page);
            }
        }
        return faults;
    }
}

// ============================================================
// Approach 3: Best (LinkedHashMap — true O(1) per operation)
// Time: O(n) | Space: O(c)
// ============================================================
class Best {
    public static int solve(int[] pages, int capacity) {
        // LinkedHashMap with accessOrder=true: accessing a key moves it to the end
        // (most recently used). The first entry is always the LRU.
        LinkedHashMap<Integer, Boolean> cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Boolean> eldest) {
                return false; // we manage eviction manually to count faults
            }
        };

        int faults = 0;
        for (int page : pages) {
            if (cache.containsKey(page)) {
                // Hit: accessing the key automatically moves it to end in accessOrder mode
                cache.get(page);
            } else {
                // Miss: page fault
                faults++;
                if (cache.size() == capacity) {
                    // Remove LRU = first entry in accessOrder LinkedHashMap
                    int lru = cache.entrySet().iterator().next().getKey();
                    cache.remove(lru);
                }
                cache.put(page, true);
            }
        }
        return faults;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Page Faults in LRU ===\n");

        int[][] pagesArr = {
            {7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2},
            {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5},
            {1, 2, 3, 1, 2, 3, 1, 2, 3},
            {1, 2, 1, 2, 1},
            {1},
        };
        int[] capacities = {4, 3, 3, 2, 1};
        int[] expecteds   = {6, 10, 3, 2, 1};

        for (int t = 0; t < pagesArr.length; t++) {
            int b = BruteForce.solve(pagesArr[t], capacities[t]);
            int o = Optimal.solve(pagesArr[t], capacities[t]);
            int h = Best.solve(pagesArr[t], capacities[t]);
            boolean pass = b == expecteds[t] && o == expecteds[t] && h == expecteds[t];

            System.out.println("Pages=" + Arrays.toString(pagesArr[t]) + " capacity=" + capacities[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + h);
            System.out.println("Expected: " + expecteds[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
