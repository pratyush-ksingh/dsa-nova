/**
 * Problem: Gray Code
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Generate the n-bit Gray code sequence (2^n numbers starting from 0,
 * each consecutive pair differs by exactly 1 bit, wraps around too).
 *
 * @author DSA_Nova
 */
import java.util.ArrayList;
import java.util.List;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Recursive Reflect-and-Prefix
    // Time: O(2^N)  |  Space: O(2^N)
    // G(1) = [0, 1]. G(n) = [0+G(n-1), 1+reverse(G(n-1))]
    // ============================================================
    public static List<Integer> bruteForce(int n) {
        if (n == 0) {
            List<Integer> base = new ArrayList<>();
            base.add(0);
            return base;
        }
        List<Integer> prev = bruteForce(n - 1);
        List<Integer> result = new ArrayList<>(prev);
        int highBit = 1 << (n - 1);
        // Mirror: iterate prev in reverse, add highBit
        for (int i = prev.size() - 1; i >= 0; i--) {
            result.add(highBit | prev.get(i));
        }
        return result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - XOR Formula: G(i) = i ^ (i >> 1)
    // Time: O(2^N)  |  Space: O(2^N)
    // The i-th Gray code is i XOR (i >> 1). Direct O(1) per element.
    // ============================================================
    public static List<Integer> optimal(int n) {
        int total = 1 << n;
        List<Integer> result = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            result.add(i ^ (i >> 1));
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: BEST - Iterative bit-flip approach
    // Time: O(2^N)  |  Space: O(2^N)
    // Start with 0. At step k, flip bit (k & -k) (lowest set bit of k).
    // Builds the sequence one flip at a time. Same complexity, different insight.
    // ============================================================
    public static List<Integer> best(int n) {
        int total = 1 << n;
        List<Integer> result = new ArrayList<>(total);
        int cur = 0;
        result.add(0);
        for (int k = 1; k < total; k++) {
            // Flip the bit at position of lowest set bit of k
            cur ^= (k & -k);
            result.add(cur);
        }
        return result;
    }

    private static void printBinary(List<Integer> codes, int n) {
        for (int c : codes) {
            System.out.printf("%" + n + "s%n", Integer.toBinaryString(c)).replace(' ', '0');
            System.out.printf("%0" + n + "d%n", Integer.parseInt(Integer.toBinaryString(c)));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Gray Code ===");

        System.out.println("n=2 brute:   " + bruteForce(2));   // [0,1,3,2]
        System.out.println("n=2 optimal: " + optimal(2));
        System.out.println("n=2 best:    " + best(2));

        System.out.println("n=3 brute:   " + bruteForce(3));   // [0,1,3,2,6,7,5,4]
        System.out.println("n=3 optimal: " + optimal(3));
        System.out.println("n=3 best:    " + best(3));

        System.out.println("n=1 brute:   " + bruteForce(1));   // [0,1]
        System.out.println("n=1 optimal: " + optimal(1));
        System.out.println("n=1 best:    " + best(1));
    }
}
