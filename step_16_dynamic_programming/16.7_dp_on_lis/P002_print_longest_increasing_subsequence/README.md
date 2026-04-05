# Print Longest Increasing Subsequence

> **Batch 3 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an integer array `nums`, find and **print** the **Longest Increasing Subsequence (LIS)** -- not just its length, but the actual subsequence. A subsequence is strictly increasing and maintains relative order.

*(Coding Ninjas / GFG)*

**Constraints:**
- `1 <= nums.length <= 2500`
- `-10^4 <= nums[i] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [10,9,2,5,3,7,101,18]` | `[2,5,7,101]` or `[2,5,7,18]` or `[2,3,7,101]` | LIS length = 4, multiple valid answers |
| `nums = [0,1,0,3,2,3]` | `[0,1,2,3]` | LIS length = 4 |
| `nums = [7,7,7,7]` | `[7]` | Strictly increasing, so length = 1 |
| `nums = [5,4,3,2,1]` | `[5]` or any single | Decreasing, LIS = any single element |

### Real-Life Analogy
> *Imagine you are stacking books on a shelf where each new book must be taller than the last. You want the LONGEST such stack from a pile of books with different heights. Finding the LENGTH tells you how many books fit. PRINTING the LIS tells you exactly WHICH books to pick and in what order. To reconstruct, you remember for each book: "which book came before me in the optimal stack?" This parent-tracking is the key to printing.*

### Key Observations
1. **Classic O(n^2) DP with parent tracking:** Use `dp[i]` = length of LIS ending at index `i`. For each `i`, check all `j < i` where `nums[j] < nums[i]` and update. Track `parent[i]` = index of the previous element in the LIS ending at `i`. <-- This is the "aha" insight
2. **Backtrack from the max dp[i]:** Find the index with the maximum `dp[i]` value. Follow the `parent` chain backwards to reconstruct the sequence. <-- This is the "aha" insight
3. **O(n log n) binary search for length only:** The patience sorting approach (using binary search on a tails array) gives O(n log n) for length, but printing requires the parent-tracking O(n^2) approach or a more complex augmented version.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** LIS ending at index `i` depends on all LIS values at indices `j < i`.
- **Optimal substructure:** The LIS ending at `i` is 1 + the longest LIS ending at some `j < i` where `nums[j] < nums[i]`.
- For **printing**, we need O(n^2) tabulation with parent tracking (cannot reconstruct from the O(n log n) tails array alone without augmentation).

### Pattern Recognition
- **Pattern:** 1D DP with parent-pointer backtracking for reconstruction
- **Classification Cue:** "When you see _print the actual LIS_ --> think _dp[i] + parent[i] array, then backtrack from max_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** For each element, try including it in the LIS (if it is greater than the previous element) or excluding it. Track the previous element's index.

**State Definition:** `solve(i, prevIdx)` = length of LIS starting from index `i` with `prevIdx` as the last taken element's index.

**Recurrence:**
```
solve(i, prevIdx):
  if i == n: return 0
  // Skip nums[i]
  notTake = solve(i+1, prevIdx)
  // Take nums[i] (only if strictly increasing)
  take = 0
  if prevIdx == -1 or nums[i] > nums[prevIdx]:
    take = 1 + solve(i+1, i)
  return max(take, notTake)
```

**Steps:**
1. Call `solve(0, -1)`.
2. Cannot easily print from this recursion without extra tracking.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Many overlapping (i, prevIdx) states. Memoize.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `dp[i][prevIdx+1]` (shift prevIdx by 1 since -1 is used). Total states: n * (n+1).

**Dry Run:** `nums = [5,4,11,1,16,8]`

Key calls:
- solve(0, -1): try taking 5 or skipping
- solve(1, 0): nums[1]=4 < nums[0]=5, cannot take, skip
- solve(2, 0): nums[2]=11 > nums[0]=5, take -> 1 + solve(3, 2)
- Eventually finds LIS = [5, 11, 16] with length 3

| Time | Space |
|------|-------|
| O(n^2) | O(n^2) |

**BUD Transition:** Convert to bottom-up tabulation with parent tracking for printing.

### Approach 3: Tabulation (Bottom-Up DP) + Parent Tracking
**What changed:** Use the classic `dp[i]` = LIS length ending at `i`, plus `parent[i]` to track which element precedes `i` in its optimal LIS.

**Steps:**
1. Initialize `dp[i] = 1` for all i (each element is an LIS of length 1).
2. Initialize `parent[i] = i` for all i (each element is its own parent initially).
3. For `i = 0` to `n-1`:
   - For `j = 0` to `i-1`:
     - If `nums[j] < nums[i]` and `dp[j] + 1 > dp[i]`:
       - `dp[i] = dp[j] + 1`
       - `parent[i] = j`
4. Find `maxIdx` = index of maximum value in `dp[]`.
5. Backtrack from `maxIdx` using `parent[]` to collect the LIS.

**Dry Run:** `nums = [10, 9, 2, 5, 3, 7, 101, 18]`

| i | nums[i] | dp[i] | parent[i] | Best ending here |
|---|---------|-------|-----------|------------------|
| 0 | 10 | 1 | 0 | [10] |
| 1 | 9 | 1 | 1 | [9] |
| 2 | 2 | 1 | 2 | [2] |
| 3 | 5 | 2 | 2 | [2, 5] |
| 4 | 3 | 2 | 2 | [2, 3] |
| 5 | 7 | 3 | 3 | [2, 5, 7] |
| 6 | 101 | 4 | 5 | [2, 5, 7, 101] |
| 7 | 18 | 4 | 5 | [2, 5, 7, 18] |

Max dp = 4 at index 6 (or 7). Backtrack from index 6:
- parent[6] = 5, parent[5] = 3, parent[3] = 2, parent[2] = 2 (self)
- Collect: 101, 7, 5, 2 --> reverse --> **[2, 5, 7, 101]**

| Time | Space |
|------|-------|
| O(n^2) | O(n) |

**BUD Transition:** For length-only, O(n log n) via binary search. For printing, this is optimal.

### Approach 4: O(n log n) Binary Search (Length) + Augmented for Print
**What changed:** For **length only**, use patience sorting:
1. Maintain a `tails[]` array where `tails[i]` = smallest tail element for IS of length `i+1`.
2. For each `nums[i]`, binary search `tails` for the position to replace.
3. Length of LIS = length of `tails`.

**For printing with O(n log n):** Maintain a list of lists (or a parent array alongside tails) that tracks which elements were placed at each position. Backtrack from the last pile.

**Simpler approach for interviews:** Use the O(n^2) tabulation + parent tracking. It is cleaner to code and explain.

| Variant | Time | Space |
|---------|------|-------|
| O(n^2) with print | O(n^2) | O(n) |
| O(n log n) length only | O(n log n) | O(n) |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each element branches into take/skip, giving O(2^n)."
**Memo:** "n * (n+1) states, each O(1). Total O(n^2)."
**Tabulation with parent:** "Double loop: for each i, scan all j < i. O(n^2) time, O(n) space."
**Binary search (length):** "For each of n elements, binary search on tails of size at most n: O(n log n)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting to initialize parent[i] = i** -- causes infinite loop during backtracking when element is its own LIS.
2. **Not reversing the backtracked sequence** -- elements are collected from end to start.
3. **Confusing strictly increasing vs non-decreasing** -- LIS requires strictly increasing (`nums[j] < nums[i]`, not `<=`).
4. **Stopping backtracking incorrectly** -- stop when `parent[idx] == idx`, meaning we reached the start of the LIS.

### Edge Cases to Test
- [ ] Single element `[5]` --> `[5]`
- [ ] Already sorted `[1,2,3,4]` --> `[1,2,3,4]`
- [ ] Reverse sorted `[4,3,2,1]` --> any single element
- [ ] All same `[7,7,7]` --> `[7]` (strictly increasing)
- [ ] Two elements increasing `[1,2]` --> `[1,2]`
- [ ] Negative numbers `[-1,3,-2,5]` --> `[-1,3,5]` or `[-2,5]` etc.

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Strictly increasing? Print any one valid LIS? What about ties?"
2. **Start with O(n^2) DP:** "dp[i] = LIS length ending at i, with parent tracking for reconstruction."
3. **Code the tabulation + backtracking** -- this is what the interviewer expects for printing.
4. **Mention O(n log n):** "For length-only, I can do O(n log n) with binary search on tails array."

### Follow-Up Questions
- "Just the length?" --> Use O(n log n) binary search.
- "Number of LIS?" --> LeetCode #673, track count alongside dp.
- "Longest non-decreasing subsequence?" --> Change `<` to `<=`.
- "Longest Bitonic Subsequence?" --> Compute LIS from left and LIS from right, merge.

---

## CONNECTIONS
- **Prerequisite:** Longest Increasing Subsequence (length only, P001 in this section)
- **Same Pattern:** Parent-pointer backtracking (same idea as printing LCS, shortest path)
- **This Unlocks:** Russian Doll Envelopes, Box Stacking, Longest Bitonic Subsequence
- **Harder Variant:** Number of LIS (LeetCode #673), Longest Chain of Pairs
