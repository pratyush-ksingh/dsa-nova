/**
 * Problem: Maximum Width of Binary Tree (LeetCode 662)
 * Difficulty: HARD | XP: 25
 *
 * The width of a level is the number of nodes between the leftmost and
 * rightmost non-null nodes (inclusive), counting nulls between them.
 * Return the maximum width across all levels.
 *
 * Index trick (heap-style numbering):
 *   Root = 1
 *   Left child of i  = 2*i
 *   Right child of i = 2*i + 1
 * Width at a level = last_index - first_index + 1
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
    // APPROACH 1: BRUTE FORCE — BFS with null padding
    // Time: O(2^h)  |  Space: O(2^h)  [TLE on deep/skewed trees]
    // ============================================================
    /**
     * Physically include null nodes in the BFS queue to preserve positions.
     * At each level, strip leading/trailing nulls and measure width.
     * WARNING: exponential time/space — only feasible for very shallow trees.
     */
    public static int bruteForce(TreeNode root) {
        if (root == null) return 0;

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int maxWidth = 0;

        while (!queue.isEmpty()) {
            // Remove leading nulls
            while (!queue.isEmpty() && queue.peekFirst() == null) queue.pollFirst();
            // Remove trailing nulls
            while (!queue.isEmpty() && queue.peekLast() == null) queue.pollLast();

            if (queue.isEmpty()) break;

            int levelSize = queue.size();
            maxWidth = Math.max(maxWidth, levelSize);

            Deque<TreeNode> next = new ArrayDeque<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.pollFirst();
                if (node != null) {
                    next.offer(node.left);
                    next.offer(node.right);
                } else {
                    next.offer(null);
                    next.offer(null);
                }
            }
            queue = next;
        }
        return maxWidth;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — BFS with index tracking + normalization
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Assign heap-style indices: root=1, left child of i = 2i, right = 2i+1.
     * Width = last_index - first_index + 1 at each level.
     * Normalize per level (subtract first index) to prevent Long overflow
     * in deep trees (index can reach 2^n for n-node tree).
     */
    public static int optimal(TreeNode root) {
        if (root == null) return 0;

        // Queue holds [node_hashCode_trick... use Object[]{node, index}]
        Deque<long[]> queue = new ArrayDeque<>(); // long[0]=nodeId not needed; use Object queue
        // Actually use Object[] pairs: {TreeNode, Long index}
        Deque<Object[]> bfsQ = new ArrayDeque<>();
        bfsQ.offer(new Object[]{root, 1L});
        int maxWidth = 0;

        while (!bfsQ.isEmpty()) {
            int levelSize = bfsQ.size();
            long firstIdx = (long) ((Object[]) bfsQ.peekFirst())[1];
            long levelFirst = 0, levelLast = 0;

            for (int i = 0; i < levelSize; i++) {
                Object[] pair = bfsQ.poll();
                TreeNode node = (TreeNode) pair[0];
                long idx = (long) pair[1] - firstIdx; // normalize

                if (i == 0) levelFirst = idx;
                levelLast = idx;

                if (node.left  != null) bfsQ.offer(new Object[]{node.left,  2 * (idx + firstIdx)});
                if (node.right != null) bfsQ.offer(new Object[]{node.right, 2 * (idx + firstIdx) + 1});
            }
            maxWidth = (int) Math.max(maxWidth, levelLast - levelFirst + 1);
        }
        return maxWidth;
    }

    // ============================================================
    // APPROACH 3: BEST — BFS with clean per-level 0-based normalization
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Same as Approach 2 but normalize at child-enqueue time:
     * children get indices 2*(idx) and 2*(idx)+1 where idx is
     * already the normalized index within the current level.
     * This ensures indices never grow beyond n at any level.
     * Uses long to be safe for edge cases.
     */
    public static int best(TreeNode root) {
        if (root == null) return 0;

        Deque<Object[]> queue = new ArrayDeque<>(); // {TreeNode, long index}
        queue.offer(new Object[]{root, 0L});
        int maxWidth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            long levelFirst = (long) ((Object[]) queue.peekFirst())[1];
            long start = 0, end = 0;

            for (int i = 0; i < levelSize; i++) {
                Object[] pair = queue.poll();
                TreeNode node = (TreeNode) pair[0];
                long idx = (long) pair[1] - levelFirst; // normalize to 0-based

                if (i == 0) start = idx;
                end = idx;

                if (node.left  != null) queue.offer(new Object[]{node.left,  2 * idx});
                if (node.right != null) queue.offer(new Object[]{node.right, 2 * idx + 1});
            }
            maxWidth = (int) Math.max(maxWidth, end - start + 1);
        }
        return maxWidth;
    }

    // Helper: build tree from level-order array
    private static TreeNode buildTree(Integer[] vals) {
        if (vals == null || vals.length == 0 || vals[0] == null) return null;
        TreeNode root = new TreeNode(vals[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode node = q.poll();
            if (i < vals.length && vals[i] != null) {
                node.left = new TreeNode(vals[i]);
                q.offer(node.left);
            }
            i++;
            if (i < vals.length && vals[i] != null) {
                node.right = new TreeNode(vals[i]);
                q.offer(node.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Max Width of Binary Tree ===");

        Integer[][] trees = {
            {1, 3, 2, 5, 3, null, 9},
            {1, 3, 2, 5, null, null, 9, 6, null, 7},
            {1},
            {1, 3, null, 5, 3}
        };
        int[] expected = {4, 7, 1, 2};

        for (int i = 0; i < trees.length; i++) {
            System.out.println("Tree=" + Arrays.toString(trees[i]));
            System.out.println("  Brute:   " + bruteForce(buildTree(trees[i])) + " (expected " + expected[i] + ")");
            System.out.println("  Optimal: " + optimal(buildTree(trees[i])));
            System.out.println("  Best:    " + best(buildTree(trees[i])));
        }
    }
}
