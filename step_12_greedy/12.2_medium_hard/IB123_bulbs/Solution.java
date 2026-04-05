import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(N^2)  |  Space: O(N)
// Simulate: for each position left to right, if bulb is off, flip all from i..N
// ============================================================
class BruteForce {
    public static int solve(int[] bulbs) {
        int n = bulbs.length;
        int[] state = bulbs.clone();
        int flips = 0;
        for (int i = 0; i < n; i++) {
            if (state[i] == 0) {
                for (int j = i; j < n; j++) state[j] ^= 1;
                flips++;
            }
        }
        return flips;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Greedy with flip parity
// Time: O(N)  |  Space: O(1)
// Key: process left to right. Track whether we've done an odd number of flips.
// An odd number of prior flips inverts the current bulb's initial state.
// If effective state is 0, we must flip again.
// ============================================================
class Optimal {
    public static int solve(int[] bulbs) {
        int flips = 0;
        for (int i = 0; i < bulbs.length; i++) {
            // effective state accounts for all prior flips (parity)
            int effectiveState = bulbs[i] ^ (flips & 1);
            if (effectiveState == 0) flips++;
        }
        return flips;
    }
}

// ============================================================
// APPROACH 3: BEST - Same O(N) but cleaner variable naming
// Time: O(N)  |  Space: O(1)
// flipCount's parity tells us if current bulb is inverted from original
// ============================================================
class Best {
    public static int solve(int[] bulbs) {
        int flipCount = 0;
        for (int b : bulbs) {
            // If (original XOR parity_of_flips) == 0, bulb is currently OFF -> flip
            if ((b ^ (flipCount & 1)) == 0) flipCount++;
        }
        return flipCount;
    }
}

public class Solution {
    public static void main(String[] args) {
        int[][] tests = {
            {0, 1, 0, 1},   // expected 4
            {1, 1, 0, 1},   // expected 2
            {1, 1, 1},      // expected 0
            {0},            // expected 1
            {1},            // expected 0
        };
        for (int[] test : tests) {
            int b = BruteForce.solve(test);
            int o = Optimal.solve(test);
            int best = Best.solve(test);
            System.out.printf("bulbs=%s -> Brute=%d, Optimal=%d, Best=%d%n",
                Arrays.toString(test), b, o, best);
        }
    }
}
