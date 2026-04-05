/**
 * Problem: Recursive Implementation of atoi (LeetCode #8)
 * Difficulty: MEDIUM | XP: 25
 *
 * Convert a string to a 32-bit signed integer. Handle whitespace,
 * optional sign, digits, overflow clamping. Implement recursively.
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int INT_MAX = Integer.MAX_VALUE; // 2147483647
    static final int INT_MIN = Integer.MIN_VALUE; // -2147483648

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterative
    // Time: O(n)  |  Space: O(1)
    //
    // Standard iterative atoi: skip whitespace, read sign, read
    // digits with overflow checking.
    // ============================================================
    public static int bruteForce(String s) {
        if (s == null || s.isEmpty()) return 0;

        int i = 0, n = s.length();

        // Skip whitespace
        while (i < n && s.charAt(i) == ' ') i++;
        if (i == n) return 0;

        // Read sign
        int sign = 1;
        if (s.charAt(i) == '+' || s.charAt(i) == '-') {
            sign = s.charAt(i) == '-' ? -1 : 1;
            i++;
        }

        // Read digits with overflow check
        int result = 0;
        while (i < n && Character.isDigit(s.charAt(i))) {
            int digit = s.charAt(i) - '0';

            // Overflow check BEFORE multiplication
            if (result > INT_MAX / 10 ||
                (result == INT_MAX / 10 && digit > 7)) {
                return sign == 1 ? INT_MAX : INT_MIN;
            }

            result = result * 10 + digit;
            i++;
        }

        return sign * result;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Digit Processing
    // Time: O(n)  |  Space: O(d) where d = number of digits
    //
    // Preprocess whitespace and sign iteratively, then recurse
    // over the digit portion. Each call processes one digit.
    // ============================================================
    public static int optimal(String s) {
        if (s == null || s.isEmpty()) return 0;

        int i = 0, n = s.length();

        // Skip whitespace
        while (i < n && s.charAt(i) == ' ') i++;
        if (i == n) return 0;

        // Read sign
        int sign = 1;
        if (i < n && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
            sign = s.charAt(i) == '-' ? -1 : 1;
            i++;
        }

        // Recurse over digits
        int result = recurseDigits(s, i, 0);
        return sign * result;
    }

    private static int recurseDigits(String s, int i, int result) {
        // Base case: end of string or non-digit
        if (i >= s.length() || !Character.isDigit(s.charAt(i))) {
            return result;
        }

        int digit = s.charAt(i) - '0';

        // Overflow check BEFORE accumulation
        if (result > INT_MAX / 10 ||
            (result == INT_MAX / 10 && digit > 7)) {
            // Return INT_MAX; the caller will apply the sign
            // to determine if it should be INT_MAX or INT_MIN
            return INT_MAX;
        }

        result = result * 10 + digit;
        return recurseDigits(s, i + 1, result);
    }

    // ============================================================
    // APPROACH 3: BEST -- Fully Recursive (state machine)
    // Time: O(n)  |  Space: O(n) call stack
    //
    // Everything is recursive: whitespace skipping, sign reading,
    // and digit processing. Uses an enum-like state parameter.
    // State: 0=WHITESPACE, 1=SIGN, 2=DIGIT
    // ============================================================
    public static int best(String s) {
        if (s == null || s.isEmpty()) return 0;
        int[] state = fullyRecursive(s, 0, 0, 1, 0);
        // state[0] = result (unsigned), state[1] = sign
        long val = (long) state[1] * state[0];
        if (val > INT_MAX) return INT_MAX;
        if (val < INT_MIN) return INT_MIN;
        return (int) val;
    }

    // Returns [result, sign]
    private static int[] fullyRecursive(String s, int i, int phase, int sign, int result) {
        if (i >= s.length()) return new int[]{result, sign};

        char c = s.charAt(i);

        if (phase == 0) { // WHITESPACE phase
            if (c == ' ') return fullyRecursive(s, i + 1, 0, sign, result);
            return fullyRecursive(s, i, 1, sign, result); // transition to SIGN
        }

        if (phase == 1) { // SIGN phase
            if (c == '-') return fullyRecursive(s, i + 1, 2, -1, result);
            if (c == '+') return fullyRecursive(s, i + 1, 2, 1, result);
            return fullyRecursive(s, i, 2, sign, result); // no sign, go to DIGIT
        }

        // phase == 2: DIGIT phase
        if (!Character.isDigit(c)) return new int[]{result, sign};

        int digit = c - '0';

        // Overflow check
        if (result > INT_MAX / 10 || (result == INT_MAX / 10 && digit > 7)) {
            return new int[]{sign == 1 ? INT_MAX : (INT_MAX), sign};
            // Caller will clamp via long multiplication
        }

        result = result * 10 + digit;
        return fullyRecursive(s, i + 1, 2, sign, result);
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Recursive Implementation of atoi ===\n");

        String[][] tests = {
            {"42",              "42"},
            {"   -42",          "-42"},
            {"+123",            "123"},
            {"4193 with words", "4193"},
            {"words and 987",   "0"},
            {"",                "0"},
            {"-91283472332",    "-2147483648"},
            {"91283472332",     "2147483647"},
            {"  +0 123",        "0"},
            {"   ",             "0"},
            {"-000123",         "-123"},
            {"+-12",            "0"},
        };

        for (String[] test : tests) {
            String input = test[0];
            int expected = Integer.parseInt(test[1]);
            int b = bruteForce(input);
            int o = optimal(input);
            int be = best(input);

            System.out.printf("Input: \"%s\"  Expected: %d%n", input, expected);
            System.out.printf("  Brute:   %d  %s%n", b, b == expected ? "PASS" : "FAIL");
            System.out.printf("  Optimal: %d  %s%n", o, o == expected ? "PASS" : "FAIL");
            System.out.printf("  Best:    %d  %s%n", be, be == expected ? "PASS" : "FAIL");
            System.out.println();
        }
    }
}
