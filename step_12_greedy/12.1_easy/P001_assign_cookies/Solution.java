/**
 * Problem: Assign Cookies (LeetCode #455)
 * Difficulty: EASY | XP: 10
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Try all assignments
    // Time: O(2^m * n)  |  Space: O(m)
    // ============================================================
    public static int bruteForce(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        boolean[] used = new boolean[s.length];
        return bruteHelper(g, s, used, 0);
    }

    private static int bruteHelper(int[] g, int[] s, boolean[] used, int childIdx) {
        if (childIdx >= g.length) return 0;
        int best = bruteHelper(g, s, used, childIdx + 1); // skip this child
        for (int j = 0; j < s.length; j++) {
            if (!used[j] && s[j] >= g[childIdx]) {
                used[j] = true;
                best = Math.max(best, 1 + bruteHelper(g, s, used, childIdx + 1));
                used[j] = false;
                break; // greedy pruning: smallest adequate cookie is optimal
            }
        }
        return best;
    }

    // ============================================================
    // APPROACH 2 & 3: OPTIMAL -- Sort + Two Pointers
    // Time: O(n log n + m log m)  |  Space: O(1)
    // ============================================================
    public static int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int i = 0; // child pointer
        int j = 0; // cookie pointer
        while (i < g.length && j < s.length) {
            if (s[j] >= g[i]) {
                i++; // child i is content
            }
            j++; // move to next cookie regardless
        }
        return i; // number of content children
    }

    public static void main(String[] args) {
        System.out.println("=== Assign Cookies ===\n");

        int[][] greeds = {{1, 2, 3}, {1, 2}, {10, 9, 8, 7}, {1}, {}};
        int[][] cookies = {{1, 1}, {1, 2, 3}, {5, 6, 7, 8}, {}, {1, 2}};
        int[] expected = {1, 2, 2, 0, 0};

        for (int t = 0; t < greeds.length; t++) {
            int brute = bruteForce(greeds[t].clone(), cookies[t].clone());
            int optimal = findContentChildren(greeds[t].clone(), cookies[t].clone());
            System.out.printf("g=%s, s=%s -> brute=%d, optimal=%d (expected=%d) %s%n",
                    Arrays.toString(greeds[t]), Arrays.toString(cookies[t]),
                    brute, optimal, expected[t],
                    (optimal == expected[t]) ? "PASS" : "FAIL");
        }
    }
}
