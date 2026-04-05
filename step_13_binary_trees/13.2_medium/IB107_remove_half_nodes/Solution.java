/**
 * Problem: Remove Half Nodes (InterviewBit)
 * Difficulty: EASY | XP: 10
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
    // APPROACH 1: BRUTE FORCE -- Repeated BFS Passes
    // Time: O(n^2) worst  |  Space: O(n)
    // Do BFS, remove half nodes found, repeat until clean.
    // ============================================================
    public static TreeNode bruteForce(TreeNode root) {
        if (root == null) return null;

        boolean changed = true;
        while (changed) {
            changed = false;
            // Handle root being a half node
            while (root != null) {
                boolean leftOnly = root.left != null && root.right == null;
                boolean rightOnly = root.left == null && root.right != null;
                if (leftOnly) { root = root.left; changed = true; }
                else if (rightOnly) { root = root.right; changed = true; }
                else break;
            }
            if (root == null) return null;

            // BFS and fix children
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                // Check left child
                if (node.left != null) {
                    TreeNode child = node.left;
                    if (child.left != null && child.right == null) {
                        node.left = child.left; changed = true;
                    } else if (child.left == null && child.right != null) {
                        node.left = child.right; changed = true;
                    }
                    queue.add(node.left);
                }
                // Check right child
                if (node.right != null) {
                    TreeNode child = node.right;
                    if (child.left != null && child.right == null) {
                        node.right = child.left; changed = true;
                    } else if (child.left == null && child.right != null) {
                        node.right = child.right; changed = true;
                    }
                    queue.add(node.right);
                }
            }
        }
        return root;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Post-Order DFS
    // Time: O(n)  |  Space: O(h)
    // Bottom-up: clean children first, then check current node.
    // ============================================================
    public static TreeNode optimal(TreeNode node) {
        if (node == null) return null;

        node.left = optimal(node.left);
        node.right = optimal(node.right);

        // Half node: only left child exists
        if (node.left != null && node.right == null) return node.left;
        // Half node: only right child exists
        if (node.left == null && node.right != null) return node.right;

        // Leaf or full node: keep it
        return node;
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative Post-Order with Parent Map
    // Time: O(n)  |  Space: O(n)
    // Same logic, no recursion. Explicit stack + parent tracking.
    // ============================================================
    public static TreeNode best(TreeNode root) {
        if (root == null) return null;

        // Iterative post-order using two stacks
        Deque<TreeNode> stack1 = new ArrayDeque<>();
        Deque<TreeNode> stack2 = new ArrayDeque<>();
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        Map<TreeNode, Boolean> isLeft = new HashMap<>();

        stack1.push(root);
        while (!stack1.isEmpty()) {
            TreeNode node = stack1.pop();
            stack2.push(node);
            if (node.left != null) {
                stack1.push(node.left);
                parent.put(node.left, node);
                isLeft.put(node.left, true);
            }
            if (node.right != null) {
                stack1.push(node.right);
                parent.put(node.right, node);
                isLeft.put(node.right, false);
            }
        }

        // Process in post-order
        while (!stack2.isEmpty()) {
            TreeNode node = stack2.pop();
            boolean hasLeft = node.left != null;
            boolean hasRight = node.right != null;

            if ((hasLeft && !hasRight) || (!hasLeft && hasRight)) {
                // Half node: replace with its only child
                TreeNode replacement = hasLeft ? node.left : node.right;
                if (parent.containsKey(node)) {
                    TreeNode par = parent.get(node);
                    if (isLeft.get(node)) par.left = replacement;
                    else par.right = replacement;
                    // Update parent map for replacement
                    parent.put(replacement, par);
                    isLeft.put(replacement, isLeft.get(node));
                } else {
                    // Node is root
                    root = replacement;
                }
            }
        }
        return root;
    }

    // Helper to print tree in preorder for verification
    private static void preorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        list.add(node.val);
        preorder(node.left, list);
        preorder(node.right, list);
    }

    public static void main(String[] args) {
        System.out.println("=== Remove Half Nodes ===");

        // Test: 1(2(null,3),4) -> should become 1(3,4)
        TreeNode t1 = new TreeNode(1, new TreeNode(2, null, new TreeNode(3)), new TreeNode(4));
        List<Integer> r1 = new ArrayList<>();
        preorder(optimal(t1), r1);
        System.out.println("Optimal 1(2(null,3),4): " + r1);  // [1,3,4]

        // Test: chain 1->2->3 (all right) -> should become 3
        TreeNode t2 = new TreeNode(1, null, new TreeNode(2, null, new TreeNode(3)));
        TreeNode res2 = optimal(t2);
        System.out.println("Optimal chain 1->2->3: " + res2.val);  // 3

        // Test with brute force
        TreeNode t3 = new TreeNode(1, new TreeNode(2, null, new TreeNode(3)), new TreeNode(4));
        List<Integer> r3 = new ArrayList<>();
        preorder(bruteForce(t3), r3);
        System.out.println("Brute 1(2(null,3),4): " + r3);  // [1,3,4]
    }
}
