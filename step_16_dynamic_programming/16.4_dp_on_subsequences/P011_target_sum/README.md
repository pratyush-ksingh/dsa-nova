# Target Sum

> **Step 16.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

You are given an integer array `nums` and an integer `target`. For each element you must assign either `'+'` or `'-'`. Build an expression and return the **number of different assignments** that evaluate to `target`.

**Key mathematical reduction:**
Let `P` = sum of elements assigned `'+'` and `N` = sum of elements assigned `'-'`.
- `P - N = target`
- `P + N = total`
- Adding: `2P = total + target` → `P = (total + target) / 2`

So the problem is equivalent to **counting subsets whose sum equals `(total + target) / 2`**.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums=[1,1,1,1,1], target=3` | `5` | Five ways: +1+1+1+1-1, +1+1+1-1+1, +1+1-1+1+1, +1-1+1+1+1, -1+1+1+1+1 |
| `nums=[1], target=1` | `1` | Only +1=1 works |
| `nums=[1], target=2` | `0` | Impossible — max reachable is 1 |
| `nums=[0,0,0,0,0,0,0,0,1], target=1` | `256` | Each zero doubles the count of valid assignments |

## Constraints

- `1 <= nums.length <= 20`
- `0 <= nums[i] <= 1000`
- `0 <= sum(nums) <= 1000`
- `-1000 <= target <= 1000`

---

## Approach 1: Brute Force — Plain Recursion

**Intuition:** At each position in the array we have exactly two choices: add or subtract the current number. A full binary decision tree has 2^n leaves. Count those where the running total equals `target`.

**Steps:**
1. Define `recurse(index, current_sum)`.
2. Base case: `index == n` → return `1` if `current_sum == target`, else `0`.
3. Recursive case: return `recurse(index+1, current+nums[index]) + recurse(index+1, current-nums[index])`.
4. Call `recurse(0, 0)`.

| Metric | Value |
|--------|-------|
| Time   | O(2^n) |
| Space  | O(n) — recursion depth |

---

## Approach 2: Optimal — 2D DP (Count Subsets)

**Intuition:** After reducing to "count subsets summing to `req = (total+target)/2`", apply 0/1 knapsack counting DP. `dp[i][j]` = number of ways to achieve sum `j` using the first `i` elements.

**Steps:**
1. Validate: if `(total + target)` is odd, or `|target| > total`, return 0.
2. Set `req = (total + target) / 2`.
3. Initialize `dp[0][0] = 1` (one way to make sum 0 with zero elements).
4. For each element `nums[i-1]` and each sum `j`: `dp[i][j] = dp[i-1][j] + (j >= nums[i-1] ? dp[i-1][j-nums[i-1]] : 0)`.
5. Return `dp[n][req]`.

| Metric | Value |
|--------|-------|
| Time   | O(n * S) where S = (total+target)/2 |
| Space  | O(n * S) |

---

## Approach 3: Best — 1D Space-Optimised DP

**Intuition:** Row `i` only reads from row `i-1`, so we can use a single array. Traverse `j` from `req` down to `nums[i]` (reverse order) to prevent an element from being counted twice in the same pass.

**Steps:**
1. Same validation as Approach 2.
2. Initialize `dp[0] = 1`, rest zeros.
3. For each `x` in `nums`: for `j` from `req` down to `x`: `dp[j] += dp[j - x]`.
4. Return `dp[req]`.

| Metric | Value |
|--------|-------|
| Time   | O(n * S) |
| Space  | O(S) — single array |

---

## Real-World Use Case

**Portfolio allocation signals:** Given a set of binary trading signals (each worth some point value), how many ways can you assign bullish (+) or bearish (-) sentiment to each signal and reach exactly a target composite score? This is used in systematic trading strategies to enumerate equivalent signal combinations.

Other applications:
- Text generation with polarity constraints (assign + / - sentiment to adjectives to hit a sentiment target score).
- Electrical circuit analysis: assign current direction to branches to hit a target node voltage sum.
- Game theory: count the number of move sequences that yield exactly a target score.

## Interview Tips

- **Always derive the reduction first** before coding. Stating `P = (total+target)/2` immediately tells the interviewer you understand the mathematical structure.
- Watch for the two early-exit conditions: parity check `(total+target) % 2 != 0` and range check `|target| > total`.
- Zeros in the input are a classic trap: they double the count without changing the achievable sums. The DP handles this naturally since `dp[j] += dp[j - 0]` doubles every entry.
- The 0/1 knapsack must traverse `j` in **reverse** — forwards traversal would implement an unbounded knapsack, incorrectly allowing an element to be used multiple times.
- Memoisation on `(index, current_sum)` can bring the recursive approach to O(n * totalSum) as well, but the iterative DP is cleaner for interviews.
- Follow-up: "What if elements can be negative?" Then the reduction breaks and you need to adjust the offset; track sums relative to `total` as origin.
