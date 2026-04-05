/**
 * Problem: Introduction to Trees
 * Difficulty: EASY | XP: 10
 *
 * Understand tree basics, build a binary tree, and implement utility methods.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // TreeNode definition
    // ============================================================
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }

    // ============================================================
    // APPROACH 1: Build tree manually (direct construction)
    // ============================================================
    public static TreeNode buildManually() {
        /*
         *       1
         *      / \
         *     2   3
         *    / \
         *   4   5
         */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        return root;
    }

    // ============================================================
    // APPROACH 2: Build tree from level-order array
    // Uses null representation: -1 means no node
    // Time: O(n) | Space: O(n)
    // ============================================================
    public static TreeNode buildFromArray(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;

        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;

        while (!queue.isEmpty() && i < arr.length) {
            TreeNode current = queue.poll();

            // Left child
            if (i < arr.length && arr[i] != null) {
                current.left = new TreeNode(arr[i]);
                queue.offer(current.left);
            }
            i++;

            // Right child
            if (i < arr.length && arr[i] != null) {
                current.right = new TreeNode(arr[i]);
                queue.offer(current.right);
            }
            i++;
        }
        return root;
    }

    // ============================================================
    // APPROACH 3: Utility functions
    // ============================================================

    // Count total nodes: O(n)
    public static int countNodes(TreeNode root) {
        if (root == null) return 0;
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    // Height of tree: O(n)
    public static int height(TreeNode root) {
        if (root == null) return -1; // height of empty tree is -1
        return 1 + Math.max(height(root.left), height(root.right));
    }

    // Check if node is a leaf
    public static boolean isLeaf(TreeNode node) {
        return node != null && node.left == null && node.right == null;
    }

    // Count leaves: O(n)
    public static int countLeaves(TreeNode root) {
        if (root == null) return 0;
        if (isLeaf(root)) return 1;
        return countLeaves(root.left) + countLeaves(root.right);
    }

    // Level-order print (BFS): O(n)
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Introduction to Trees ===\n");

        // --- Manual Construction ---
        System.out.println("--- Approach 1: Manual Construction ---");
        TreeNode root = buildManually();
        System.out.println("Level order: " + levelOrder(root));  // [[1],[2,3],[4,5]]
        System.out.println("Count: " + countNodes(root));         // 5
        System.out.println("Height: " + height(root));            // 2
        System.out.println("Leaves: " + countLeaves(root));       // 3
        System.out.println("Is root a leaf? " + isLeaf(root));    // false
        System.out.println("Is node 4 a leaf? " + isLeaf(root.left.left)); // true

        // --- Array Construction ---
        System.out.println("\n--- Approach 2: From Array ---");
        Integer[] arr = {1, 2, 3, 4, 5, null, 6};
        TreeNode root2 = buildFromArray(arr);
        System.out.println("Level order: " + levelOrder(root2));  // [[1],[2,3],[4,5,6]]
        System.out.println("Count: " + countNodes(root2));         // 6
        System.out.println("Height: " + height(root2));            // 2

        // --- Edge Cases ---
        System.out.println("\n--- Edge Cases ---");
        System.out.println("Empty tree height: " + height(null));    // -1
        System.out.println("Empty tree count: " + countNodes(null)); // 0

        TreeNode single = new TreeNode(42);
        System.out.println("Single node height: " + height(single));  // 0
        System.out.println("Single node is leaf: " + isLeaf(single)); // true

        // Skewed tree
        TreeNode skewed = new TreeNode(1);
        skewed.right = new TreeNode(2);
        skewed.right.right = new TreeNode(3);
        skewed.right.right.right = new TreeNode(4);
        System.out.println("Skewed tree height: " + height(skewed));  // 3
        System.out.println("Skewed tree levels: " + levelOrder(skewed));
    }
}
