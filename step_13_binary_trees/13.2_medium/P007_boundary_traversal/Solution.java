/**
 * Problem: Boundary Traversal
 * Difficulty: MEDIUM | XP: 25
 *
 * Return the boundary of a binary tree in anti-clockwise order:
 * - Left boundary (top to bottom, excluding leaf)
 * - All leaves (left to right)
 * - Right boundary (bottom to top, excluding leaf and root)
 * Real-life use: Polygon boundary extraction, image contour tracing, map rendering.
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
    // APPROACH 1: BRUTE FORCE
    // Collect left boundary, leaves, and right boundary separately
    // using simple recursive helpers — straightforward but three passes.
    // Time: O(N)  |  Space: O(N) for output + O(H) recursion
    // ============================================================
    static class BruteForce {
        public static List<Integer> boundaryOfBinaryTree(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) return result;
            if (!isLeaf(root)) result.add(root.val);
            addLeftBoundary(root.left, result);
            addLeaves(root, result);
            addRightBoundary(root.right, result);
            return result;
        }

        private static boolean isLeaf(TreeNode node) {
            return node.left == null && node.right == null;
        }

        private static void addLeftBoundary(TreeNode node, List<Integer> res) {
            if (node == null || isLeaf(node)) return;
            res.add(node.val);
            if (node.left != null) addLeftBoundary(node.left, res);
            else                   addLeftBoundary(node.right, res);
        }

        private static void addLeaves(TreeNode node, List<Integer> res) {
            if (node == null) return;
            if (isLeaf(node)) { res.add(node.val); return; }
            addLeaves(node.left, res);
            addLeaves(node.right, res);
        }

        private static void addRightBoundary(TreeNode node, List<Integer> res) {
            if (node == null || isLeaf(node)) return;
            if (node.right != null) addRightBoundary(node.right, res);
            else                    addRightBoundary(node.left, res);
            res.add(node.val); // add after recursion = bottom-up
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Same three-part logic but iterative for left and right boundaries
    // to avoid stack overflow on deep trees.
    // Time: O(N)  |  Space: O(H) for right boundary reversal
    // ============================================================
    static class Optimal {
        public static List<Integer> boundaryOfBinaryTree(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) return result;
            if (!isLeaf(root)) result.add(root.val);

            // Left boundary (iterative)
            TreeNode node = root.left;
            while (node != null && !isLeaf(node)) {
                result.add(node.val);
                node = (node.left != null) ? node.left : node.right;
            }

            // Leaves (recursive DFS)
            addLeaves(root, result);

            // Right boundary (iterative, collect then reverse)
            Deque<Integer> rightBound = new ArrayDeque<>();
            node = root.right;
            while (node != null && !isLeaf(node)) {
                rightBound.push(node.val);
                node = (node.right != null) ? node.right : node.left;
            }
            while (!rightBound.isEmpty()) result.add(rightBound.pop());

            return result;
        }

        private static boolean isLeaf(TreeNode node) {
            return node.left == null && node.right == null;
        }

        private static void addLeaves(TreeNode node, List<Integer> res) {
            if (node == null) return;
            if (isLeaf(node)) { res.add(node.val); return; }
            addLeaves(node.left, res);
            addLeaves(node.right, res);
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Single DFS pass: classify each node as LEFT_BOUND, RIGHT_BOUND, LEAF, or NORMAL.
    // Process each node according to its role — one traversal, cleanest code.
    // Time: O(N)  |  Space: O(N) for output + O(H) recursion
    // ============================================================
    static class Best {
        private static final int LEFT_BOUND  = 0;
        private static final int RIGHT_BOUND = 1;
        private static final int NEITHER     = 2;

        public static List<Integer> boundaryOfBinaryTree(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) return result;
            dfs(root, LEFT_BOUND, result);
            return result;
        }

        private static void dfs(TreeNode node, int flag, List<Integer> res) {
            if (node == null) return;
            boolean isLeaf = (node.left == null && node.right == null);
            boolean isLeftBound  = (flag == LEFT_BOUND);
            boolean isRightBound = (flag == RIGHT_BOUND);

            // Add to result if: left boundary (pre-order), or leaf, or right boundary (post-order)
            if (isLeftBound || isLeaf) res.add(node.val);

            // Determine flags for children
            int leftFlag, rightFlag;
            if (isLeftBound) {
                leftFlag  = LEFT_BOUND;
                rightFlag = (node.left == null) ? LEFT_BOUND : NEITHER;
            } else if (isRightBound) {
                leftFlag  = (node.right == null) ? RIGHT_BOUND : NEITHER;
                rightFlag = RIGHT_BOUND;
            } else {
                leftFlag = rightFlag = NEITHER;
            }

            dfs(node.left,  leftFlag,  res);
            dfs(node.right, rightFlag, res);

            if (isRightBound && !isLeaf) res.add(node.val); // post-order for right
        }
    }

    static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode node = q.poll();
            if (i < arr.length && arr[i] != null) { node.left = new TreeNode(arr[i]); q.offer(node.left); }
            i++;
            if (i < arr.length && arr[i] != null) { node.right = new TreeNode(arr[i]); q.offer(node.right); }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Boundary Traversal ===");

        Integer[][] trees = {
            {1, null, 2, 3, 4},
            {1, 2, 3, 4, 5, 6, null, null, null, 7, 8, 9, 10},
            {1},
        };

        for (Integer[] arr : trees) {
            TreeNode root = buildTree(arr);
            System.out.printf("%nTree=%s%n", Arrays.toString(arr));
            System.out.println("  Brute  : " + BruteForce.boundaryOfBinaryTree(root));
            System.out.println("  Optimal: " + Optimal.boundaryOfBinaryTree(root));
            System.out.println("  Best   : " + Best.boundaryOfBinaryTree(root));
        }
    }
}
