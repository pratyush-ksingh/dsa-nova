/**
 * Problem: Check if Linked List is Palindrome (LeetCode #234)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Copy to array, check palindrome
    // Time: O(n)  |  Space: O(n)
    //
    // Collect all node values into an ArrayList. Use two-pointer
    // comparison on the array.
    // ============================================================
    static class BruteForce {
        public boolean isPalindrome(ListNode head) {
            List<Integer> vals = new ArrayList<>();
            ListNode curr = head;
            while (curr != null) {
                vals.add(curr.val);
                curr = curr.next;
            }
            int l = 0, r = vals.size() - 1;
            while (l < r) {
                if (!vals.get(l).equals(vals.get(r))) return false;
                l++; r--;
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Reverse second half + compare
    // Time: O(n)  |  Space: O(1)
    //
    // 1. Find mid with slow/fast pointers.
    // 2. Reverse the second half.
    // 3. Compare both halves.
    // 4. Restore the list.
    // ============================================================
    static class Optimal {
        private ListNode reverse(ListNode head) {
            ListNode prev = null, curr = head;
            while (curr != null) {
                ListNode nxt = curr.next;
                curr.next = prev;
                prev = curr;
                curr = nxt;
            }
            return prev;
        }

        public boolean isPalindrome(ListNode head) {
            if (head == null || head.next == null) return true;

            // Find end of first half
            ListNode slow = head, fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            ListNode secondStart = slow.next;
            slow.next = null;  // cut
            ListNode revSecond = reverse(secondStart);

            // Compare
            ListNode p1 = head, p2 = revSecond;
            boolean result = true;
            while (p2 != null) {
                if (p1.val != p2.val) { result = false; break; }
                p1 = p1.next;
                p2 = p2.next;
            }

            // Restore
            slow.next = reverse(revSecond);
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal, written for clarity
    // Time: O(n)  |  Space: O(1)
    //
    // Identical algorithm with more verbose variable names and
    // explicit restoration step highlighted -- ideal for whiteboard.
    // ============================================================
    static class Best {
        public boolean isPalindrome(ListNode head) {
            if (head == null || head.next == null) return true;

            // Step 1: find mid
            ListNode slow = head, fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            // Step 2: reverse second half
            ListNode prev = null, curr = slow.next;
            slow.next = null;
            while (curr != null) {
                ListNode nxt = curr.next;
                curr.next = prev;
                prev = curr;
                curr = nxt;
            }
            ListNode secondHead = prev;

            // Step 3: compare
            ListNode a = head, b = secondHead;
            boolean palindrome = true;
            while (b != null) {
                if (a.val != b.val) { palindrome = false; break; }
                a = a.next;
                b = b.next;
            }

            // Step 4: restore second half
            prev = null; curr = secondHead;
            while (curr != null) {
                ListNode nxt = curr.next;
                curr.next = prev;
                prev = curr;
                curr = nxt;
            }
            slow.next = prev;

            return palindrome;
        }
    }

    // --------------- helpers ---------------
    static ListNode buildList(int[] vals) {
        if (vals.length == 0) return null;
        ListNode head = new ListNode(vals[0]);
        ListNode curr = head;
        for (int i = 1; i < vals.length; i++) {
            curr.next = new ListNode(vals[i]);
            curr = curr.next;
        }
        return head;
    }

    static List<Integer> toList(ListNode head) {
        List<Integer> res = new ArrayList<>();
        while (head != null) { res.add(head.val); head = head.next; }
        return res;
    }

    public static void main(String[] args) {
        System.out.println("=== Check if Linked List is Palindrome ===\n");

        int[][] vals = {{1,2,2,1}, {1,2}, {1}, {1,2,3,2,1}, {1,2,3,4,5}};
        boolean[] exp = {true, false, true, true, false};

        for (int t = 0; t < vals.length; t++) {
            ListNode h1 = buildList(vals[t]);
            ListNode h2 = buildList(vals[t]);
            ListNode h3 = buildList(vals[t]);
            boolean b  = new BruteForce().isPalindrome(h1);
            boolean o  = new Optimal().isPalindrome(h2);
            boolean bt = new Best().isPalindrome(h3);
            String st = (b == exp[t] && o == exp[t] && bt == exp[t]) ? "OK" : "MISMATCH";
            System.out.printf("List=%s  Expected=%b  Brute=%b  Optimal=%b  Best=%b  [%s]%n",
                    Arrays.toString(vals[t]), exp[t], b, o, bt, st);
            System.out.printf("  Optimal restored: %s%n", toList(h2));
            System.out.printf("  Best restored:    %s%n", toList(h3));
        }
    }
}
