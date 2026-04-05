/**
 * Problem: Valid BST from Preorder
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n²)  |  Space: O(n)
// ============================================================
// Simulate BST insertion for each element in the preorder array.
// For every new element, insert it into a BST built so far and
// verify at each step that the insertion path is consistent with
// the preorder sequence (i.e. the element actually ends up as the
// next node to be visited in preorder).
// Simpler version: try to construct the BST completely, then
// generate its preorder and compare with the input.
class BruteForce {
    private int[] pre;

    // BST node insert + preorder dump
    private int[] insert(int[] bst, int val) {
        // Use array-based BST: index i has left=2i+1, right=2i+2
        // For simplicity we'll use a TreeNode approach instead.
        return bst; // placeholder — see below
    }

    // Proper approach: build BST from preorder, then check
    public boolean isValidPreorder(int[] preorder) {
        // Build BST naively (O(n²) for each insertion)
        int[] root = {-1}; // store root value index; we use an actual tree

        // Use recursive BST build + verify
        return canBuild(preorder, 0, preorder.length - 1,
                        Integer.MIN_VALUE, Integer.MAX_VALUE) == preorder.length;
    }

    // Try to match preorder[idx..] using BST property with valid range [lo, hi].
    // Returns how many elements from preorder were consumed.
    private int canBuild(int[] pre, int idx, int end, int lo, int hi) {
        if (idx > end) return idx;
        int val = pre[idx];
        if (val <= lo || val >= hi) return idx; // out of valid BST range
        idx++;                                   // consume root
        idx = canBuild(pre, idx, end, lo, val);  // consume left subtree
        idx = canBuild(pre, idx, end, val, hi);  // consume right subtree
        return idx;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(n)
// ============================================================
// Stack-based single-pass algorithm:
// - Maintain a monotone-decreasing stack (mimics the path from root
//   down the left spine of the BST built so far).
// - Track `lowerBound` = the value of the last popped element
//   (= the smallest value that any future element must exceed to be
//   placed correctly as a right-child descendant).
// - For each new element v:
//     If v < lowerBound  => v violates BST ordering => return false.
//     While stack top < v => pop (going up the tree to find parent),
//                            update lowerBound to the popped value.
//     Push v.
// This works because in a valid BST preorder, once you go right
// from a node, you can never go left of that node again.
class Optimal {
    public boolean isValidPreorder(int[] preorder) {
        java.util.Deque<Integer> stack = new java.util.ArrayDeque<>();
        int lowerBound = Integer.MIN_VALUE;

        for (int val : preorder) {
            // Current value must be > the last right-turn we made
            if (val < lowerBound) return false;

            // Pop all nodes that are now "above" val in the BST path
            while (!stack.isEmpty() && stack.peek() < val) {
                lowerBound = stack.pop();
            }
            stack.push(val);
        }
        return true;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(1)  (in-place stack using input array)
// ============================================================
// Exactly the same O(n) stack algorithm as Optimal but reuses the
// input array as the stack to achieve O(1) extra space.
// `top` acts as the stack pointer into preorder[].
class Best {
    public boolean isValidPreorder(int[] preorder) {
        int top = -1;               // stack pointer (reusing preorder array)
        int lowerBound = Integer.MIN_VALUE;

        for (int val : preorder) {
            if (val < lowerBound) return false;

            // Pop: anything smaller than val goes "above" in tree
            while (top >= 0 && preorder[top] < val) {
                lowerBound = preorder[top--];
            }
            preorder[++top] = val;  // push
        }
        return true;
    }
}

public class Solution {

    public static void main(String[] args) {
        System.out.println("=== Valid BST from Preorder ===\n");

        // Test cases: (input, expected)
        int[][] inputs = {
            {5, 2, 1, 3, 6},        // true  — valid BST preorder
            {5, 2, 6, 1, 3},        // false — 1 appears after right-turn at 6
            {1, 2, 3},              // true  — right-skewed BST
            {3, 2, 1},              // true  — left-skewed BST
            {2, 1, 3},              // true  — simple: 2 is root, 1 left, 3 right
            {3, 4, 5, 1, 2},        // false — 1 < lowerBound after seeing 3->4->5
            {10},                   // true  — single element
        };
        boolean[] expected = {true, false, true, true, true, false, true};

        BruteForce bf = new BruteForce();
        Optimal    op = new Optimal();
        // Best modifies the array in-place, so we copy for it

        System.out.printf("%-25s %-10s %-10s %-10s %-10s%n",
            "Input", "Expected", "BruteForce", "Optimal", "Best");
        System.out.println("-".repeat(68));

        for (int i = 0; i < inputs.length; i++) {
            int[] arr = inputs[i];
            boolean bfRes = bf.isValidPreorder(arr.clone());
            boolean opRes = op.isValidPreorder(arr.clone());

            int[] copy = arr.clone();
            boolean bestRes = new Best().isValidPreorder(copy);

            System.out.printf("%-25s %-10b %-10b %-10b %-10b%n",
                java.util.Arrays.toString(arr),
                expected[i], bfRes, opRes, bestRes);
        }
    }
}
