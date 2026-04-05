/**
 * Problem: Check for Prime
 * Difficulty: EASY | XP: 10
 *
 * Determine whether N is a prime number.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check 2 to N-1
    // Time: O(N)  |  Space: O(1)
    // Try every candidate divisor from 2 to N-1.
    // ============================================================
    static class BruteForce {
        public static boolean isPrime(int n) {
            if (n <= 1) return false;
            for (int i = 2; i < n; i++) {
                if (n % i == 0) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Check up to sqrt(N)
    // Time: O(sqrt(N))  |  Space: O(1)
    // If N has a factor, one of the pair is <= sqrt(N).
    // ============================================================
    static class Optimal {
        public static boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            for (int i = 2; (long) i * i <= n; i++) {
                if (n % i == 0) return false;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- sqrt(N) with 6k +/- 1 Skip
    // Time: O(sqrt(N)) with ~3x constant improvement  |  Space: O(1)
    // After checking 2 and 3, only check i and i+2 stepping by 6.
    // ============================================================
    static class Best {
        public static boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;

            for (int i = 5; (long) i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return false;
            }
            return true;
        }
    }

    // ============================================================
    // TESTS
    // ============================================================
    public static void main(String[] args) {
        int[] testCases =    {0, 1, 2, 3, 4, 7, 12, 13, 97, 100, 9973};
        boolean[] expected = {false, false, true, true, false, true, false, true, true, false, true};

        System.out.println("=== Check for Prime ===\n");
        for (int i = 0; i < testCases.length; i++) {
            int n = testCases[i];
            boolean b = BruteForce.isPrime(n);
            boolean o = Optimal.isPrime(n);
            boolean bt = Best.isPrime(n);

            String status = (b == expected[i] && o == expected[i] && bt == expected[i]) ? "PASS" : "FAIL";
            System.out.printf("[%s] N=%-10d | Brute=%-5b | Optimal=%-5b | Best=%-5b | Expected=%-5b%n",
                    status, n, b, o, bt, expected[i]);
        }
    }
}
