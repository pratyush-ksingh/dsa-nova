import java.util.*;

/**
 * Covered and Uncovered Nodes
 *
 * Find |sum_uncovered - sum_covered| in a binary tree.
 * Uncovered = boundary nodes (left boundary + right boundary + leaves).
 * Covered = all other interior nodes.
 */
public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- HashSet to mark uncovered
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    public static long bruteForce(TreeNode root) {
        if (root == null) return 0;

        long totalSum = totalSum(root);

        // Collect all uncovered nodes in a set
        Set<TreeNode> uncovered = new HashSet<>();

        // Left boundary (excluding leaves)
        TreeNode cur = root;
        while (cur != null) {
            uncovered.add(cur);
            if (cur.left != null) cur = cur.left;
            else cur = cur.right;
        }

        // Right boundary (excluding leaves)
        cur = root;
        while (cur != null) {
            uncovered.add(cur);
            if (cur.right != null) cur = cur.right;
            else cur = cur.left;
        }

        // All leaves
        addLeaves(root, uncovered);

        long uncoveredSum = 0;
        for (TreeNode node : uncovered) {
            uncoveredSum += node.val;
        }

        long coveredSum = totalSum - uncoveredSum;
        return Math.abs(uncoveredSum - coveredSum);
    }

    private static void addLeaves(TreeNode node, Set<TreeNode> set) {
        if (node == null) return;
        if (node.left == null && node.right == null) {
            set.add(node);
            return;
        }
        addLeaves(node.left, set);
        addLeaves(node.right, set);
    }

    private static long totalSum(TreeNode node) {
        if (node == null) return 0;
        return node.val + totalSum(node.left) + totalSum(node.right);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Direct sum computation (no HashSet)
    // Time: O(N)  |  Space: O(H)
    // ============================================================
    public static long optimal(TreeNode root) {
        if (root == null) return 0;

        long total = totalSum(root);
        long uncovered = uncoveredSum(root);
        long covered = total - uncovered;
        return Math.abs(uncovered - covered);
    }

    private static long uncoveredSum(TreeNode root) {
        if (root == null) return 0;

        long sum = 0;

        // Left boundary: go left-first, fall to right if no left
        TreeNode cur = root.left;
        while (cur != null) {
            sum += cur.val;
            if (cur.left != null) cur = cur.left;
            else cur = cur.right;
        }

        // Right boundary: go right-first, fall to left if no right
        cur = root.right;
        while (cur != null) {
            sum += cur.val;
            if (cur.right != null) cur = cur.right;
            else cur = cur.left;
        }

        // Add root (not counted above since we started from root.left/right)
        sum += root.val;

        return sum;
    }

    // ============================================================
    // APPROACH 3: BEST -- Formula: |total - 2 * covered| = |2 * uncovered - total|
    // Time: O(N)  |  Space: O(H)
    // ============================================================
    public static long best(TreeNode root) {
        if (root == null) return 0;

        long total = totalSum(root);
        long uncovered = computeUncovered(root);
        // covered = total - uncovered
        // |uncovered - covered| = |uncovered - (total - uncovered)| = |2*uncovered - total|
        return Math.abs(2 * uncovered - total);
    }

    private static long computeUncovered(TreeNode root) {
        if (root == null) return 0;
        long sum = root.val;

        // Left boundary walk (from root.left downward)
        TreeNode cur = root.left;
        while (cur != null) {
            sum += cur.val;
            cur = (cur.left != null) ? cur.left : cur.right;
        }

        // Right boundary walk (from root.right downward)
        cur = root.right;
        while (cur != null) {
            sum += cur.val;
            cur = (cur.right != null) ? cur.right : cur.left;
        }

        return sum;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        //         8
        //        / \
        //       3   10
        //      / \    \
        //     1   6    14
        //        / \   /
        //       4   7 13
        TreeNode root = new TreeNode(8);
        root.left = new TreeNode(3);
        root.right = new TreeNode(10);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(6);
        root.right.right = new TreeNode(14);
        root.left.right.left = new TreeNode(4);
        root.left.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(13);

        System.out.println("=== Covered Uncovered Nodes ===");
        System.out.println("Brute:   " + bruteForce(root));  // 54
        System.out.println("Optimal: " + optimal(root));      // 54
        System.out.println("Best:    " + best(root));          // 54
    }
}
