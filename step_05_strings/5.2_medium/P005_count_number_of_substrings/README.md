# Count Number of Substrings with Exactly K Distinct Characters

> **Step 05.5.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given a string `s` and an integer `k`, return the **number of substrings** of `s` that contain **exactly `k` distinct characters**.

**GFG / Striver A2Z Sheet problem.**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `s="abc", k=2` | `2` | `"ab"` and `"bc"` have 2 distinct chars |
| `s="aba", k=2` | `3` | `"ab"`, `"ba"`, `"aba"` each have 2 distinct chars |
| `s="aa", k=1` | `3` | `"a"` (×2) and `"aa"` all have 1 distinct char |
| `s="aabab", k=2` | `9` | All substrings with exactly 2 distinct chars |

### Constraints
- `1 <= s.length <= 10^5`
- `1 <= k <= 26`
- `s` consists of lowercase English letters only

### Real-Life Analogy
Imagine a music playlist. Each song is a character. You want to count how many contiguous stretches of songs have exactly k distinct artists. Too few or too many artists don't count.

### 3 Key Observations
1. **"aha" -- the complement trick:** Substrings with exactly k distinct chars = substrings with at most k distinct chars minus substrings with at most k-1 distinct chars.
2. **"aha" -- sliding window for atMost:** A sliding window naturally counts substrings with at most k distinct chars: every time we extend the right pointer, the new valid substrings are `right - left + 1`.
3. **"aha" -- freq array beats HashMap:** Since input is lowercase only, a fixed int[26] with a separate `distinct` counter avoids HashMap overhead entirely.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **Two-pointer / sliding window**: the window maintains a valid range where distinct count <= k. Shrink left when violated.
- **HashMap or int[26]**: track character frequencies inside the window.

### Pattern Recognition Cue
Any time the problem says "exactly k", think `f(k) = atMost(k) - atMost(k-1)`. This transforms an "exactly" constraint into two "at most" problems, both solvable with a simple sliding window.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Check All Substrings
**Intuition:** For every possible starting index i, expand j rightward. Maintain a set of distinct characters. When `set.size() == k`, count it; when it exceeds k, stop.

**Steps:**
1. Iterate over all starting indices `i` from 0 to n-1.
2. For each `i`, maintain a `HashSet` and extend `j` from `i` to n-1.
3. Add `s[j]` to the set. If `set.size() == k`, increment count.
4. If `set.size() > k`, break (extending further only adds more distinct chars).
5. Return count.

**BUD Transition:** We check O(n²) substrings and set operations are O(1), so total O(n²). But we can do better with a two-pointer approach.

**Dry Run:** `s = "abc"`, `k = 2`
```
i=0: j=0 -> {a} size=1, j=1 -> {a,b} size=2 count=1, j=2 -> {a,b,c} break
i=1: j=1 -> {b} size=1, j=2 -> {b,c} size=2 count=2
i=2: j=2 -> {c} size=1
Result: 2
```

| Metric | Value |
|--------|-------|
| Time   | O(n²) with the early-break optimization (O(n³) in worst case without it) |
| Space  | O(k)  |

---

### Approach 2: Optimal -- atMost(k) - atMost(k-1) Sliding Window
**Intuition:** `exactly(k) = atMost(k) - atMost(k-1)`. The function `atMost(k)` counts all substrings with at most k distinct chars using a two-pointer window: every time right advances, `right - left + 1` new substrings end at `right` and are valid.

**Steps:**
1. Define `atMost(k)`: use a HashMap `freq` and two pointers `left`, `right`.
2. Advance `right`, add `s[right]` to `freq`.
3. While `freq.size() > k`: remove one occurrence of `s[left]`, advance `left`. Delete from map if frequency reaches 0.
4. Add `right - left + 1` to the running total.
5. Return `atMost(k) - atMost(k-1)`.

**BUD Transition from Brute:** Reduced from O(n²) to O(n) by reusing window state instead of rebuilding from scratch.

**Dry Run:** `atMost("aba", 2)`
```
right=0: freq={a:1} size=1, count += 1 = 1
right=1: freq={a:1,b:1} size=2, count += 2 = 3
right=2: freq={a:2,b:1} size=2, count += 3 = 6
atMost("aba",2)=6, atMost("aba",1)=3, exactly=3 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(k)  |

---

### Approach 3: Best -- Same Sliding Window with int[26] Array
**Intuition:** Identical to Optimal but replaces the HashMap with a fixed `int[26]` array and an explicit `distinct` integer counter. Eliminates hashing overhead and is O(1) space.

**Steps:**
1. Same two-pointer structure as Optimal.
2. Replace `HashMap` with `int[26] freq` and an integer `distinct`.
3. When `freq[c]` goes from 0 to 1, increment `distinct`. When it goes from 1 to 0, decrement `distinct`.
4. Shrink left when `distinct > k`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1) -- only 26 slots |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** Each character enters and leaves the window at most once. Two separate O(n) passes for atMost(k) and atMost(k-1).
- **Space O(1):** The int[26] array is constant size regardless of input length.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected | Why It Trips People |
|-----------|----------|---------------------|
| `k > len(s)` | `0` | Impossible to have more distinct chars than string length |
| `k = 1` | Count all runs of same char | People forget single-char substrings count |
| All same char `"aaaa"` | depends on k | For k=1: 10 (all substrings); for k=2: 0 |
| `k = 0` | `0` | Edge case -- no substring has 0 distinct chars |

**Common Mistakes:**
- Forgetting `atMost(k-1)` when k=0 causes k-1=-1 which must return 0.
- Counting `right - left` instead of `right - left + 1` (off-by-one).
- Using `set.size() == k` as the only check in brute force, missing the early break.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "Count substrings with exactly k distinct characters."
2. **Match:** "Exactly k suggests the complement trick: atMost(k) - atMost(k-1). Each is a sliding window problem."
3. **Plan:** Define atMost helper, call it twice.
4. **Implement:** Sliding window with HashMap, then optimize to int[26].
5. **Review:** Trace through "aba", k=2.
6. **Evaluate:** O(n) time, O(1) space.

### Follow-Up Questions
- *"What if the string contains uppercase or digits?"* -- Use a HashMap instead of int[26]; still O(n) time.
- *"What if k can be larger than 26?"* -- Return 0 immediately (impossible for lowercase-only strings).
- *"Can you solve it in one pass instead of two?"* -- Yes, but it complicates the logic; the two-pass atMost approach is preferred in interviews.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Longest Substring Without Repeating Characters (LC #3) |
| **Same Pattern** | Longest Substring with At Most K Distinct Characters (LC #340) |
| **Same Pattern** | Fruit Into Baskets (LC #904) |
| **Harder** | Subarrays with K Different Integers (LC #992) -- same atMost trick |
