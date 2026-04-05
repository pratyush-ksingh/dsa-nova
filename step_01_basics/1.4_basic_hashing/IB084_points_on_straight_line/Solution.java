import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^3)  |  Space: O(1)
// For every pair of points define a line; count how many
// other points lie on that line using collinearity check.
// ============================================================
class BruteForce {
    public static int solve(int[] x, int[] y) {
        int n = x.length;
        if (n <= 2) return n;
        int maxPts = 2;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cnt = 2;
                // Line through (x[i],y[i]) and (x[j],y[j])
                // Three points collinear: (y[j]-y[i])*(x[k]-x[i]) == (y[k]-y[i])*(x[j]-x[i])
                for (int k = j + 1; k < n; k++) {
                    long cross = (long)(y[j] - y[i]) * (x[k] - x[i])
                               - (long)(y[k] - y[i]) * (x[j] - x[i]);
                    if (cross == 0) cnt++;
                }
                maxPts = Math.max(maxPts, cnt);
            }
        }
        return maxPts;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n^2)  |  Space: O(n)
// Fix one point; for all others compute slope as a reduced
// fraction (dy/dx in lowest terms) stored in a HashMap.
// Handle vertical lines and duplicate points separately.
// ============================================================
class Optimal {
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int solve(int[] x, int[] y) {
        int n = x.length;
        if (n <= 2) return n;
        int maxPts = 1;
        for (int i = 0; i < n; i++) {
            Map<String, Integer> slopeMap = new HashMap<>();
            int duplicates = 0;
            int localMax = 0;
            for (int j = i + 1; j < n; j++) {
                int dx = x[j] - x[i];
                int dy = y[j] - y[i];
                if (dx == 0 && dy == 0) { duplicates++; continue; }
                // Normalise slope
                int g = gcd(Math.abs(dx), Math.abs(dy));
                dx /= g; dy /= g;
                // Canonical form: dx always positive; if dx==0 dy=1
                if (dx < 0) { dx = -dx; dy = -dy; }
                String key = dy + "/" + dx;
                slopeMap.merge(key, 1, Integer::sum);
                localMax = Math.max(localMax, slopeMap.get(key));
            }
            maxPts = Math.max(maxPts, localMax + duplicates + 1);
        }
        return maxPts;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n^2)  |  Space: O(n)
// Same as Optimal but encode slope as a long (pack dy and dx
// into a single 64-bit key) to avoid string allocation overhead.
// ============================================================
class Best {
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int solve(int[] x, int[] y) {
        int n = x.length;
        if (n <= 2) return n;
        int maxPts = 1;
        for (int i = 0; i < n; i++) {
            Map<Long, Integer> slopeMap = new HashMap<>();
            int dups = 0, localMax = 0;
            for (int j = i + 1; j < n; j++) {
                int dx = x[j] - x[i];
                int dy = y[j] - y[i];
                if (dx == 0 && dy == 0) { dups++; continue; }
                int g = gcd(Math.abs(dx), Math.abs(dy));
                dx /= g; dy /= g;
                if (dx < 0) { dx = -dx; dy = -dy; }
                // pack into long: dy in high 32 bits, dx in low 32 bits
                long key = ((long)(dy + 20001)) << 16 | (dx + 20001);
                slopeMap.merge(key, 1, Integer::sum);
                localMax = Math.max(localMax, slopeMap.get(key));
            }
            maxPts = Math.max(maxPts, localMax + dups + 1);
        }
        return maxPts;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Points on Straight Line ===");

        // Test 1: simple line
        int[] x1 = {1, 2, 3};
        int[] y1 = {1, 2, 3};
        System.out.println("Test1 (expect 3): BF=" + BruteForce.solve(x1, y1)
            + " OPT=" + Optimal.solve(x1, y1) + " BEST=" + Best.solve(x1, y1));

        // Test 2: L-shape
        int[] x2 = {1, 1, 2};
        int[] y2 = {1, 1, 2};  // duplicate + collinear
        System.out.println("Test2 (expect 3): BF=" + BruteForce.solve(x2, y2)
            + " OPT=" + Optimal.solve(x2, y2) + " BEST=" + Best.solve(x2, y2));

        // Test 3: general
        int[] x3 = {0, 1, -1, 2, -2};
        int[] y3 = {0, 1, -1, 2, -2};
        System.out.println("Test3 (expect 5): BF=" + BruteForce.solve(x3, y3)
            + " OPT=" + Optimal.solve(x3, y3) + " BEST=" + Best.solve(x3, y3));

        // Test 4: no 3 collinear
        int[] x4 = {0, 0, 1};
        int[] y4 = {0, 1, 1};
        System.out.println("Test4 (expect 2): BF=" + BruteForce.solve(x4, y4)
            + " OPT=" + Optimal.solve(x4, y4) + " BEST=" + Best.solve(x4, y4));
    }
}
