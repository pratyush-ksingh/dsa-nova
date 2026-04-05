/**
 * Problem: Search in BST (LeetCode #700)
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
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- DFS ignoring BST property
    // Time: O(n)  |  Space: O(h)
    // ============================================================
    public static TreeNode searchBrute(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;
        TreeNode left = searchBrute(root.left, val);
        if (left != null) return left;
        return searchBrute(root.right, val);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive BST Search
    // Time: O(h)  |  Space: O(h)
    // ============================================================
    public static TreeNode searchRecursive(TreeNode root, int val) {
        if (root == null) return null;
        if (val == root.val) return root;
        if (val < root.val) return searchRecursive(root.left, val);
        return searchRecursive(root.right, val);
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative BST Search
    // Time: O(h)  |  Space: O(1)
    // ============================================================
    public static TreeNode searchIterative(TreeNode root, int val) {
        TreeNode current = root;
        while (current != null) {
            if (val == current.val) return current;
            if (val < current.val) current = current.left;
            else current = current.right;
        }
        return null;
    }

    // Helper: build BST from level-order array
    public static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < arr.length) {
            TreeNode curr = queue.poll();
            if (i < arr.length && arr[i] != null) {
                curr.left = new TreeNode(arr[i]);
                queue.offer(curr.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                curr.right = new TreeNode(arr[i]);
                queue.offer(curr.right);
            }
            i++;
        }
        return root;
    }

    // Helper: level-order for display
    public static List<Integer> levelOrder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            result.add(node.val);
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Search in BST ===\n");

        Integer[] arr = {4, 2, 7, 1, 3};
        TreeNode root = buildTree(arr);

        // Test 1: val exists
        int val1 = 2;
        TreeNode r1 = searchIterative(root, val1);
        System.out.println("Search for " + val1 + ": " + (r1 != null ? levelOrder(r1) : "null"));
        // [2, 1, 3]

        // Test 2: val does not exist
        int val2 = 5;
        TreeNode r2 = searchIterative(root, val2);
        System.out.println("Search for " + val2 + ": " + (r2 != null ? levelOrder(r2) : "null"));
        // null

        // Test 3: val at root
        int val3 = 4;
        TreeNode r3 = searchRecursive(root, val3);
        System.out.println("Search for " + val3 + ": " + (r3 != null ? levelOrder(r3) : "null"));
        // [4, 2, 7, 1, 3]

        // Test 4: single node
        TreeNode single = new TreeNode(1);
        System.out.println("Search 1 in [1]: " + (searchIterative(single, 1) != null ? "found" : "null"));
        System.out.println("Search 2 in [1]: " + (searchIterative(single, 2) != null ? "found" : "null"));

        // Test 5: empty tree
        System.out.println("Search in empty: " + (searchIterative(null, 1) != null ? "found" : "null"));

        // Verify all approaches agree
        System.out.println("\n--- Consistency Check ---");
        for (int v : new int[]{1, 2, 3, 4, 5, 7}) {
            TreeNode b = searchBrute(root, v);
            TreeNode r = searchRecursive(root, v);
            TreeNode it = searchIterative(root, v);
            boolean match = (b == null && r == null && it == null) ||
                           (b != null && r != null && it != null &&
                            b.val == r.val && r.val == it.val);
            System.out.printf("  val=%d: brute=%s, recursive=%s, iterative=%s %s%n",
                    v,
                    b != null ? b.val : "null",
                    r != null ? r.val : "null",
                    it != null ? it.val : "null",
                    match ? "MATCH" : "MISMATCH");
        }
    }
}
