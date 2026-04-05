/**
 * Problem: Two Sum in BST (LeetCode #653)
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
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- For each node, BST search complement
    // Time: O(n * h)  |  Space: O(h)
    // ============================================================
    public static boolean twoSumBrute(TreeNode root, int k) {
        return dfsCheck(root, root, k);
    }

    private static boolean dfsCheck(TreeNode current, TreeNode root, int k) {
        if (current == null) return false;
        int complement = k - current.val;
        if (complement != current.val && bstSearch(root, complement)) return true;
        return dfsCheck(current.left, root, k) || dfsCheck(current.right, root, k);
    }

    private static boolean bstSearch(TreeNode root, int val) {
        TreeNode curr = root;
        while (curr != null) {
            if (val == curr.val) return true;
            if (val < curr.val) curr = curr.left;
            else curr = curr.right;
        }
        return false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- HashSet + DFS
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static boolean twoSumHashSet(TreeNode root, int k) {
        Set<Integer> seen = new HashSet<>();
        return dfsHashSet(root, k, seen);
    }

    private static boolean dfsHashSet(TreeNode node, int k, Set<Integer> seen) {
        if (node == null) return false;
        if (seen.contains(k - node.val)) return true;
        seen.add(node.val);
        return dfsHashSet(node.left, k, seen) || dfsHashSet(node.right, k, seen);
    }

    // ============================================================
    // APPROACH 3: BEST -- Inorder + Two Pointers
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static boolean twoSumTwoPointers(TreeNode root, int k) {
        List<Integer> sorted = new ArrayList<>();
        inorder(root, sorted);

        int lo = 0, hi = sorted.size() - 1;
        while (lo < hi) {
            int sum = sorted.get(lo) + sorted.get(hi);
            if (sum == k) return true;
            if (sum < k) lo++;
            else hi--;
        }
        return false;
    }

    private static void inorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.val);
        inorder(node.right, list);
    }

    // Helper: build tree from level-order array
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
        System.out.println("=== Two Sum in BST ===\n");

        Object[][] tests = {
            {new Integer[]{5, 3, 6, 2, 4, null, 7}, 9, true},
            {new Integer[]{5, 3, 6, 2, 4, null, 7}, 28, false},
            {new Integer[]{2, 1, 3}, 4, true},
            {new Integer[]{2, 1, 3}, 1, false},
            {new Integer[]{1}, 2, false},
            {new Integer[]{1, null, 2}, 3, true},
        };

        for (Object[] test : tests) {
            Integer[] arr = (Integer[]) test[0];
            int k = (int) test[1];
            boolean expected = (boolean) test[2];
            TreeNode root = buildTree(arr);

            boolean brute = twoSumBrute(root, k);
            boolean hashSet = twoSumHashSet(root, k);
            boolean twoPtr = twoSumTwoPointers(root, k);

            String status = (brute == expected && hashSet == expected && twoPtr == expected)
                    ? "PASS" : "FAIL";
            System.out.printf("Tree=%s, k=%d%n", Arrays.toString(arr), k);
            System.out.printf("  Brute=%b, HashSet=%b, TwoPtr=%b (expected=%b) %s%n%n",
                    brute, hashSet, twoPtr, expected, status);
        }
    }
}
