/**
 * Problem: Serialize and Deserialize Binary Tree
 * Difficulty: HARD | XP: 50
 *
 * Convert a binary tree to a string representation and reconstruct it exactly.
 * The serialized form must uniquely encode the tree structure.
 *
 * Real-life use case: Persisting tree structures to disk/DB, sending trees
 * over network (e.g., expression trees, ASTs, decision trees).
 *
 * @author DSA_Nova
 */

import java.util.*;

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

// ============================================================
// APPROACH 1: BRUTE FORCE (Preorder with "null" markers)
// Time: O(n)  |  Space: O(n)
// DFS preorder traversal. Null children encoded as "#".
// Deserialize by consuming tokens in the same preorder sequence.
// ============================================================
class BruteForce {
    private static final String NULL_MARKER = "#";
    private static final String DELIMITER = ",";

    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        // Remove trailing comma
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append(NULL_MARKER).append(DELIMITER);
            return;
        }
        sb.append(node.val).append(DELIMITER);
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    public TreeNode deserialize(String data) {
        if (data == null || data.isEmpty()) return null;
        Queue<String> tokens = new LinkedList<>(Arrays.asList(data.split(DELIMITER)));
        return deserializeHelper(tokens);
    }

    private TreeNode deserializeHelper(Queue<String> tokens) {
        String token = tokens.poll();
        if (token == null || token.equals(NULL_MARKER)) return null;
        TreeNode node = new TreeNode(Integer.parseInt(token));
        node.left = deserializeHelper(tokens);
        node.right = deserializeHelper(tokens);
        return node;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (BFS level-order with "null" markers)
// Time: O(n)  |  Space: O(n)
// Serialize using BFS queue. Null children explicitly added as "#".
// Deserialize by re-running BFS and assigning children from token stream.
// More intuitive, mirrors the visual tree layout.
// ============================================================
class Optimal {
    private static final String NULL_MARKER = "null";
    private static final String DELIMITER = ",";

    public String serialize(TreeNode root) {
        if (root == null) return "";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node == null) {
                sb.append(NULL_MARKER).append(DELIMITER);
            } else {
                sb.append(node.val).append(DELIMITER);
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        if (data == null || data.isEmpty()) return null;
        String[] tokens = data.split(DELIMITER);
        int i = 0;
        TreeNode root = new TreeNode(Integer.parseInt(tokens[i++]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty() && i < tokens.length) {
            TreeNode node = queue.poll();
            // Left child
            if (!tokens[i].equals(NULL_MARKER)) {
                node.left = new TreeNode(Integer.parseInt(tokens[i]));
                queue.offer(node.left);
            }
            i++;
            // Right child
            if (i < tokens.length && !tokens[i].equals(NULL_MARKER)) {
                node.right = new TreeNode(Integer.parseInt(tokens[i]));
                queue.offer(node.right);
            }
            i++;
        }
        return root;
    }
}

// ============================================================
// APPROACH 3: BEST (Preorder with index tracking via int[] wrapper)
// Time: O(n)  |  Space: O(n)
// Same complexity as BruteForce but avoids Queue overhead by using
// an index array to track position during recursive deserialization.
// Slightly faster in practice due to array access vs queue operations.
// ============================================================
class Best {
    private static final String NULL_MARKER = "N";
    private static final String DELIMITER = ",";

    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        dfs(root, sb);
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void dfs(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append(NULL_MARKER).append(DELIMITER);
            return;
        }
        sb.append(node.val).append(DELIMITER);
        dfs(node.left, sb);
        dfs(node.right, sb);
    }

    public TreeNode deserialize(String data) {
        if (data == null || data.isEmpty()) return null;
        String[] tokens = data.split(DELIMITER);
        int[] idx = {0};
        return build(tokens, idx);
    }

    private TreeNode build(String[] tokens, int[] idx) {
        if (idx[0] >= tokens.length) return null;
        String token = tokens[idx[0]++];
        if (token.equals(NULL_MARKER)) return null;
        TreeNode node = new TreeNode(Integer.parseInt(token));
        node.left = build(tokens, idx);
        node.right = build(tokens, idx);
        return node;
    }
}

public class Solution {

    // Helper: inorder traversal to verify trees match
    static String inorder(TreeNode root) {
        if (root == null) return "#";
        return "(" + inorder(root.left) + " " + root.val + " " + inorder(root.right) + ")";
    }

    // Helper: build a tree manually for testing
    //        1
    //       / \
    //      2   3
    //         / \
    //        4   5
    static TreeNode buildSampleTree() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Serialize and Deserialize BT ===");

        TreeNode original = buildSampleTree();
        System.out.println("Original inorder: " + inorder(original));

        // BruteForce (preorder + null markers)
        BruteForce bf = new BruteForce();
        String s1 = bf.serialize(original);
        TreeNode t1 = bf.deserialize(s1);
        System.out.println("\n[Brute - Preorder]");
        System.out.println("  Serialized: " + s1);
        System.out.println("  Restored inorder: " + inorder(t1));

        // Optimal (BFS level-order)
        Optimal opt = new Optimal();
        String s2 = opt.serialize(original);
        TreeNode t2 = opt.deserialize(s2);
        System.out.println("\n[Optimal - BFS]");
        System.out.println("  Serialized: " + s2);
        System.out.println("  Restored inorder: " + inorder(t2));

        // Best (preorder + index array)
        Best best = new Best();
        String s3 = best.serialize(original);
        TreeNode t3 = best.deserialize(s3);
        System.out.println("\n[Best - Preorder+Index]");
        System.out.println("  Serialized: " + s3);
        System.out.println("  Restored inorder: " + inorder(t3));

        // Edge case: null root
        System.out.println("\n[Edge: null tree]");
        System.out.println("  Brute:   " + bf.deserialize(bf.serialize(null)));
        System.out.println("  Optimal: " + opt.deserialize(opt.serialize(null)));
        System.out.println("  Best:    " + best.deserialize(best.serialize(null)));

        // Edge case: single node
        TreeNode single = new TreeNode(42);
        System.out.println("\n[Edge: single node val=42]");
        TreeNode rs = best.deserialize(best.serialize(single));
        System.out.println("  Restored val: " + (rs != null ? rs.val : "null"));
    }
}
