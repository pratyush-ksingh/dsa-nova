/**
 * Problem: Build BT from Inorder Preorder
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int val) { this.val = val; }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n²)  |  Space: O(n)  (recursion stack + no map)
// ============================================================
// Classic recursive approach: preorder[0] is the root.
// Search linearly in inorder to find root's index => splits
// left and right subtrees.  Repeat recursively.
// Slow due to linear scan at each level => O(n²) worst case.
class BruteForce {
    private int preIdx;
    private int[] preorder, inorder;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder;
        this.inorder  = inorder;
        this.preIdx   = 0;
        return build(0, inorder.length - 1);
    }

    private TreeNode build(int inLeft, int inRight) {
        if (inLeft > inRight) return null;

        int rootVal = preorder[preIdx++];
        TreeNode root = new TreeNode(rootVal);

        // Linear search for root in inorder
        int inMid = inLeft;
        while (inorder[inMid] != rootVal) inMid++;

        root.left  = build(inLeft, inMid - 1);
        root.right = build(inMid + 1, inRight);
        return root;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)
// ============================================================
// Same recursive logic as BruteForce but precompute a HashMap
// from value -> inorder index so each lookup is O(1) instead
// of O(n), giving O(n) overall time.
class Optimal {
    private int preIdx;
    private int[] preorder;
    private java.util.HashMap<Integer, Integer> inMap;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        this.preorder = preorder;
        this.preIdx   = 0;
        this.inMap    = new java.util.HashMap<>();
        for (int i = 0; i < inorder.length; i++) inMap.put(inorder[i], i);
        return build(0, inorder.length - 1);
    }

    private TreeNode build(int inLeft, int inRight) {
        if (inLeft > inRight) return null;

        int rootVal = preorder[preIdx++];
        TreeNode root = new TreeNode(rootVal);
        int inMid = inMap.get(rootVal);   // O(1) lookup

        root.left  = build(inLeft, inMid - 1);
        root.right = build(inMid + 1, inRight);
        return root;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(n)
// ============================================================
// Iterative stack-based construction — avoids recursion overhead.
// Idea: maintain a stack of nodes whose left subtree is being built.
//   - For each preorder value, if it equals inorder[inIdx], the
//     current top's right child should be popped and set next.
//   - Otherwise create a new node and attach it as the left child
//     of the stack's top, then push it.
// This is O(n) time and O(n) space (stack) with no recursion.
class Best {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) return null;

        java.util.Deque<TreeNode> stack = new java.util.ArrayDeque<>();
        TreeNode root = new TreeNode(preorder[0]);
        stack.push(root);

        int inIdx = 0;
        for (int i = 1; i < preorder.length; i++) {
            TreeNode node = new TreeNode(preorder[i]);
            TreeNode parent = null;

            // Pop nodes whose value matches inorder[inIdx] — they have
            // no more left children; the new node goes to the right.
            while (!stack.isEmpty() && stack.peek().val == inorder[inIdx]) {
                parent = stack.pop();
                inIdx++;
            }

            if (parent != null) {
                parent.right = node;
            } else {
                stack.peek().left = node;
            }
            stack.push(node);
        }
        return root;
    }
}

public class Solution {

    // In-order traversal to verify the reconstructed tree
    static java.util.List<Integer> inorder(TreeNode root) {
        java.util.List<Integer> res = new java.util.ArrayList<>();
        inorderHelper(root, res);
        return res;
    }

    static void inorderHelper(TreeNode node, java.util.List<Integer> res) {
        if (node == null) return;
        inorderHelper(node.left, res);
        res.add(node.val);
        inorderHelper(node.right, res);
    }

    // Pre-order traversal to verify
    static java.util.List<Integer> preorder(TreeNode root) {
        java.util.List<Integer> res = new java.util.ArrayList<>();
        preorderHelper(root, res);
        return res;
    }

    static void preorderHelper(TreeNode node, java.util.List<Integer> res) {
        if (node == null) return;
        res.add(node.val);
        preorderHelper(node.left, res);
        preorderHelper(node.right, res);
    }

    public static void main(String[] args) {
        System.out.println("=== Build BT from Inorder Preorder ===\n");

        // Test 1: preorder=[3,9,20,15,7], inorder=[9,3,15,20,7]
        // Expected tree:    3
        //                  / \
        //                 9  20
        //                   /  \
        //                  15    7
        int[] pre1 = {3, 9, 20, 15, 7};
        int[] in1  = {9, 3, 15, 20, 7};

        System.out.println("[Approach 1 - BruteForce: Linear Search O(n²)]");
        TreeNode t1 = new BruteForce().buildTree(pre1, in1);
        System.out.println("Inorder:  " + inorder(t1));
        System.out.println("Preorder: " + preorder(t1));

        System.out.println("\n[Approach 2 - Optimal: HashMap O(n)]");
        TreeNode t2 = new Optimal().buildTree(pre1, in1);
        System.out.println("Inorder:  " + inorder(t2));
        System.out.println("Preorder: " + preorder(t2));

        System.out.println("\n[Approach 3 - Best: Iterative Stack O(n)]");
        TreeNode t3 = new Best().buildTree(pre1, in1);
        System.out.println("Inorder:  " + inorder(t3));
        System.out.println("Preorder: " + preorder(t3));

        // Test 2: single node
        System.out.println("\n[Single node: preorder=[1], inorder=[1]]");
        TreeNode s = new Best().buildTree(new int[]{1}, new int[]{1});
        System.out.println("Root val: " + s.val
            + ", left: " + s.left + ", right: " + s.right);

        // Test 3: left-skewed tree
        System.out.println("\n[Left-skewed: preorder=[4,3,2,1], inorder=[1,2,3,4]]");
        TreeNode ls = new Best().buildTree(
            new int[]{4, 3, 2, 1}, new int[]{1, 2, 3, 4});
        System.out.println("Inorder:  " + inorder(ls));
        System.out.println("Preorder: " + preorder(ls));
    }
}
