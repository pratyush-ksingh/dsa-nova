import java.util.*;

/**
 * Problem: Leaders in an Array
 * Difficulty: EASY | XP: 10
 *
 * Find all leaders: element is a leader if strictly greater than
 * all elements to its right. Rightmost element is always a leader.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check Every Suffix
    // Time: O(n^2)  |  Space: O(1) excluding output
    // For each element, scan all to its right.
    // ============================================================
    static class BruteForce {
        public static List<Integer> findLeaders(int[] arr) {
            List<Integer> leaders = new ArrayList<>();
            int n = arr.length;

            for (int i = 0; i < n; i++) {
                boolean isLeader = true;
                for (int j = i + 1; j < n; j++) {
                    if (arr[j] >= arr[i]) {
                        isLeader = false;
                        break;
                    }
                }
                if (isLeader) {
                    leaders.add(arr[i]);
                }
            }
            return leaders;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Right-to-Left Single Pass
    // Time: O(n)  |  Space: O(1) excluding output
    // Track max_from_right, scan backwards, reverse result.
    // ============================================================
    static class Optimal {
        public static List<Integer> findLeaders(int[] arr) {
            List<Integer> leaders = new ArrayList<>();
            int n = arr.length;
            int maxFromRight = Integer.MIN_VALUE;

            for (int i = n - 1; i >= 0; i--) {
                if (arr[i] > maxFromRight) {
                    leaders.add(arr[i]);
                }
                maxFromRight = Math.max(maxFromRight, arr[i]);
            }

            Collections.reverse(leaders);
            return leaders;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Stack-Based Variant
    // Time: O(n)  |  Space: O(n) for the stack
    // Right-to-left scan using a stack. Same time, more space.
    // Included for pattern completeness.
    // ============================================================
    static class Best {
        public static List<Integer> findLeaders(int[] arr) {
            Deque<Integer> stack = new ArrayDeque<>();
            int n = arr.length;

            for (int i = n - 1; i >= 0; i--) {
                if (stack.isEmpty() || arr[i] > stack.peek()) {
                    stack.push(arr[i]);
                }
            }

            // Stack now has leaders from top (leftmost) to bottom (rightmost)
            List<Integer> leaders = new ArrayList<>();
            while (!stack.isEmpty()) {
                leaders.add(stack.pop());
            }
            return leaders;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Leaders in an Array ===");

        int[] test1 = {16, 17, 4, 3, 5, 2};
        int[] test2 = {1, 2, 3, 4, 5};
        int[] test3 = {5, 4, 3, 2, 1};
        int[] test4 = {7};
        int[] test5 = {5, 5, 5};

        System.out.println("--- Brute Force ---");
        System.out.println(BruteForce.findLeaders(test1));  // [17, 5, 2]
        System.out.println(BruteForce.findLeaders(test2));  // [5]
        System.out.println(BruteForce.findLeaders(test3));  // [5, 4, 3, 2, 1]

        System.out.println("--- Optimal ---");
        System.out.println(Optimal.findLeaders(test1));     // [17, 5, 2]
        System.out.println(Optimal.findLeaders(test2));     // [5]
        System.out.println(Optimal.findLeaders(test3));     // [5, 4, 3, 2, 1]

        System.out.println("--- Best (Stack) ---");
        System.out.println(Best.findLeaders(test1));        // [17, 5, 2]
        System.out.println(Best.findLeaders(test4));        // [7]
        System.out.println(Best.findLeaders(test5));        // [5]
    }
}
