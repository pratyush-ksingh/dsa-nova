/**
 * Problem: Bottom View of Binary Tree
 * Difficulty: MEDIUM | XP: 25
 *
 * The bottom view of a binary tree contains, for each horizontal distance (HD),
 * the last node seen during BFS (deepest / rightmost at that column).
 * Return nodes left to right (sorted by HD).
 *
 * Horizontal Distance:
 *   Root = 0, Left child = parent HD - 1, Right child = parent HD + 1
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
    // APPROACH 1: BRUTE FORCE — DFS with depth tracking
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * DFS: track (depth, value) for each HD.
     * At each HD, keep the entry with the maximum depth.
     * If depths are equal, the later-visited (right-side) value wins
     * — but in DFS we visit left first, so right overwrites with >=.
     * Sort by HD and return values.
     */
    public static List<Integer> bruteForce(TreeNode root) {
        // hd -> int[]{depth, value}
        TreeMap<Integer, int[]> hdMap = new TreeMap<>();
        dfs(root, 0, 0, hdMap);

        List<Integer> result = new ArrayList<>();
        for (int[] entry : hdMap.values()) result.add(entry[1]);
        return result;
    }

    private static void dfs(TreeNode node, int hd, int depth, TreeMap<Integer, int[]> hdMap) {
        if (node == null) return;
        if (!hdMap.containsKey(hd) || depth >= hdMap.get(hd)[0]) {
            hdMap.put(hd, new int[]{depth, node.val});
        }
        dfs(node.left,  hd - 1, depth + 1, hdMap);
        dfs(node.right, hd + 1, depth + 1, hdMap);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — BFS with TreeMap
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * BFS guarantees that for any HD, the last node processed is
     * at the greatest depth (or same depth, rightmost).
     * Simply overwrite hdMap[hd] = node.val at each BFS step.
     * TreeMap keeps keys sorted → reading values gives left-to-right bottom view.
     */
    public static List<Integer> optimal(TreeNode root) {
        if (root == null) return new ArrayList<>();

        TreeMap<Integer, Integer> hdMap = new TreeMap<>();
        Queue<int[]> queue = new LinkedList<>(); // [node_ref via index trick — use Object queue]

        // Use Object queue to hold (TreeNode, hd) pairs
        Queue<Object[]> bfsQ = new LinkedList<>();
        bfsQ.offer(new Object[]{root, 0});

        while (!bfsQ.isEmpty()) {
            Object[] pair = bfsQ.poll();
            TreeNode node = (TreeNode) pair[0];
            int hd = (int) pair[1];

            hdMap.put(hd, node.val); // overwrite — BFS means later = deeper

            if (node.left  != null) bfsQ.offer(new Object[]{node.left,  hd - 1});
            if (node.right != null) bfsQ.offer(new Object[]{node.right, hd + 1});
        }

        return new ArrayList<>(hdMap.values());
    }

    // ============================================================
    // APPROACH 3: BEST — BFS level-by-level with TreeMap
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Process BFS level by level for clarity.
     * Within each level, overwriting ensures the rightmost node at any HD wins.
     * Between levels, deeper levels always overwrite shallower ones.
     * TreeMap provides sorted HD order for final output.
     */
    public static List<Integer> best(TreeNode root) {
        if (root == null) return new ArrayList<>();

        TreeMap<Integer, Integer> hdMap = new TreeMap<>();
        Queue<Object[]> queue = new LinkedList<>();
        queue.offer(new Object[]{root, 0});

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                Object[] pair = queue.poll();
                TreeNode node = (TreeNode) pair[0];
                int hd = (int) pair[1];

                hdMap.put(hd, node.val); // deeper level overwrites shallower

                if (node.left  != null) queue.offer(new Object[]{node.left,  hd - 1});
                if (node.right != null) queue.offer(new Object[]{node.right, hd + 1});
            }
        }

        return new ArrayList<>(hdMap.values());
    }

    // Helper: build tree from level-order array (null = missing node)
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
        System.out.println("=== Bottom View of Binary Tree ===");

        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6  7
        // Bottom view: [4, 2, 6, 3, 7]
        // HD: 4→-2, 2→-1, {5,6}→0(6 wins via BFS last), 3→+1, 7→+2
        TreeNode t1 = buildTree(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        System.out.println("Tree 1 Brute:   " + bruteForce(t1));
        System.out.println("Tree 1 Optimal: " + optimal(t1));
        System.out.println("Tree 1 Best:    " + best(t1));

        //       20
        //      /  \
        //     8    22
        //    / \
        //   5   3
        //      / \
        //     10  14
        // Bottom view: [5, 10, 3, 14, 22]
        TreeNode t2 = buildTree(new Integer[]{20, 8, 22, 5, 3, null, null, null, null, 10, 14});
        System.out.println("Tree 2 Optimal: " + optimal(t2));
    }
}
