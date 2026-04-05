# Largest Divisible Subset

> **Batch 4 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given a set of **distinct** positive integers `nums`, find the largest subset such that every pair of elements `(a, b)` in the subset satisfies either `a % b == 0` or `b % a == 0`.

**LeetCode #368**

**Key Insight:** If we sort the array, and a subset `[a1, a2, ..., ak]` is valid (every pair divisible), then we only need to check that each consecutive pair is divisible! This is because divisibility is **transitive**: if `a | b` and `b | c`, then `a | c`. So sorting + checking consecutive elements is sufficient.

This transforms the problem into an **LIS variant** where instead of `nums[j] < nums[i]`, the condition is `nums[i] % nums[j] == 0`.

**Constraints:**
- `1 <= nums.length <= 1000`
- `1 <= nums[i] <= 2 * 10^9`
- All elements are distinct

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3]` | `[1, 2]` | {1,2}: 2%1=0. Or {1,3}: 3%1=0. Both size 2. {1,2,3}: 3%2!=0, invalid |
| `[1, 2, 4, 8]` | `[1, 2, 4, 8]` | Every pair divisible. Size 4 |
| `[3, 4, 16, 8]` | `[4, 8, 16]` | Sort: [3,4,8,16]. Subset {4,8,16}: 8%4=0, 16%8=0, 16%4=0 |
| `[1]` | `[1]` | Single element |

### Real-Life Analogy
> *Imagine a company org chart where you want to find the longest chain of managers. Person A reports to person B if B's ID is a multiple of A's ID (divisibility = "reports to" relationship). You want the longest possible reporting chain. This is exactly the longest chain in a partial order defined by divisibility.*

### Key Observations
1. **Sort first:** After sorting, we only need to check if `nums[i] % nums[j] == 0` for `j < i`.
2. **Transitivity:** If `a | b` and `b | c`, then `a | c`. So checking consecutive pairs in the sorted subset is enough to ensure all pairs are valid.
3. **LIS framework:** `dp[i]` = size of largest divisible subset ending at index `i`. `dp[i] = max(dp[j] + 1)` for all `j < i` where `nums[i] % nums[j] == 0`.
4. **Reconstruction:** Track parent pointers to reconstruct the actual subset.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- Sort + LIS-style DP with divisibility condition instead of `<` condition.
- Unlike standard LIS, we need the **actual subset**, not just the length.

### Pattern Recognition
- **Pattern:** LIS variant with custom comparison (divisibility instead of <)
- **Classification Cue:** "When you see _largest subset where every pair satisfies a transitive relation_ --> think _sort + LIS with that relation_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion (Generate All Subsets)
**Idea:** Try all 2^n subsets. For each, check if every pair is divisible.

**Steps:**
1. Generate all subsets using take/not-take.
2. For each subset, verify the divisibility condition for all pairs.
3. Track the largest valid subset.

**Why it is slow:** 2^n subsets, each with O(k^2) pair checking.

| Time | Space |
|------|-------|
| O(2^n * n^2) | O(n) recursion stack + O(n) for subset |

**BUD Transition:** Sort the array. Then use DP to avoid checking all subsets.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Sort the array first. Define `solve(idx, prevIdx)` = size of largest divisible subset starting from `idx` where the last included element is at `prevIdx`.

**Steps:**
1. Sort `nums`.
2. `solve(idx, prevIdx)`: at index `idx`, with last included element at `prevIdx`.
3. **Not take:** `solve(idx + 1, prevIdx)`.
4. **Take:** if `prevIdx == -1` or `nums[idx] % nums[prevIdx] == 0`, then `1 + solve(idx + 1, idx)`.
5. Return max.

| Time | Space |
|------|-------|
| O(n^2) | O(n^2) cache + O(n) stack |

**BUD Transition:** Flip to bottom-up.

### Approach 3: Tabulation (Bottom-Up DP -- LIS Style)
**What changed:** Classic LIS-style DP after sorting.

**Steps:**
1. Sort `nums`.
2. `dp[i] = 1` for all `i`. `parent[i] = -1` (for reconstruction).
3. For `i = 1` to `n-1`, for `j = 0` to `i-1`:
   - If `nums[i] % nums[j] == 0` and `dp[j] + 1 > dp[i]`:
     - `dp[i] = dp[j] + 1`
     - `parent[i] = j`
4. Find `maxIdx` = index with largest `dp[i]`.
5. Backtrack through `parent` to reconstruct the subset.

**Dry Run:** `nums = [3, 4, 16, 8]` -> sorted: `[3, 4, 8, 16]`

| i | nums[i] | Check j < i | dp[i] | parent[i] |
|---|---------|-------------|-------|-----------|
| 0 | 3 | - | 1 | -1 |
| 1 | 4 | 4%3!=0 | 1 | -1 |
| 2 | 8 | 8%3!=0, 8%4=0 -> dp=2 | 2 | 1 |
| 3 | 16 | 16%3!=0, 16%4=0(2), 16%8=0(3) | 3 | 2 |

max dp = 3 at index 3. Backtrack: 3->2->1 = [16, 8, 4]. Reverse: **[4, 8, 16]**

| Time | Space |
|------|-------|
| O(n^2) | O(n) for dp + parent arrays |

**BUD Transition:** This is already the standard approach. Space is O(n) which is optimal for storing the dp and parent arrays.

### Approach 4: Space Optimized (Practical Considerations)
**What changed:** Unlike standard LIS where we can use binary search for O(n log n), the divisibility condition does not maintain the sorted-tails property needed for binary search. So O(n^2) is the best known approach for this problem.

However, we can optimize constants:
1. **Early termination:** If `nums[i] / nums[j] < 1` (impossible after sorting) or `dp[j] + (n - i) <= current max` (remaining elements can't beat current best), skip.
2. **Memory:** We can reconstruct without a full parent array by using index tracking.

The "space optimized" version here keeps the same O(n) space but cleans up the implementation.

| Time | Space |
|------|-------|
| O(n^2) | O(n) |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "2^n subsets, each needing O(n^2) pair validation."
**DP:** "n elements, each checking all previous elements: O(n^2). Same as LIS."
**Why not O(n log n)?** "Binary search LIS works because the '<' relation is compatible with sorted order. Divisibility is not -- 6 divides 12 but 8 doesn't divide 12, yet 8 > 6. The tails array trick doesn't apply."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting to sort:** Divisibility isn't about position in the original array. Sorting is essential.
2. **Not reconstructing:** The problem asks for the actual subset, not just its size.
3. **Using `<` instead of `%`:** This is LIS with divisibility, not with the less-than relation.
4. **Element 1:** 1 divides everything. If present, it can always be prepended to any valid subset.

### Edge Cases to Test
- [ ] Single element --> [that element]
- [ ] All elements are powers of 2 --> entire sorted array
- [ ] No pair is divisible (e.g., primes) --> any single element
- [ ] Contains 1 --> 1 can always be included

---

## INTERVIEW LENS

### How to Present
1. **Recognize the pattern:** "After sorting, the divisibility condition is transitive, so I only need consecutive checks. This is LIS with divisibility."
2. **Write the DP:** "dp[i] = size of largest divisible subset ending at nums[i]. Check all j < i where nums[i] % nums[j] == 0."
3. **Reconstruct:** "I track parent pointers to backtrack from the maximum dp index."
4. **Complexity:** "O(n^2) time, O(n) space. This cannot be improved to O(n log n) because divisibility doesn't have the same monotonic structure as '<'."

### Follow-Up Questions
- "Why can't we use binary search like LIS?" --> The tails array works because '<' preserves order. Divisibility doesn't: [4, 6, 8] -- 8 doesn't divide 6 even though 8 > 6.
- "What if elements are not distinct?" --> Handle duplicates: a % a = 0, so duplicates can all be in the subset (but LeetCode guarantees distinct).
- "Can you extend to find ALL largest divisible subsets?" --> Yes, but need to track all parents, making it more complex.

---

## CONNECTIONS
- **Prerequisite:** Longest Increasing Subsequence, Binary Search
- **Same Pattern:** LIS, Longest Chain of Pairs
- **This Unlocks:** Russian Doll Envelopes, Box Stacking
- **Harder Variant:** Largest subset with GCD > 1, Longest divisible chain in a DAG
