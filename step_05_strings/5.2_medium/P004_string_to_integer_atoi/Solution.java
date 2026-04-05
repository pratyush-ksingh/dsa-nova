/**
 * Problem: String to Integer Atoi (LeetCode #8)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Strip + Parse with Many Checks
    // Time: O(n)  |  Space: O(1)
    //
    // Trim whitespace, check sign, parse digit by digit with
    // explicit overflow checks at each step.
    // ============================================================
    static class BruteForce {
        public int myAtoi(String s) {
            s = s.trim();
            if (s.isEmpty()) return 0;

            int sign = 1;
            int i = 0;

            if (s.charAt(i) == '-') {
                sign = -1;
                i++;
            } else if (s.charAt(i) == '+') {
                i++;
            }

            int result = 0;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                int digit = s.charAt(i) - '0';

                // Overflow check before accumulation
                if (result > (Integer.MAX_VALUE - digit) / 10) {
                    return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                }

                result = result * 10 + digit;
                i++;
            }

            return sign * result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Clean Single Pass with Overflow Guard
    // Time: O(n)  |  Space: O(1)
    //
    // Three phases: skip whitespace, read sign, read digits.
    // Pre-emptive overflow check before each digit accumulation.
    // ============================================================
    static class Optimal {
        public int myAtoi(String s) {
            int n = s.length();
            int i = 0;

            // Phase 1: Skip whitespace
            while (i < n && s.charAt(i) == ' ') i++;
            if (i == n) return 0;

            // Phase 2: Read sign
            int sign = 1;
            if (s.charAt(i) == '-') {
                sign = -1;
                i++;
            } else if (s.charAt(i) == '+') {
                i++;
            }

            // Phase 3: Read digits with overflow guard
            int result = 0;
            while (i < n && s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                int digit = s.charAt(i) - '0';

                // Pre-emptive overflow check
                if (result > (Integer.MAX_VALUE - digit) / 10) {
                    return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                }

                result = result * 10 + digit;
                i++;
            }

            return sign * result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- DFA State Machine
    // Time: O(n)  |  Space: O(1)
    //
    // Model parsing as a finite automaton with states:
    // START -> SIGNED -> IN_NUMBER -> END.
    // Clean separation of concerns.
    // ============================================================
    static class Best {
        private static final int START = 0;
        private static final int SIGNED = 1;
        private static final int IN_NUMBER = 2;
        private static final int END = 3;

        public int myAtoi(String s) {
            int state = START;
            int sign = 1;
            long result = 0;  // use long for easy overflow detection

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                switch (state) {
                    case START:
                        if (c == ' ') {
                            // stay in START
                        } else if (c == '+' || c == '-') {
                            sign = (c == '-') ? -1 : 1;
                            state = SIGNED;
                        } else if (c >= '0' && c <= '9') {
                            result = c - '0';
                            state = IN_NUMBER;
                        } else {
                            state = END;
                        }
                        break;
                    case SIGNED:
                    case IN_NUMBER:
                        if (c >= '0' && c <= '9') {
                            result = result * 10 + (c - '0');
                            state = IN_NUMBER;
                            // Clamp early
                            if (sign == 1 && result > Integer.MAX_VALUE) {
                                return Integer.MAX_VALUE;
                            }
                            if (sign == -1 && -result < Integer.MIN_VALUE) {
                                return Integer.MIN_VALUE;
                            }
                        } else {
                            state = END;
                        }
                        break;
                    case END:
                        break;
                }
                if (state == END) break;
            }

            return (int) (sign * result);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== String to Integer Atoi ===\n");

        String[] tests = {"42", "   -42", "4193 with words", "words and 987",
                           "-91283472332", "", "   ", "+-12", "+1",
                           "2147483648", "  0000042"};
        int[] expected = {42, -42, 4193, 0, -2147483648, 0, 0, 0, 1,
                          2147483647, 42};

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t];
            System.out.println("Input:    \"" + s + "\"");
            System.out.println("Expected: " + expected[t]);
            System.out.println("Brute:    " + new BruteForce().myAtoi(s));
            System.out.println("Optimal:  " + new Optimal().myAtoi(s));
            System.out.println("Best:     " + new Best().myAtoi(s));
            System.out.println();
        }
    }
}
