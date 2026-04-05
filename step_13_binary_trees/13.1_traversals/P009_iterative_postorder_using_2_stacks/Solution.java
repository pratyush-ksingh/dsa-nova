/**
 * Problem: Iterative Postorder using 2 Stacks
 * Difficulty: HARD | XP: 50
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
    // APPROACH 1: BRUTE FORCE -- Recursive Postorder
    // Time: O(n)  |  Space: O(h) recursion stack
    // Standard recursive approach as baseline.
    // ============================================================
    public static List<Integer> bruteForce(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        recurse(root, result);
        return result;
    }

    private static void recurse(TreeNode node, List<Integer> result) {
        if (node == null) return;
        recurse(node.left, result);   // Left
        recurse(node.right, result);  // Right
        result.add(node.val);         // Root
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Two Stacks
    // Time: O(n)  |  Space: O(n)
    // Push root to s1, pop from s1, push to s2, push children
    // to s1. s2 reversed gives postorder.
    // ============================================================
    public static List<Integer> optimal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> s1 = new ArrayDeque<>();
        Deque<TreeNode> s2 = new ArrayDeque<>();

        s1.push(root);

        while (!s1.isEmpty()) {
            TreeNode node = s1.pop();
            s2.push(node);

            // Push left first, then right
            if (node.left != null) s1.push(node.left);
            if (node.right != null) s1.push(node.right);
        }

        while (!s2.isEmpty()) {
            result.add(s2.pop().val);
        }

        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Two Stacks (compact)
    // Time: O(n)  |  Space: O(n)
    // Same two-stack approach, using LinkedList addFirst for reversal.
    // ============================================================
    public static List<Integer> best(TreeNode root) {
        LinkedList<Integer> result = new LinkedList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.addFirst(node.val);  // Add to front = automatic reversal

            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Iterative Postorder using 2 Stacks ===");

        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3));

        System.out.println("Brute:   " + bruteForce(root));  // [4,5,2,3,1]
        System.out.println("Optimal: " + optimal(root));      // [4,5,2,3,1]
        System.out.println("Best:    " + best(root));          // [4,5,2,3,1]

        // Edge: empty tree
        System.out.println("Empty:   " + optimal(null));       // []
    }
}
