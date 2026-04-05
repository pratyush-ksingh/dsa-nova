# Generate Parentheses

> **Step 07.7.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given `n` pairs of parentheses, write a function to generate all combinations of well-formed (valid) parentheses.

**LeetCode #22**

**Constraints:**
- `1 <= n <= 8`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `n = 1` | `["()"]` | Only one valid combination |
| `n = 2` | `["(())", "()()"]` | Two valid combinations |
| `n = 3` | `["((()))", "(()())", "(())()", "()(())", "()()()"]` | Five valid combinations (Catalan number C3) |

### Real-Life Analogy
> *Think of nested function calls in code. Each `(` is opening a function call and `)` is closing it. You can nest calls inside each other or chain them sequentially, but you can never close a call that was never opened. Generating all valid parentheses is like enumerating every possible nesting pattern of n function calls.*

### Key Observations
1. At any point while building the string, the number of `(` used must not exceed `n`, and the number of `)` must not exceed the number of `(` already placed. This is the core constraint that prunes invalid branches.
2. The total number of valid combinations is the nth Catalan number: C(n) = C(2n, n) / (n+1).
3. Every valid string has exactly `n` opening and `n` closing parentheses (length 2n).

---

## APPROACH LADDER

### Approach 1: Brute Force -- Generate All & Filter
**Intuition:** Generate every possible string of length 2n using `(` and `)`, then check each string for validity. A string is valid if at every prefix the count of `(` >= count of `)` and at the end they are equal.

**Steps:**
1. Recursively build all strings of length `2n` using characters `(` and `)`.
2. For each complete string, validate it by scanning left to right tracking balance.
3. If valid (balance never goes negative and ends at 0), add to result.

**Why it's slow:** We generate 2^(2n) strings but only ~4^n/sqrt(n) are valid. Most generated strings are invalid.

| Metric | Value |
|--------|-------|
| Time   | O(2^(2n) * n) |
| Space  | O(2^(2n) * n) |

---

### Approach 2: Optimal -- Backtracking with Open/Close Counts
**Intuition:** Instead of generating all strings and filtering, we prune during generation. We track how many `(` and `)` we have placed. We can add `(` only if open_count < n, and `)` only if close_count < open_count. This ensures we never build an invalid string.

**Steps:**
1. Start with an empty string/list, `open = 0`, `close = 0`.
2. If length equals `2n`, add the string to results.
3. If `open < n`, recurse with an added `(`.
4. If `close < open`, recurse with an added `)`.
5. Backtrack by removing the last character after each recursive call.

**Dry Run:** `n = 2`
```
backtrack("", 0, 0)
  -> backtrack("(", 1, 0)
       -> backtrack("((", 2, 0)
            -> backtrack("(()", 2, 1)
                 -> backtrack("(())", 2, 2) --> ADD "(())"
       -> backtrack("()", 1, 1)
            -> backtrack("()(", 2, 1)
                 -> backtrack("()()", 2, 2) --> ADD "()()"
```

| Metric | Value |
|--------|-------|
| Time   | O(4^n / sqrt(n)) -- nth Catalan number |
| Space  | O(n) recursion depth |

---

### Approach 3: Best -- Same Backtracking (Clean Variant)
**Intuition:** The backtracking approach IS the optimal solution for this generation problem. This variant uses immutable string concatenation for cleaner code. The time complexity cannot be improved since we must output all valid combinations.

| Metric | Value |
|--------|-------|
| Time   | O(4^n / sqrt(n)) |
| Space  | O(n) recursion depth |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting the close < open constraint:** Allowing `)` before enough `(` are placed leads to invalid strings.
2. **Off-by-one in termination:** The final string length should be `2*n`, not `n`.
3. **Not backtracking:** When using a mutable StringBuilder/list, forgetting to pop the last character after recursion.

### Edge Cases
- `n = 1` -- only one valid combination: `"()"`
- `n = 0` -- empty result or `[""]` depending on problem definition

---

## Real-World Use Case
Generating valid parentheses maps directly to enumerating all possible **expression trees** with n operators. Compilers use this when analyzing all possible parse trees. It also appears in generating all possible **binary tree structures** with n nodes (each structure corresponds to a unique parenthesization).

## Interview Tips
- Start by clarifying: "I need to generate ALL valid combinations, not just count them."
- Mention the Catalan number connection to show mathematical depth.
- Use the backtracking template: choose, explore, unchoose.
- The key insight is the pruning condition: `close < open` ensures we never create invalid prefixes.
- Follow-up: "How would you generate the kth valid combination without generating all?" -- use the Catalan number ranking/unranking.
