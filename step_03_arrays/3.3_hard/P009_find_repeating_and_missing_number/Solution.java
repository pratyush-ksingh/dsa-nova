import java.util.*;

/**
 * Problem: Find Repeating and Missing Number
 * Difficulty: HARD | XP: 50
 *
 * Given an array of n integers in range [1, n] where one number appears
 * twice (repeating) and one is missing, find both.
 * Return [repeating, missing].
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // For each value 1..n, count occurrences. O(n) per value.
    // ============================================================
    public static int[] bruteForce(int[] A) {
        int n = A.length;
        int repeating = -1, missing = -1;
        for (int val = 1; val <= n; val++) {
            int count = 0;
            for (int x : A) if (x == val) count++;
            if (count == 2) repeating = val;
            else if (count == 0) missing = val;
        }
        return new int[]{repeating, missing};
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Math (Sum and Sum of Squares)
    // Time: O(n)  |  Space: O(1)
    // Let rep = repeating, mis = missing.
    //   S_actual - S_expected = rep - mis             ... (1)
    //   S2_actual - S2_expected = rep^2 - mis^2       ... (2)
    //   (2)/(1) = rep + mis                            ... (3)
    // From (1) and (3): rep = ((rep-mis) + (rep+mis)) / 2
    //                   mis = rep - (rep - mis)
    // Use long to avoid overflow.
    // ============================================================
    public static int[] optimal(int[] A) {
        long n = A.length;
        long sumActual = 0, sum2Actual = 0;
        for (int x : A) {
            sumActual += x;
            sum2Actual += (long) x * x;
        }
        long sumExpected  = n * (n + 1) / 2;
        long sum2Expected = n * (n + 1) * (2 * n + 1) / 6;

        long diff  = sumActual  - sumExpected;   // rep - mis
        long diff2 = sum2Actual - sum2Expected;  // rep^2 - mis^2 = (rep-mis)(rep+mis)

        long sumRepMis = diff2 / diff;           // rep + mis
        long rep = (diff + sumRepMis) / 2;
        long mis = sumRepMis - rep;
        return new int[]{(int) rep, (int) mis};
    }

    // ============================================================
    // APPROACH 3: BEST — XOR Approach
    // Time: O(n)  |  Space: O(1)
    // XOR all array elements with 1..n. Result = rep XOR mis.
    // Find a set bit (they differ here), partition elements into
    // two groups, XOR each group separately to isolate rep and mis.
    // Then verify which is repeating by scanning once more.
    // ============================================================
    public static int[] best(int[] A) {
        int n = A.length;
        int xorAll = 0;
        for (int x : A) xorAll ^= x;
        for (int i = 1; i <= n; i++) xorAll ^= i;
        // xorAll = rep XOR mis

        // Find the rightmost set bit
        int setBit = xorAll & (-xorAll);

        int x = 0, y = 0;
        for (int val : A) {
            if ((val & setBit) != 0) x ^= val;
            else y ^= val;
        }
        for (int i = 1; i <= n; i++) {
            if ((i & setBit) != 0) x ^= i;
            else y ^= i;
        }

        // Determine which of x, y is repeating
        for (int val : A) {
            if (val == x) return new int[]{x, y};
        }
        return new int[]{y, x};
    }

    public static void main(String[] args) {
        System.out.println("=== Find Repeating and Missing Number ===");
        // A[i] in 1..n, one repeating, one missing
        int[][] tests = {
            {3, 1, 2, 5, 3},   // rep=3, mis=4
            {1, 1},            // rep=1, mis=2
            {2, 2},            // rep=2, mis=1
            {4, 3, 6, 2, 1, 1}, // rep=1, mis=5
        };
        for (int[] t : tests) {
            int[] bf = bruteForce(t);
            int[] op = optimal(t);
            int[] be = best(t);
            System.out.printf("A=%s -> Brute=%s, Optimal=%s, Best=%s%n",
                    Arrays.toString(t),
                    Arrays.toString(bf),
                    Arrays.toString(op),
                    Arrays.toString(be));
        }
    }
}
