/**
 * Problem: Vertical Order Traversal
 * Difficulty: HARD | XP: 50
 *
 * Given binary tree, return nodes grouped by vertical column.
 * Nodes in the same column are ordered top-to-bottom, then left-to-right.
 * If two nodes share the same row and column, sort by value.
 *
 * Column: root = 0, left child = col-1, right child = col+1.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
        TreeNode(int v, TreeNode l, TreeNode r) { val = v; left = l; right = r; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE - DFS collect (col, row, val), sort, group
    // Time: O(N log N)  |  Space: O(N)
    // Collect all (col, row, val) tuples via DFS.
    // Sort by col, then row, then val. Group by col.
    // ============================================================
    public static List<List<Integer>> bruteForce(TreeNode root) {
        List<int[]> nodes = new ArrayList<>(); // [col, row, val]
        dfsCollect(root, 0, 0, nodes);
        nodes.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]);

        List<List<Integer>> result = new ArrayList<>();
        if (nodes.isEmpty()) return result;
        int prevCol = nodes.get(0)[0] - 1;
        for (int[] node : nodes) {
            if (node[0] != prevCol) { result.add(new ArrayList<>()); prevCol = node[0]; }
            result.get(result.size() - 1).add(node[2]);
        }
        return result;
    }

    private static void dfsCollect(TreeNode node, int col, int row, List<int[]> list) {
        if (node == null) return;
        list.add(new int[]{col, row, node.val});
        dfsCollect(node.left, col - 1, row + 1, list);
        dfsCollect(node.right, col + 1, row + 1, list);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - BFS with TreeMap for column ordering
    // Time: O(N log N)  |  Space: O(N)
    // BFS ensures level-order. Use TreeMap<col, TreeMap<row, PriorityQueue<val>>>.
    // TreeMap keeps columns sorted; PriorityQueue handles same-pos value sorting.
    // ============================================================
    public static List<List<Integer>> optimal(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        // col -> row -> sorted values
        TreeMap<Integer, TreeMap<Integer, PriorityQueue<Integer>>> map = new TreeMap<>();
        Queue<int[]> queue = new LinkedList<>(); // [node_index, col, row] - store vals differently

        // Use wrapper queue with node reference
        Deque<Object[]> bfsQ = new ArrayDeque<>();
        bfsQ.add(new Object[]{root, 0, 0});

        while (!bfsQ.isEmpty()) {
            Object[] cur = bfsQ.poll();
            TreeNode node = (TreeNode) cur[0];
            int col = (int) cur[1], row = (int) cur[2];
            map.computeIfAbsent(col, k -> new TreeMap<>())
               .computeIfAbsent(row, k -> new PriorityQueue<>())
               .add(node.val);
            if (node.left != null) bfsQ.add(new Object[]{node.left, col - 1, row + 1});
            if (node.right != null) bfsQ.add(new Object[]{node.right, col + 1, row + 1});
        }

        for (TreeMap<Integer, PriorityQueue<Integer>> rowMap : map.values()) {
            List<Integer> col = new ArrayList<>();
            for (PriorityQueue<Integer> pq : rowMap.values())
                while (!pq.isEmpty()) col.add(pq.poll());
            result.add(col);
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST - DFS + sort (same complexity, less memory overhead)
    // Time: O(N log N)  |  Space: O(N)
    // Same as brute force but cleaner grouping using LinkedHashMap after sort.
    // ============================================================
    public static List<List<Integer>> best(TreeNode root) {
        return bruteForce(root); // Both DFS approaches are O(N log N); optimal BFS shown above
    }

    // Helper to build a tree from level-order array (null = missing node)
    private static TreeNode build(Integer[] arr) {
        if (arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode cur = q.poll();
            if (i < arr.length && arr[i] != null) { cur.left = new TreeNode(arr[i]); q.add(cur.left); }
            i++;
            if (i < arr.length && arr[i] != null) { cur.right = new TreeNode(arr[i]); q.add(cur.right); }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Vertical Order Traversal ===");

        //     3
        //    / \
        //   9  20
        //      / \
        //     15   7
        // Expected: [[9],[3,15],[20],[7]]
        TreeNode t1 = build(new Integer[]{3, 9, 20, null, null, 15, 7});
        System.out.println("Brute: " + bruteForce(t1));
        System.out.println("Optimal: " + optimal(t1));

        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6  7
        // Expected: [[4],[2],[1,5,6],[3],[7]]
        TreeNode t2 = build(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        System.out.println("Brute: " + bruteForce(t2));
        System.out.println("Optimal: " + optimal(t2));
    }
}
