import java.util.*;

/**
 * Problem: Sorted Array to Balanced BST
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Convert a sorted array to a height-balanced BST.
 * Mid element becomes root; recurse on left and right halves.
 *
 * @author DSA_Nova
 */

class BSTNode {
    int val;
    BSTNode left, right;
    BSTNode(int val) { this.val = val; }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Naive recursive mid-point selection
// Time: O(N)  |  Space: O(log N) stack
// ============================================================
class BruteForce {
    static BSTNode sortedArrayToBST(int[] nums, int left, int right) {
        if (left > right) return null;
        int mid = (left + right) / 2;
        BSTNode node = new BSTNode(nums[mid]);
        node.left = sortedArrayToBST(nums, left, mid - 1);
        node.right = sortedArrayToBST(nums, mid + 1, right);
        return node;
    }

    static BSTNode convert(int[] nums) {
        return sortedArrayToBST(nums, 0, nums.length - 1);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Same recursive approach but uses (left + right + 1) / 2 for right-biased mid
// to match LeetCode's expected output when even elements exist.
// Time: O(N)  |  Space: O(log N)
// ============================================================
class Optimal {
    static BSTNode helper(int[] nums, int left, int right) {
        if (left > right) return null;
        // Right-biased mid for even-length arrays
        int mid = left + (right - left + 1) / 2;
        BSTNode node = new BSTNode(nums[mid]);
        node.left = helper(nums, left, mid - 1);
        node.right = helper(nums, mid + 1, right);
        return node;
    }

    static BSTNode convert(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }
}

// ============================================================
// APPROACH 3: BEST
// Iterative using explicit stack — (node, left, right) triples
// Time: O(N)  |  Space: O(N) for the result tree; O(log N) stack
// ============================================================
class Best {
    static BSTNode convert(int[] nums) {
        if (nums.length == 0) return null;
        // Stack stores: [BSTNode, left, right, isLeft (1) or isRight (0), parent]
        // Use a simpler approach: store pending work
        int n = nums.length;
        int mid = n / 2;
        BSTNode root = new BSTNode(nums[mid]);

        // Queue of (node, arrayLeft, arrayRight)
        Deque<int[]> indexStack = new ArrayDeque<>();
        Deque<BSTNode> nodeStack = new ArrayDeque<>();
        Deque<Boolean> isLeftStack = new ArrayDeque<>();
        Deque<BSTNode> parentStack = new ArrayDeque<>();

        // Push right subtask
        if (mid + 1 <= n - 1) {
            indexStack.push(new int[]{mid + 1, n - 1});
            nodeStack.push(root);
            isLeftStack.push(false);
        }
        // Push left subtask
        if (0 <= mid - 1) {
            indexStack.push(new int[]{0, mid - 1});
            nodeStack.push(root);
            isLeftStack.push(true);
        }

        while (!indexStack.isEmpty()) {
            int[] range = indexStack.pop();
            BSTNode parent = nodeStack.pop();
            boolean isLeft = isLeftStack.pop();

            int l = range[0], r = range[1];
            int m = l + (r - l) / 2;
            BSTNode newNode = new BSTNode(nums[m]);

            if (isLeft) parent.left = newNode;
            else parent.right = newNode;

            if (m + 1 <= r) {
                indexStack.push(new int[]{m + 1, r});
                nodeStack.push(newNode);
                isLeftStack.push(false);
            }
            if (l <= m - 1) {
                indexStack.push(new int[]{l, m - 1});
                nodeStack.push(newNode);
                isLeftStack.push(true);
            }
        }
        return root;
    }
}

public class Solution {
    static List<Integer> inorder(BSTNode root) {
        List<Integer> res = new ArrayList<>();
        Deque<BSTNode> stack = new ArrayDeque<>();
        BSTNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) { stack.push(cur); cur = cur.left; }
            cur = stack.pop();
            res.add(cur.val);
            cur = cur.right;
        }
        return res;
    }

    static int height(BSTNode root) {
        if (root == null) return 0;
        return 1 + Math.max(height(root.left), height(root.right));
    }

    public static void main(String[] args) {
        System.out.println("=== Sorted Array to Balanced BST ===");

        int[] nums = {-10, -3, 0, 5, 9};
        BSTNode r1 = BruteForce.convert(nums);
        BSTNode r2 = Optimal.convert(nums);
        BSTNode r3 = Best.convert(nums);

        System.out.println("BruteForce inorder: " + inorder(r1) + " height=" + height(r1));
        System.out.println("Optimal    inorder: " + inorder(r2) + " height=" + height(r2));
        System.out.println("Best       inorder: " + inorder(r3) + " height=" + height(r3));

        int[] single = {1};
        System.out.println("Single element: " + inorder(BruteForce.convert(single)));

        int[] even = {1, 2, 3, 4};
        BSTNode re = BruteForce.convert(even);
        System.out.println("Even array inorder: " + inorder(re) + " height=" + height(re));
    }
}
