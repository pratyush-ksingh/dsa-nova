/**
 * Problem: Requirements for Unique Binary Tree
 * Difficulty: MEDIUM | XP: 25
 *
 * Given two types of traversals, determine whether they uniquely identify
 * a binary tree structure.
 *
 * Rules:
 *   - Preorder + Inorder  -> UNIQUE (classic reconstruct)
 *   - Postorder + Inorder -> UNIQUE
 *   - Preorder + Postorder -> NOT unique (ambiguous for non-full trees)
 *   - Any single traversal -> NOT unique
 *   - Level order + Inorder -> UNIQUE
 *   - Preorder + Postorder WITH full BT constraint -> UNIQUE
 *
 * This is a knowledge-based problem; the "approaches" demonstrate
 * reconstruction from different traversal pairs.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Check uniqueness by enumeration (small trees)
    // Time: O(N!)  |  Space: O(N)
    // Enumerate all possible BST/BT structures and check traversal match.
    // NOTE: This is illustrative — practical answer is the rule-based approach below.
    // In practice: implement reconstruction and check if result is unique.
    // ============================================================
    public static String bruteForce(String t1, String t2) {
        // Rule-based: given traversal type names, return if combination is unique
        Set<String> unique = new HashSet<>(Arrays.asList(
            "preorder+inorder", "inorder+preorder",
            "postorder+inorder", "inorder+postorder",
            "levelorder+inorder", "inorder+levelorder"
        ));
        String key = (t1 + "+" + t2).toLowerCase();
        return unique.contains(key) ? "UNIQUE" : "NOT UNIQUE";
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Reconstruct from preorder + inorder
    // Time: O(N)  |  Space: O(N) with index map
    // Classic divide & conquer: root = preorder[0],
    // split inorder at root index to get left/right subtrees
    // ============================================================
    public static TreeNode buildFromPreIn(int[] preorder, int[] inorder) {
        Map<Integer, Integer> inIdx = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) inIdx.put(inorder[i], i);
        return buildPreIn(preorder, 0, preorder.length - 1,
                          0, inorder.length - 1, inIdx);
    }

    private static TreeNode buildPreIn(int[] pre, int preL, int preR,
                                        int inL, int inR, Map<Integer, Integer> inIdx) {
        if (preL > preR) return null;
        TreeNode root = new TreeNode(pre[preL]);
        int k = inIdx.get(root.val) - inL; // left subtree size
        root.left  = buildPreIn(pre, preL + 1,     preL + k, inL,     inL + k - 1, inIdx);
        root.right = buildPreIn(pre, preL + k + 1, preR,     inL + k + 1, inR,     inIdx);
        return root;
    }

    // ============================================================
    // APPROACH 3: BEST - Reconstruct from postorder + inorder
    // Time: O(N)  |  Space: O(N)
    // Root = postorder[last]; split inorder at root for left/right.
    // ============================================================
    public static TreeNode buildFromPostIn(int[] postorder, int[] inorder) {
        Map<Integer, Integer> inIdx = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) inIdx.put(inorder[i], i);
        return buildPostIn(postorder, 0, postorder.length - 1,
                           0, inorder.length - 1, inIdx);
    }

    private static TreeNode buildPostIn(int[] post, int postL, int postR,
                                         int inL, int inR, Map<Integer, Integer> inIdx) {
        if (postL > postR) return null;
        TreeNode root = new TreeNode(post[postR]);
        int k = inIdx.get(root.val) - inL;
        root.left  = buildPostIn(post, postL,     postL + k - 1, inL,         inL + k - 1, inIdx);
        root.right = buildPostIn(post, postL + k, postR - 1,     inL + k + 1, inR,         inIdx);
        return root;
    }

    // Helper: inorder traversal for verification
    static List<Integer> inorder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorderHelper(root, res);
        return res;
    }
    static void inorderHelper(TreeNode n, List<Integer> r) {
        if (n == null) return;
        inorderHelper(n.left, r); r.add(n.val); inorderHelper(n.right, r);
    }

    public static void main(String[] args) {
        System.out.println("=== Requirements for Unique Binary Tree ===");

        // Rule-based uniqueness check
        System.out.println("\n-- Uniqueness Rules --");
        String[][] pairs = {
            {"preorder", "inorder"},
            {"postorder", "inorder"},
            {"preorder", "postorder"},
            {"levelorder", "inorder"},
            {"preorder", "preorder"},
        };
        for (String[] p : pairs) {
            System.out.println(p[0] + " + " + p[1] + " -> " + bruteForce(p[0], p[1]));
        }

        // Reconstruct tree from preorder + inorder
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder  = {9, 3, 15, 20, 7};
        System.out.println("\n-- Reconstruct from preorder + inorder --");
        System.out.println("Preorder: " + Arrays.toString(preorder));
        System.out.println("Inorder:  " + Arrays.toString(inorder));
        TreeNode tree1 = buildFromPreIn(preorder, inorder);
        System.out.println("Inorder of reconstructed: " + inorder(tree1)); // [9,3,15,20,7]

        // Reconstruct from postorder + inorder
        int[] postorder = {9, 15, 7, 20, 3};
        System.out.println("\n-- Reconstruct from postorder + inorder --");
        System.out.println("Postorder: " + Arrays.toString(postorder));
        System.out.println("Inorder:   " + Arrays.toString(inorder));
        TreeNode tree2 = buildFromPostIn(postorder, inorder);
        System.out.println("Inorder of reconstructed: " + inorder(tree2)); // [9,3,15,20,7]
    }
}
