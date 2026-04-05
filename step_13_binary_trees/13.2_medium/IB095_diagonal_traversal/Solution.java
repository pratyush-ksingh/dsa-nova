/**
 * Problem: Diagonal Traversal (InterviewBit)
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
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
    // APPROACH 1: BRUTE FORCE -- HashMap + DFS
    // Time: O(n + d*log(d))  |  Space: O(n)
    // Assign diagonal IDs via DFS, group into map, sort keys.
    // ============================================================
    public static List<List<Integer>> bruteForce(TreeNode root) {
        Map<Integer, List<Integer>> map = new TreeMap<>();
        dfs(root, 0, map);

        return new ArrayList<>(map.values());
    }

    private static void dfs(TreeNode node, int d, Map<Integer, List<Integer>> map) {
        if (node == null) return;
        map.computeIfAbsent(d, k -> new ArrayList<>()).add(node.val);
        dfs(node.left, d + 1, map);   // Left child: next diagonal
        dfs(node.right, d, map);       // Right child: same diagonal
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BFS with Right-Chain Following
    // Time: O(n)  |  Space: O(n)
    // Process diagonals one at a time. Follow right chains,
    // enqueue left children for the next diagonal.
    // ============================================================
    public static List<List<Integer>> optimal(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> diagonal = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();

                // Follow the entire right chain (same diagonal)
                while (node != null) {
                    diagonal.add(node.val);
                    if (node.left != null) {
                        queue.add(node.left);  // Left child -> next diagonal
                    }
                    node = node.right;  // Stay on same diagonal
                }
            }
            result.add(diagonal);
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST -- Streamlined Queue BFS (flat output)
    // Time: O(n)  |  Space: O(n)
    // Same algorithm, returns flattened diagonal order.
    // ============================================================
    public static List<Integer> best(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            // Follow right chain, enqueue left children
            while (node != null) {
                result.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                node = node.right;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Diagonal Traversal ===");

        //         8
        //        / \
        //       3   10
        //      / \    \
        //     1   6    14
        //        / \   /
        //       4   7 13
        TreeNode root = new TreeNode(8,
            new TreeNode(3,
                new TreeNode(1),
                new TreeNode(6, new TreeNode(4), new TreeNode(7))),
            new TreeNode(10,
                null,
                new TreeNode(14, new TreeNode(13), null)));

        System.out.println("Brute:   " + bruteForce(root));
        // [[8,10,14], [3,6,7,13], [1,4]]

        System.out.println("Optimal: " + optimal(root));
        // [[8,10,14], [3,6,7,13], [1,4]]

        System.out.println("Best (flat): " + best(root));
        // [8,10,14,3,6,7,13,1,4]

        // Edge: empty tree
        System.out.println("Empty:   " + optimal(null));  // []

        // Edge: single node
        System.out.println("Single:  " + optimal(new TreeNode(42)));  // [[42]]
    }
}
