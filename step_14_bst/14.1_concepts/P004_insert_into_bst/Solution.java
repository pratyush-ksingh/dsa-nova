/**
 * Problem: Insert into a Binary Search Tree
 * Difficulty: MEDIUM | XP: 25
 * LeetCode: #701
 *
 * Given the root of a BST and a value, insert the value into the BST
 * and return the root. The input guarantees no duplicate values.
 * Return any valid BST after insertion.
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
    // APPROACH 1: BRUTE FORCE -- Iterative with parent tracking
    // Time: O(h)  |  Space: O(1)
    // ============================================================
    public static TreeNode insertIntoBSTBrute(TreeNode root, int val) {
        TreeNode newNode = new TreeNode(val);
        if (root == null) return newNode;

        TreeNode current = root;
        while (true) {
            if (val < current.val) {
                if (current.left == null) {
                    current.left = newNode;
                    break;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = newNode;
                    break;
                }
                current = current.right;
            }
        }
        return root;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive insert
    // Time: O(h)  |  Space: O(h) -- recursion stack
    // ============================================================
    public static TreeNode insertIntoBSTOptimal(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);

        if (val < root.val) {
            root.left = insertIntoBSTOptimal(root.left, val);
        } else {
            root.right = insertIntoBSTOptimal(root.right, val);
        }
        return root;
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative with explicit parent + direction, O(1) space
    // Time: O(h)  |  Space: O(1)
    // ============================================================
    public static TreeNode insertIntoBSTBest(TreeNode root, int val) {
        TreeNode newNode = new TreeNode(val);
        if (root == null) return newNode;

        TreeNode parent = null;
        TreeNode current = root;
        boolean isLeft = false;

        while (current != null) {
            parent = current;
            if (val < current.val) {
                current = current.left;
                isLeft = true;
            } else {
                current = current.right;
                isLeft = false;
            }
        }

        if (isLeft) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        return root;
    }

    // ============================================================
    // Helpers
    // ============================================================
    public static List<Integer> inorderOf(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inHelper(root, res);
        return res;
    }

    private static void inHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        inHelper(node.left, res);
        res.add(node.val);
        inHelper(node.right, res);
    }

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

    public static void main(String[] args) {
        System.out.println("=== Insert into BST ===\n");

        Integer[][] treeArrs = {
            {4, 2, 7, 1, 3},
            {40, 20, 60, 10, 30, 50, 70},
            {},
            {1},
            {5}
        };
        int[] vals = {5, 25, 5, 2, 3};
        int[][] expectedInorders = {
            {1, 2, 3, 4, 5, 7},
            {10, 20, 25, 30, 40, 50, 60, 70},
            {5},
            {1, 2},
            {3, 5}
        };

        for (int t = 0; t < vals.length; t++) {
            int val = vals[t];
            TreeNode r1 = insertIntoBSTBrute(buildTree(treeArrs[t]), val);
            TreeNode r2 = insertIntoBSTOptimal(buildTree(treeArrs[t]), val);
            TreeNode r3 = insertIntoBSTBest(buildTree(treeArrs[t]), val);

            List<Integer> io1 = inorderOf(r1), io2 = inorderOf(r2), io3 = inorderOf(r3);
            List<Integer> exp = new ArrayList<>();
            for (int v : expectedInorders[t]) exp.add(v);

            System.out.printf("Tree=%s, insert=%d%n", Arrays.toString(treeArrs[t]), val);
            System.out.printf("  Brute:   %s%n", io1);
            System.out.printf("  Optimal: %s%n", io2);
            System.out.printf("  Best:    %s%n", io3);
            System.out.printf("  Expected:%s%n", exp);
            System.out.printf("  %s%n%n", io1.equals(exp) && io2.equals(exp) && io3.equals(exp) ? "PASS" : "FAIL");
        }
    }
}
