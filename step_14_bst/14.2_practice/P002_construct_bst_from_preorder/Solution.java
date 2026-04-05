/**
 * Problem: Construct BST from Preorder Traversal (LeetCode #1008)
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
    // APPROACH 1: BRUTE FORCE -- Sequential BST Insertion
    // Time: O(n*h) -> O(n^2) worst  |  Space: O(h)
    // Insert each element into BST one by one.
    // ============================================================
    public static TreeNode bruteForce(int[] preorder) {
        if (preorder.length == 0) return null;
        TreeNode root = new TreeNode(preorder[0]);
        for (int i = 1; i < preorder.length; i++) {
            insertBST(root, preorder[i]);
        }
        return root;
    }

    private static void insertBST(TreeNode root, int val) {
        TreeNode curr = root;
        while (true) {
            if (val < curr.val) {
                if (curr.left == null) { curr.left = new TreeNode(val); return; }
                curr = curr.left;
            } else {
                if (curr.right == null) { curr.right = new TreeNode(val); return; }
                curr = curr.right;
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Partition
    // Time: O(n log n) avg, O(n^2) worst  |  Space: O(n)
    // Find partition point, recurse on left and right halves.
    // ============================================================
    public static TreeNode optimal(int[] preorder) {
        return buildPartition(preorder, 0, preorder.length - 1);
    }

    private static TreeNode buildPartition(int[] pre, int lo, int hi) {
        if (lo > hi) return null;
        TreeNode node = new TreeNode(pre[lo]);

        // Find first element greater than root (partition point)
        int split = hi + 1;
        for (int i = lo + 1; i <= hi; i++) {
            if (pre[i] > pre[lo]) { split = i; break; }
        }

        node.left = buildPartition(pre, lo + 1, split - 1);
        node.right = buildPartition(pre, split, hi);
        return node;
    }

    // ============================================================
    // APPROACH 3: BEST -- Upper Bound Recursion (O(n))
    // Time: O(n)  |  Space: O(h)
    // Global index + bound. Each element consumed exactly once.
    // ============================================================
    private static int idx;

    public static TreeNode best(int[] preorder) {
        idx = 0;
        return buildBound(preorder, Integer.MAX_VALUE);
    }

    private static TreeNode buildBound(int[] pre, int bound) {
        if (idx >= pre.length || pre[idx] > bound) return null;

        TreeNode node = new TreeNode(pre[idx]);
        idx++;

        node.left = buildBound(pre, node.val);    // Left: values < node
        node.right = buildBound(pre, bound);       // Right: values < parent's bound
        return node;
    }

    // Helper: preorder traversal for verification
    private static List<Integer> preorderList(TreeNode node) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(node, result);
        return result;
    }

    private static void preorderHelper(TreeNode node, List<Integer> list) {
        if (node == null) return;
        list.add(node.val);
        preorderHelper(node.left, list);
        preorderHelper(node.right, list);
    }

    public static void main(String[] args) {
        System.out.println("=== Construct BST from Preorder ===");

        int[] pre1 = {8, 5, 1, 7, 10, 12};
        System.out.println("Brute:   " + preorderList(bruteForce(pre1)));   // [8,5,1,7,10,12]
        System.out.println("Optimal: " + preorderList(optimal(pre1)));      // [8,5,1,7,10,12]
        System.out.println("Best:    " + preorderList(best(pre1)));          // [8,5,1,7,10,12]

        // Edge: single element
        int[] pre2 = {5};
        System.out.println("Single:  " + preorderList(best(pre2)));          // [5]

        // Edge: sorted ascending (right-skewed)
        int[] pre3 = {1, 2, 3, 4, 5};
        System.out.println("Sorted:  " + preorderList(best(pre3)));          // [1,2,3,4,5]
    }
}
