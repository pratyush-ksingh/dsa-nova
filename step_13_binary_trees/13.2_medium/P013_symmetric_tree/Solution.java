/**
 * Problem: Symmetric Tree
 * Difficulty: EASY | XP: 10
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
    // APPROACH 1: BRUTE FORCE -- Mirror Tree and Compare
    // Time: O(n)  |  Space: O(n) for mirrored copy
    // Create a mirrored copy, then check if original == mirror.
    // ============================================================
    public static boolean bruteForce(TreeNode root) {
        if (root == null) return true;
        TreeNode mirrored = mirror(root);
        return isSame(root, mirrored);
    }

    private static TreeNode mirror(TreeNode node) {
        if (node == null) return null;
        TreeNode newNode = new TreeNode(node.val);
        newNode.left = mirror(node.right);
        newNode.right = mirror(node.left);
        return newNode;
    }

    private static boolean isSame(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) return true;
        if (t1 == null || t2 == null) return false;
        return t1.val == t2.val &&
               isSame(t1.left, t2.left) &&
               isSame(t1.right, t2.right);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Mirror Comparison
    // Time: O(n)  |  Space: O(h) recursion stack
    // Compare left.left with right.right and left.right with
    // right.left simultaneously.
    // ============================================================
    public static boolean optimal(TreeNode root) {
        if (root == null) return true;
        return isMirror(root.left, root.right);
    }

    private static boolean isMirror(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        return left.val == right.val &&
               isMirror(left.left, right.right) &&
               isMirror(left.right, right.left);
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative with Queue
    // Time: O(n)  |  Space: O(n) for queue
    // Use a queue to compare mirror pairs iteratively.
    // ============================================================
    public static boolean best(TreeNode root) {
        if (root == null) return true;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root.left);
        queue.offer(root.right);

        while (!queue.isEmpty()) {
            TreeNode left = queue.poll();
            TreeNode right = queue.poll();

            if (left == null && right == null) continue;
            if (left == null || right == null) return false;
            if (left.val != right.val) return false;

            // Enqueue in mirror order
            queue.offer(left.left);
            queue.offer(right.right);
            queue.offer(left.right);
            queue.offer(right.left);
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Symmetric Tree ===");

        //       1
        //      / \
        //     2   2
        //    / \ / \
        //   3  4 4  3
        TreeNode root = new TreeNode(1,
            new TreeNode(2, new TreeNode(3), new TreeNode(4)),
            new TreeNode(2, new TreeNode(4), new TreeNode(3)));

        System.out.println("Brute:   " + bruteForce(root));  // true
        System.out.println("Optimal: " + optimal(root));      // true
        System.out.println("Best:    " + best(root));          // true

        // Not symmetric
        TreeNode root2 = new TreeNode(1,
            new TreeNode(2, null, new TreeNode(3)),
            new TreeNode(2, null, new TreeNode(3)));

        System.out.println("Not sym: " + optimal(root2));     // false

        System.out.println("Empty:   " + optimal(null));       // true
    }
}
