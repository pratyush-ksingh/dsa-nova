import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Inorder collect + sort + reassign
// Time: O(n log n)  |  Space: O(n)
// ============================================================
// Collect all nodes and values during inorder traversal. Sort
// the values. Reassign sorted values back to nodes in inorder
// order. The two swapped nodes are wherever values don't match.
// ============================================================

class TreeNode {
    int val;
    TreeNode left, right;
    TreeNode(int v) { val = v; }
}

class BruteForce {
    List<Integer> vals = new ArrayList<>();
    List<TreeNode> nodes = new ArrayList<>();

    private void inorder(TreeNode root) {
        if (root == null) return;
        inorder(root.left);
        vals.add(root.val);
        nodes.add(root);
        inorder(root.right);
    }

    public void recoverTree(TreeNode root) {
        inorder(root);
        List<Integer> sorted = new ArrayList<>(vals);
        Collections.sort(sorted);
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).val = sorted.get(i);
        }
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Inorder with violation detection
// Time: O(n)  |  Space: O(h) where h = tree height (recursion)
// ============================================================
// During inorder traversal, track prev node. Whenever
// prev.val > curr.val, it is a violation. First violation:
// first = prev, second = curr. Second violation: second = curr.
// Swap first.val and second.val after traversal completes.
// ============================================================

class Optimal {
    TreeNode first = null, second = null, prev = null;

    private void inorder(TreeNode root) {
        if (root == null) return;
        inorder(root.left);
        if (prev != null && prev.val > root.val) {
            if (first == null) first = prev;
            second = root;
        }
        prev = root;
        inorder(root.right);
    }

    public void recoverTree(TreeNode root) {
        inorder(root);
        int tmp = first.val;
        first.val = second.val;
        second.val = tmp;
    }
}

// ============================================================
// APPROACH 3: BEST - Morris Traversal (constant space)
// Time: O(n)  |  Space: O(1)
// ============================================================
// Morris traversal threads null right-child pointers to inorder
// successors, enabling O(1) space traversal. Apply the same
// violation detection (first/second) while visiting nodes in
// inorder order without any recursion stack or auxiliary array.
// Real-life use: recovering corrupted in-memory BST indexes in
// embedded systems where extra heap allocation is forbidden.
// ============================================================

class Best {
    public void recoverTree(TreeNode root) {
        TreeNode first = null, second = null, prev = null;
        TreeNode curr = root;

        while (curr != null) {
            if (curr.left == null) {
                // Visit curr
                if (prev != null && prev.val > curr.val) {
                    if (first == null) first = prev;
                    second = curr;
                }
                prev = curr;
                curr = curr.right;
            } else {
                // Find inorder predecessor
                TreeNode pred = curr.left;
                while (pred.right != null && pred.right != curr) {
                    pred = pred.right;
                }
                if (pred.right == null) {
                    pred.right = curr;   // thread
                    curr = curr.left;
                } else {
                    pred.right = null;   // remove thread
                    if (prev != null && prev.val > curr.val) {
                        if (first == null) first = prev;
                        second = curr;
                    }
                    prev = curr;
                    curr = curr.right;
                }
            }
        }

        // Swap values of the two misplaced nodes
        int tmp = first.val;
        first.val = second.val;
        second.val = tmp;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    static TreeNode build(Integer[] arr) {
        if (arr == null || arr.length == 0) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        int i = 1;
        while (!q.isEmpty() && i < arr.length) {
            TreeNode node = q.poll();
            if (i < arr.length && arr[i] != null) {
                node.left = new TreeNode(arr[i]);
                q.add(node.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                node.right = new TreeNode(arr[i]);
                q.add(node.right);
            }
            i++;
        }
        return root;
    }

    static List<Integer> inorder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<TreeNode> stk = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !stk.isEmpty()) {
            while (cur != null) { stk.push(cur); cur = cur.left; }
            cur = stk.pop();
            res.add(cur.val);
            cur = cur.right;
        }
        return res;
    }

    public static void main(String[] args) {
        // Test 1: [1,3,null,null,2] => 3 and 1 swapped => fix to [3,1,null,null,2]
        TreeNode t1 = build(new Integer[]{1, 3, null, null, 2});
        new Best().recoverTree(t1);
        System.out.println("Test1 Morris  (expect [1,2,3]): " + inorder(t1));

        TreeNode t2 = build(new Integer[]{3, 1, 4, null, null, 2});
        new Best().recoverTree(t2);
        System.out.println("Test2 Morris  (expect [1,2,3,4]): " + inorder(t2));

        TreeNode t3 = build(new Integer[]{3, 1, 4, null, null, 2});
        new Optimal().recoverTree(t3);
        System.out.println("Test3 Optimal (expect [1,2,3,4]): " + inorder(t3));

        TreeNode t4 = build(new Integer[]{1, 3, null, null, 2});
        new BruteForce().recoverTree(t4);
        System.out.println("Test4 Brute   (expect [1,2,3]): " + inorder(t4));

        // Test 5: adjacent swap (nodes are adjacent in inorder)
        // BST [6,3,8,1,4] with 3 and 4 swapped: [6,4,8,1,3]
        TreeNode t5 = build(new Integer[]{6, 4, 8, 1, 3});
        new Best().recoverTree(t5);
        System.out.println("Test5 Morris  (expect [1,3,4,6,8]): " + inorder(t5));
    }
}
