import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(max(n,m))  |  Space: O(n+m)
// Split both strings by ".", pad shorter one with "0"s,
// compare each corresponding integer token left to right.
// ============================================================
class BruteForce {
    public static int solve(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int n = Math.max(v1.length, v2.length);
        for (int i = 0; i < n; i++) {
            int n1 = (i < v1.length) ? Integer.parseInt(v1[i]) : 0;
            int n2 = (i < v2.length) ? Integer.parseInt(v2[i]) : 0;
            if (n1 < n2) return -1;
            if (n1 > n2) return 1;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Two-pointer without extra array
// Time: O(max(n,m))  |  Space: O(1) extra (ignoring output)
// Use two pointers on each string, parse each segment on the fly.
// ============================================================
class Optimal {
    public static int solve(String version1, String version2) {
        int i = 0, j = 0;
        int n1 = version1.length(), n2 = version2.length();
        while (i < n1 || j < n2) {
            int num1 = 0, num2 = 0;
            while (i < n1 && version1.charAt(i) != '.') num1 = num1 * 10 + (version1.charAt(i++) - '0');
            while (j < n2 && version2.charAt(j) != '.') num2 = num2 * 10 + (version2.charAt(j++) - '0');
            if (num1 < num2) return -1;
            if (num1 > num2) return 1;
            i++; j++;
        }
        return 0;
    }
}

// ============================================================
// APPROACH 3: BEST - Split + Integer.parseInt with leading zero handling
// Time: O(max(n,m))  |  Space: O(n+m)
// Cleanest Java approach; Integer.parseInt handles leading zeros ("001" -> 1)
// ============================================================
class Best {
    public static int solve(String version1, String version2) {
        String[] a = version1.split("\\.", -1);
        String[] b = version2.split("\\.", -1);
        int len = Math.max(a.length, b.length);
        for (int i = 0; i < len; i++) {
            int va = (i < a.length) ? Integer.parseInt(a[i]) : 0;
            int vb = (i < b.length) ? Integer.parseInt(b[i]) : 0;
            if (va != vb) return va < vb ? -1 : 1;
        }
        return 0;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Compare Version Numbers ===");

        String[][] tests = {
            {"1.0", "1.0.0"},  // expected 0
            {"0.1", "1.1"},    // expected -1
            {"1.0.1", "1"},    // expected 1
            {"1.01", "1.001"}, // expected 0 (leading zeros)
        };
        int[] expected = {0, -1, 1, 0};

        for (int t = 0; t < tests.length; t++) {
            String v1 = tests[t][0], v2 = tests[t][1];
            int bf = BruteForce.solve(v1, v2);
            int op = Optimal.solve(v1, v2);
            int be = Best.solve(v1, v2);
            System.out.printf("\"%s\" vs \"%s\" => Brute=%d, Optimal=%d, Best=%d (exp=%d)%n",
                v1, v2, bf, op, be, expected[t]);
        }
    }
}
