import java.util.*;

/**
 * Populate Next Right Pointers (LeetCode #116)
 *
 * Connect each node to its next right node at the same level in a perfect binary tree.
 */
public class Solution {

    static class Node {
        int val;
        Node left, right, next;
        Node(int val) {
            this.val = val;
            this.next = null;
        }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- BFS with Queue
    // Time: O(N)  |  Space: O(N)
    // ============================================================
    public static Node bruteForce(Node root) {
        if (root == null) return null;

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                // Connect to next node in the level (not the last one)
                node.next = (i < size - 1) ? queue.peek() : null;

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
        }
        return root;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive DFS (pre-order)
    // Time: O(N)  |  Space: O(log N) recursion stack
    // ============================================================
    public static Node optimal(Node root) {
        if (root == null) return null;
        connectDFS(root);
        return root;
    }

    private static void connectDFS(Node node) {
        if (node == null || node.left == null) return; // leaf or null

        // Connect left child to right child (same parent)
        node.left.next = node.right;

        // Connect right child to next subtree's left child (cross-link)
        if (node.next != null) {
            node.right.next = node.next.left;
        }

        connectDFS(node.left);
        connectDFS(node.right);
    }

    // ============================================================
    // APPROACH 3: BEST -- O(1) Space using established next pointers
    // Time: O(N)  |  Space: O(1)
    // ============================================================
    public static Node best(Node root) {
        if (root == null) return null;

        Node leftmost = root;

        // While there is a next level to connect
        while (leftmost.left != null) {
            // Walk across the current level using next pointers
            Node curr = leftmost;
            while (curr != null) {
                // Connection 1: left child -> right child (same parent)
                curr.left.next = curr.right;

                // Connection 2: right child -> next node's left child (cross-link)
                if (curr.next != null) {
                    curr.right.next = curr.next.left;
                }

                curr = curr.next; // move to next node in current level
            }
            leftmost = leftmost.left; // go to next level
        }

        return root;
    }

    // ============================================================
    // HELPER: Print next pointers level by level
    // ============================================================
    private static void printNextPointers(Node root) {
        Node leftmost = root;
        while (leftmost != null) {
            Node curr = leftmost;
            StringBuilder sb = new StringBuilder();
            while (curr != null) {
                sb.append(curr.val);
                sb.append(" -> ");
                curr = curr.next;
            }
            sb.append("null");
            System.out.println(sb.toString());
            leftmost = leftmost.left;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        //      1
        //     / \
        //    2   3
        //   / \ / \
        //  4  5 6  7

        // Test brute force
        Node root1 = buildTree();
        bruteForce(root1);
        System.out.println("=== Brute Force ===");
        printNextPointers(root1);

        // Test optimal
        Node root2 = buildTree();
        optimal(root2);
        System.out.println("=== Optimal ===");
        printNextPointers(root2);

        // Test best
        Node root3 = buildTree();
        best(root3);
        System.out.println("=== Best ===");
        printNextPointers(root3);
    }

    private static Node buildTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        return root;
    }
}
