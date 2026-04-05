/**
 * Problem: Online Stock Span (LeetCode 901)
 * Difficulty: MEDIUM | XP: 25
 *
 * Design a StockSpanner class with a next(price) method that returns
 * the span: the number of consecutive days (ending today) where the
 * stock price was <= today's price.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n²) total  |  Space: O(n)
    // Store all prices. For each call, scan backwards until a higher
    // price is found, counting days.
    // ============================================================

    static class StockSpannerBrute {
        private final List<Integer> prices = new ArrayList<>();

        /**
         * Append price to history, then scan backwards.
         * Stop when a strictly greater price is encountered.
         * O(n) per call, O(n^2) overall.
         */
        public int next(int price) {
            prices.add(price);
            int span = 1;
            int i = prices.size() - 2;
            while (i >= 0 && prices.get(i) <= price) {
                span++;
                i--;
            }
            return span;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (monotonic decreasing stack)
    // Time: O(n) amortised  |  Space: O(n)
    // Stack stores (price, span) pairs. When a new price arrives,
    // pop all entries with price <= new price, accumulating their spans.
    // Each element is pushed and popped at most once => O(1) amortised.
    // ============================================================

    static class StockSpannerOptimal {
        // Stack stores int[]{price, span}
        private final Deque<int[]> stack = new ArrayDeque<>();

        /**
         * Monotonic decreasing stack by price.
         * When stack top's price <= current price, absorb its span
         * (those days are also <= today) and pop it.
         * Push (price, accumulated_span) onto the stack.
         */
        public int next(int price) {
            int span = 1;
            while (!stack.isEmpty() && stack.peek()[0] <= price) {
                span += stack.pop()[1];
            }
            stack.push(new int[]{price, span});
            return span;
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (stack stores (price, index) for flexibility)
    // Time: O(n) amortised  |  Space: O(n)
    // Same monotonic stack but stores day index instead of span.
    // span = currentDay - stackTop.index. Useful when you also need
    // to know WHICH previous day had a higher price.
    // ============================================================

    static class StockSpannerBest {
        // Stack stores int[]{price, dayIndex}
        private final Deque<int[]> stack = new ArrayDeque<>();
        private int day = -1;

        /**
         * Track the current day index. Pop all stack entries whose
         * price <= today. Span = today's index minus the index of the
         * surviving stack top (or -1 if stack is empty, meaning all
         * previous days were <= today).
         * Push (price, todayIndex) onto the stack.
         */
        public int next(int price) {
            day++;
            while (!stack.isEmpty() && stack.peek()[0] <= price) {
                stack.pop();
            }
            int span = day - (stack.isEmpty() ? -1 : stack.peek()[1]);
            stack.push(new int[]{price, day});
            return span;
        }
    }

    // ============================================================
    // Helpers & main
    // ============================================================
    static int[] simulate(String type, int[] prices) {
        int[] spans = new int[prices.length];
        if (type.equals("brute")) {
            StockSpannerBrute s = new StockSpannerBrute();
            for (int i = 0; i < prices.length; i++) spans[i] = s.next(prices[i]);
        } else if (type.equals("optimal")) {
            StockSpannerOptimal s = new StockSpannerOptimal();
            for (int i = 0; i < prices.length; i++) spans[i] = s.next(prices[i]);
        } else {
            StockSpannerBest s = new StockSpannerBest();
            for (int i = 0; i < prices.length; i++) spans[i] = s.next(prices[i]);
        }
        return spans;
    }

    public static void main(String[] args) {
        System.out.println("=== Online Stock Span ===\n");

        Object[][] tests = {
            {new int[]{100,80,60,70,60,75,85}, new int[]{1,1,1,2,1,4,6}},
            {new int[]{7,2,1,2},               new int[]{1,1,1,3}},
            {new int[]{100},                   new int[]{1}},
            {new int[]{1,1,1,1},               new int[]{1,2,3,4}},
            {new int[]{5,3,1,2,4},             new int[]{1,1,1,2,4}},
        };

        for (Object[] tc : tests) {
            int[] prices   = (int[]) tc[0];
            int[] expected = (int[]) tc[1];

            int[] b   = simulate("brute",   prices);
            int[] o   = simulate("optimal", prices);
            int[] bst = simulate("best",    prices);

            String status = (Arrays.equals(b, expected) &&
                             Arrays.equals(o, expected) &&
                             Arrays.equals(bst, expected)) ? "PASS" : "FAIL";

            System.out.printf("[%s] Prices:  %s%n", status, Arrays.toString(prices));
            System.out.printf("       Brute:   %s%n", Arrays.toString(b));
            System.out.printf("       Optimal: %s%n", Arrays.toString(o));
            System.out.printf("       Best:    %s%n", Arrays.toString(bst));
            System.out.printf("       Expect:  %s%n%n", Arrays.toString(expected));
        }
    }
}
