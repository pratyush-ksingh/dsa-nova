# Frog Jump

> **Batch 1 of 12** | **Topic:** Dynamic Programming | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
A frog is at stair 0 and wants to reach stair `N-1`. The frog can jump **1 stair** or **2 stairs** at a time. Each stair has a height given by `height[i]`. The energy cost of a jump from stair `i` to stair `j` is `|height[i] - height[j]|`. Find the **minimum total energy** to reach stair `N-1`.

*(Striver's DP Series)*

**Constraints:**
- `1 <= N <= 10^5`
- `0 <= height[i] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[10, 20, 30, 10]` | `20` | Path 0->1->3: cost = \|10-20\| + \|20-10\| = 10 + 10 = 20 |
| `[10, 50, 10]` | `0` | Path 0->2: cost = \|10-10\| = 0 |
| `[20, 30, 40, 20]` | `20` | Path 0->1->3: cost = \|20-30\| + \|30-20\| = 10 + 10 = 20 |

### Real-Life Analogy
> *A frog sits at the start of a row of stepping stones in a stream. Each stone is at a different height above the water. The frog can jump to the next stone or skip one stone. Longer jumps tire the frog more based on the height difference. The frog wants to reach the last stone while minimizing total effort. At each stone, the frog makes a local choice: jump 1 or jump 2? But the optimal global strategy requires considering all possibilities -- classic DP.*

### Key Observations
1. **Recurrence:** `minCost(i) = min(minCost(i-1) + |h[i]-h[i-1]|, minCost(i-2) + |h[i]-h[i-2]|)`.
2. This is like Climbing Stairs but with **costs** instead of counting ways.
3. Each state only depends on the previous two states -- same 4-step DP progression applies. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** `minCost(4)` depends on `minCost(3)` and `minCost(2)`. `minCost(3)` also depends on `minCost(2)`. Same subproblems reappear.
- **Optimal substructure:** The optimal path to stair `i` is built from optimal paths to stairs `i-1` and `i-2`.
- The Fibonacci-like dependency (two previous states) makes this a direct extension of Climbing Stairs.

### Pattern Recognition
- **Pattern:** Linear 1D DP with optimization (min/max instead of count)
- **Classification Cue:** "When you see _minimum cost to reach position N_ with _1 or 2 step jumps_ --> think _1D DP with min of two transitions_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** From stair `n-1`, recursively try coming from `n-2` (1-jump) and `n-3` (2-jump). Take the minimum.

**Steps:**
1. `solve(i)`: min cost to reach stair `i`.
2. Base case: `solve(0) = 0` (already at start).
3. `solve(i) = min(solve(i-1) + |h[i]-h[i-1]|, solve(i-2) + |h[i]-h[i-2]|)` (for `i >= 2`).
4. For `i == 1`: `solve(1) = |h[1]-h[0]|` (can only come from stair 0).

| Time | Space |
|------|-------|
| O(2^n) | O(n) recursion stack |

**BUD Transition:** Exponential recomputation. Add memoization.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Cache `solve(i)` in a `dp[]` array.

**Dry Run:** `height = [10, 20, 30, 10]`

| Call | i | Cached? | Compute | dp[] |
|------|---|---------|---------|------|
| 1 | 3 | No | min(solve(2)+|10-30|, solve(1)+|10-20|) | |
| 2 | 2 | No | min(solve(1)+|30-20|, solve(0)+|30-10|) | |
| 3 | 1 | No | solve(0)+|20-10| = 0+10 = 10 | dp[1]=10 |
| 4 | 0 | Base | 0 | dp[0]=0 |
| back | 2 | Store | min(10+10, 0+20) = min(20,20) = 20 | dp[2]=20 |
| back | 3 | Store | min(20+20, 10+10) = min(40,20) = 20 | dp[3]=20 |

**Result:** 20

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Eliminate recursion with bottom-up.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Fill dp iteratively from stair 0 upward.

**Steps:**
1. `dp[0] = 0`.
2. `dp[1] = |h[1] - h[0]|`.
3. For `i = 2` to `n-1`:
   - `oneJump = dp[i-1] + |h[i] - h[i-1]|`
   - `twoJump = dp[i-2] + |h[i] - h[i-2]|`
   - `dp[i] = min(oneJump, twoJump)`
4. Return `dp[n-1]`.

**Dry Run:** `height = [10, 20, 30, 10]`

| i | h[i] | dp[i-1]+|h[i]-h[i-1]| | dp[i-2]+|h[i]-h[i-2]| | dp[i] |
|---|-------|----------------------|----------------------|-------|
| 0 | 10 | - | - | 0 |
| 1 | 20 | 0+10=10 | - | 10 |
| 2 | 30 | 10+10=20 | 0+20=20 | 20 |
| 3 | 10 | 20+20=40 | 10+10=20 | 20 |

| Time | Space |
|------|-------|
| O(n) | O(n) |

**BUD Transition:** Only need `dp[i-1]` and `dp[i-2]`. Two variables suffice.

### Approach 4: Space Optimized
**What changed:** Replace dp array with `prev2` and `prev1`.

**Steps:**
1. `prev2 = 0` (cost to reach stair 0).
2. `prev1 = |h[1] - h[0]|` (cost to reach stair 1).
3. For `i = 2` to `n-1`:
   - `curr = min(prev1 + |h[i]-h[i-1]|, prev2 + |h[i]-h[i-2]|)`
   - `prev2 = prev1`
   - `prev1 = curr`
4. Return `prev1`.

| Time | Space |
|------|-------|
| O(n) | **O(1)** |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each stair branches into 2 choices, giving O(2^n) total calls."
**Memo/Tab/Space:** "We compute each of n states once, doing O(1) work per state. Total O(n). Space ranges from O(n) to O(1) depending on approach."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting that `i == 1` can only jump from `i == 0` (no 2-jump option for the first stair).
2. Using `height[i] - height[j]` without absolute value -- costs must be non-negative.
3. Returning `dp[n]` instead of `dp[n-1]` -- there are `n` stairs indexed 0 to `n-1`.

### Edge Cases to Test
- [ ] `n = 1` --> 0 (already at destination)
- [ ] `n = 2` --> `|h[1] - h[0]|` (only one jump possible)
- [ ] All same heights --> 0 (all jumps are free)
- [ ] Strictly increasing heights --> 2-jumps might be cheaper per jump but cover more distance

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Frog starts at stair 0 and wants to reach stair n-1? Costs are absolute height differences? Can only jump 1 or 2 stairs?"
2. **Recurrence:** "At stair i, I could have come from i-1 or i-2. I take the minimum of both."
3. **Progression:** Recursion -> Memo -> Tab -> Space, just like Fibonacci/Climbing Stairs.
4. **Code:** Write space-optimized version. Mention the recursion tree for intuition.

### Follow-Up Questions
- "What if the frog can jump up to K stairs?" --> `dp[i] = min(dp[i-j] + |h[i]-h[i-j]|)` for `j = 1..K`. O(nK) time.
- "What is the actual path, not just the cost?" --> Track the choice at each step (came from i-1 or i-2) and backtrack.

---

## CONNECTIONS
- **Prerequisite:** Climbing Stairs (P002), Introduction to DP (P001)
- **Same Pattern:** Min Cost Climbing Stairs (LeetCode #746)
- **This Unlocks:** Frog Jump with K distances, all minimization 1D DP problems
- **Harder Variant:** Frog Jump K steps (O(nK)), Min Cost Path in grid (2D DP)
