/**
 * Problem: Reverse Words in a String (LeetCode #151)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Split, Reverse, Join
    // Time: O(n)  |  Space: O(n)
    //
    // Split by whitespace, reverse the array, join.
    // ============================================================
    static class BruteForce {
        public String reverseWords(String s) {
            String[] words = s.trim().split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (int i = words.length - 1; i >= 0; i--) {
                sb.append(words[i]);
                if (i > 0) sb.append(' ');
            }
            return sb.toString();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Backward Scan with Two Pointers
    // Time: O(n)  |  Space: O(n)
    //
    // Scan from the end, extract words in reverse order.
    // ============================================================
    static class Optimal {
        public String reverseWords(String s) {
            StringBuilder sb = new StringBuilder();
            int n = s.length();
            int i = n - 1;

            while (i >= 0) {
                while (i >= 0 && s.charAt(i) == ' ') i--;
                if (i < 0) break;

                int end = i;
                while (i >= 0 && s.charAt(i) != ' ') i--;

                if (sb.length() > 0) sb.append(' ');
                sb.append(s, i + 1, end + 1);
            }
            return sb.toString();
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- In-Place Double Reversal
    // Time: O(n)  |  Space: O(n) due to Java string immutability
    //
    // Reverse entire char array, reverse each word, compact spaces.
    // ============================================================
    static class Best {
        public String reverseWords(String s) {
            char[] arr = s.toCharArray();
            int n = arr.length;

            reverse(arr, 0, n - 1);

            int write = 0;
            int i = 0;
            while (i < n) {
                if (arr[i] == ' ') { i++; continue; }
                if (write > 0) arr[write++] = ' ';

                int start = write;
                while (i < n && arr[i] != ' ') {
                    arr[write++] = arr[i++];
                }
                reverse(arr, start, write - 1);
            }
            return new String(arr, 0, write);
        }

        private void reverse(char[] arr, int left, int right) {
            while (left < right) {
                char tmp = arr[left];
                arr[left++] = arr[right];
                arr[right--] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse Words in a String ===\n");

        String[] tests = {"the sky is blue", "  hello world  ", "a good   example"};
        String[] expected = {"blue is sky the", "world hello", "example good a"};

        for (int t = 0; t < tests.length; t++) {
            String s = tests[t];
            System.out.println("Input:    \"" + s + "\"");
            System.out.println("Expected: \"" + expected[t] + "\"");
            System.out.println("Brute:    \"" + new BruteForce().reverseWords(s) + "\"");
            System.out.println("Optimal:  \"" + new Optimal().reverseWords(s) + "\"");
            System.out.println("Best:     \"" + new Best().reverseWords(s) + "\"");
            System.out.println();
        }
    }
}
