import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - BFS level order, take first of each level
// Time: O(N)  |  Space: O(N)
// BFS: at each level, the first node encountered (leftmost) is the visible one
// ============================================================
class BruteForce {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static List<Integer> solve(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                if (i == 0) result.add(node.val);  // first = leftmost
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return result;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - DFS (preorder: root, left, right)
// Time: O(N)  |  Space: O(H) where H = height
// DFS: track depth, first visit at each depth = leftmost node
// ============================================================
class Optimal {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    private static List<Integer> result;

    public static List<Integer> solve(TreeNode root) {
        result = new ArrayList<>();
        dfs(root, 0);
        return result;
    }

    private static void dfs(TreeNode node, int depth) {
        if (node == null) return;
        if (depth == result.size()) result.add(node.val);  // first visit at this depth
        dfs(node.left, depth + 1);
        dfs(node.right, depth + 1);
    }
}

// ============================================================
// APPROACH 3: BEST - Reverse-level BFS (right to left), last seen per level
// Time: O(N)  |  Space: O(N)
// Alternatively: iterative DFS with explicit stack, clean and cache-friendly
// ============================================================
class Best {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static List<Integer> solve(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        // Iterative DFS using stack (root, left, right order)
        Deque<Object[]> stack = new ArrayDeque<>();
        stack.push(new Object[]{root, 0});
        while (!stack.isEmpty()) {
            Object[] top = stack.pop();
            TreeNode node = (TreeNode) top[0];
            int depth = (int) top[1];
            if (depth == result.size()) result.add(node.val);
            // Push right first so left is processed first
            if (node.right != null) stack.push(new Object[]{node.right, depth + 1});
            if (node.left != null) stack.push(new Object[]{node.left, depth + 1});
        }
        return result;
    }
}

public class Solution {
    // Helper to build a simple test tree
    //        1
    //       / \
    //      2   3
    //       \   \
    //        5   4
    // Left side view: [1, 2, 5]
    public static void main(String[] args) {
        // Build tree for BruteForce
        BruteForce.TreeNode r1 = new BruteForce.TreeNode(1);
        r1.left = new BruteForce.TreeNode(2);
        r1.right = new BruteForce.TreeNode(3);
        r1.left.right = new BruteForce.TreeNode(5);
        r1.right.right = new BruteForce.TreeNode(4);
        System.out.println("BruteForce: " + BruteForce.solve(r1));  // [1,2,5]

        Optimal.TreeNode r2 = new Optimal.TreeNode(1);
        r2.left = new Optimal.TreeNode(2);
        r2.right = new Optimal.TreeNode(3);
        r2.left.right = new Optimal.TreeNode(5);
        r2.right.right = new Optimal.TreeNode(4);
        System.out.println("Optimal:    " + Optimal.solve(r2));     // [1,2,5]

        Best.TreeNode r3 = new Best.TreeNode(1);
        r3.left = new Best.TreeNode(2);
        r3.right = new Best.TreeNode(3);
        r3.left.right = new Best.TreeNode(5);
        r3.right.right = new Best.TreeNode(4);
        System.out.println("Best:       " + Best.solve(r3));        // [1,2,5]

        // Edge case: right-skewed tree
        Optimal.TreeNode r4 = new Optimal.TreeNode(1);
        r4.right = new Optimal.TreeNode(2);
        r4.right.right = new Optimal.TreeNode(3);
        System.out.println("Right-skewed Optimal: " + Optimal.solve(r4));  // [1,2,3]
    }
}
