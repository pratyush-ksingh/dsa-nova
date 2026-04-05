/**
 * Problem: Construct Binary Tree from Inorder and Postorder Traversal
 * Difficulty: MEDIUM | XP: 25
 * LeetCode: #106
 *
 * Given inorder and postorder traversal arrays of a binary tree,
 * construct and return the binary tree.
 *
 * Key insight:
 *  - Postorder: [left][right][ROOT] -- last element is always the root
 *  - Inorder:   [left][ROOT][right] -- root splits into left/right subtrees
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
    // APPROACH 1: BRUTE FORCE -- Recursive with linear inorder search
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    public static TreeNode buildTreeBrute(int[] inorder, int[] postorder) {
        return buildBrute(inorder, 0, inorder.length - 1,
                          postorder, 0, postorder.length - 1);
    }

    private static TreeNode buildBrute(int[] inorder, int inLo, int inHi,
                                        int[] postorder, int postLo, int postHi) {
        if (inLo > inHi || postLo > postHi) return null;

        int rootVal = postorder[postHi];
        TreeNode root = new TreeNode(rootVal);

        // Linear scan in inorder for root
        int mid = inLo;
        while (mid <= inHi && inorder[mid] != rootVal) mid++;

        int leftSize = mid - inLo;

        root.left  = buildBrute(inorder, inLo, mid - 1,
                                postorder, postLo, postLo + leftSize - 1);
        root.right = buildBrute(inorder, mid + 1, inHi,
                                postorder, postLo + leftSize, postHi - 1);
        return root;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashMap for O(1) inorder lookup
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static Map<Integer, Integer> idxMap;
    static int[] postArr;
    static int postIdx;

    public static TreeNode buildTreeOptimal(int[] inorder, int[] postorder) {
        idxMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) idxMap.put(inorder[i], i);
        postArr = postorder;
        postIdx = postorder.length - 1;
        return buildOptimal(0, inorder.length - 1);
    }

    private static TreeNode buildOptimal(int inLo, int inHi) {
        if (inLo > inHi) return null;

        int rootVal = postArr[postIdx--];
        TreeNode root = new TreeNode(rootVal);
        int mid = idxMap.get(rootVal);

        // Build right FIRST (postorder is consumed right-to-left)
        root.right = buildOptimal(mid + 1, inHi);
        root.left  = buildOptimal(inLo, mid - 1);
        return root;
    }

    // ============================================================
    // APPROACH 3: BEST -- HashMap + explicit postEnd parameter (no global state)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static TreeNode buildTreeBest(int[] inorder, int[] postorder) {
        if (inorder.length == 0) return null;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) map.put(inorder[i], i);
        return buildBest(inorder, postorder, map, 0, inorder.length - 1, postorder.length - 1);
    }

    private static TreeNode buildBest(int[] inorder, int[] postorder,
                                       Map<Integer, Integer> map,
                                       int inLo, int inHi, int postEnd) {
        if (inLo > inHi) return null;

        int rootVal = postorder[postEnd];
        TreeNode root = new TreeNode(rootVal);
        int mid = map.get(rootVal);
        int rightSize = inHi - mid;

        root.right = buildBest(inorder, postorder, map, mid + 1, inHi, postEnd - 1);
        root.left  = buildBest(inorder, postorder, map, inLo, mid - 1, postEnd - rightSize - 1);
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

    public static List<Integer> postorderOf(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postHelper(root, res);
        return res;
    }

    private static void postHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        postHelper(node.left, res);
        postHelper(node.right, res);
        res.add(node.val);
    }

    public static void main(String[] args) {
        System.out.println("=== Build Binary Tree from Inorder and Postorder ===\n");

        int[][] inorders   = {{9,3,15,20,7}, {2,1,3}, {1}, {-1}};
        int[][] postorders = {{9,15,7,20,3}, {2,3,1}, {1}, {-1}};

        for (int t = 0; t < inorders.length; t++) {
            int[] inorder = inorders[t];
            int[] postorder = postorders[t];

            TreeNode r1 = buildTreeBrute(inorder, postorder);
            TreeNode r2 = buildTreeOptimal(inorder, postorder);
            TreeNode r3 = buildTreeBest(inorder, postorder);

            List<Integer> io1 = inorderOf(r1),  po1 = postorderOf(r1);
            List<Integer> io2 = inorderOf(r2),  po2 = postorderOf(r2);
            List<Integer> io3 = inorderOf(r3),  po3 = postorderOf(r3);

            // Convert expected arrays to lists
            List<Integer> expIn = new ArrayList<>(), expPost = new ArrayList<>();
            for (int v : inorder)   expIn.add(v);
            for (int v : postorder) expPost.add(v);

            boolean ok = io1.equals(expIn) && io2.equals(expIn) && io3.equals(expIn)
                      && po1.equals(expPost) && po2.equals(expPost) && po3.equals(expPost);
            System.out.printf("inorder=%s postorder=%s%n",
                Arrays.toString(inorder), Arrays.toString(postorder));
            System.out.printf("  Brute   inorder=%s postorder=%s%n", io1, po1);
            System.out.printf("  Optimal inorder=%s postorder=%s%n", io2, po2);
            System.out.printf("  Best    inorder=%s postorder=%s%n", io3, po3);
            System.out.printf("  %s%n%n", ok ? "PASS" : "FAIL");
        }
    }
}
