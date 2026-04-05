import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Inorder traversal, check sorted
// Time: O(N)  |  Space: O(N)
// A BST's inorder traversal is strictly increasing
// ============================================================
class BruteForce {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static boolean solve(TreeNode root) {
        List<Integer> inorder = new ArrayList<>();
        collectInorder(root, inorder);
        for (int i = 1; i < inorder.size(); i++) {
            if (inorder.get(i) <= inorder.get(i - 1)) return false;
        }
        return true;
    }

    private static void collectInorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        collectInorder(node.left, list);
        list.add(node.val);
        collectInorder(node.right, list);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Min/Max range validation
// Time: O(N)  |  Space: O(H)
// Each node must be in range (min, max); propagate constraints top-down
// ============================================================
class Optimal {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static boolean solve(TreeNode root) {
        return validate(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean validate(TreeNode node, long min, long max) {
        if (node == null) return true;
        if (node.val <= min || node.val >= max) return false;
        return validate(node.left, min, node.val) && validate(node.right, node.val, max);
    }
}

// ============================================================
// APPROACH 3: BEST - Iterative inorder with previous tracking
// Time: O(N)  |  Space: O(H)
// No extra list needed; track previous value during inorder traversal
// ============================================================
class Best {
    static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    public static boolean solve(TreeNode root) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        long prev = Long.MIN_VALUE;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();
            if (cur.val <= prev) return false;
            prev = cur.val;
            cur = cur.right;
        }
        return true;
    }
}

public class Solution {
    public static void main(String[] args) {
        // Valid BST:     5
        //               / \
        //              3   7
        //             / \
        //            1   4
        BruteForce.TreeNode valid = new BruteForce.TreeNode(5);
        valid.left = new BruteForce.TreeNode(3);
        valid.right = new BruteForce.TreeNode(7);
        valid.left.left = new BruteForce.TreeNode(1);
        valid.left.right = new BruteForce.TreeNode(4);
        System.out.println("Valid BST BruteForce: " + BruteForce.solve(valid));  // true

        // Invalid BST:   5
        //               / \
        //              3   7
        //             / \
        //            1   6   <- 6 > 5, violates BST property
        BruteForce.TreeNode invalid = new BruteForce.TreeNode(5);
        invalid.left = new BruteForce.TreeNode(3);
        invalid.right = new BruteForce.TreeNode(7);
        invalid.left.left = new BruteForce.TreeNode(1);
        invalid.left.right = new BruteForce.TreeNode(6);
        System.out.println("Invalid BST BruteForce: " + BruteForce.solve(invalid));  // false

        Optimal.TreeNode v2 = new Optimal.TreeNode(5);
        v2.left = new Optimal.TreeNode(3);
        v2.right = new Optimal.TreeNode(7);
        v2.left.left = new Optimal.TreeNode(1);
        v2.left.right = new Optimal.TreeNode(4);
        System.out.println("Valid BST Optimal: " + Optimal.solve(v2));  // true

        Optimal.TreeNode inv2 = new Optimal.TreeNode(5);
        inv2.left = new Optimal.TreeNode(3);
        inv2.right = new Optimal.TreeNode(7);
        inv2.left.left = new Optimal.TreeNode(1);
        inv2.left.right = new Optimal.TreeNode(6);
        System.out.println("Invalid BST Optimal: " + Optimal.solve(inv2));  // false

        Best.TreeNode v3 = new Best.TreeNode(5);
        v3.left = new Best.TreeNode(3);
        v3.right = new Best.TreeNode(7);
        v3.left.left = new Best.TreeNode(1);
        v3.left.right = new Best.TreeNode(4);
        System.out.println("Valid BST Best: " + Best.solve(v3));  // true

        // Edge: single node
        Best.TreeNode single = new Best.TreeNode(1);
        System.out.println("Single node Best: " + Best.solve(single));  // true
    }
}
