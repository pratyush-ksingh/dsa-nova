/**
 * Problem: Binary Tree Representation
 * Difficulty: EASY | XP: 10
 *
 * Array and linked node representations with conversions.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // TreeNode definition (Linked Representation)
    // ============================================================
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    // ============================================================
    // APPROACH 1: Array Representation -- index arithmetic
    // Parent of i: (i-1)/2 | Left child: 2i+1 | Right child: 2i+2
    // ============================================================
    static class ArrayTree {
        Integer[] data;
        int capacity;

        ArrayTree(int capacity) {
            this.capacity = capacity;
            this.data = new Integer[capacity];
        }

        void set(int index, int val) {
            if (index < capacity) data[index] = val;
        }

        Integer get(int index) {
            return (index < capacity) ? data[index] : null;
        }

        int parentIndex(int i) { return (i - 1) / 2; }
        int leftIndex(int i)   { return 2 * i + 1; }
        int rightIndex(int i)  { return 2 * i + 2; }

        Integer parent(int i)     { return (i > 0) ? get(parentIndex(i)) : null; }
        Integer leftChild(int i)  { return get(leftIndex(i)); }
        Integer rightChild(int i) { return get(rightIndex(i)); }

        @Override
        public String toString() {
            return Arrays.toString(data);
        }
    }

    // ============================================================
    // CONVERSION: Array -> Linked (BFS)
    // Time: O(n) | Space: O(n)
    // ============================================================
    public static TreeNode arrayToLinked(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) return null;

        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;

        while (!queue.isEmpty() && i < arr.length) {
            TreeNode curr = queue.poll();

            // Left child
            if (i < arr.length && arr[i] != null) {
                curr.left = new TreeNode(arr[i]);
                queue.offer(curr.left);
            }
            i++;

            // Right child
            if (i < arr.length && arr[i] != null) {
                curr.right = new TreeNode(arr[i]);
                queue.offer(curr.right);
            }
            i++;
        }
        return root;
    }

    // ============================================================
    // CONVERSION: Linked -> Array (BFS with index tracking)
    // Time: O(n) | Space: O(n)
    // ============================================================
    public static List<Integer> linkedToArray(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;

        // BFS with (node, index) pairs
        Queue<Object[]> queue = new LinkedList<>();
        queue.offer(new Object[]{root, 0});
        int maxIndex = 0;

        Map<Integer, Integer> indexMap = new TreeMap<>();

        while (!queue.isEmpty()) {
            Object[] pair = queue.poll();
            TreeNode node = (TreeNode) pair[0];
            int idx = (int) pair[1];

            indexMap.put(idx, node.val);
            maxIndex = Math.max(maxIndex, idx);

            if (node.left != null) queue.offer(new Object[]{node.left, 2 * idx + 1});
            if (node.right != null) queue.offer(new Object[]{node.right, 2 * idx + 2});
        }

        for (int i = 0; i <= maxIndex; i++) {
            result.add(indexMap.getOrDefault(i, null));
        }
        return result;
    }

    // Helper: level-order display
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== Binary Tree Representation ===\n");

        // --- Array Representation ---
        System.out.println("--- Array Representation ---");
        ArrayTree at = new ArrayTree(7);
        at.set(0, 1); at.set(1, 2); at.set(2, 3); at.set(3, 4); at.set(4, 5);
        System.out.println("Array: " + at);
        System.out.println("Root: " + at.get(0));
        System.out.println("Left child of root: " + at.leftChild(0));
        System.out.println("Right child of root: " + at.rightChild(0));
        System.out.println("Parent of index 3: " + at.parent(3));
        System.out.println("Parent of index 4: " + at.parent(4));

        // --- Array -> Linked Conversion ---
        System.out.println("\n--- Array -> Linked ---");
        Integer[] arr = {1, 2, 3, 4, 5, null, 6};
        TreeNode root = arrayToLinked(arr);
        System.out.println("Input array: " + Arrays.toString(arr));
        System.out.println("Level order: " + levelOrder(root));

        // --- Linked -> Array Conversion ---
        System.out.println("\n--- Linked -> Array ---");
        List<Integer> backToArray = linkedToArray(root);
        System.out.println("Back to array: " + backToArray);

        // --- Round-trip verification ---
        System.out.println("\n--- Round-trip ---");
        Integer[] original = {1, 2, 3, null, 4, 5, null};
        TreeNode tree = arrayToLinked(original);
        List<Integer> roundTrip = linkedToArray(tree);
        System.out.println("Original:   " + Arrays.toString(original));
        System.out.println("Round-trip: " + roundTrip);

        // --- Edge: empty ---
        System.out.println("\n--- Edge Cases ---");
        System.out.println("Empty array -> linked: " + arrayToLinked(new Integer[]{}));
        System.out.println("Null root -> array: " + linkedToArray(null));

        // Single node
        TreeNode single = arrayToLinked(new Integer[]{42});
        System.out.println("Single node level order: " + levelOrder(single));
        System.out.println("Single node to array: " + linkedToArray(single));
    }
}
