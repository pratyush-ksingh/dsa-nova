# Longest Increasing Subsequence -- Binary Search

> **Batch 4 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an integer array `nums`, find the length of the **longest strictly increasing subsequence** (LIS). This problem focuses on the **O(n log n)** approach using patience sorting / tails array with binary search.

While the classic DP approach solves LIS in O(n^2), this approach maintains a "tails" array where `tails[i]` is the smallest possible tail element for an increasing subsequence of length `i+1`. Binary search on this array gives O(n log n).

**Constraints:**
- `1 <= nums.length <= 2500`
- `-10^4 <= nums[i] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[10, 9, 2, 5, 3, 7, 101, 18]` | `4` | LIS = [2, 3, 7, 101] or [2, 5, 7, 101] or [2, 3, 7, 18] |
| `[0, 1, 0, 3, 2, 3]` | `4` | LIS = [0, 1, 2, 3] |
| `[7, 7, 7, 7, 7]` | `1` | All same, strictly increasing means length 1 |
| `[1, 2, 3, 4, 5]` | `5` | Already sorted |
| `[5, 4, 3, 2, 1]` | `1` | Decreasing, any single element |

### Real-Life Analogy
> *Imagine you are sorting a hand of cards using "patience sorting" (like in the card game Solitaire). You place each card on the leftmost pile whose top card is >= the current card, or start a new pile if no such pile exists. The number of piles at the end equals the LIS length! The top cards of the piles form the "tails" array -- each pile's top is the smallest possible ending for a subsequence of that length.*

### Key Observations
1. **Tails Array Invariant:** `tails[i]` = smallest possible tail element for an increasing subsequence of length `i+1`. The tails array is always sorted.
2. **Binary Search:** For each new element, binary search for its position in tails. If it is larger than all tails, append (extend LIS). Otherwise, replace the first tail >= it (keep the door open for longer subsequences).
3. **The tails array is NOT the actual LIS** -- it is a tool for computing the length. The actual LIS requires backtracking.
4. **Why replacing works:** By putting a smaller value at position `i`, we are not changing the count of subsequences of length `i+1` that exist, but we are making it easier for future elements to extend beyond length `i+1`.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Classic O(n^2) DP:** `dp[i] = max(dp[j] + 1)` for all `j < i` where `nums[j] < nums[i]`. Too slow for large inputs.
- **Tails + Binary Search:** Maintains a sorted array of "best tails". Binary search finds the right position in O(log n), giving O(n log n) total.

### Pattern Recognition
- **Pattern:** Greedy + Binary Search on a maintained sorted structure
- **Classification Cue:** "When you see _longest increasing subsequence_ and need _better than O(n^2)_ --> think _tails array + binary search (patience sorting)_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion (Generate All Subsequences)
**Idea:** For each element, decide to include or exclude it. If including, it must be greater than the previous included element.

**Steps:**
1. `solve(idx, prevIdx)`: at index `idx`, with last included element at `prevIdx`.
2. **Not take:** `solve(idx + 1, prevIdx)`.
3. **Take:** if `nums[idx] > nums[prevIdx]` (or `prevIdx == -1`), then `1 + solve(idx + 1, idx)`.
4. Return max of take and not-take.

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Many `(idx, prevIdx)` pairs repeat. Cache them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `(idx, prevIdx)`. Since `prevIdx` can be -1, we shift: use `prevIdx + 1` as index.

**Steps:**
1. `dp[idx][prevIdx+1]` = LIS length from index `idx` with `prevIdx` as last taken.
2. Same take/not-take logic with caching.

**Dry Run:** `nums = [10, 9, 2, 5, 3, 7, 101, 18]`

The recursion explores take/not-take at each index. With memoization, each `(idx, prevIdx)` pair is computed once. There are `n * (n+1)` states.

| Time | Space |
|------|-------|
| O(n^2) | O(n^2) cache + O(n) stack |

**BUD Transition:** Can we do better than O(n^2)?

### Approach 3: Tabulation -- Classic O(n^2) DP
**What changed:** Define `dp[i]` = LIS length ending at index `i`. Fill left to right.

**Steps:**
1. `dp[i] = 1` for all `i` (each element is a subsequence of length 1).
2. For `i = 1` to `n-1`, for `j = 0` to `i-1`:
   - If `nums[j] < nums[i]`: `dp[i] = max(dp[i], dp[j] + 1)`.
3. Return `max(dp)`.

**Dry Run:** `nums = [10, 9, 2, 5, 3, 7, 101, 18]`

| i | nums[i] | Check j < i | dp[i] |
|---|---------|-------------|-------|
| 0 | 10 | - | 1 |
| 1 | 9 | 10>9 skip | 1 |
| 2 | 2 | 10>2, 9>2 skip | 1 |
| 3 | 5 | 2<5: dp=2 | 2 |
| 4 | 3 | 2<3: dp=2 | 2 |
| 5 | 7 | 2<7(2), 5<7(3), 3<7(3) | 3 |
| 6 | 101 | all<101, best: 7->3+1=4 | 4 |
| 7 | 18 | 2(2), 5(3), 3(3), 7(4) | 4 |

`max(dp) = 4`

| Time | Space |
|------|-------|
| O(n^2) | O(n) |

**BUD Transition:** The inner loop searches linearly. Can we use binary search?

### Approach 4: Tails Array + Binary Search (O(n log n))
**What changed:** Maintain a sorted "tails" array. For each element, binary search for its position.

**Steps:**
1. Initialize empty `tails` array.
2. For each `num` in `nums`:
   - Binary search for the leftmost position in `tails` where `tails[pos] >= num`.
   - If `pos == len(tails)`: append `num` (extends LIS).
   - Else: `tails[pos] = num` (replace with smaller tail).
3. Return `len(tails)`.

**Dry Run:** `nums = [10, 9, 2, 5, 3, 7, 101, 18]`

| num | Action | tails |
|-----|--------|-------|
| 10 | append | [10] |
| 9 | replace tails[0] | [9] |
| 2 | replace tails[0] | [2] |
| 5 | append | [2, 5] |
| 3 | replace tails[1] | [2, 3] |
| 7 | append | [2, 3, 7] |
| 101 | append | [2, 3, 7, 101] |
| 18 | replace tails[3] | [2, 3, 7, 18] |

`len(tails) = 4`

**Why it works:** The tails array is always sorted. Its length equals the LIS length. Replacing elements keeps smaller tails, allowing more room for future elements to extend subsequences.

| Time | Space |
|------|-------|
| **O(n log n)** | O(n) for tails array |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "2 choices per element, 2^n subsequences total."
**Memo / Classic DP:** "n elements, each checking all previous elements: O(n^2)."
**Tails + Binary Search:** "n elements, each doing O(log n) binary search: O(n log n)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Strictly increasing:** Use `<` not `<=`. `[7, 7, 7]` has LIS = 1, not 3.
2. **Binary search target:** Search for `>= num` (lower bound), not `> num`. Using `> num` would allow equal elements in the subsequence.
3. **Tails is NOT the LIS:** The tails array at the end is NOT a valid increasing subsequence. It only gives the correct length.
4. **Off-by-one in binary search:** Use `lo <= hi` with proper `mid` computation.

### Edge Cases to Test
- [ ] Single element --> 1
- [ ] Already sorted --> n
- [ ] Reverse sorted --> 1
- [ ] All elements equal --> 1
- [ ] Two elements: increasing / decreasing

---

## INTERVIEW LENS

### How to Present
1. **Start with O(n^2) DP:** "dp[i] = longest subsequence ending at i. For each i, scan all j < i."
2. **Identify bottleneck:** "The inner loop is a linear scan for the best predecessor. Can I speed this up?"
3. **Introduce tails:** "I'll maintain a sorted array of smallest tails. Binary search replaces the linear scan."
4. **Walk through example:** Show how tails evolves with [10, 9, 2, 5, 3, 7, 101, 18].

### Follow-Up Questions
- "How to reconstruct the actual LIS?" --> Maintain a `parent` array alongside tails, or use the classic O(n^2) DP with backtracking.
- "What about longest non-decreasing subsequence?" --> Use `upper_bound` (first position `> num`) instead of `lower_bound`.
- "Can you count the number of LIS?" --> Need additional tracking (segment tree or BIT).

---

## CONNECTIONS
- **Prerequisite:** Binary Search, Basic LIS DP
- **Same Pattern:** Patience Sorting, Russian Doll Envelopes
- **This Unlocks:** Largest Divisible Subset (P004), Number of LIS
- **Harder Variant:** 2D LIS (Russian Doll Envelopes), LIS with specific constraints
