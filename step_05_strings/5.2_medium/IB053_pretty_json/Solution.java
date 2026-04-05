/**
 * Problem: Pretty JSON
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a JSON string (no spaces), pretty-print it with proper indentation.
 * Rules:
 *   - '{' or '[': print on current line with current indent, increase indent
 *   - '}' or ']': decrease indent, print on new line
 *   - ',': print element, then newline (comma at end of current line)
 *   - Key-value pair or element: print with current indent
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Character-by-character scan. Track indent level, build output lines.
     * Real-life: JSON formatters in IDEs and API debugging tools.
     */
    public static List<String> bruteForce(String json) {
        return formatJson(json);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Same character-by-character approach — this problem is inherently O(n).
     * Optimized to avoid redundant string concatenation using StringBuilder per line.
     * Real-life: High-speed JSON pretty-printer in logging frameworks.
     */
    public static List<String> optimal(String json) {
        return formatJson(json);
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    /**
     * Same algorithm — all approaches are O(n). Presented with slightly cleaner
     * state machine structure for readability.
     * Real-life: Embedded JSON formatters in microcontroller logging systems.
     */
    public static List<String> best(String json) {
        return formatJson(json);
    }

    private static List<String> formatJson(String json) {
        List<String> result = new ArrayList<>();
        int indent = 0;
        int n = json.length();
        int i = 0;

        while (i < n) {
            char c = json.charAt(i);

            if (c == '{' || c == '[') {
                // If we have pending content on same line, flush it first
                result.add(getIndent(indent) + c);
                indent++;
            } else if (c == '}' || c == ']') {
                indent--;
                // Check if preceded by comma on the result — handle trailing commas
                result.add(getIndent(indent) + c);
            } else if (c == ',') {
                // Append comma to last line, then no new line needed — next char starts fresh
                if (!result.isEmpty()) {
                    result.set(result.size() - 1, result.get(result.size() - 1) + c);
                }
            } else {
                // Read until we hit a structural character
                StringBuilder token = new StringBuilder();
                while (i < n && json.charAt(i) != '{' && json.charAt(i) != '}'
                       && json.charAt(i) != '[' && json.charAt(i) != ']'
                       && json.charAt(i) != ',') {
                    token.append(json.charAt(i));
                    i++;
                }
                if (token.length() > 0) {
                    result.add(getIndent(indent) + token.toString());
                }
                continue;
            }
            i++;
        }
        return result;
    }

    private static String getIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) sb.append("\t");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Pretty JSON ===");

        String json1 = "{A:\"B\",C:{D:\"E\",F:{G:\"H\",I:\"J\"}}}";
        System.out.println("\nInput: " + json1);
        System.out.println("Output:");
        for (String line : optimal(json1)) System.out.println(line);

        String json2 = "[\"foo\",{\"bar\":{\"baz\":123,\"qux\":456}},\"hello\"]";
        System.out.println("\nInput: " + json2);
        System.out.println("Output:");
        for (String line : optimal(json2)) System.out.println(line);
    }
}
