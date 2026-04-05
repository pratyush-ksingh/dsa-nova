/**
 * Problem: Last Node in Complete Binary Tree
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
    // APPROACH 1: BRUTE FORCE -- BFS Level Order
    // Time: O(n)  |  Space: O(n)
    // Traverse all nodes level by level, return the last one.
    // ============================================================
    public static int bruteForce(TreeNode root) {
        if (root == null) return -1;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        TreeNode last = root;

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            last = node;
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }

        return last.val;
    }

    private static int getHeight(TreeNode node) {
        int h = 0;
        while (node != null) {
            h++;
            node = node.left;
        }
        return h;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Binary Search on Last Level
    // Time: O(log^2 n)  |  Space: O(log n) recursion
    // Compare left and right subtree heights. If equal, left is
    // perfect so go right. Else go left.
    // ============================================================
    public static int optimal(TreeNode root) {
        if (root == null) return -1;
        return findLast(root);
    }

    private static int findLast(TreeNode node) {
        if (node == null) return -1;
        if (node.left == null && node.right == null) return node.val;

        int leftH = getHeight(node.left);
        int rightH = getHeight(node.right);

        if (leftH == rightH) {
            // Left subtree is perfect, last node is in right
            return findLast(node.right);
        } else {
            // Right subtree is perfect (shorter), last is in left
            return findLast(node.left);
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same Binary Search (iterative)
    // Time: O(log^2 n)  |  Space: O(1)
    // Same logic iteratively to avoid recursion stack.
    // ============================================================
    public static int best(TreeNode root) {
        if (root == null) return -1;

        TreeNode curr = root;
        while (curr != null) {
            if (curr.left == null) {
                return curr.val;
            }

            int leftH = getHeight(curr.left);
            int rightH = getHeight(curr.right);

            if (leftH == rightH) {
                curr = curr.right;
            } else {
                curr = curr.left;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        System.out.println("=== Last Node in Complete Binary Tree ===");

        //        1
        //       / \
        //      2   3
        //     / \  /
        //    4  5 6
        TreeNode root = new TreeNode(1,
            new TreeNode(2, new TreeNode(4), new TreeNode(5)),
            new TreeNode(3, new TreeNode(6), null));

        System.out.println("Brute:   " + bruteForce(root));  // 6
        System.out.println("Optimal: " + optimal(root));      // 6
        System.out.println("Best:    " + best(root));          // 6

        // Single node
        System.out.println("Single:  " + optimal(new TreeNode(42)));  // 42
        System.out.println("Empty:   " + optimal(null));              // -1
    }
}
