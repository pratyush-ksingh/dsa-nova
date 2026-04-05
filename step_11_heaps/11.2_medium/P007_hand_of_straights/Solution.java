/**
 * Problem: Hand of Straights
 * LeetCode 846 | Difficulty: MEDIUM | XP: 25
 *
 * Alice has a hand of cards represented by integer values. She wants to
 * rearrange them into groups of groupSize consecutive numbers.
 * Return true if she can, false otherwise.
 *
 * Key Insight: Always start a new group from the smallest available card.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (Sort + greedy scan with list removal)
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Sort the hand. Repeatedly find the smallest card, then try to remove
         * a complete group of groupSize consecutive cards. Uses ArrayList.remove()
         * which is O(n) -> O(n^2) total.
         */
        public boolean isNStraightHand(int[] hand, int groupSize) {
            if (hand.length % groupSize != 0) return false;
            List<Integer> arr = new ArrayList<>();
            for (int c : hand) arr.add(c);
            Collections.sort(arr);
            while (!arr.isEmpty()) {
                int start = arr.get(0);
                for (int card = start; card < start + groupSize; card++) {
                    int idx = arr.indexOf(card);
                    if (idx == -1) return false;
                    arr.remove(idx);
                }
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (TreeMap + greedy from smallest key)
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    static class Optimal {
        /**
         * Build a frequency TreeMap (sorted by key).
         * For each smallest key with count > 0, consume `count` full groups
         * of groupSize starting from that key.
         * If any required successor key has insufficient count, return false.
         */
        public boolean isNStraightHand(int[] hand, int groupSize) {
            if (hand.length % groupSize != 0) return false;
            TreeMap<Integer, Integer> freq = new TreeMap<>();
            for (int c : hand) freq.merge(c, 1, Integer::sum);

            for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
                int card = entry.getKey();
                int count = entry.getValue();
                if (count == 0) continue;
                for (int i = 0; i < groupSize; i++) {
                    int needed = freq.getOrDefault(card + i, 0);
                    if (needed < count) return false;
                    freq.put(card + i, needed - count);
                }
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Same O(n log n), minor early-exit tweak)
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Same as Optimal but exits immediately once a frequency goes negative,
         * reducing constant factor for failing cases.
         * No asymptotic improvement exists: sorting unique keys is inherently O(n log n).
         */
        public boolean isNStraightHand(int[] hand, int groupSize) {
            if (hand.length % groupSize != 0) return false;
            TreeMap<Integer, Integer> freq = new TreeMap<>();
            for (int c : hand) freq.merge(c, 1, Integer::sum);

            for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
                int card = entry.getKey();
                int count = entry.getValue();
                if (count <= 0) continue;
                for (int step = 1; step < groupSize; step++) {
                    freq.merge(card + step, -count, Integer::sum);
                    if (freq.get(card + step) < 0) return false;
                }
                freq.put(card, 0);
            }
            return true;
        }
    }

    public static void main(String[] args) {
        int[][] hands     = {{1,2,3,6,2,3,4,7,8}, {1,2,3,4,5}, {1}, {3,2,1,2,3,4,3,4,5,9,10,11}};
        int[]   groupSzs  = {3, 4, 1, 3};
        boolean[] exps    = {true, false, true, true};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        System.out.println("=== Hand of Straights ===");
        for (int t = 0; t < hands.length; t++) {
            boolean b  = bf.isNStraightHand(hands[t].clone(), groupSzs[t]);
            boolean o  = opt.isNStraightHand(hands[t].clone(), groupSzs[t]);
            boolean be = bst.isNStraightHand(hands[t].clone(), groupSzs[t]);
            String status = (b == o && o == be && be == exps[t]) ? "OK" : "FAIL";
            System.out.printf("  brute=%b opt=%b best=%b (exp=%b) [%s]%n", b, o, be, exps[t], status);
        }
    }
}
