import java.util.*;

/**
 * Problem: Morris Preorder Traversal
 * Difficulty: HARD | XP: 50
 *
 * O(1) space preorder traversal using threaded binary tree (Morris algorithm).
 * No stack, no recursion — modifies tree temporarily then restores it.
 *
 * @author DSA_Nova
 */

class MorrisTreeNode {
    int val;
    MorrisTreeNode left, right;
    MorrisTreeNode(int val) { this.val = val; }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Iterative preorder using explicit stack
// Time: O(N)  |  Space: O(N)
// ============================================================
class BruteForce {
    static List<Integer> preorder(MorrisTreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Deque<MorrisTreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            MorrisTreeNode node = stack.pop();
            result.add(node.val);
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
        }
        return result;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Morris Traversal — standard implementation
// For each node: if no left child, visit and go right.
// Else, find inorder predecessor. If its right is null, thread it back
// to current (visit current now for preorder), go left.
// If threaded, unthread and go right.
// Time: O(N)  |  Space: O(1)
// ============================================================
class Optimal {
    static List<Integer> preorder(MorrisTreeNode root) {
        List<Integer> result = new ArrayList<>();
        MorrisTreeNode curr = root;
        while (curr != null) {
            if (curr.left == null) {
                result.add(curr.val);
                curr = curr.right;
            } else {
                // Find inorder predecessor
                MorrisTreeNode pred = curr.left;
                while (pred.right != null && pred.right != curr) {
                    pred = pred.right;
                }
                if (pred.right == null) {
                    // First visit: thread and visit current (preorder = visit before going left)
                    result.add(curr.val);
                    pred.right = curr;
                    curr = curr.left;
                } else {
                    // Second visit: unthread, move right
                    pred.right = null;
                    curr = curr.right;
                }
            }
        }
        return result;
    }
}

// ============================================================
// APPROACH 3: BEST
// Recursive preorder — simplest correct reference implementation
// Time: O(N)  |  Space: O(H) stack
// ============================================================
class Best {
    static List<Integer> preorder(MorrisTreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfs(root, result);
        return result;
    }

    static void dfs(MorrisTreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);
        dfs(node.left, result);
        dfs(node.right, result);
    }
}

public class Solution {
    static MorrisTreeNode build(Integer[] vals) {
        if (vals == null || vals.length == 0) return null;
        MorrisTreeNode root = new MorrisTreeNode(vals[0]);
        Queue<MorrisTreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            MorrisTreeNode cur = q.poll();
            if (i < vals.length && vals[i] != null) { cur.left = new MorrisTreeNode(vals[i]); q.offer(cur.left); }
            i++;
            if (i < vals.length && vals[i] != null) { cur.right = new MorrisTreeNode(vals[i]); q.offer(cur.right); }
            i++;
        }
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== Morris Preorder Traversal ===");

        // Tree: 1->2,3; 2->4,5
        Integer[] vals = {1, 2, 3, 4, 5};
        // Expected preorder: [1, 2, 4, 5, 3]

        System.out.println("BruteForce: " + BruteForce.preorder(build(vals)));
        System.out.println("Optimal (Morris): " + Optimal.preorder(build(vals)));
        System.out.println("Best (Recursive): " + Best.preorder(build(vals)));

        // Skewed left tree
        MorrisTreeNode skewed = new MorrisTreeNode(1);
        skewed.left = new MorrisTreeNode(2);
        skewed.left.left = new MorrisTreeNode(3);
        System.out.println("Skewed Morris: " + Optimal.preorder(skewed)); // [1,2,3]

        // Single node
        System.out.println("Single node: " + Optimal.preorder(new MorrisTreeNode(42))); // [42]
    }
}
