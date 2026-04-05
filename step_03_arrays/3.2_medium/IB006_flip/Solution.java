import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(1)
// Try every possible contiguous flip window; count 1s after flip.
// Return [L, R] (1-indexed) that maximises 1s, empty if no 0s.
// ============================================================
class BruteForce {
    public static int[] solve(String A) {
        int n = A.length();
        // Count initial 1s
        int ones = 0;
        for (char c : A.toCharArray()) if (c == '1') ones++;

        int bestL = -1, bestR = -1, bestTotal = ones;

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                // Count gain: flip [i..j], each 0 becomes +1, each 1 becomes -1
                int gain = 0;
                for (int k = i; k <= j; k++) gain += (A.charAt(k) == '0') ? 1 : -1;
                if (ones + gain > bestTotal) {
                    bestTotal = ones + gain;
                    bestL = i + 1; bestR = j + 1;
                }
            }
        }
        return bestL == -1 ? new int[]{} : new int[]{bestL, bestR};
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Transform + Kadane's)
// Time: O(n)  |  Space: O(n)
// Transform: treat '0' as +1 and '1' as -1.
// Flipping [L,R] gains (sum of transformed values).
// Find max-sum subarray of transformed array using Kadane's.
// If max gain <= 0, no flip needed.
// ============================================================
class Optimal {
    public static int[] solve(String A) {
        int n = A.length();
        int[] t = new int[n];
        for (int i = 0; i < n; i++) t[i] = (A.charAt(i) == '0') ? 1 : -1;

        int maxGain = 0, curSum = 0;
        int start = 0, bestL = -1, bestR = -1, tempStart = 0;

        for (int i = 0; i < n; i++) {
            curSum += t[i];
            if (curSum > maxGain) {
                maxGain = curSum;
                bestL = tempStart + 1;
                bestR = i + 1;
            }
            if (curSum < 0) {
                curSum = 0;
                tempStart = i + 1;
            }
        }
        return bestL == -1 ? new int[]{} : new int[]{bestL, bestR};
    }
}

// ============================================================
// APPROACH 3: BEST (Kadane's in-place, no extra array)
// Time: O(n)  |  Space: O(1)
// Same transform but done on-the-fly without allocating array.
// Tie-breaking: prefer leftmost, smallest window per problem spec.
// ============================================================
class Best {
    public static int[] solve(String A) {
        int n = A.length();
        int maxGain = 0, curSum = 0, tempStart = 0;
        int bestL = -1, bestR = -1;

        for (int i = 0; i < n; i++) {
            int val = (A.charAt(i) == '0') ? 1 : -1;
            curSum += val;
            if (curSum > maxGain) {
                maxGain = curSum;
                bestL = tempStart + 1;
                bestR = i + 1;
            }
            if (curSum < 0) {
                curSum = 0;
                tempStart = i + 1;
            }
        }
        return bestL == -1 ? new int[]{} : new int[]{bestL, bestR};
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Flip Binary String ===");
        String[] tests  = {"010", "111", "0000", "1111", "010101"};
        // Expected flips: [1,1], [], [1,4], [], [1,1] or [2,2] or [3,3] etc.
        for (String t : tests) {
            int[] bf   = BruteForce.solve(t);
            int[] op   = Optimal.solve(t);
            int[] best = Best.solve(t);
            System.out.printf("'%s' => BF=%s  OPT=%s  BEST=%s%n",
                t, Arrays.toString(bf), Arrays.toString(op), Arrays.toString(best));
        }
    }
}
