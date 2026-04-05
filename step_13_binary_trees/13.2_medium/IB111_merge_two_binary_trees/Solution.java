/**
 * Problem: Merge Two Binary Trees (LeetCode #617)
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
    // APPROACH 1: BRUTE FORCE -- Create New Tree
    // Time: O(min(n1,n2))  |  Space: O(min(h1,h2)) + new nodes
    // Create a brand new merged tree, preserving originals.
    // ============================================================
    public static TreeNode bruteForce(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) return null;
        if (t1 == null) return new TreeNode(t2.val, bruteForce(null, t2.left), bruteForce(null, t2.right));
        if (t2 == null) return new TreeNode(t1.val, bruteForce(t1.left, null), bruteForce(t1.right, null));

        TreeNode merged = new TreeNode(t1.val + t2.val);
        merged.left = bruteForce(t1.left, t2.left);
        merged.right = bruteForce(t1.right, t2.right);
        return merged;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Merge into t1
    // Time: O(min(n1,n2))  |  Space: O(min(h1,h2))
    // Modify t1 in-place. Attach t2's subtrees where t1 is null.
    // ============================================================
    public static TreeNode optimal(TreeNode t1, TreeNode t2) {
        if (t1 == null) return t2;
        if (t2 == null) return t1;

        t1.val += t2.val;
        t1.left = optimal(t1.left, t2.left);
        t1.right = optimal(t1.right, t2.right);
        return t1;
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative BFS Merge
    // Time: O(min(n1,n2))  |  Space: O(min(n1,n2))
    // Level-by-level merge using queue. Modifies t1 in-place.
    // ============================================================
    public static TreeNode best(TreeNode t1, TreeNode t2) {
        if (t1 == null) return t2;
        if (t2 == null) return t1;

        Queue<TreeNode[]> queue = new LinkedList<>();
        queue.add(new TreeNode[]{t1, t2});

        while (!queue.isEmpty()) {
            TreeNode[] pair = queue.poll();
            TreeNode n1 = pair[0], n2 = pair[1];

            // n2 is guaranteed non-null here (we only enqueue valid pairs)
            n1.val += n2.val;

            // Handle left children
            if (n1.left != null && n2.left != null) {
                queue.add(new TreeNode[]{n1.left, n2.left});
            } else if (n1.left == null) {
                n1.left = n2.left;  // Attach t2's left subtree
            }

            // Handle right children
            if (n1.right != null && n2.right != null) {
                queue.add(new TreeNode[]{n1.right, n2.right});
            } else if (n1.right == null) {
                n1.right = n2.right;  // Attach t2's right subtree
            }
        }
        return t1;
    }

    // Helper: preorder traversal for verification
    private static List<Integer> preorder(TreeNode node) {
        List<Integer> result = new ArrayList<>();
        if (node == null) return result;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            TreeNode n = stack.pop();
            result.add(n.val);
            if (n.right != null) stack.push(n.right);
            if (n.left != null) stack.push(n.left);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Merge Two Binary Trees ===");

        // t1 = [1,3,2], t2 = [2,1,3] -> [3,4,5]
        TreeNode t1 = new TreeNode(1, new TreeNode(3), new TreeNode(2));
        TreeNode t2 = new TreeNode(2, new TreeNode(1), new TreeNode(3));

        // Test brute force (creates new tree)
        TreeNode r1 = bruteForce(t1, t2);
        System.out.println("Brute:   " + preorder(r1));  // [3,4,5]

        // Test optimal (modifies t1)
        TreeNode t1b = new TreeNode(1, new TreeNode(3), new TreeNode(2));
        TreeNode t2b = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        TreeNode r2 = optimal(t1b, t2b);
        System.out.println("Optimal: " + preorder(r2));  // [3,4,5]

        // Test best (iterative, modifies t1)
        TreeNode t1c = new TreeNode(1, new TreeNode(3), new TreeNode(2));
        TreeNode t2c = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        TreeNode r3 = best(t1c, t2c);
        System.out.println("Best:    " + preorder(r3));  // [3,4,5]

        // Edge: one null
        System.out.println("One null: " + preorder(optimal(null, new TreeNode(5))));  // [5]
    }
}
