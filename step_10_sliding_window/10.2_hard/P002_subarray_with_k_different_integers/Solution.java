import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(N^2)  |  Space: O(N)
// Enumerate all subarrays, track distinct count with a set
// ============================================================
class BruteForce {
    public static int solve(int[] nums, int k) {
        int count = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            Set<Integer> distinct = new HashSet<>();
            for (int j = i; j < n; j++) {
                distinct.add(nums[j]);
                if (distinct.size() == k) count++;
                else if (distinct.size() > k) break;
            }
        }
        return count;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - atMost(K) - atMost(K-1)
// Time: O(N)  |  Space: O(N)
// exactlyK distinct = subarrays with atMost(K) - atMost(K-1)
// ============================================================
class Optimal {
    private static int atMost(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0, count = 0;
        for (int right = 0; right < nums.length; right++) {
            freq.merge(nums[right], 1, Integer::sum);
            while (freq.size() > k) {
                int leftVal = nums[left++];
                freq.merge(leftVal, -1, Integer::sum);
                if (freq.get(leftVal) == 0) freq.remove(leftVal);
            }
            count += right - left + 1;
        }
        return count;
    }

    public static int solve(int[] nums, int k) {
        return atMost(nums, k) - atMost(nums, k - 1);
    }
}

// ============================================================
// APPROACH 3: BEST - Two left pointers single pass
// Time: O(N)  |  Space: O(N)
// Maintain two windows simultaneously to avoid two passes
// ============================================================
class Best {
    public static int solve(int[] nums, int k) {
        // left1: leftmost start for atMost(k) window
        // left2: leftmost start for atMost(k-1) window
        Map<Integer, Integer> freq1 = new HashMap<>(), freq2 = new HashMap<>();
        int left1 = 0, left2 = 0, count = 0;
        for (int right = 0; right < nums.length; right++) {
            freq1.merge(nums[right], 1, Integer::sum);
            freq2.merge(nums[right], 1, Integer::sum);
            while (freq1.size() > k) {
                int v = nums[left1++];
                freq1.merge(v, -1, Integer::sum);
                if (freq1.get(v) == 0) freq1.remove(v);
            }
            while (freq2.size() > k - 1) {
                int v = nums[left2++];
                freq2.merge(v, -1, Integer::sum);
                if (freq2.get(v) == 0) freq2.remove(v);
            }
            // subarrays ending at right with exactly k distinct
            count += left2 - left1;
        }
        return count;
    }
}

public class Solution {
    public static void main(String[] args) {
        int[][] tests = {{1, 2, 1, 2, 3}, {1, 2, 1, 3, 4}};
        int[] ks = {2, 3};
        int[] expected = {7, 3};
        for (int i = 0; i < tests.length; i++) {
            int b = BruteForce.solve(tests[i], ks[i]);
            int o = Optimal.solve(tests[i], ks[i]);
            int best = Best.solve(tests[i], ks[i]);
            System.out.printf("Test %d: Brute=%d, Optimal=%d, Best=%d (expected=%d)%n",
                i + 1, b, o, best, expected[i]);
        }
        // Test 1: [1,2,1,2,3] k=2 -> 7
        // Test 2: [1,2,1,3,4] k=3 -> 3
    }
}
