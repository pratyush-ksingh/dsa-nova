# Rod Cutting Problem

> **Step 16.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given a rod of length `n` and an array `prices` where `prices[i]` is the selling price of a rod of length `i+1`, determine the maximum profit obtainable by cutting the rod into pieces (any combination of lengths) and selling them. Each piece length can be reused any number of times.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| prices=[1,5,8,9,10,17,17,20], n=8 | 22 | Two pieces of length 6 and 2 → 17+5=22 |
| prices=[3,5,8,9,10,17,17,20], n=8 | 24 | Eight pieces of length 1 → 8*3=24 |
| prices=[5], n=1 | 5 | Must sell as length-1 piece |

### Constraints
- `1 <= n <= 1000`
- `1 <= prices[i] <= 1000`
- `prices.length == n`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** This is unbounded knapsack in disguise. Piece lengths 1..n are the "item weights", prices are "values", rod length n is the "capacity". For each piece length, decide: skip it or cut one unit and stay at the same length option.

**Steps:**
1. `solve(idx, remaining)`: idx tracks which piece length we're considering (length = idx+1)
2. Base: idx == 0, use only length-1 pieces: return `remaining * prices[0]`
3. `not_cut = solve(idx-1, remaining)` — never use this piece length
4. `cut = prices[idx] + solve(idx, remaining - (idx+1))` if piece fits — stay at idx for reuse
5. Return `max(not_cut, cut)`

```
Dry-run: prices=[1,5,8,9], n=4 (pieces: L1=1, L2=5, L3=8, L4=9)
solve(3,4): max(solve(2,4), 9+solve(3,0)=9)
  solve(2,4): max(solve(1,4), 8+solve(2,1))
    solve(1,4): max(solve(0,4)=4, 5+solve(1,2))
      solve(1,2): max(2, 5+solve(1,0)=5) = 5
    solve(1,4) = max(4, 5+5=10) = 10
    solve(2,1): max(solve(1,1)=1, can't fit L3) = 1
  solve(2,4) = max(10, 8+1=9) = 10
solve(3,4) = max(10, 9) = 10  ✓ (L2+L2 = 5+5 = 10)
```

| Metric | Value |
|--------|-------|
| Time   | O(2^n) |
| Space  | O(n) |

---

### Approach 2: Optimal -- 2D DP Tabulation

**Intuition:** `dp[i][j]` = max profit for rod of length j when allowed piece lengths are 1..i+1. Fill bottom-up. The "cut" transition stays on row i to allow reusing piece length i+1.

**Steps:**
1. Base: `dp[0][j] = j * prices[0]` — only length-1 cuts available
2. For i from 1 to n-1, for j from 0 to n:
   - `not_cut = dp[i-1][j]`
   - `cut = prices[i] + dp[i][j - (i+1)]` if `(i+1) <= j` — same row = reuse
   - `dp[i][j] = max(not_cut, cut)`
3. Return `dp[n-1][n]`

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n^2) |

---

### Approach 3: Best -- 1D Space-Optimized DP

**Intuition:** Compress to 1D `dp[j]` = max profit for rod of length j. Left-to-right sweep enables reusing the same piece length (unbounded behavior). Each outer loop iteration "adds" one more piece length option.

**Steps:**
1. `dp[j] = j * prices[0]` for all j (base: only length-1 cuts)
2. For piece length `p` = 2 to n (i.e., i from 1 to n-1):
   - For j from p to n (left to right):
     - `dp[j] = max(dp[j], prices[i] + dp[j - p])`
3. Return `dp[n]`

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| n = 1 | Only one option: sell as is | `prices[0]` |
| Keeping whole rod is best | dp[n] unchanged from base | Handled naturally |
| Monotonically increasing prices | Optimal is single piece | Still computed correctly |

**Common Mistakes:**
- Off-by-one: `prices[i]` is price for piece of length `i+1`
- Using right-to-left iteration (limits each piece to one cut)
- Forgetting to initialize base case correctly when only length-1 is available

---

## Real-World Use Case
**Steel bar manufacturing:** A steel mill produces bars of standard lengths at fixed prices. Given a raw bar of length n, the optimal cutting schedule that maximizes total sale price is exactly the rod cutting problem — used in real industrial optimization systems.

## Interview Tips
- This is the canonical textbook example of unbounded knapsack; frame it that way immediately
- The key reduction: "piece lengths = weights, prices = values, n = capacity"
- Greedy by price-per-unit does NOT always work; always demonstrate with a counterexample
- Common follow-up: print the actual cuts made (track decisions in a separate array)
- Compare with coin change: same structure, different objective (maximize vs minimize)
