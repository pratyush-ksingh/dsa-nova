/**
 * Problem: Iterative Postorder using 1 Stack
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
        recurse(node.left, result);
        recurse(node.right, result);
        result.add(node.val);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Single Stack with Last Visited
    // Time: O(n)  |  Space: O(h)
    // Track last visited node. Process current only when right
    // child is null or was last visited.
    // ============================================================
    public static List<Integer> optimal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        TreeNode lastVisited = null;

        while (curr != null || !stack.isEmpty()) {
            // Go all the way left
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }

            // Peek at top
            TreeNode top = stack.peek();

            // If right exists and not yet visited, go right
            if (top.right != null && top.right != lastVisited) {
                curr = top.right;
            } else {
                // Process this node
                result.add(top.val);
                lastVisited = stack.pop();
            }
        }

        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Single Stack (streamlined)
    // Time: O(n)  |  Space: O(h)
    // Same last-visited tracking, slightly cleaner flow.
    // ============================================================
    public static List<Integer> best(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        TreeNode prev = null;

        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                curr = curr.left;
            } else {
                curr = stack.peek();
                if (curr.right != null && curr.right != prev) {
                    curr = curr.right;
                } else {
                    result.add(curr.val);
                    prev = stack.pop();
                    curr = null;  // Don't go left again
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Iterative Postorder using 1 Stack ===");

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
