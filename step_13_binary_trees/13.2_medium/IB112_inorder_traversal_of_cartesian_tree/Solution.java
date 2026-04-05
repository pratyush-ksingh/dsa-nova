/**
 * Problem: Inorder Traversal of Cartesian Tree (InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given the inorder traversal of a Cartesian tree, construct the tree and
 * return the root. Root = max element; left/right subtrees from elements
 * left/right of the max in the inorder array.
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
    // APPROACH 1: BRUTE FORCE -- Recursive with linear max search
    // Time: O(n^2)  |  Space: O(n) recursion stack
    // ============================================================
    public static TreeNode buildCartesianBrute(int[] inorder) {
        return buildBrute(inorder, 0, inorder.length - 1);
    }

    private static TreeNode buildBrute(int[] arr, int lo, int hi) {
        if (lo > hi) return null;
        // Find index of max in arr[lo..hi]
        int maxIdx = lo;
        for (int i = lo + 1; i <= hi; i++) {
            if (arr[i] > arr[maxIdx]) maxIdx = i;
        }
        TreeNode node = new TreeNode(arr[maxIdx]);
        node.left = buildBrute(arr, lo, maxIdx - 1);
        node.right = buildBrute(arr, maxIdx + 1, hi);
        return node;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Sparse Table for O(1) range-max queries
    // Time: O(n log n) preprocess + O(n) build  |  Space: O(n log n)
    // ============================================================
    static int[] sparseArr;
    static int[][] sparseIdx;
    static int[] log2Table;

    private static void buildSparseTable(int[] arr) {
        int n = arr.length;
        int LOG = 1;
        while ((1 << LOG) <= n) LOG++;
        sparseArr = arr;
        sparseIdx = new int[LOG][n];
        log2Table = new int[n + 1];

        for (int i = 2; i <= n; i++)
            log2Table[i] = log2Table[i / 2] + 1;

        for (int i = 0; i < n; i++) sparseIdx[0][i] = i;
        for (int k = 1; k < LOG; k++) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                int l = sparseIdx[k - 1][i];
                int r = sparseIdx[k - 1][i + (1 << (k - 1))];
                sparseIdx[k][i] = arr[l] >= arr[r] ? l : r;
            }
        }
    }

    private static int queryMaxIdx(int lo, int hi) {
        if (lo > hi) return -1;
        int k = log2Table[hi - lo + 1];
        int l = sparseIdx[k][lo];
        int r = sparseIdx[k][hi - (1 << k) + 1];
        return sparseArr[l] >= sparseArr[r] ? l : r;
    }

    public static TreeNode buildCartesianOptimal(int[] inorder) {
        if (inorder.length == 0) return null;
        buildSparseTable(inorder);
        return buildOptimal(inorder, 0, inorder.length - 1);
    }

    private static TreeNode buildOptimal(int[] arr, int lo, int hi) {
        if (lo > hi) return null;
        int maxIdx = queryMaxIdx(lo, hi);
        TreeNode node = new TreeNode(arr[maxIdx]);
        node.left = buildOptimal(arr, lo, maxIdx - 1);
        node.right = buildOptimal(arr, maxIdx + 1, hi);
        return node;
    }

    // ============================================================
    // APPROACH 3: BEST -- Stack-based single pass O(n)
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static TreeNode buildCartesianBest(int[] inorder) {
        if (inorder.length == 0) return null;

        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode root = null;

        for (int val : inorder) {
            TreeNode node = new TreeNode(val);
            TreeNode lastPopped = null;

            // Pop all nodes with smaller values -- they form left subtree
            while (!stack.isEmpty() && stack.peek().val < val) {
                lastPopped = stack.pop();
            }

            // Last popped becomes left child of current node
            node.left = lastPopped;

            // Current node becomes right child of new stack top
            if (!stack.isEmpty()) {
                stack.peek().right = node;
            } else {
                root = node;  // new root
            }

            stack.push(node);
        }
        return root;
    }

    // ============================================================
    // Helpers
    // ============================================================
    public static List<Integer> inorderOf(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorderHelper(root, res);
        return res;
    }

    private static void inorderHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        inorderHelper(node.left, res);
        res.add(node.val);
        inorderHelper(node.right, res);
    }

    public static List<Integer> preorderOf(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        preorderHelper(root, res);
        return res;
    }

    private static void preorderHelper(TreeNode node, List<Integer> res) {
        if (node == null) return;
        res.add(node.val);
        preorderHelper(node.left, res);
        preorderHelper(node.right, res);
    }

    public static void main(String[] args) {
        System.out.println("=== Inorder Traversal of Cartesian Tree ===\n");

        int[][] tests = {
            {1, 2, 3, 4, 5},
            {5, 10, 40, 30, 28},
            {3},
            {1},
        };

        for (int[] arr : tests) {
            TreeNode r1 = buildCartesianBrute(arr);
            TreeNode r2 = buildCartesianOptimal(arr);
            TreeNode r3 = buildCartesianBest(arr);
            List<Integer> io1 = inorderOf(r1), io2 = inorderOf(r2), io3 = inorderOf(r3);
            List<Integer> pre1 = preorderOf(r1), pre2 = preorderOf(r2), pre3 = preorderOf(r3);
            System.out.printf("Input: %s%n", Arrays.toString(arr));
            System.out.printf("  Brute   inorder=%s  preorder=%s%n", io1, pre1);
            System.out.printf("  Optimal inorder=%s  preorder=%s%n", io2, pre2);
            System.out.printf("  Best    inorder=%s  preorder=%s%n", io3, pre3);
            boolean ok = io1.equals(io2) && io2.equals(io3) && pre1.equals(pre2) && pre2.equals(pre3);
            System.out.printf("  %s%n%n", ok ? "PASS" : "FAIL");
        }
    }
}
