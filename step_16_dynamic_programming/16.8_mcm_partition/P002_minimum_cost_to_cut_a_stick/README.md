# Minimum Cost to Cut a Stick

> **Step 16.8** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED | **LeetCode:** 1547

## Problem Statement

You have a wooden stick of length `n` units. The stick has some marks on it; these marks are given in an integer array `cuts` where `cuts[i]` denotes a position you must cut the stick.

You should make the cuts in order that minimizes the total cost. The cost of one cut is the length of the stick on which you are making the cut (not just the piece being cut off).

Return the minimum total cost of the cuts.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `n=7, cuts=[1,3,4,5]` | `16` | Cut order 3→5→1→4: costs 7+4+4+1=16. This is optimal. |
| `n=9, cuts=[5,6,1,4,2]` | `22` | Optimal ordering of cuts gives total cost 22 |
| `n=10, cuts=[2,4,7]` | `20` | Cut at 4 (cost 10), then 2 (cost 4), then 7 (cost 6) = 20 |
| `n=5, cuts=[3]` | `5` | Only one cut at position 3; costs the full length = 5 |

## Constraints

- `2 <= n <= 10^6`
- `1 <= cuts.length <= min(n - 1, 100)`
- `1 <= cuts[i] <= n - 1`
- All values in `cuts` are distinct

---

## Approach 1: Brute Force (Try all cut orderings)

**Intuition:** There are `c!` orderings of the cuts. For each ordering, simulate the process: maintain the set of segment endpoints, find which segment each cut falls in (segment length = cost), then split it. The minimum over all orderings is the answer. Correct but completely infeasible for large inputs.

**Steps:**
1. Generate every permutation of the `cuts` array.
2. For each permutation, use a sorted set of endpoints (starting with `{0, n}`).
3. For each cut position in order, find its enclosing segment (predecessor and successor in the sorted set), add segment length to cost, insert the cut.
4. Track the global minimum cost.

| Metric | Value |
|--------|-------|
| Time   | O(c! * c) — factorial |
| Space  | O(c) |

---

## Approach 2: Optimal (Top-Down Interval DP / Memoization)

**Intuition:** Create a new array `newCuts = sorted([0] + cuts + [n])`. Now think of the problem as: what is the minimum cost to make all cuts that lie strictly between `newCuts[i]` and `newCuts[j]`?

When we make the very first cut at some position `newCuts[k]` (where `i < k < j`), it costs `newCuts[j] - newCuts[i]` (the full length of the current segment). Then we recurse on the two halves independently. This is the key reversal: instead of thinking about the last cut (hard), think about the first cut in each sub-segment.

`dp(i, j) = min over k in (i+1..j-1) of: (newCuts[j] - newCuts[i]) + dp(i, k) + dp(k, j)`

**Steps:**
1. Build `newCuts = sorted([0] + cuts + [n])`, length `m = c + 2`.
2. Memoize `dp(i, j)` in a dictionary or 2-D table.
3. Base case: `j == i + 1` (no cut positions strictly between them) → return 0.
4. For each candidate first-cut `k`, compute cost and take minimum.
5. Return `dp(0, m-1)`.

| Metric | Value |
|--------|-------|
| Time   | O(c^3) — c^2 states * c split points |
| Space  | O(c^2) memo + O(c) stack |

---

## Approach 3: Best (Bottom-Up Interval DP)

**Intuition:** Same recurrence as Approach 2, but computed iteratively in order of increasing gap (`j - i`). When we compute `dp[i][j]` for gap `g`, all pairs with smaller gaps are already filled. This eliminates the recursion call stack and is the standard production-ready solution.

**Steps:**
1. Build and sort `newCuts`, size `m`.
2. Allocate `dp[m][m]` (0 by default, covers base cases automatically).
3. Outer loop: `gap` from 2 to `m-1`.
4. Inner loop: `i` from 0 to `m-1-gap`; set `j = i + gap`.
5. Initialize `dp[i][j] = INF`, then try all splits `k` in `(i+1..j-1)`.
6. Return `dp[0][m-1]`.

| Metric | Value |
|--------|-------|
| Time   | O(c^3) |
| Space  | O(c^2) — no stack |

---

## Real-World Use Case

**Lumber and steel fabrication:** When cutting a long beam/pipe into required segments using a saw, each cut costs time/energy proportional to the current length (the machine must push through the entire cross-section). Scheduling cuts in the optimal order reduces total machining time. The same model applies in paper/textile manufacturing (roll cutting) and semiconductor wafer dicing, where re-fixturing cost depends on the current piece length.

---

## Interview Tips

- The critical reframe: instead of "which cut is last?" think "which cut is first?" in any sub-segment `(newCuts[i], newCuts[j])`. The first cut always costs `newCuts[j] - newCuts[i]`.
- Adding `0` and `n` as sentinel endpoints is essential — it defines the boundary of the sub-segments cleanly.
- This is structurally identical to Matrix Chain Multiplication and Burst Balloons. Recognizing the interval DP pattern is the key.
- The "cost = length of the entire segment being cut" is what makes brute-force ordering expensive — the cost of a cut depends on what cuts have already been made around it.
- `cuts.length <= 100` is the hint that O(c^3) = O(10^6) is acceptable.
- If asked "why not greedy?" — point out that cutting at a nearby position first might save cost later but be expensive now, so local greedy choices are not globally optimal.
