/**
 * Problem: MAXSPPROD
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given array A, for each element find:
 *   L(i) = index of nearest element GREATER than A[i] to the LEFT (1-indexed)
 *   R(i) = index of nearest element GREATER than A[i] to the RIGHT (1-indexed)
 * Return max of L(i) * R(i) over all i. If no such element, L(i)=0 or R(i)=0.
 * Answer modulo 1e9+7.
 *
 * @author DSA_Nova
 */
import java.util.Stack;

public class Solution {

    static final int MOD = 1_000_000_007;

    // ============================================================
    // APPROACH 1: BRUTE FORCE - O(N^2) scan for each element
    // Time: O(N^2)  |  Space: O(N)
    // For each i, scan left and right for nearest larger.
    // ============================================================
    public static long bruteForce(int[] A) {
        int n = A.length;
        long ans = 0;
        for (int i = 0; i < n; i++) {
            // Find nearest greater on left (1-indexed)
            int left = 0;
            for (int j = i - 1; j >= 0; j--) {
                if (A[j] > A[i]) { left = j + 1; break; }
            }
            // Find nearest greater on right (1-indexed)
            int right = 0;
            for (int j = i + 1; j < n; j++) {
                if (A[j] > A[i]) { right = j + 1; break; }
            }
            long prod = ((long) left % MOD) * ((long) right % MOD) % MOD;
            ans = Math.max(ans, prod);
        }
        return ans;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Monotonic Stack for NSL and NSR
    // Time: O(N)  |  Space: O(N)
    // Two passes with decreasing monotonic stack.
    // NSL: for i left-to-right, pop while stack top <= A[i].
    // NSR: for i right-to-left, pop while stack top <= A[i].
    // ============================================================
    public static long optimal(int[] A) {
        int n = A.length;
        int[] L = new int[n]; // 1-indexed nearest greater left
        int[] R = new int[n]; // 1-indexed nearest greater right
        Stack<Integer> stack = new Stack<>();

        // Compute L
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && A[stack.peek()] <= A[i]) stack.pop();
            L[i] = stack.isEmpty() ? 0 : stack.peek() + 1; // 1-indexed
            stack.push(i);
        }

        stack.clear();
        // Compute R
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && A[stack.peek()] <= A[i]) stack.pop();
            R[i] = stack.isEmpty() ? 0 : stack.peek() + 1; // 1-indexed
            stack.push(i);
        }

        long ans = 0;
        for (int i = 0; i < n; i++) {
            long prod = ((long) L[i] % MOD) * ((long) R[i] % MOD) % MOD;
            ans = Math.max(ans, prod);
        }
        return ans;
    }

    // ============================================================
    // APPROACH 3: BEST - Single array pass with two stacks simultaneously
    // Time: O(N)  |  Space: O(N)
    // Same O(N) as optimal, but merges L and R computations into fewer passes.
    // Also handles edge case where product might overflow via modular arithmetic.
    // ============================================================
    public static long best(int[] A) {
        int n = A.length;
        int[] L = new int[n];
        int[] R = new int[n];
        Stack<Integer> stL = new Stack<>(), stR = new Stack<>();

        // Forward pass: NSL (nearest strictly greater to left)
        for (int i = 0; i < n; i++) {
            while (!stL.isEmpty() && A[stL.peek()] <= A[i]) stL.pop();
            L[i] = stL.isEmpty() ? 0 : (stL.peek() + 1);
            stL.push(i);
        }

        // Backward pass: NSR (nearest strictly greater to right)
        for (int i = n - 1; i >= 0; i--) {
            while (!stR.isEmpty() && A[stR.peek()] <= A[i]) stR.pop();
            R[i] = stR.isEmpty() ? 0 : (stR.peek() + 1);
            stR.push(i);
        }

        long ans = 0;
        for (int i = 0; i < n; i++) {
            // Only consider if both L and R exist
            if (L[i] != 0 && R[i] != 0) {
                long prod = ((long) L[i] % MOD * (R[i] % MOD)) % MOD;
                ans = Math.max(ans, prod);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println("=== MAXSPPROD ===");

        // Example: A = [5, 4, 3, 4, 5]
        // For i=1 (A[1]=4): L=1 (A[0]=5>4), R=5 (A[4]=5>4), prod=5
        // For i=2 (A[2]=3): L=2 (A[1]=4>3), R=4 (A[3]=4>3), prod=8
        // For i=3 (A[3]=4): L=1 (A[0]=5>4), R=5 (A[4]=5>4), prod=5
        // Expected max = 8
        int[] A1 = {5, 4, 3, 4, 5};
        System.out.println("A1=[5,4,3,4,5]: brute=" + bruteForce(A1) + " opt=" + optimal(A1) + " best=" + best(A1));

        int[] A2 = {1, 2, 3, 4};
        // i=0: L=0, R=2. i=1: L=0, R=3. i=2: L=0, R=4. i=3: no valid both.
        System.out.println("A2=[1,2,3,4]: brute=" + bruteForce(A2) + " opt=" + optimal(A2) + " best=" + best(A2));

        int[] A3 = {4, 2, 1, 3};
        System.out.println("A3=[4,2,1,3]: brute=" + bruteForce(A3) + " opt=" + optimal(A3) + " best=" + best(A3));
    }
}
