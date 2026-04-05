/**
 * Problem: Design Browser History (LeetCode #1472)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- ArrayList with current index
    // Time: visit O(n) worst, back/forward O(1)  |  Space: O(n)
    //
    // Store pages in an ArrayList. curr tracks position.
    // On visit: subList to truncate forward history, then add.
    // On back/forward: clamp-move curr.
    // ============================================================
    static class BrowserHistoryBrute {
        private List<String> history = new ArrayList<>();
        private int curr = 0;

        public BrowserHistoryBrute(String homepage) {
            history.add(homepage);
        }

        public void visit(String url) {
            // Truncate everything after curr
            while (history.size() > curr + 1) history.remove(history.size() - 1);
            history.add(url);
            curr++;
        }

        public String back(int steps) {
            curr = Math.max(0, curr - steps);
            return history.get(curr);
        }

        public String forward(int steps) {
            curr = Math.min(history.size() - 1, curr + steps);
            return history.get(curr);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Doubly Linked List
    // Time: visit O(1), back O(steps), forward O(steps)  |  Space: O(n)
    //
    // Each page is a DLL node. Visit creates a new node linked to
    // current (discarding the forward chain). Back/forward walks
    // the pointer.
    // ============================================================
    static class BrowserHistoryOptimal {
        private static class Page {
            String url;
            Page prev, next;
            Page(String url) { this.url = url; }
        }

        private Page curr;

        public BrowserHistoryOptimal(String homepage) {
            curr = new Page(homepage);
        }

        public void visit(String url) {
            Page page = new Page(url);
            page.prev = curr;
            curr.next = page;   // forward chain is silently discarded
            curr = page;
        }

        public String back(int steps) {
            while (steps-- > 0 && curr.prev != null) curr = curr.prev;
            return curr.url;
        }

        public String forward(int steps) {
            while (steps-- > 0 && curr.next != null) curr = curr.next;
            return curr.url;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Single array with two pointers
    // Time: visit O(1), back O(1), forward O(1)  |  Space: O(n)
    //
    // Use a String[] with curr and last pointers. Avoids node
    // allocation and pointer chasing -- best cache locality.
    // ============================================================
    static class BrowserHistoryBest {
        private String[] history;
        private int curr = 0, last = 0;

        public BrowserHistoryBest(String homepage) {
            history = new String[5001]; // max calls = 5000
            history[0] = homepage;
        }

        public void visit(String url) {
            history[++curr] = url;
            last = curr;   // invalidate forward history
        }

        public String back(int steps) {
            curr = Math.max(0, curr - steps);
            return history[curr];
        }

        public String forward(int steps) {
            curr = Math.min(last, curr + steps);
            return history[curr];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Design Browser History ===\n");

        String[] expected = {"facebook.com", "google.com", "facebook.com",
                             "linkedin.com", "google.com", "leetcode.com"};

        for (int approach = 0; approach < 3; approach++) {
            String[] result = new String[6];
            if (approach == 0) {
                BrowserHistoryBrute bh = new BrowserHistoryBrute("leetcode.com");
                bh.visit("google.com"); bh.visit("facebook.com"); bh.visit("youtube.com");
                result[0] = bh.back(1); result[1] = bh.back(1); result[2] = bh.forward(1);
                bh.visit("linkedin.com");
                result[3] = bh.forward(2); result[4] = bh.back(2); result[5] = bh.back(7);
            } else if (approach == 1) {
                BrowserHistoryOptimal bh = new BrowserHistoryOptimal("leetcode.com");
                bh.visit("google.com"); bh.visit("facebook.com"); bh.visit("youtube.com");
                result[0] = bh.back(1); result[1] = bh.back(1); result[2] = bh.forward(1);
                bh.visit("linkedin.com");
                result[3] = bh.forward(2); result[4] = bh.back(2); result[5] = bh.back(7);
            } else {
                BrowserHistoryBest bh = new BrowserHistoryBest("leetcode.com");
                bh.visit("google.com"); bh.visit("facebook.com"); bh.visit("youtube.com");
                result[0] = bh.back(1); result[1] = bh.back(1); result[2] = bh.forward(1);
                bh.visit("linkedin.com");
                result[3] = bh.forward(2); result[4] = bh.back(2); result[5] = bh.back(7);
            }
            boolean ok = Arrays.equals(result, expected);
            String[] names = {"Brute", "Optimal", "Best"};
            System.out.printf("%s: %s  [%s]%n", names[approach],
                    Arrays.toString(result), ok ? "OK" : "MISMATCH");
        }
    }
}
