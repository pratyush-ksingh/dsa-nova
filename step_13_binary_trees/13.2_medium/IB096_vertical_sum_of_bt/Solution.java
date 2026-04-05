import java.util.*;

/**
 * Vertical Sum of Binary Tree
 *
 * Sum all node values at each vertical line (horizontal distance from root).
 * Return sums from leftmost to rightmost vertical line.
 */
public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- DFS + HashMap + Sort Keys
    // Time: O(N + k log k)  |  Space: O(N)
    // ============================================================
    public static ArrayList<Long> bruteForce(TreeNode root) {
        ArrayList<Long> result = new ArrayList<>();
        if (root == null) return result;

        HashMap<Integer, Long> map = new HashMap<>();
        dfsBrute(root, 0, map);

        List<Integer> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        for (int hd : keys) {
            result.add(map.get(hd));
        }
        return result;
    }

    private static void dfsBrute(TreeNode node, int hd, HashMap<Integer, Long> map) {
        if (node == null) return;
        map.put(hd, map.getOrDefault(hd, 0L) + node.val);
        dfsBrute(node.left, hd - 1, map);
        dfsBrute(node.right, hd + 1, map);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DFS + TreeMap (auto-sorted keys)
    // Time: O(N log k)  |  Space: O(N)
    // ============================================================
    public static ArrayList<Long> optimal(TreeNode root) {
        ArrayList<Long> result = new ArrayList<>();
        if (root == null) return result;

        TreeMap<Integer, Long> map = new TreeMap<>();
        dfsOptimal(root, 0, map);
        result.addAll(map.values());
        return result;
    }

    private static void dfsOptimal(TreeNode node, int hd, TreeMap<Integer, Long> map) {
        if (node == null) return;
        map.merge(hd, (long) node.val, Long::sum);
        dfsOptimal(node.left, hd - 1, map);
        dfsOptimal(node.right, hd + 1, map);
    }

    // ============================================================
    // APPROACH 3: BEST -- BFS + HashMap + min/max HD tracking
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    public static ArrayList<Long> best(TreeNode root) {
        ArrayList<Long> result = new ArrayList<>();
        if (root == null) return result;

        HashMap<Integer, Long> map = new HashMap<>();
        int minHD = 0, maxHD = 0;

        // BFS with (node, hd) pairs
        Queue<TreeNode> nodeQ = new LinkedList<>();
        Queue<Integer> hdQ = new LinkedList<>();
        nodeQ.offer(root);
        hdQ.offer(0);

        while (!nodeQ.isEmpty()) {
            TreeNode node = nodeQ.poll();
            int hd = hdQ.poll();

            map.merge(hd, (long) node.val, Long::sum);
            minHD = Math.min(minHD, hd);
            maxHD = Math.max(maxHD, hd);

            if (node.left != null) {
                nodeQ.offer(node.left);
                hdQ.offer(hd - 1);
            }
            if (node.right != null) {
                nodeQ.offer(node.right);
                hdQ.offer(hd + 1);
            }
        }

        for (int hd = minHD; hd <= maxHD; hd++) {
            result.add(map.getOrDefault(hd, 0L));
        }
        return result;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        //        1
        //       / \
        //      2    3
        //     / \  / \
        //    4   5 6   7
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        System.out.println("=== Vertical Sum of Binary Tree ===");
        System.out.println("Brute:   " + bruteForce(root));   // [4, 2, 12, 3, 7]
        System.out.println("Optimal: " + optimal(root));       // [4, 2, 12, 3, 7]
        System.out.println("Best:    " + best(root));           // [4, 2, 12, 3, 7]
    }
}
