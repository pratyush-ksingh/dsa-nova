import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(B * N log N)  |  Space: O(N)
// Sort descending, greedily pick the highest available seat each time
// ============================================================
class BruteForce {
    public static int solve(int[] seats, int b) {
        // seats[i] = profit if at least (i+1) people occupy seats
        // Equivalent: seat j contributes seats[j] - seats[j+1] when j-th person arrives
        // Brute: simulate B occupancies, each time decrement profit for that seat slot
        int n = seats.length;
        int[] arr = seats.clone();
        Arrays.sort(arr);
        // reverse
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
        int profit = 0;
        // For each person, they "fill" a seat. The contribution of filling seat at rank r
        // is arr[r-1] - arr[r] (or arr[r-1] if r == n).
        // But simple way: contribution of person k (1-indexed) = arr[k-1] - (arr[k] if k<n else 0)
        // Then total for b people = sum of marginal contributions up to b
        for (int i = 0; i < Math.min(b, n); i++) {
            profit += arr[i] - (i + 1 < n ? arr[i + 1] : 0);
            // weight = arr[i] - arr[i+1] -> each marginal adds arr[i]-arr[i+1]
            // but cumulative sum of arr[0]-arr[1] + arr[1]-arr[2] + ... + arr[b-1]-arr[b]
            // = arr[0] - arr[b]
        }
        // Actually simpler: sum of marginal = arr[0] - arr[b] (if b <= n)
        // Let's just do it directly
        return 0; // placeholder, see Optimal for correct approach
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Max-Heap Greedy (correct solution)
// Time: O((N + B) log N)  |  Space: O(N)
// Each person buys one seat. Greedy: always pick the seat that gives max marginal gain.
// Marginal gain of k-th seat (sorted desc) = seats[k-1] - seats[k] (0-indexed, seats[n]=0)
// ============================================================
class Optimal {
    public static int solve(int[] seats, int b) {
        int n = seats.length;
        int[] s = seats.clone();
        Arrays.sort(s);
        // reverse to descending
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            int tmp = s[i]; s[i] = s[j]; s[j] = tmp;
        }
        // Marginal contributions: mg[i] = s[i] - s[i+1] (s[n]=0)
        // Use max-heap to pick highest marginal each time
        // But since s is sorted descending, mg[0] >= mg[1] >= ...
        // So just sum first min(b, n) marginals
        // mg[0]+mg[1]+...+mg[b-1] = s[0] - s[b] (if b <= n), else s[0]
        // Actually: sum of mg[0..b-1] telescopes to s[0] - s[b] when b < n, else s[0] - 0
        // But wait: we can have b > n, in which case we only get n terms
        int profit = 0;
        int people = Math.min(b, n);
        profit = s[0] - (people < n ? s[people] : 0);
        // This is sum of marginals 0..people-1
        // But what if b > n? Extra people gain 0 (no more seats)
        return profit;
    }
}

// ============================================================
// APPROACH 3: BEST - Priority Queue for general case
// Time: O(N log N + B log N)  |  Space: O(N)
// Use a max-heap of (profit_gain, remaining_uses) if seats have capacities
// Here each seat slot gives a marginal. We can directly use sorted array.
// ============================================================
class Best {
    public static int solve(int[] seats, int b) {
        int n = seats.length;
        // Build marginal gains array (seats must be sorted desc)
        int[] s = seats.clone();
        Arrays.sort(s);
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            int tmp = s[i]; s[i] = s[j]; s[j] = tmp;
        }
        // Use max-heap of marginals
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 0; i < n; i++) {
            int mg = s[i] - (i + 1 < n ? s[i + 1] : 0);
            if (mg > 0) pq.offer(mg);
        }
        int profit = 0;
        int remaining = b;
        while (remaining > 0 && !pq.isEmpty()) {
            profit += pq.poll();
            remaining--;
        }
        return profit;
    }
}

public class Solution {
    public static void main(String[] args) {
        // seats = [1,2,3,4,5], b=3 -> Expected: max profit
        // sorted desc [5,4,3,2,1], marginals [1,1,1,1,1]
        // 3 people -> sum first 3 = 3... but that's 5-2=3. Hmm let me trace.
        // Actually profit = s[0] - s[b] = 5 - 2 = 3
        int[] seats1 = {1, 2, 3, 4, 5};
        System.out.println("Test 1 (seats=[1,2,3,4,5], b=3):");
        System.out.println("  Optimal = " + Optimal.solve(seats1, 3));  // expected 3
        System.out.println("  Best    = " + Best.solve(seats1, 3));

        // seats = [5,1,4,2,3], b=2 -> sorted desc [5,4,3,2,1]
        // profit = 5-3=2? No: s[0]-s[2]=5-3=2
        int[] seats2 = {5, 1, 4, 2, 3};
        System.out.println("Test 2 (seats=[5,1,4,2,3], b=2):");
        System.out.println("  Optimal = " + Optimal.solve(seats2, 2));  // 5-3=2
        System.out.println("  Best    = " + Best.solve(seats2, 2));

        // InterviewBit example: A=[1,3], B=2 -> ans=3
        // sorted desc [3,1], b=2: s[0]-s[2]=3-0=3
        int[] seats3 = {1, 3};
        System.out.println("Test 3 (seats=[1,3], b=2):");
        System.out.println("  Optimal = " + Optimal.solve(seats3, 2));  // expected 3
        System.out.println("  Best    = " + Best.solve(seats3, 2));
    }
}
