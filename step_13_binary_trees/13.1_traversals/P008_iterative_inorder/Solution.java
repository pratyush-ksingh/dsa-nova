/**
 * Problem: Iterative Inorder Traversal
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
    // APPROACH 1: BRUTE FORCE -- Recursive Inorder
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
        recurse(node.left, result);  // Left
        result.add(node.val);        // Root
        recurse(node.right, result); // Right
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Iterative "Push All Left" Pattern
    // Time: O(n)  |  Space: O(h)
    // Push entire left spine, pop and process, then go right.
    // ============================================================
    public static List<Integer> optimal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            // Phase 1: Push all left nodes
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            // Phase 2: Pop, process, go right
            curr = stack.pop();
            result.add(curr.val);
            curr = curr.right;
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Morris Inorder Traversal
    // Time: O(n)  |  Space: O(1)
    // Thread tree temporarily. Add to result on SECOND visit.
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
                // Find inorder predecessor
                TreeNode predecessor = curr.left;
                while (predecessor.right != null && predecessor.right != curr) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    // First visit: create thread, go left
                    predecessor.right = curr;
                    curr = curr.left;
                } else {
                    // Second visit: remove thread, process, go right
                    predecessor.right = null;
                    result.add(curr.val);  // Inorder: add on SECOND visit
                    curr = curr.right;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Iterative Inorder Traversal ===");

        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3));

        System.out.println("Brute:   " + bruteForce(root));  // [4,2,5,1,3]
        System.out.println("Optimal: " + optimal(root));      // [4,2,5,1,3]
        System.out.println("Best:    " + best(root));          // [4,2,5,1,3]

        // Edge: empty tree
        System.out.println("Empty:   " + optimal(null));       // []

        // Edge: single node
        System.out.println("Single:  " + optimal(new TreeNode(42)));  // [42]
    }
}
