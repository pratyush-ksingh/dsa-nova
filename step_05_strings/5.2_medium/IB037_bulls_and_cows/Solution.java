/**
 * Problem: Bulls and Cows
 * LeetCode 299 | Difficulty: MEDIUM | XP: 25
 *
 * Given secret and guess (same-length digit strings):
 *   Bulls = digits in the correct position.
 *   Cows  = correct digits in the wrong position (not already bulls).
 * Return the hint as "xAyB".
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  -  Two-pass with frequency arrays
    // Time: O(n)  |  Space: O(1)  (digit arrays size 10)
    // ============================================================
    static class BruteForce {
        /**
         * Pass 1: Identify bulls; collect non-bull digit counts for secret and guess.
         * Pass 2: Cows = sum of min(secretCount[d], guessCount[d]) for each digit d.
         */
        public String getHint(String secret, String guess) {
            int bulls = 0, cows = 0;
            int[] secretCount = new int[10];
            int[] guessCount  = new int[10];

            for (int i = 0; i < secret.length(); i++) {
                if (secret.charAt(i) == guess.charAt(i)) {
                    bulls++;
                } else {
                    secretCount[secret.charAt(i) - '0']++;
                    guessCount[guess.charAt(i)   - '0']++;
                }
            }

            for (int d = 0; d <= 9; d++) {
                cows += Math.min(secretCount[d], guessCount[d]);
            }

            return bulls + "A" + cows + "B";
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  -  Single pass with two frequency arrays
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * Single pass:
         *   - Exact match -> bull.
         *   - Otherwise, if the current secret digit was previously seen as
         *     an excess guess digit -> cow (decrement guess array).
         *     Otherwise record it in the secret array.
         *   - Similarly for the current guess digit vs. secret array.
         */
        public String getHint(String secret, String guess) {
            int bulls = 0, cows = 0;
            int[] sCount = new int[10];
            int[] gCount = new int[10];

            for (int i = 0; i < secret.length(); i++) {
                int s = secret.charAt(i) - '0';
                int g = guess.charAt(i)  - '0';

                if (s == g) {
                    bulls++;
                } else {
                    if (gCount[s] > 0) { cows++; gCount[s]--; }
                    else               { sCount[s]++; }

                    if (sCount[g] > 0) { cows++; sCount[g]--; }
                    else               { gCount[g]++; }
                }
            }

            return bulls + "A" + cows + "B";
        }
    }

    // ============================================================
    // APPROACH 3: BEST  -  Single pass with one signed frequency array
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * One frequency array: positive = secret surplus, negative = guess surplus.
         * For each non-bull position:
         *   - freq[s] < 0  => s was a guess surplus before -> cow.
         *   - freq[g] > 0  => g was a secret surplus before -> cow.
         *   Then update: freq[s]++, freq[g]--.
         */
        public String getHint(String secret, String guess) {
            int bulls = 0, cows = 0;
            int[] freq = new int[10];

            for (int i = 0; i < secret.length(); i++) {
                int s = secret.charAt(i) - '0';
                int g = guess.charAt(i)  - '0';

                if (s == g) {
                    bulls++;
                } else {
                    if (freq[s] < 0) cows++;
                    if (freq[g] > 0) cows++;
                    freq[s]++;
                    freq[g]--;
                }
            }

            return bulls + "A" + cows + "B";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Bulls and Cows ===");
        System.out.println("Brute  ('1807','7810'): " + new BruteForce().getHint("1807", "7810")); // 1A3B
        System.out.println("Optimal('1123','0111'): " + new Optimal().getHint("1123", "0111"));    // 1A1B
        System.out.println("Best   ('1807','7810'): " + new Best().getHint("1807", "7810"));       // 1A3B
    }
}
