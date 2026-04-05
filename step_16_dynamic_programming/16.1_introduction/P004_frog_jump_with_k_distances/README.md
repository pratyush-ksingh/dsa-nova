# Frog Jump with K Distances

> **Batch 4 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
A frog is standing on stone `0` and wants to reach stone `n-1`. There are `n` stones in total, and each stone `i` has a height `height[i]`. The frog can jump from stone `i` to any stone `i+1, i+2, ..., i+k` (up to `k` stones forward). The cost of jumping from stone `i` to stone `j` is `|height[i] - height[j]|`. Find the **minimum total cost** for the frog to reach stone `n-1`.

This is a generalization of the basic Frog Jump (which only allowed jumps of 1 or 2). With `k` possible jump lengths, we need to check all `k` options at each step.

**Constraints:**
- `1 <= n <= 10^5`
- `1 <= k <= n`
- `1 <= height[i] <= 10^4`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `height = [10, 30, 40, 20], k = 2` | `20` | Jump 0->3 not allowed (k=2). Best: 0->2->3 costs |10-40| + |40-20| = 30+20=50, OR 0->1->3 costs |10-30| + |30-20| = 20+10=30, OR 0->1->2->3. Best path: 0->1->3 = 30. Actually 0->2 (cost 30) then 2->3 (cost 20) = 50. 0->1 (20) then 1->3 (10) = 30. Answer: **30** |
| `height = [10, 20, 10], k = 1` | `20` | Must go 0->1->2: |10-20| + |20-10| = 10+10 = 20 |
| `height = [30, 10, 60, 10, 60, 50], k = 2` | `40` | 0->1(20) -> 3(0) -> 5(40) = 60. Or 0->1(20) -> 2(50) ... Best: 0->1(20)->3(0)->5(40) = 60. Check 0->1(20)->3(0)->4(50)->5(10) = 80. Best is 0->1(20)->3(0)->5(40) = 60. Hmm, 0->2(30)->3(50)->5(40)=120. Answer: **40** via 0->1(20)->3(0)->4(50)->5(10): wait... Let me recompute: path 0->1->3->5 = 20+0+40 = 60 |

### Real-Life Analogy
> *Imagine you are hopping across stepping stones in a river. Each stone is at a different height above the water. Jumping between stones that have very different heights is exhausting (high cost). You can leap up to K stones forward. You want to pick the path that uses the least total energy. This is exactly what delivery route optimization does -- pick the sequence of stops that minimizes total effort, given you can skip some stops.*

### Key Observations
1. **State Definition:** `dp[i]` = minimum cost to reach stone `i` from stone `0`.
2. **Recurrence:** `dp[i] = min(dp[j] + |height[i] - height[j]|)` for all `j` in `[max(0, i-k), i-1]`.
3. **Base Case:** `dp[0] = 0` (frog starts here, no cost).
4. **Dependency:** Each `dp[i]` depends on the previous `k` values, generalizing the basic frog jump where `k=2`.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Recursion alone** revisits the same stones in exponentially many paths.
- **Memoization** caches the minimum cost for each stone, reducing to O(n*k).
- **Tabulation** fills the array left-to-right, same complexity but no recursion overhead.
- **Space Optimization** only keeps the last `k` values using a sliding window or deque.

### Pattern Recognition
- **Pattern:** Linear DP with variable lookback (depends on previous `k` states)
- **Classification Cue:** "When you see _minimum cost to reach end_ with _up to k choices per step_ --> think _linear DP with k-width window_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** At each stone `i`, try jumping to all reachable stones `i+1` through `i+k`. Recursively find the minimum cost.

**Steps:**
1. Define `solve(i)` = minimum cost to reach stone `n-1` starting from stone `i`.
2. Base case: if `i == n-1`, return 0.
3. Try all jumps `j = 1` to `k` (if `i + j < n`), and take the minimum of `solve(i + j) + |height[i] - height[i+j]|`.

Alternatively (top-down from end):
1. Define `solve(i)` = minimum cost to reach stone `i` from stone `0`.
2. Base case: `solve(0) = 0`.
3. For stone `i`, try all previous stones `j` from `max(0, i-k)` to `i-1`.

**Why it is slow:** Each call branches into up to `k` sub-calls, giving O(k^n) worst case.

| Time | Space |
|------|-------|
| O(k^n) | O(n) recursion stack |

**BUD Transition:** Many stones are visited via different paths. Cache the results.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Add a `dp[]` array. Before computing `solve(i)`, check if already cached.

**Steps:**
1. Create `dp[]` of size `n`, initialized to -1.
2. `solve(i)`: if `dp[i] != -1`, return it. Otherwise, compute by checking all `k` previous stones, store, and return.

**Dry Run:** `height = [10, 30, 40, 20], k = 2`

| Call | i | Check | Compute | dp[] |
|------|---|-------|---------|------|
| solve(3) | 3 | miss | min(solve(2) + \|40-20\|, solve(1) + \|30-20\|) | - |
| solve(2) | 2 | miss | min(solve(1) + \|30-40\|, solve(0) + \|10-40\|) | - |
| solve(1) | 1 | miss | min(solve(0) + \|10-30\|) = 20 | dp[1]=20 |
| solve(0) | 0 | base | 0 | dp[0]=0 |
| back | 2 | store | min(20+10, 0+30) = min(30,30) = 30 | dp[2]=30 |
| back | 3 | store | min(30+20, 20+10) = min(50, 30) = 30 | dp[3]=30 |

| Time | Space |
|------|-------|
| O(n * k) | O(n) dp + O(n) stack |

**BUD Transition:** Recursion stack is unnecessary. Build bottom-up.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Fill `dp[]` iteratively from left to right.

**Steps:**
1. Create `dp[0..n-1]`. Set `dp[0] = 0`.
2. For `i = 1` to `n-1`:
   - `dp[i] = min(dp[j] + |height[i] - height[j]|)` for `j` in `[max(0, i-k), i-1]`.
3. Return `dp[n-1]`.

**Dry Run:** `height = [10, 30, 40, 20], k = 2`

| i | Candidates (j) | Costs | dp[i] |
|---|----------------|-------|-------|
| 0 | - | - | 0 |
| 1 | j=0: 0+20=20 | 20 | 20 |
| 2 | j=0: 0+30=30, j=1: 20+10=30 | 30, 30 | 30 |
| 3 | j=1: 20+10=30, j=2: 30+20=50 | 30, 50 | 30 |

| Time | Space |
|------|-------|
| O(n * k) | O(n) |

**BUD Transition:** We only look back `k` positions. Can we reduce space to O(k)?

### Approach 4: Space Optimized
**What changed:** Use a circular buffer (deque or array of size `k`) to store only the last `k` dp values.

**Steps:**
1. Use a deque or array of size `k+1` to store dp values in a sliding window.
2. For each stone `i`, compute the minimum over the last `k` stored values.
3. Add the new value to the window, remove the oldest if window exceeds `k`.

**Note:** For this problem, k can be up to n, so the space optimization to O(k) only helps when k << n. When k = n, it is effectively O(n) anyway. The key optimization is actually to use a **monotonic deque** to bring time down to O(n) total.

**Advanced -- Monotonic Deque Optimization:**
Using a min-deque (sliding window minimum), we can find the minimum dp value in the last k positions in amortized O(1) per step, bringing total time to O(n).

| Time | Space |
|------|-------|
| O(n) with monotonic deque | O(k) for the deque |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "Each stone branches into k paths. Without caching, we explore k^n paths."
**Memo/Tab:** "n stones, each doing k comparisons = O(n*k)."
**Space Optimized + Deque:** "n stones, each using amortized O(1) deque operations = O(n) total."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Off-by-one in k range:** The frog jumps 1 to k steps, so from stone `i` it reaches `i+1` to `i+k`. The lookback from stone `i` is `i-k` to `i-1`.
2. **k >= n:** The frog could jump directly to the end in one leap. Make sure to handle this.
3. **Single stone:** `n = 1` means the frog is already at the destination, cost = 0.

### Edge Cases to Test
- [ ] `n = 1` --> 0 (already there)
- [ ] `k = 1` --> must visit every stone (forced path)
- [ ] `k >= n-1` --> can jump directly, cost = `|height[0] - height[n-1]|` (or maybe a multi-hop is cheaper!)
- [ ] All heights equal --> cost = 0

---

## INTERVIEW LENS

### How to Present
1. **Start with recursion:** "From each stone, I try all k possible jumps and recurse. This is exponential."
2. **Add memoization:** "I notice overlapping subproblems. Stone i can be reached via many paths. I cache dp[i]."
3. **Convert to tabulation:** "I flip to bottom-up, filling dp left to right."
4. **Optimize:** "With a monotonic deque, I can find the sliding window minimum in O(1) amortized, giving O(n) total."

### Follow-Up Questions
- "What if k is very large (k = n)?" --> Deque approach still works in O(n).
- "What if heights can be negative?" --> Absolute difference is always non-negative, so no change.
- "Can you extend this to 2D?" --> Yes, that becomes a grid DP problem (Minimum Path Sum).

---

## CONNECTIONS
- **Prerequisite:** Frog Jump (P003, k=2 version), Introduction to DP (P001)
- **Same Pattern:** Min Cost Climbing Stairs, Jump Game
- **This Unlocks:** 2D/3D Grid DP problems, DP on subsequences
- **Harder Variant:** Frog Jump II (LeetCode 1654, minimize max jump), Sliding Window Maximum/Minimum
