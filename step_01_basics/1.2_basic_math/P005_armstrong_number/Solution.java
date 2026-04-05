/**
 * Problem: Armstrong Number
 * Difficulty: EASY | XP: 10
 *
 * Check if N equals the sum of its digits each raised to the
 * power of the total digit count.
 * e.g. 153 = 1^3 + 5^3 + 3^3
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Two-Pass Division
    // Time: O(d)  |  Space: O(1)
    // First pass counts digits, second pass computes power sum.
    // ============================================================
    static class BruteForce {
        public static boolean isArmstrong(int n) {
            if (n < 0) return false;
            int original = n;

            // Pass 1: count digits
            int d = 0;
            int temp = n;
            if (temp == 0) d = 1;
            while (temp > 0) {
                d++;
                temp /= 10;
            }

            // Pass 2: compute power sum
            long sum = 0;
            temp = n;
            while (temp > 0) {
                int digit = temp % 10;
                sum += power(digit, d);
                temp /= 10;
            }

            return sum == original;
        }

        private static long power(int base, int exp) {
            long result = 1;
            for (int i = 0; i < exp; i++) {
                result *= base;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Log10 Digit Count + Single Pass
    // Time: O(d)  |  Space: O(1)
    // Use log10 to get digit count in O(1), then one extraction pass.
    // ============================================================
    static class Optimal {
        public static boolean isArmstrong(int n) {
            if (n < 0) return false;
            if (n == 0) return true;

            int d = (int) Math.log10(n) + 1;
            long sum = 0;
            int temp = n;

            while (temp > 0) {
                int digit = temp % 10;
                sum += Math.pow(digit, d);
                temp /= 10;
            }

            return sum == n;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- String-Based
    // Time: O(d)  |  Space: O(d)
    // Convert to string, iterate chars, compute power sum.
    // ============================================================
    static class Best {
        public static boolean isArmstrong(int n) {
            if (n < 0) return false;
            String s = String.valueOf(n);
            int d = s.length();
            long sum = 0;

            for (char ch : s.toCharArray()) {
                int digit = ch - '0';
                sum += Math.pow(digit, d);
            }

            return sum == n;
        }
    }

    // ============================================================
    // TESTS
    // ============================================================
    public static void main(String[] args) {
        int[] testCases = {0, 1, 5, 10, 123, 153, 370, 371, 407, 9474, 9475};
        boolean[] expected = {true, true, true, false, false, true, true, true, true, true, false};

        System.out.println("=== Armstrong Number ===\n");
        for (int i = 0; i < testCases.length; i++) {
            int n = testCases[i];
            boolean b = BruteForce.isArmstrong(n);
            boolean o = Optimal.isArmstrong(n);
            boolean bt = Best.isArmstrong(n);

            String status = (b == expected[i] && o == expected[i] && bt == expected[i]) ? "PASS" : "FAIL";
            System.out.printf("[%s] N=%-10d | Brute=%-5b | Optimal=%-5b | Best=%-5b | Expected=%-5b%n",
                    status, n, b, o, bt, expected[i]);
        }
    }
}
