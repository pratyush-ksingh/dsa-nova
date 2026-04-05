/**
 * Problem: Detect Loop in Linked List
 * Difficulty: EASY | XP: 10
 *
 * Given the head of a singly linked list, determine if it contains a cycle.
 * LeetCode #141 - Linked List Cycle
 *
 * @author DSA_Nova
 */
import java.util.HashSet;

public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- HashSet Visited Tracking
    // Time: O(n)  |  Space: O(n)
    // Store each visited node in a set. If we revisit, there's a cycle.
    // ============================================================
    static class BruteForce {
        public static boolean hasCycle(ListNode head) {
            HashSet<ListNode> visited = new HashSet<>();
            ListNode current = head;

            while (current != null) {
                if (visited.contains(current)) {
                    return true;  // Already seen this node -- cycle!
                }
                visited.add(current);
                current = current.next;
            }
            return false;  // Reached null -- no cycle
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Floyd's Tortoise and Hare
    // Time: O(n)  |  Space: O(1)
    // Two pointers: slow (1 step) and fast (2 steps). If they meet, cycle exists.
    // ============================================================
    static class Optimal {
        public static boolean hasCycle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;          // Move 1 step
                fast = fast.next.next;     // Move 2 steps

                if (slow == fast) {
                    return true;           // They met inside the cycle
                }
            }
            return false;  // Fast reached null -- no cycle
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Floyd's with Cycle Start Detection
    // Time: O(n)  |  Space: O(1)
    // Same Floyd's detection, plus utility to find where the cycle begins.
    // ============================================================
    static class Best {
        public static boolean hasCycle(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Bonus: Find the node where the cycle begins (null if no cycle).
         * After slow and fast meet, reset one to head.
         * Move both at speed 1 -- they meet at the cycle start.
         */
        public static ListNode findCycleStart(ListNode head) {
            ListNode slow = head;
            ListNode fast = head;

            // Phase 1: Detect cycle
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    // Phase 2: Find cycle start
                    slow = head;
                    while (slow != fast) {
                        slow = slow.next;
                        fast = fast.next;
                    }
                    return slow;  // Cycle start node
                }
            }
            return null;  // No cycle
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Detect Loop in Linked List ===\n");

        // Test 1: List with cycle (1->2->3->4->2)
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n2; // cycle at 2

        System.out.println("Test 1: 1->2->3->4->2 (cycle)");
        System.out.println("  Brute Force: " + BruteForce.hasCycle(n1));
        System.out.println("  Optimal:     " + Optimal.hasCycle(n1));
        System.out.println("  Best:        " + Best.hasCycle(n1));
        System.out.println("  Cycle start: " + Best.findCycleStart(n1).val);

        // Test 2: No cycle (1->2->3->null)
        ListNode a1 = new ListNode(1);
        ListNode a2 = new ListNode(2);
        ListNode a3 = new ListNode(3);
        a1.next = a2; a2.next = a3;

        System.out.println("\nTest 2: 1->2->3->null (no cycle)");
        System.out.println("  Brute Force: " + BruteForce.hasCycle(a1));
        System.out.println("  Optimal:     " + Optimal.hasCycle(a1));
        System.out.println("  Best:        " + Best.hasCycle(a1));

        // Test 3: Single node, no cycle
        ListNode single = new ListNode(1);
        System.out.println("\nTest 3: Single node (no cycle)");
        System.out.println("  Brute Force: " + BruteForce.hasCycle(single));
        System.out.println("  Optimal:     " + Optimal.hasCycle(single));

        // Test 4: Single node pointing to itself
        ListNode selfLoop = new ListNode(1);
        selfLoop.next = selfLoop;
        System.out.println("\nTest 4: Single node self-loop");
        System.out.println("  Brute Force: " + BruteForce.hasCycle(selfLoop));
        System.out.println("  Optimal:     " + Optimal.hasCycle(selfLoop));

        // Test 5: Empty list
        System.out.println("\nTest 5: Empty list");
        System.out.println("  Brute Force: " + BruteForce.hasCycle(null));
        System.out.println("  Optimal:     " + Optimal.hasCycle(null));
    }
}
