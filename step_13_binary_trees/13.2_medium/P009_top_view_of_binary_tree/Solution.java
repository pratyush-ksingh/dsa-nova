/**
 * Problem: Top View of Binary Tree
 * Difficulty: MEDIUM | XP: 25
 *
 * Return the top view of a binary tree: the first node visible at each
 * horizontal distance (column) when viewed from above.
 * Process left-to-right (sorted by column).
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
    // APPROACH 1: BRUTE FORCE - DFS collect (col, level, val), pick min level per col
    // Time: O(N log N)  |  Space: O(N)
    // For each column, the node at minimum level (closest to root) is visible from top.
    // ============================================================
    public static List<Integer> bruteForce(TreeNode root) {
        if (root == null) return new ArrayList<>();
        Map<Integer, int[]> map = new TreeMap<>(); // col -> [minLevel, val]
        dfsTop(root, 0, 0, map);
        List<Integer> result = new ArrayList<>();
        for (int[] entry : map.values()) result.add(entry[1]);
        return result;
    }

    private static void dfsTop(TreeNode node, int col, int level, Map<Integer, int[]> map) {
        if (node == null) return;
        if (!map.containsKey(col) || level < map.get(col)[0])
            map.put(col, new int[]{level, node.val});
        dfsTop(node.left, col - 1, level + 1, map);
        dfsTop(node.right, col + 1, level + 1, map);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - BFS (level order) guarantees first-seen = top view
    // Time: O(N)  |  Space: O(N)
    // BFS processes nodes level by level. First time we see a column = top view node.
    // Use TreeMap to keep columns sorted.
    // ============================================================
    public static List<Integer> optimal(TreeNode root) {
        if (root == null) return new ArrayList<>();
        TreeMap<Integer, Integer> topMap = new TreeMap<>();
        Queue<Object[]> queue = new LinkedList<>();
        queue.add(new Object[]{root, 0});
        while (!queue.isEmpty()) {
            Object[] cur = queue.poll();
            TreeNode node = (TreeNode) cur[0];
            int col = (int) cur[1];
            if (!topMap.containsKey(col)) topMap.put(col, node.val);
            if (node.left != null) queue.add(new Object[]{node.left, col - 1});
            if (node.right != null) queue.add(new Object[]{node.right, col + 1});
        }
        return new ArrayList<>(topMap.values());
    }

    // ============================================================
    // APPROACH 3: BEST - BFS with min/max col tracking (avoids TreeMap overhead)
    // Time: O(N)  |  Space: O(N)
    // Same BFS but track minCol and maxCol to build result array directly.
    // ============================================================
    public static List<Integer> best(TreeNode root) {
        if (root == null) return new ArrayList<>();
        // Use HashMap for O(1) access; collect min/max col for ordered output
        Map<Integer, Integer> topMap = new HashMap<>();
        int[] colRange = {0, 0};
        Queue<Object[]> queue = new LinkedList<>();
        queue.add(new Object[]{root, 0});
        while (!queue.isEmpty()) {
            Object[] cur = queue.poll();
            TreeNode node = (TreeNode) cur[0];
            int col = (int) cur[1];
            topMap.putIfAbsent(col, node.val);
            colRange[0] = Math.min(colRange[0], col);
            colRange[1] = Math.max(colRange[1], col);
            if (node.left != null) queue.add(new Object[]{node.left, col - 1});
            if (node.right != null) queue.add(new Object[]{node.right, col + 1});
        }
        List<Integer> result = new ArrayList<>();
        for (int c = colRange[0]; c <= colRange[1]; c++) result.add(topMap.get(c));
        return result;
    }

    private static TreeNode build(int[] arr, int i) {
        if (i >= arr.length || arr[i] == -1) return null;
        TreeNode node = new TreeNode(arr[i]);
        node.left = build(arr, 2 * i + 1);
        node.right = build(arr, 2 * i + 2);
        return node;
    }

    public static void main(String[] args) {
        System.out.println("=== Top View of Binary Tree ===");

        //     1
        //    / \
        //   2   3
        //  / \   \
        // 4   5   6
        // Top view: [4, 2, 1, 3, 6]
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        root1.right.right = new TreeNode(6);
        System.out.println("Brute:   " + bruteForce(root1));  // [4,2,1,3,6]
        System.out.println("Optimal: " + optimal(root1));
        System.out.println("Best:    " + best(root1));

        //     1
        //    / \
        //   2   3
        //    \
        //     4
        //      \
        //       5
        // Top view: [2, 1, 3, 4, 5] -> actually [2,1,3] visible (4 and 5 hidden by 2/1/3)?
        // 4 is at col=0 (same as root 1 but deeper), 5 at col=1 (same as 3 but deeper)
        // Top view: [2, 1, 3] for cols -1,0,1? No: 4 is right of 2 (col0), right of that is col1...
        // Let's just print and observe
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(3);
        root2.left.right = new TreeNode(4);
        root2.left.right.right = new TreeNode(5);
        System.out.println("Brute:   " + bruteForce(root2));
        System.out.println("Optimal: " + optimal(root2));
    }
}
