/**
 * Problem: Same Tree (LeetCode #100)
 * Difficulty: EASY | XP: 10
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
    // APPROACH 1: BRUTE FORCE -- Serialize & Compare
    // Time: O(n)  |  Space: O(n)
    // Serialize both trees to lists (including nulls), then compare.
    // ============================================================
    public static boolean bruteForce(TreeNode p, TreeNode q) {
        List<Integer> listP = new ArrayList<>();
        List<Integer> listQ = new ArrayList<>();
        serialize(p, listP);
        serialize(q, listQ);
        return listP.equals(listQ);
    }

    private static void serialize(TreeNode node, List<Integer> list) {
        if (node == null) {
            list.add(null);
            return;
        }
        list.add(node.val);
        serialize(node.left, list);
        serialize(node.right, list);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Simultaneous DFS
    // Time: O(n)  |  Space: O(h)
    // Compare node by node recursively. Short-circuits on mismatch.
    // ============================================================
    public static boolean optimal(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return optimal(p.left, q.left) && optimal(p.right, q.right);
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative with Stack
    // Time: O(n)  |  Space: O(h)
    // Same logic but with explicit stack, avoiding recursion overhead.
    // ============================================================
    public static boolean best(TreeNode p, TreeNode q) {
        Deque<TreeNode[]> stack = new ArrayDeque<>();
        stack.push(new TreeNode[]{p, q});

        while (!stack.isEmpty()) {
            TreeNode[] pair = stack.pop();
            TreeNode a = pair[0], b = pair[1];

            if (a == null && b == null) continue;
            if (a == null || b == null) return false;
            if (a.val != b.val) return false;

            stack.push(new TreeNode[]{a.left, b.left});
            stack.push(new TreeNode[]{a.right, b.right});
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Same Tree ===");

        // Test: p = [1,2,3], q = [1,2,3] -> true
        TreeNode p1 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        TreeNode q1 = new TreeNode(1, new TreeNode(2), new TreeNode(3));

        // Test: p = [1,2], q = [1,null,2] -> false
        TreeNode p2 = new TreeNode(1, new TreeNode(2), null);
        TreeNode q2 = new TreeNode(1, null, new TreeNode(2));

        System.out.println("Brute [1,2,3] vs [1,2,3]: " + bruteForce(p1, q1));   // true
        System.out.println("Brute [1,2] vs [1,null,2]: " + bruteForce(p2, q2));   // false
        System.out.println("Optimal [1,2,3] vs [1,2,3]: " + optimal(p1, q1));     // true
        System.out.println("Optimal [1,2] vs [1,null,2]: " + optimal(p2, q2));     // false
        System.out.println("Best [1,2,3] vs [1,2,3]: " + best(p1, q1));           // true
        System.out.println("Best [1,2] vs [1,null,2]: " + best(p2, q2));           // false
    }
}
