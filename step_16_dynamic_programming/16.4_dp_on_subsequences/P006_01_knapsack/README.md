# 0/1 Knapsack

> **Step 16.16.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given `n` items, each with a weight `weights[i]` and a value `values[i]`, and a knapsack with capacity `W`, find the maximum total value you can put in the knapsack. Each item can be taken at most once (0/1 choice).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| wt=[1,2,4,5], val=[1,4,5,7], W=7 | 11 | Take items with wt 2,5 -> val 4+7=11 |
| wt=[3,4,5], val=[30,50,60], W=8 | 90 | Take items with wt 3,5 -> val 30+60=90 |
| wt=[1,2], val=[3,4], W=0 | 0 | No capacity, take nothing |

### Constraints
- `1 <= n <= 1000`
- `1 <= weights[i], values[i] <= 1000`
- `1 <= W <= 1000`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** For each item, we have two choices: take it (if it fits) or skip it. Try all combinations and return the maximum value achievable.

**Steps:**
1. Start from the last item, recurse backward
2. Base case: at item 0, take it if it fits, else value is 0
3. For each item: `max(not_take, take)` where take = `val[i] + solve(i-1, cap-wt[i])`
4. Return the result for `solve(n-1, W)`

```
Dry-run: wt=[1,2,4,5], val=[1,4,5,7], W=7

solve(3, 7): max(solve(2,7), 7+solve(2,2))
  solve(2,7): max(solve(1,7), 5+solve(1,3))
    solve(1,7): max(solve(0,7)=1, 4+solve(0,5)=5) = 5
    solve(1,3): max(solve(0,3)=1, 4+solve(0,1)=5) = 5
  solve(2,7) = max(5, 5+5=10) = 10
  solve(2,2): max(solve(1,2), can't take wt=4) = max(solve(1,2), -)
    solve(1,2): max(solve(0,2)=1, 4+solve(0,0)=4) = 4
  solve(2,2) = 4
solve(3,7) = max(10, 7+4=11) = 11
```

| Metric | Value |
|--------|-------|
| Time   | O(2^n) -- two choices per item |
| Space  | O(n) -- recursion depth |

---

### Approach 2: Optimal -- 2D DP Tabulation

**Intuition:** Build a 2D table `dp[i][w]` = max value using items 0 to i with capacity w. Fill bottom-up.

**Steps:**
1. Base: for item 0, `dp[0][w] = val[0]` for all `w >= wt[0]`
2. For each item i from 1 to n-1, for each capacity w:
   - `not_take = dp[i-1][w]`
   - `take = val[i] + dp[i-1][w-wt[i]]` if `wt[i] <= w`
   - `dp[i][w] = max(not_take, take)`
3. Return `dp[n-1][W]`

| Metric | Value |
|--------|-------|
| Time   | O(n * W) |
| Space  | O(n * W) |

---

### Approach 3: Best -- Space-Optimized 1D DP

**Intuition:** Each row only depends on the previous row. Use a single 1D array and iterate capacity from RIGHT to LEFT. This ensures that when computing `dp[w]`, the value `dp[w - wt[i]]` is still from the previous item's row (not yet overwritten).

**Steps:**
1. Initialize `dp[w] = val[0]` for `w >= wt[0]`
2. For each item i from 1 to n-1:
   - For w from W down to `wt[i]`:
     - `dp[w] = max(dp[w], val[i] + dp[w - wt[i]])`
3. Return `dp[W]`

**Why right-to-left?** If we go left-to-right, `dp[w - wt[i]]` might already reflect item i being taken, effectively allowing unlimited takes (unbounded knapsack). Right-to-left preserves the "previous row" semantics.

| Metric | Value |
|--------|-------|
| Time   | O(n * W) |
| Space  | O(W) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| W = 0 | Return 0 | No item fits |
| Single item | Take it if it fits | Check weight vs capacity |
| All items too heavy | Return 0 | No valid picks |
| All items fit | Sum all values | Still need to verify |

**Common Mistakes:**
- Iterating left-to-right in 1D optimization (gives unbounded knapsack behavior)
- Off-by-one in base case initialization
- Forgetting that each item can only be used once

---

## Real-World Use Case
**Budget allocation:** A company has a fixed budget (capacity) and several projects (items), each with a cost (weight) and expected ROI (value). The 0/1 knapsack finds the optimal set of projects to fund for maximum return.

## Interview Tips
- This is THE foundational DP problem -- master it thoroughly
- Explain why right-to-left iteration works for 1D optimization
- Know the difference between 0/1 (this) and unbounded knapsack (left-to-right works)
- Common follow-up: reconstruct which items were selected (backtrack through DP table)
- Mention that fractional knapsack (greedy) is a different, easier problem
