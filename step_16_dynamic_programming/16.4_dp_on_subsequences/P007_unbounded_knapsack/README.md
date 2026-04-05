# Unbounded Knapsack

> **Step 16.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given `n` items, each with a weight `weights[i]` and a value `values[i]`, and a knapsack with capacity `W`, find the maximum total value you can achieve. Unlike 0/1 knapsack, **each item can be picked any number of times** (unbounded).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| wt=[2,4,6], val=[5,11,13], W=10 | 27 | Take item 0 (wt=2,val=5) five times → 25? No. Take item 1 twice (wt=4,val=11) → 22? Take item 0 once + item 1 twice = 2+8=10, val=5+22=27 |
| wt=[1,3,4,5], val=[1,4,5,7], W=8 | 11 | Take item 1 (wt=3,val=4) + item 3 (wt=5,val=7) = 11 |

### Constraints
- `1 <= n <= 1000`
- `1 <= weights[i], values[i] <= 1000`
- `1 <= W <= 1000`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** At each item index, either skip it (move to idx-1) or take one unit of it and **stay at the same index** (allowing unlimited future picks of the same item).

**Steps:**
1. `solve(idx, cap)`: if idx == 0, take as many of item 0 as fit: `(cap / wt[0]) * val[0]`
2. `not_take = solve(idx-1, cap)` — skip current item
3. `take = val[idx] + solve(idx, cap - wt[idx])` if it fits — **stay at idx**
4. Return `max(not_take, take)`

```
Dry-run: wt=[2,4,6], val=[5,11,13], W=10
solve(2,10): max(solve(1,10), 13+solve(2,4))
  solve(1,10): max(solve(0,10)=25, 11+solve(1,6))
    solve(1,6): max(solve(0,6)=15, 11+solve(1,2))
      solve(1,2): max(solve(0,2)=5, can't take wt=4) = 5
    solve(1,6) = max(15, 11+5=16) = 16
  solve(1,10) = max(25, 11+16=27) = 27
  solve(2,4): max(solve(1,4), can't take wt=6) = solve(1,4)
    solve(1,4) = max(solve(0,4)=10, 11+solve(1,0)=11) = 11
  solve(2,4) = 11
solve(2,10) = max(27, 13+11=24) = 27
```

| Metric | Value |
|--------|-------|
| Time   | O(n^W) -- exponential without memoization |
| Space  | O(W) -- recursion depth |

---

### Approach 2: Optimal -- 2D DP Tabulation

**Intuition:** Build a 2D table `dp[i][w]` = max value using items 0..i with capacity w. The key difference from 0/1 knapsack: when taking item i, look at `dp[i][w - wt[i]]` (same row) instead of `dp[i-1][w - wt[i]]`.

**Steps:**
1. Base: `dp[0][w] = (w / wt[0]) * val[0]` for all w
2. For each i from 1 to n-1, for each w:
   - `not_take = dp[i-1][w]`
   - `take = val[i] + dp[i][w - wt[i]]` if `wt[i] <= w`
   - `dp[i][w] = max(not_take, take)`
3. Return `dp[n-1][W]`

**Why `dp[i]` not `dp[i-1]` for take?** Referencing the same row means item i is still available after taking it once — enabling unlimited reuse.

| Metric | Value |
|--------|-------|
| Time   | O(n * W) |
| Space  | O(n * W) |

---

### Approach 3: Best -- Space-Optimized 1D DP

**Intuition:** Compress to 1D. Unlike 0/1 knapsack (right-to-left), iterate **left to right** so that `dp[w - wt[i]]` is already updated for item i — reflecting that item i can be used again.

**Steps:**
1. Base: `dp[w] = (w / wt[0]) * val[0]`
2. For each item i from 1 to n-1:
   - For w from `wt[i]` to W (left to right):
     - `dp[w] = max(dp[w], val[i] + dp[w - wt[i]])`
3. Return `dp[W]`

**The key insight:** Left-to-right in unbounded vs right-to-left in 0/1 knapsack is the single critical difference between the two variants.

| Metric | Value |
|--------|-------|
| Time   | O(n * W) |
| Space  | O(W) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| W = 0 | Return 0 | No capacity |
| Single item | Take floor(W / wt[0]) copies | Integer division |
| All items too heavy | Return 0 | No valid picks |

**Common Mistakes:**
- Using right-to-left iteration (gives 0/1 knapsack behavior instead)
- For take transition, using `dp[i-1]` instead of `dp[i]` in 2D DP
- Confusing unbounded with 0/1 — the only code difference is loop direction or row reference

---

## Real-World Use Case
**Manufacturing optimization:** A factory can produce as many units of each product type as it wants, each consuming machine-hours (weight) and generating profit (value). Given a fixed production capacity, find the optimal mix — this is exactly unbounded knapsack.

## Interview Tips
- Know the **single line difference** from 0/1 knapsack: left-to-right vs right-to-left
- In 2D DP, `dp[i][w - wt[i]]` (same row) enables reuse; `dp[i-1][w - wt[i]]` prevents it
- Rod cutting and coin change (count/min) are direct applications of this pattern
- Always verify with a simple example: if you can pick item 0 (wt=1) 5 times to fill W=5, the answer should reflect that
