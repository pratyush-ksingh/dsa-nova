/**
 * Problem: Longest Consecutive Sequence
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Sort then scan
    // Time: O(n log n)  |  Space: O(1)
    // ============================================================
    public static int bruteForce(int[] nums) {
        if (nums.length == 0) return 0;
        Arrays.sort(nums);
        int longest = 1, current = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) continue; // skip duplicates
            if (nums[i] == nums[i - 1] + 1) {
                current++;
            } else {
                current = 1;
            }
            longest = Math.max(longest, current);
        }
        return longest;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - HashSet, find sequence starts
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static int optimal(int[] nums) {
        if (nums.length == 0) return 0;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) set.add(num);

        int longest = 0;
        for (int num : set) {
            // Only start counting from the beginning of a sequence
            if (!set.contains(num - 1)) {
                int current = num;
                int streak = 1;
                while (set.contains(current + 1)) {
                    current++;
                    streak++;
                }
                longest = Math.max(longest, streak);
            }
        }
        return longest;
    }

    // ============================================================
    // APPROACH 3: BEST - Union-Find (Disjoint Set)
    // Time: O(n * alpha(n)) ~ O(n)  |  Space: O(n)
    // ============================================================
    static Map<Integer, Integer> parent, size;

    static int find(int x) {
        if (parent.get(x) != x) parent.put(x, find(parent.get(x)));
        return parent.get(x);
    }

    static void union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return;
        if (size.get(ra) < size.get(rb)) { int tmp = ra; ra = rb; rb = tmp; }
        parent.put(rb, ra);
        size.put(ra, size.get(ra) + size.get(rb));
    }

    public static int best(int[] nums) {
        if (nums.length == 0) return 0;
        parent = new HashMap<>();
        size = new HashMap<>();
        for (int num : nums) {
            parent.put(num, num);
            size.put(num, 1);
        }
        for (int num : nums) {
            if (parent.containsKey(num + 1)) union(num, num + 1);
        }
        int longest = 0;
        for (int s : size.values()) longest = Math.max(longest, s);
        return longest;
    }

    public static void main(String[] args) {
        System.out.println("=== Longest Consecutive Sequence ===");
        int[] nums = {100, 4, 200, 1, 3, 2};
        System.out.println("Brute Force: " + bruteForce(nums));
        System.out.println("Optimal:     " + optimal(nums));
        System.out.println("Best:        " + best(nums));
    }
}
