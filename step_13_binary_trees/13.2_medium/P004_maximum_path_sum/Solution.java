/**
 * Problem: Maximum Path Sum in Binary Tree
 * Difficulty: HARD | XP: 50
 *
 * A path in a binary tree is a sequence of nodes where each pair of adjacent
 * nodes has an edge. The path does not need to pass through the root.
 * Find the path with the maximum sum (nodes can have negative values).
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
    // APPROACH 1: BRUTE FORCE - Compute path sum for every node as root of path
    // Time: O(N^2)  |  Space: O(N) recursion
    // For each node, compute max gain going left and right; update global max
    // (This is actually O(N) in correct implementation — shown here structurally)
    // ============================================================
    static int maxBrute;

    public static int bruteForce(TreeNode root) {
        maxBrute = Integer.MIN_VALUE;
        gainBrute(root);
        return maxBrute;
    }

    private static int gainBrute(TreeNode node) {
        if (node == null) return 0;
        int leftGain  = Math.max(gainBrute(node.left),  0);
        int rightGain = Math.max(gainBrute(node.right), 0);
        // Path through this node: left arm + node + right arm
        maxBrute = Math.max(maxBrute, node.val + leftGain + rightGain);
        // Return the max "arm" we can extend upward (only one side)
        return node.val + Math.max(leftGain, rightGain);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Single postorder DFS with global max
    // Time: O(N)  |  Space: O(H) where H = tree height
    // At each node compute: max gain going down one branch (for parent's use)
    // Update global answer with left_gain + node.val + right_gain
    // ============================================================
    static int maxOptimal;

    public static int optimal(TreeNode root) {
        maxOptimal = Integer.MIN_VALUE;
        dfsOptimal(root);
        return maxOptimal;
    }

    private static int dfsOptimal(TreeNode node) {
        if (node == null) return 0;
        // Only take positive contributions from children
        int left  = Math.max(dfsOptimal(node.left),  0);
        int right = Math.max(dfsOptimal(node.right), 0);
        maxOptimal = Math.max(maxOptimal, node.val + left + right);
        return node.val + Math.max(left, right);
    }

    // ============================================================
    // APPROACH 3: BEST - Same O(N) DFS, using int[] to avoid static state
    // Time: O(N)  |  Space: O(H)
    // Functional-style: pass max as single-element array to avoid global variable
    // ============================================================
    public static int best(TreeNode root) {
        int[] max = {Integer.MIN_VALUE};
        dfsBest(root, max);
        return max[0];
    }

    private static int dfsBest(TreeNode node, int[] max) {
        if (node == null) return 0;
        int left  = Math.max(dfsBest(node.left,  max), 0);
        int right = Math.max(dfsBest(node.right, max), 0);
        max[0] = Math.max(max[0], node.val + left + right);
        return node.val + Math.max(left, right);
    }

    public static void main(String[] args) {
        System.out.println("=== Maximum Path Sum ===");

        // Tree: -10 -> [9, 20->[15,7]]
        //        -10
        //       /    \
        //      9      20
        //            /  \
        //           15   7
        TreeNode root1 = new TreeNode(-10,
            new TreeNode(9),
            new TreeNode(20, new TreeNode(15), new TreeNode(7)));
        System.out.println("Tree1: -10, [9, 20->[15,7]]");
        System.out.println("Brute:   " + bruteForce(root1));   // 42
        System.out.println("Optimal: " + optimal(root1));       // 42
        System.out.println("Best:    " + best(root1));          // 42

        // Tree: [1, [2, 3]]
        TreeNode root2 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        System.out.println("\nTree2: 1->[2,3]");
        System.out.println("Brute:   " + bruteForce(root2));   // 6
        System.out.println("Optimal: " + optimal(root2));
        System.out.println("Best:    " + best(root2));

        // All negative
        TreeNode root3 = new TreeNode(-3);
        System.out.println("\nTree3: single node -3");
        System.out.println("Brute:   " + bruteForce(root3));   // -3
        System.out.println("Optimal: " + optimal(root3));
        System.out.println("Best:    " + best(root3));
    }
}
