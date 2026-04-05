/**
 * Problem: Consecutive Parent Child
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
    // APPROACH 1: BRUTE FORCE -- BFS checking each parent-child
    // Time: O(n)  |  Space: O(n) for queue
    // Level order traversal, check each parent-child pair.
    // ============================================================
    public static int bruteForce(TreeNode root) {
        if (root == null) return 0;

        int count = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (node.left != null) {
                if (node.left.val == node.val + 1) count++;
                queue.offer(node.left);
            }

            if (node.right != null) {
                if (node.right.val == node.val + 1) count++;
                queue.offer(node.right);
            }
        }

        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DFS (recursive with instance var)
    // Time: O(n)  |  Space: O(h) recursion stack
    // Recurse through tree, at each node check children.
    // ============================================================
    private static int optCount;

    public static int optimal(TreeNode root) {
        optCount = 0;
        dfsOptimal(root);
        return optCount;
    }

    private static void dfsOptimal(TreeNode node) {
        if (node == null) return;

        if (node.left != null) {
            if (node.left.val == node.val + 1) optCount++;
            dfsOptimal(node.left);
        }

        if (node.right != null) {
            if (node.right.val == node.val + 1) optCount++;
            dfsOptimal(node.right);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- DFS returning count (functional)
    // Time: O(n)  |  Space: O(h) recursion stack
    // Same DFS but returns count, no global variable.
    // ============================================================
    public static int best(TreeNode root) {
        return dfsBest(root);
    }

    private static int dfsBest(TreeNode node) {
        if (node == null) return 0;

        int count = 0;

        if (node.left != null) {
            if (node.left.val == node.val + 1) count++;
            count += dfsBest(node.left);
        }

        if (node.right != null) {
            if (node.right.val == node.val + 1) count++;
            count += dfsBest(node.right);
        }

        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== Consecutive Parent Child ===");

        //       1
        //      / \
        //     2   3
        //    / \   \
        //   4   5   4
        // Consecutive: (1,2), (3,4) -> 2
        TreeNode root = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3, null, new TreeNode(4)));

        System.out.println("Brute:   " + bruteForce(root));  // 2
        System.out.println("Optimal: " + optimal(root));      // 2
        System.out.println("Best:    " + best(root));          // 2

        // All consecutive
        TreeNode root2 = new TreeNode(1, new TreeNode(2), new TreeNode(2));
        System.out.println("All:     " + best(root2));         // 2

        System.out.println("Empty:   " + best(null));          // 0
    }
}
