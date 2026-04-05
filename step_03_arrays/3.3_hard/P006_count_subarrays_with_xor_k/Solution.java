import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Check every subarray [i..j], compute XOR, count those equal to k
// ============================================================
class BruteForce {
    public static int solve(int[] A, int k) {
        int n = A.length, count = 0;
        for (int i = 0; i < n; i++) {
            int xor = 0;
            for (int j = i; j < n; j++) {
                xor ^= A[j];
                if (xor == k) count++;
            }
        }
        return count;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Prefix XOR + HashMap
// Time: O(n)  |  Space: O(n)
// Key insight: XOR(i..j) = prefXOR[j] ^ prefXOR[i-1]
// We want prefXOR[j] ^ k in the map. Equivalent to sum-k subarray pattern.
// prefXOR[j] ^ prefXOR[i-1] = k  =>  prefXOR[i-1] = prefXOR[j] ^ k
// ============================================================
class Optimal {
    public static int solve(int[] A, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1); // empty prefix
        int xor = 0, count = 0;
        for (int x : A) {
            xor ^= x;
            count += freq.getOrDefault(xor ^ k, 0);
            freq.merge(xor, 1, Integer::sum);
        }
        return count;
    }
}

// ============================================================
// APPROACH 3: BEST - Same as Optimal, slight code cleanup
// Time: O(n)  |  Space: O(n)
// Using array-based map when values are bounded (generalizes with HashMap)
// ============================================================
class Best {
    public static int solve(int[] A, int k) {
        // Same O(n) HashMap approach — this IS the optimal solution.
        // "Best" here documents the clean single-pass implementation.
        Map<Integer, Integer> prefCount = new HashMap<>();
        prefCount.put(0, 1);
        int prefXOR = 0, count = 0;
        for (int num : A) {
            prefXOR ^= num;
            // We need prefXOR ^ k to have appeared before
            count += prefCount.getOrDefault(prefXOR ^ k, 0);
            prefCount.merge(prefXOR, 1, Integer::sum);
        }
        return count;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Count Subarrays with XOR K ===");

        int[][] arrays = {{4, 2, 2, 6, 4}, {5, 6, 7, 8, 9}};
        int[] ks = {6, 5};

        for (int t = 0; t < arrays.length; t++) {
            int[] A = arrays[t];
            int k = ks[t];
            System.out.printf("A=%s, k=%d => Brute=%d, Optimal=%d, Best=%d%n",
                Arrays.toString(A), k,
                BruteForce.solve(A, k),
                Optimal.solve(A, k),
                Best.solve(A, k));
        }
    }
}
