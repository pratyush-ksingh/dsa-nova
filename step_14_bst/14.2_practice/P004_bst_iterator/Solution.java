/**
 * Problem: Binary Search Tree Iterator
 * Difficulty: MEDIUM | XP: 25
 * LeetCode: #173
 *
 * Implement BSTIterator for inorder traversal:
 *   - next(): returns next smallest number
 *   - hasNext(): returns true if there is a next number
 * next() and hasNext() must run in average O(1) time and O(h) space.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Flatten entire inorder into array
    // Time: O(n) init  |  O(1) next/hasNext  |  Space: O(n)
    // ============================================================
    static class BSTIteratorBrute {
        private final List<Integer> inorder = new ArrayList<>();
        private int idx = 0;

        public BSTIteratorBrute(TreeNode root) {
            flatten(root);
        }

        private void flatten(TreeNode node) {
            if (node == null) return;
            flatten(node.left);
            inorder.add(node.val);
            flatten(node.right);
        }

        public int next() {
            return inorder.get(idx++);
        }

        public boolean hasNext() {
            return idx < inorder.size();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Controlled traversal with stack
    // Time: Amortized O(1) next()  |  O(1) hasNext()  |  Space: O(h)
    // ============================================================
    static class BSTIteratorOptimal {
        private final Deque<TreeNode> stack = new ArrayDeque<>();

        public BSTIteratorOptimal(TreeNode root) {
            pushLeft(root);
        }

        private void pushLeft(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        public int next() {
            TreeNode node = stack.pop();
            // Push the left spine of the right subtree
            if (node.right != null) pushLeft(node.right);
            return node.val;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same stack, "advance left" framing
    // Time: Amortized O(1) next()  |  O(1) hasNext()  |  Space: O(h)
    // ============================================================
    static class BSTIteratorBest {
        private final Deque<TreeNode> stack = new ArrayDeque<>();

        public BSTIteratorBest(TreeNode root) {
            advanceLeft(root);
        }

        private void advanceLeft(TreeNode curr) {
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
        }

        public int next() {
            TreeNode curr = stack.pop();
            advanceLeft(curr.right);  // prepare the next inorder segment
            return curr.val;
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    // ============================================================
    // Helpers
    // ============================================================
    public static TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        while (!queue.isEmpty() && i < arr.length) {
            TreeNode curr = queue.poll();
            if (i < arr.length && arr[i] != null) {
                curr.left = new TreeNode(arr[i]);
                queue.offer(curr.left);
            }
            i++;
            if (i < arr.length && arr[i] != null) {
                curr.right = new TreeNode(arr[i]);
                queue.offer(curr.right);
            }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== BST Iterator ===\n");

        // Test 1: [7, 3, 15, null, null, 9, 20]  -> inorder [3,7,9,15,20]
        // Test 2: [4,2,6,1,3,5,7]                -> inorder [1,2,3,4,5,6,7]
        // Test 3: [1]                             -> inorder [1]
        Integer[][] trees = {
            {7, 3, 15, null, null, 9, 20},
            {4, 2, 6, 1, 3, 5, 7},
            {1}
        };
        int[][] expectedArr = {
            {3, 7, 9, 15, 20},
            {1, 2, 3, 4, 5, 6, 7},
            {1}
        };

        for (int t = 0; t < trees.length; t++) {
            TreeNode root = buildTree(trees[t]);
            BSTIteratorBrute it1 = new BSTIteratorBrute(root);
            BSTIteratorOptimal it2 = new BSTIteratorOptimal(root);
            BSTIteratorBest it3 = new BSTIteratorBest(root);

            List<Integer> res1 = new ArrayList<>(), res2 = new ArrayList<>(), res3 = new ArrayList<>();
            while (it1.hasNext()) res1.add(it1.next());
            while (it2.hasNext()) res2.add(it2.next());
            while (it3.hasNext()) res3.add(it3.next());

            List<Integer> exp = new ArrayList<>();
            for (int v : expectedArr[t]) exp.add(v);

            System.out.printf("Tree: %s%n", Arrays.toString(trees[t]));
            System.out.printf("  Brute:   %s%n", res1);
            System.out.printf("  Optimal: %s%n", res2);
            System.out.printf("  Best:    %s%n", res3);
            System.out.printf("  Expected:%s%n", exp);
            System.out.printf("  %s%n%n",
                res1.equals(exp) && res2.equals(exp) && res3.equals(exp) ? "PASS" : "FAIL");
        }

        // LC #173 interleaved test
        System.out.println("--- Interleaved test (LC example) ---");
        TreeNode root = buildTree(new Integer[]{7, 3, 15, null, null, 9, 20});
        BSTIteratorOptimal it = new BSTIteratorOptimal(root);
        // Expected sequence: next=3, next=7, hasNext=true, next=9,
        //                    hasNext=true, next=15, hasNext=true, next=20, hasNext=false
        int[][] ops = {{0, 3}, {0, 7}, {1, 1}, {0, 9}, {1, 1}, {0, 15}, {1, 1}, {0, 20}, {1, 0}};
        // op: 0=next, 1=hasNext
        boolean allPass = true;
        for (int[] op : ops) {
            int result = op[0] == 0 ? it.next() : (it.hasNext() ? 1 : 0);
            boolean ok = result == op[1];
            allPass = allPass && ok;
            System.out.printf("  %s() = %d  expected=%d  %s%n",
                op[0] == 0 ? "next" : "hasNext", result, op[1], ok ? "OK" : "FAIL");
        }
        System.out.printf("  Overall: %s%n", allPass ? "PASS" : "FAIL");
    }
}
