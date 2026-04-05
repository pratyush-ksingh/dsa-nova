/**
 * Problem: Clone LL with Random Pointer
 * Difficulty: HARD | XP: 50
 *
 * @author DSA_Nova
 */

// Node definition for a linked list with a random pointer
class NodeRandom {
    int val;
    NodeRandom next;
    NodeRandom random;

    NodeRandom(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n)  |  Space: O(n)
// ============================================================
// Use a HashMap to map each original node to its clone.
// First pass: create all clone nodes and store mappings.
// Second pass: wire up next and random pointers using the map.
class BruteForce {
    public NodeRandom cloneList(NodeRandom head) {
        if (head == null) return null;

        java.util.HashMap<NodeRandom, NodeRandom> map = new java.util.HashMap<>();

        // First pass: create clone nodes
        NodeRandom curr = head;
        while (curr != null) {
            map.put(curr, new NodeRandom(curr.val));
            curr = curr.next;
        }

        // Second pass: wire next and random pointers
        curr = head;
        while (curr != null) {
            if (curr.next != null)   map.get(curr).next   = map.get(curr.next);
            if (curr.random != null) map.get(curr).random = map.get(curr.random);
            curr = curr.next;
        }

        return map.get(head);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n)  |  Space: O(1)  (no extra map)
// ============================================================
// Interweave technique — three passes:
//   Pass 1: Insert clones between originals: A -> A' -> B -> B' -> ...
//   Pass 2: Set random pointers of clones: A'.random = A.random.next
//   Pass 3: Separate the two lists and restore the original.
class Optimal {
    public NodeRandom cloneList(NodeRandom head) {
        if (head == null) return null;

        // Pass 1: interweave clone nodes
        NodeRandom curr = head;
        while (curr != null) {
            NodeRandom clone = new NodeRandom(curr.val);
            clone.next = curr.next;
            curr.next = clone;
            curr = clone.next;
        }

        // Pass 2: assign random pointers for clones
        curr = head;
        while (curr != null) {
            if (curr.random != null) {
                curr.next.random = curr.random.next;
            }
            curr = curr.next.next;
        }

        // Pass 3: separate lists
        NodeRandom dummy = new NodeRandom(0);
        NodeRandom cloneCurr = dummy;
        curr = head;
        while (curr != null) {
            cloneCurr.next = curr.next;
            curr.next = curr.next.next;
            cloneCurr = cloneCurr.next;
            curr = curr.next;
        }

        return dummy.next;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n)  |  Space: O(1)
// ============================================================
// Same interweave approach as Optimal — this IS the best known
// algorithm for this problem (O(n) time, O(1) extra space).
// Written here as a clean self-contained version with clearer
// variable names and combined into one tight method.
class Best {
    public NodeRandom cloneList(NodeRandom head) {
        if (head == null) return null;

        // Step 1: Interleave — insert each clone right after its original
        for (NodeRandom node = head; node != null; node = node.next.next) {
            NodeRandom copy = new NodeRandom(node.val);
            copy.next  = node.next;
            node.next  = copy;
        }

        // Step 2: Wire random pointers of clones
        for (NodeRandom node = head; node != null; node = node.next.next) {
            if (node.random != null) {
                node.next.random = node.random.next;
            }
        }

        // Step 3: Unweave — restore original list and extract clone list
        NodeRandom cloneHead = head.next;
        for (NodeRandom node = head; node != null; node = node.next) {
            NodeRandom copy = node.next;
            node.next = copy.next;
            if (copy.next != null) copy.next = copy.next.next;
        }

        return cloneHead;
    }
}

public class Solution {

    // Helper: build a list from int array; random pointers set manually after
    static NodeRandom buildList(int[] vals) {
        if (vals.length == 0) return null;
        NodeRandom[] nodes = new NodeRandom[vals.length];
        for (int i = 0; i < vals.length; i++) nodes[i] = new NodeRandom(vals[i]);
        for (int i = 0; i + 1 < vals.length; i++) nodes[i].next = nodes[i + 1];
        return nodes[0];
    }

    // Helper: print list with random pointer values
    static void printList(NodeRandom head) {
        NodeRandom curr = head;
        StringBuilder sb = new StringBuilder();
        while (curr != null) {
            sb.append("[").append(curr.val).append(",")
              .append(curr.random == null ? "null" : curr.random.val)
              .append("]");
            if (curr.next != null) sb.append(" -> ");
            curr = curr.next;
        }
        System.out.println(sb);
    }

    // Helper: verify deep copy (different object references, same structure)
    static boolean verifyDeepCopy(NodeRandom orig, NodeRandom clone) {
        NodeRandom o = orig, c = clone;
        while (o != null && c != null) {
            if (o == c) return false; // same reference — not a deep copy
            if (o.val != c.val) return false;
            boolean randomMatch =
                (o.random == null && c.random == null) ||
                (o.random != null && c.random != null && o.random.val == c.random.val);
            if (!randomMatch) return false;
            o = o.next;
            c = c.next;
        }
        return o == null && c == null;
    }

    public static void main(String[] args) {
        System.out.println("=== Clone LL with Random Pointer ===\n");

        // Build test list: 7 -> 13 -> 11 -> 10 -> 1
        // Random: 7->null, 13->7, 11->1, 10->11, 1->7
        int[] vals = {7, 13, 11, 10, 1};
        NodeRandom[] nodes = new NodeRandom[5];
        for (int i = 0; i < 5; i++) nodes[i] = new NodeRandom(vals[i]);
        nodes[0].next = nodes[1]; nodes[1].next = nodes[2];
        nodes[2].next = nodes[3]; nodes[3].next = nodes[4];
        nodes[1].random = nodes[0];
        nodes[2].random = nodes[4];
        nodes[3].random = nodes[2];
        nodes[4].random = nodes[0];
        // nodes[0].random stays null

        System.out.print("Original: "); printList(nodes[0]);

        // --- Brute Force ---
        System.out.println("\n[Approach 1 - BruteForce: HashMap]");
        NodeRandom clone1 = new BruteForce().cloneList(nodes[0]);
        System.out.print("Clone:    "); printList(clone1);
        System.out.println("Deep copy verified: " + verifyDeepCopy(nodes[0], clone1));

        // --- Optimal ---
        System.out.println("\n[Approach 2 - Optimal: Interweave]");
        NodeRandom clone2 = new Optimal().cloneList(nodes[0]);
        System.out.print("Clone:    "); printList(clone2);
        System.out.println("Deep copy verified: " + verifyDeepCopy(nodes[0], clone2));

        // --- Best ---
        System.out.println("\n[Approach 3 - Best: Interweave (clean)]");
        NodeRandom clone3 = new Best().cloneList(nodes[0]);
        System.out.print("Clone:    "); printList(clone3);
        System.out.println("Deep copy verified: " + verifyDeepCopy(nodes[0], clone3));

        // Edge case: single node with random pointing to itself
        System.out.println("\n[Edge case: single node, random -> self]");
        NodeRandom single = new NodeRandom(42);
        single.random = single;
        NodeRandom sc = new Best().cloneList(single);
        System.out.println("Clone val: " + sc.val + ", random val: " + sc.random.val
            + ", different object: " + (sc != single));
    }
}
