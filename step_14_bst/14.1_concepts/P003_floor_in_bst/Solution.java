import java.util.*;

/**
 * Floor in BST
 *
 * Find the largest value in BST that is <= given key.
 * Return -1 if no such value exists.
 */
public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Inorder to sorted array + search
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    public static int bruteForce(TreeNode root, int key) {
        List<Integer> sorted = new ArrayList<>();
        inorder(root, sorted);

        int floor = -1;
        for (int val : sorted) {
            if (val <= key) {
                floor = val; // keep updating; last one <= key is the largest
            } else {
                break; // sorted, so no point continuing
            }
        }
        return floor;
    }

    private static void inorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.val);
        inorder(node.right, list);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive BST search
    // Time: O(H)  |  Space: O(H)
    // ============================================================
    public static int optimal(TreeNode root, int key) {
        if (root == null) return -1;

        if (root.val == key) {
            return key; // exact match
        }

        if (root.val > key) {
            // Current node too large, floor must be in left subtree
            return optimal(root.left, key);
        }

        // root.val < key: current node is a candidate
        // But a better (larger) candidate might exist in right subtree
        int rightFloor = optimal(root.right, key);
        return (rightFloor != -1) ? rightFloor : root.val;
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative BST search
    // Time: O(H)  |  Space: O(1)
    // ============================================================
    public static int best(TreeNode root, int key) {
        int floor = -1;
        TreeNode curr = root;

        while (curr != null) {
            if (curr.val == key) {
                return key; // exact match is the best floor
            } else if (curr.val < key) {
                floor = curr.val; // update candidate
                curr = curr.right; // look for larger candidate
            } else {
                curr = curr.left; // current too large, go left
            }
        }

        return floor;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        //       10
        //      /  \
        //     5    15
        //    / \   / \
        //   2   7 12  20
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(15);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(7);
        root.right.left = new TreeNode(12);
        root.right.right = new TreeNode(20);

        int[] testKeys = {13, 10, 1, 15, 8, 20, 25};

        System.out.println("=== Floor in BST ===");
        for (int key : testKeys) {
            System.out.printf("Key=%2d -> Brute: %2d | Optimal: %2d | Best: %2d%n",
                key, bruteForce(root, key), optimal(root, key), best(root, key));
        }
        // Key=13 -> 12, Key=10 -> 10, Key=1 -> -1, Key=15 -> 15
        // Key=8 -> 7, Key=20 -> 20, Key=25 -> 20
    }
}
