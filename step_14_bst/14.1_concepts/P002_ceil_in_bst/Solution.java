/**
 * Problem: Ceil in BST
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Inorder to Sorted Array + Search
    // Collect all values via inorder, then find first >= key.
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        public static int findCeil(TreeNode root, int key) {
            List<Integer> sorted = new ArrayList<>();
            inorder(root, sorted);

            int ceil = -1;
            for (int val : sorted) {
                if (val >= key) {
                    ceil = val;
                    break;
                }
            }
            return ceil;
        }

        private static void inorder(TreeNode node, List<Integer> list) {
            if (node == null) return;
            inorder(node.left, list);
            list.add(node.val);
            inorder(node.right, list);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BST-Guided Recursive
    // If val == key, return key. If val < key, go right.
    // If val > key, record candidate and go left.
    // Time: O(h)  |  Space: O(h)
    // ============================================================
    static class OptimalRecursive {
        public static int findCeil(TreeNode root, int key) {
            return helper(root, key, -1);
        }

        private static int helper(TreeNode node, int key, int ceil) {
            if (node == null) return ceil;
            if (node.val == key) return key;
            if (node.val < key) {
                return helper(node.right, key, ceil);
            }
            // node.val > key: this is a candidate, try to find smaller ceil
            return helper(node.left, key, node.val);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- BST-Guided Iterative
    // Same logic, iterative. O(1) extra space.
    // Time: O(h)  |  Space: O(1)
    // ============================================================
    static class BestIterative {
        public static int findCeil(TreeNode root, int key) {
            int ceil = -1;
            TreeNode node = root;

            while (node != null) {
                if (node.val == key) {
                    return key;
                } else if (node.val < key) {
                    node = node.right;
                } else {
                    // node.val > key: candidate for ceil
                    ceil = node.val;
                    node = node.left;
                }
            }
            return ceil;
        }
    }

    // Helper: build BST from level-order array
    public static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < arr.length) {
            TreeNode curr = queue.poll();
            if (i < arr.length && arr[i] != null) {
                curr.left = new TreeNode(arr[i]);
                queue.offer(curr.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                curr.right = new TreeNode(arr[i]);
                queue.offer(curr.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Ceil in BST ===\n");

        // BST:       8
        //          /   \
        //         4    12
        //        / \  / \
        //       2  6 10 14
        Integer[] bstArr = {8, 4, 12, 2, 6, 10, 14};

        int[][] tests = {
            {5, 6},    // 5 not in tree, next >= is 6
            {7, 8},    // 7 not in tree, next >= is 8
            {8, 8},    // exact match
            {3, 4},    // 3 not in tree, next >= is 4
            {1, 2},    // smaller than min, ceil = min = 2
            {9, 10},   // between 8 and 10
            {13, 14},  // between 12 and 14
            {15, -1},  // larger than max, no ceil
            {14, 14},  // exact match at leaf
            {11, 12},  // between 10 and 12
        };

        for (int[] test : tests) {
            int key = test[0], expected = test[1];
            TreeNode root = buildTree(bstArr);

            int brute = BruteForce.findCeil(root, key);
            int optimal = OptimalRecursive.findCeil(root, key);
            int best = BestIterative.findCeil(root, key);

            boolean pass = brute == expected && optimal == expected && best == expected;
            String status = pass ? "PASS" : "FAIL";
            System.out.printf("key=%2d -> Brute: %2d, Optimal: %2d, Best: %2d (expected %2d) %s%n",
                    key, brute, optimal, best, expected, status);
        }

        // Edge case: single node
        System.out.println("\nSingle node BST [5]:");
        TreeNode single = buildTree(new Integer[]{5});
        System.out.printf("  key=3 -> ceil=%d (expected 5) %s%n",
                BestIterative.findCeil(single, 3),
                BestIterative.findCeil(single, 3) == 5 ? "PASS" : "FAIL");
        System.out.printf("  key=6 -> ceil=%d (expected -1) %s%n",
                BestIterative.findCeil(single, 6),
                BestIterative.findCeil(single, 6) == -1 ? "PASS" : "FAIL");
    }
}
