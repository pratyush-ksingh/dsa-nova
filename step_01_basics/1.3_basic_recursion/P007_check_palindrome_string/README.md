# Check Palindrome String (Recursion)

> **Batch 3 of 12** | **Topic:** Recursion | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given a string **s**, determine whether it is a **palindrome** using **recursion**. A palindrome reads the same forwards and backwards (e.g., "madam", "racecar"). Ignore case differences for simplicity, or treat the string as-is depending on the variant.

**Example:**
```
Input: s = "madam"
Output: true
Explanation: "madam" reversed is "madam".

Input: s = "hello"
Output: false
Explanation: "hello" reversed is "olleh".
```

| Input     | Output | Explanation |
|-----------|--------|-------------|
| "madam"   | true   | Reads same forwards and backwards |
| "racecar" | true   | Palindrome |
| "hello"   | false  | "h" != "o" on first check |
| "a"       | true   | Single character is trivially a palindrome |
| ""        | true   | Empty string is a palindrome |
| "ab"      | false  | "a" != "b" |

### Real-Life Analogy
Think of a **mirror placed at the center** of a word. A palindrome is a word where the left half perfectly reflects the right half. Checking recursively is like having two people start at opposite ends of the word, each walking toward the center one step at a time. At each step they compare the letters they are standing on. If every pair matches, the word is a palindrome. Recursion naturally models this "shrink the problem by one step from each end" approach.

### Key Observations
1. A string is a palindrome if the first and last characters match AND the substring between them is also a palindrome. This is a textbook recursive definition.
2. Base cases: empty string or single character is always a palindrome.
3. **Aha moment:** The recursive structure is `isPalindrome(s) = (s[0] == s[n-1]) AND isPalindrome(s[1..n-2])`. Each call shrinks the problem by 2 characters. This is identical to the two-pointer approach but expressed recursively -- it teaches you how iteration and recursion are two sides of the same coin.

### Constraints
- 0 <= s.length <= 10^4
- s consists of printable ASCII characters

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Recursion?
This problem specifically asks for a recursive solution. The recursive definition of a palindrome directly maps to a recursive function: compare endpoints, recurse on the inner substring.

### Pattern Recognition
**Classification cue:** "Check a property that can be broken into the same property on a smaller input" --> recursion. The palindrome check is a canonical example of the "shrink from both ends" recursive pattern, which also appears in string reversal, binary search, and merge sort.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Reverse and Compare
**Intuition:** Reverse the string and check if it equals the original. While not recursive, this is the simplest check.

**Steps:**
1. Reverse string s to get r.
2. Return `s == r`.

**Dry Run Trace (s = "madam"):**

| Step | Action | Result |
|------|--------|--------|
| 1 | Reverse "madam" | "madam" |
| 2 | "madam" == "madam" | true |

| Metric | Value |
|--------|-------|
| Time   | O(N) |
| Space  | O(N) for the reversed string |

**BUD Transition:** This uses O(N) extra space for the reversed copy. Can we check in-place? Yes -- two pointers or recursion.

---

### Approach 2: Optimal -- Recursive Two-Pointer
**Intuition:** Compare characters at positions `left` and `right`. If they match, recurse with `left+1, right-1`. Base case: when `left >= right`, return true.

**Steps:**
1. Define `check(s, left, right)`.
2. If `left >= right`, return true (base case).
3. If `s[left] != s[right]`, return false.
4. Return `check(s, left+1, right-1)`.

**Dry Run Trace (s = "racecar"):**

| Call | left | right | s[left] | s[right] | Match? | Action |
|------|------|-------|---------|----------|--------|--------|
| 1 | 0 | 6 | 'r' | 'r' | Yes | Recurse |
| 2 | 1 | 5 | 'a' | 'a' | Yes | Recurse |
| 3 | 2 | 4 | 'c' | 'c' | Yes | Recurse |
| 4 | 3 | 3 | 'e' | 'e' | left >= right (base case) | Return true |

Unwind: true -> true -> true -> true.

| Metric | Value |
|--------|-------|
| Time   | O(N) -- N/2 comparisons |
| Space  | O(N) recursion stack depth |

---

### Approach 3: Best -- Iterative Two-Pointer (Bonus: O(1) Space)
**Intuition:** Same logic as Approach 2 but with a while loop instead of recursion. Two pointers start at opposite ends and move inward. This avoids recursion stack overhead.

**Steps:**
1. Set `left = 0`, `right = len(s) - 1`.
2. While `left < right`:
   - If `s[left] != s[right]`, return false.
   - Increment left, decrement right.
3. Return true.

| Metric | Value |
|--------|-------|
| Time   | O(N) |
| Space  | O(1) -- no stack, no extra string |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N)?** A palindrome check must examine at least every character once (consider "aaa...ab...aaa" where only the middle differs). We compare N/2 pairs, which is O(N). The reverse-and-compare approach also scans the full string. The space trade-off: reversing uses O(N), recursion uses O(N) stack, and the iterative two-pointer uses O(1).

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Stack overflow on long strings | Recursion depth = N/2, default stack may not be enough | Increase stack or use iteration for production |
| Off-by-one on right index | Using `len(s)` instead of `len(s) - 1` | Right starts at last index |
| Forgetting base case | Infinite recursion | Return true when `left >= right` |
| Case sensitivity | "Madam" fails if case-sensitive | Lowercase both ends, or clarify requirements |

### Edge Cases Checklist
- Empty string "" --> true
- Single character "a" --> true
- Two same characters "aa" --> true
- Two different characters "ab" --> false
- Even-length palindrome "abba" --> true
- All same characters "aaaa" --> true

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Is the check case-sensitive? Spaces matter? Just letters?"
2. **M**atch: "Recursive shrink-from-both-ends pattern."
3. **P**lan: "Compare s[left] and s[right], recurse on inner substring."
4. **I**mplement: Write recursive function with two index parameters.
5. **R**eview: Trace "racecar" and "hello".
6. **E**valuate: "O(N) time, O(N) stack space. Can optimize to O(1) space with iteration."

### Follow-Up Questions
- "What if we ignore non-alphanumeric characters?" --> Skip non-alnum in the pointer movement (LeetCode 125).
- "Check if a number is a palindrome?" --> Convert to string, or reverse the number mathematically.
- "Find longest palindromic substring?" --> Expand-around-center O(N^2) or Manacher's O(N).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic recursion (base case + recursive call) |
| **Same pattern** | Reverse a string recursively, binary search recursively |
| **Harder variant** | Valid Palindrome II (allow one deletion), Longest Palindromic Substring |
| **Unlocks** | Palindrome partitioning, Manacher's algorithm |
