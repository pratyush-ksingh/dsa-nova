/**
 * Problem: Iterative Preorder Traversal
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
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val; this.left = left; this.right = right;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursive Preorder
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
        result.add(node.val);        // Root
        recurse(node.left, result);  // Left
        recurse(node.right, result); // Right
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Iterative with Explicit Stack
    // Time: O(n)  |  Space: O(h)
    // Push right first, then left. Pop = process immediately.
    // ============================================================
    public static List<Integer> optimal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);

            // Push right first so left is processed first (LIFO)
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Morris Preorder Traversal
    // Time: O(n)  |  Space: O(1)
    // Thread the tree temporarily to eliminate stack usage.
    // ============================================================
    public static List<Integer> best(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode curr = root;

        while (curr != null) {
            if (curr.left == null) {
                // No left child: process and go right
                result.add(curr.val);
                curr = curr.right;
            } else {
                // Find inorder predecessor (rightmost in left subtree)
                TreeNode predecessor = curr.left;
                while (predecessor.right != null && predecessor.right != curr) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    // First visit: create thread, process node, go left
                    predecessor.right = curr;
                    result.add(curr.val);  // Preorder: add on FIRST visit
                    curr = curr.left;
                } else {
                    // Second visit: remove thread, go right
                    predecessor.right = null;
                    curr = curr.right;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Iterative Preorder Traversal ===");

        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3));

        System.out.println("Brute:   " + bruteForce(root));  // [1,2,4,5,3]
        System.out.println("Optimal: " + optimal(root));      // [1,2,4,5,3]
        System.out.println("Best:    " + best(root));          // [1,2,4,5,3]

        // Edge: empty tree
        System.out.println("Empty:   " + optimal(null));       // []
    }
}
