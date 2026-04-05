import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Check every pair (i, j) with i < j and count those where A[i]^A[j] == B
// ============================================================
class BruteForce {
    public static int solve(int[] A, int B) {
        int count = 0;
        int n = A.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((A[i] ^ A[j]) == B) count++;
            }
        }
        return count;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - HashSet
// Time: O(n)  |  Space: O(n)
// For each element x, if x^B is already in the set, we found a pair.
// Use a visited set to avoid counting duplicates.
// ============================================================
class Optimal {
    public static int solve(int[] A, int B) {
        Set<Integer> seen = new HashSet<>();
        int count = 0;
        for (int x : A) {
            int partner = x ^ B;
            if (seen.contains(partner)) count++;
            seen.add(x);
        }
        return count;
    }
}

// ============================================================
// APPROACH 3: BEST - HashMap for frequency (handles duplicates correctly)
// Time: O(n)  |  Space: O(n)
// When B == 0 we need pairs of equal elements: freq*(freq-1)/2
// Otherwise same as Optimal but uses frequency map.
// ============================================================
class Best {
    public static int solve(int[] A, int B) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : A) freq.merge(x, 1, Integer::sum);

        if (B == 0) {
            long count = 0;
            for (int f : freq.values()) count += (long) f * (f - 1) / 2;
            return (int) count;
        }

        int count = 0;
        Set<Integer> visited = new HashSet<>();
        for (int x : freq.keySet()) {
            int partner = x ^ B;
            if (!visited.contains(x) && freq.containsKey(partner)) {
                count += freq.get(x) * freq.get(partner);
                visited.add(x);
                visited.add(partner);
            }
        }
        return count;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Pairs With Given XOR ===");

        int[][] tests = {{6, 1, 3, 5, 2}, {4, 3, 7}};
        int[] xorVals = {4, 7};

        for (int t = 0; t < tests.length; t++) {
            int[] A = tests[t];
            int B = xorVals[t];
            System.out.println("Array: " + Arrays.toString(A) + "  XOR target: " + B);
            System.out.println("  Brute:   " + BruteForce.solve(A, B));
            System.out.println("  Optimal: " + Optimal.solve(A, B));
            System.out.println("  Best:    " + Best.solve(A, B));
        }

        // Edge: B == 0 (pairs of equal elements)
        int[] dup = {1, 1, 2, 2, 3};
        System.out.println("Array: " + Arrays.toString(dup) + "  XOR target: 0");
        System.out.println("  Brute:   " + BruteForce.solve(dup, 0));
        System.out.println("  Best:    " + Best.solve(dup, 0));
    }
}
