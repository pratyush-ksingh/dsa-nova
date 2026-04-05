/**
 * Problem: Root to Leaf Paths With Sum
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Collect All Paths, Check Sums
    // Time: O(n * h)  |  Space: O(n * h) storing all paths
    // Find all root-to-leaf paths, then check if any has target sum.
    // ============================================================
    public static boolean bruteForce(TreeNode root, int target) {
        if (root == null) return false;

        List<List<Integer>> allPaths = new ArrayList<>();
        collectPaths(root, new ArrayList<>(), allPaths);

        for (List<Integer> path : allPaths) {
            int sum = 0;
            for (int val : path) sum += val;
            if (sum == target) return true;
        }
        return false;
    }

    private static void collectPaths(TreeNode node, List<Integer> path,
                                      List<List<Integer>> allPaths) {
        if (node == null) return;
        path.add(node.val);
        if (node.left == null && node.right == null) {
            allPaths.add(new ArrayList<>(path));
        } else {
            collectPaths(node.left, path, allPaths);
            collectPaths(node.right, path, allPaths);
        }
        path.remove(path.size() - 1);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DFS with Running Sum Subtraction
    // Time: O(n)  |  Space: O(h) recursion stack
    // Subtract current node value from target. At leaf, check
    // if remaining equals 0.
    // ============================================================
    public static boolean optimal(TreeNode root, int target) {
        if (root == null) return false;

        int remaining = target - root.val;

        // Leaf check
        if (root.left == null && root.right == null) {
            return remaining == 0;
        }

        return optimal(root.left, remaining) || optimal(root.right, remaining);
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative DFS with Stack
    // Time: O(n)  |  Space: O(h)
    // Same approach iteratively using stack of (node, remaining).
    // ============================================================
    public static boolean best(TreeNode root, int target) {
        if (root == null) return false;

        Deque<Object[]> stack = new ArrayDeque<>();
        stack.push(new Object[]{root, target - root.val});

        while (!stack.isEmpty()) {
            Object[] pair = stack.pop();
            TreeNode node = (TreeNode) pair[0];
            int remaining = (int) pair[1];

            if (node.left == null && node.right == null && remaining == 0) {
                return true;
            }

            if (node.right != null) {
                stack.push(new Object[]{node.right, remaining - node.right.val});
            }
            if (node.left != null) {
                stack.push(new Object[]{node.left, remaining - node.left.val});
            }
        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== Root to Leaf Paths With Sum ===");

        //       5
        //      / \
        //     4   8
        //    /   / \
        //   11  13  4
        //  / \       \
        // 7   2       1
        TreeNode root = new TreeNode(5,
            new TreeNode(4, new TreeNode(11, new TreeNode(7), new TreeNode(2)), null),
            new TreeNode(8, new TreeNode(13), new TreeNode(4, null, new TreeNode(1))));

        System.out.println("Brute (22):   " + bruteForce(root, 22));  // true
        System.out.println("Optimal (22): " + optimal(root, 22));      // true
        System.out.println("Best (22):    " + best(root, 22));          // true

        System.out.println("Optimal (100): " + optimal(root, 100));    // false
        System.out.println("Empty:         " + optimal(null, 0));      // false
    }
}
