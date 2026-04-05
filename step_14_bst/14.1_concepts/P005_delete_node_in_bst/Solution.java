/**
 * Problem: Delete Node in BST (LeetCode 450)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given the root of a BST and a key, delete the node with that key and
 * return the (possibly new) root. The resulting tree must be a valid BST.
 *
 * Three cases:
 *   1. Node is a leaf             -> remove it
 *   2. Node has one child         -> replace with that child
 *   3. Node has two children      -> replace with inorder successor (min of right),
 *                                    then delete that successor
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ------------------------------------------------------------------
    // Shared TreeNode
    // ------------------------------------------------------------------
    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int v) { val = v; }
    }

    // Insert into BST
    static TreeNode insert(TreeNode root, int v) {
        if (root == null) return new TreeNode(v);
        if (v < root.val) root.left  = insert(root.left,  v);
        else if (v > root.val) root.right = insert(root.right, v);
        return root;
    }

    static TreeNode buildBST(int[] vals) {
        TreeNode root = null;
        for (int v : vals) root = insert(root, v);
        return root;
    }

    static List<Integer> inorder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode curr = root;
        while (curr != null || !stack.isEmpty()) {
            while (curr != null) { stack.push(curr); curr = curr.left; }
            curr = stack.pop();
            res.add(curr.val);
            curr = curr.right;
        }
        return res;
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Inorder array, remove, rebuild
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Collect all node values via inorder traversal into a sorted list.
         * Remove the target key. Rebuild a balanced BST from the sorted list
         * using the "insert from middle" technique.
         */
        List<Integer> vals = new ArrayList<>();

        void collect(TreeNode node) {
            if (node == null) return;
            collect(node.left);
            vals.add(node.val);
            collect(node.right);
        }

        TreeNode buildFromSorted(List<Integer> list, int lo, int hi) {
            if (lo > hi) return null;
            int mid = (lo + hi) / 2;
            TreeNode node = new TreeNode(list.get(mid));
            node.left  = buildFromSorted(list, lo, mid - 1);
            node.right = buildFromSorted(list, mid + 1, hi);
            return node;
        }

        TreeNode deleteNode(TreeNode root, int key) {
            collect(root);
            if (!vals.contains(key)) return root;
            vals.remove(Integer.valueOf(key));
            return buildFromSorted(vals, 0, vals.size() - 1);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Recursive 3-case deletion
    // Time: O(h)  |  Space: O(h) call stack
    // ============================================================
    static class Optimal {
        /**
         * Recursively navigate to the target using BST ordering.
         * Once found:
         *   - No left child  -> return right child
         *   - No right child -> return left child
         *   - Both children  -> find inorder successor (leftmost of right),
         *                       copy its value here, delete it from right subtree.
         */
        TreeNode deleteNode(TreeNode root, int key) {
            if (root == null) return null;
            if (key < root.val) {
                root.left = deleteNode(root.left, key);
            } else if (key > root.val) {
                root.right = deleteNode(root.right, key);
            } else {
                if (root.left  == null) return root.right;
                if (root.right == null) return root.left;
                // Find inorder successor
                TreeNode succ = root.right;
                while (succ.left != null) succ = succ.left;
                root.val   = succ.val;
                root.right = deleteNode(root.right, succ.val);
            }
            return root;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Iterative deletion, O(1) extra space
    // Time: O(h)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Iteratively locate the node and its parent. Apply the same 3-case
         * logic without any recursion stack. For the two-children case, attach
         * the deleted node's left subtree to the leftmost position of its right
         * subtree, then use the right subtree as replacement — this satisfies
         * the BST ordering property.
         */
        void attachLeftToRightmost(TreeNode node, TreeNode leftSubtree) {
            TreeNode curr = node;
            while (curr.left != null) curr = curr.left;
            curr.left = leftSubtree;
        }

        TreeNode deleteNode(TreeNode root, int key) {
            TreeNode parent = null, curr = root;
            while (curr != null && curr.val != key) {
                parent = curr;
                curr = (key < curr.val) ? curr.left : curr.right;
            }
            if (curr == null) return root; // not found

            TreeNode replacement;
            if (curr.left == null) {
                replacement = curr.right;
            } else if (curr.right == null) {
                replacement = curr.left;
            } else {
                attachLeftToRightmost(curr.right, curr.left);
                replacement = curr.right;
            }

            if (parent == null) return replacement;
            if (parent.left == curr) parent.left  = replacement;
            else                     parent.right = replacement;
            return root;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Delete Node in BST ===\n");
        int[] vals = {5, 3, 6, 2, 4, 7};
        int key = 3;
        // Expected inorder after deleting 3: [2, 4, 5, 6, 7]

        TreeNode r1 = buildBST(vals);
        System.out.println("Brute:           " + inorder(new BruteForce().deleteNode(r1, key)));

        TreeNode r2 = buildBST(vals);
        System.out.println("Optimal:         " + inorder(new Optimal().deleteNode(r2, key)));

        TreeNode r3 = buildBST(vals);
        System.out.println("Best (Iterative):" + inorder(new Best().deleteNode(r3, key)));
    }
}
