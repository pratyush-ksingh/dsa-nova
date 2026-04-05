# Koko Eating Bananas

> **Batch 1 of 12** | **Topic:** Binary Search on Answers | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Koko loves to eat bananas. There are `n` piles of bananas, the `i`-th pile has `piles[i]` bananas. The guards have gone and will come back in `h` hours.

Koko can decide her bananas-per-hour eating speed of `k`. Each hour, she chooses a pile and eats `k` bananas from it. If the pile has fewer than `k` bananas, she eats all of them and does not eat any more during that hour.

Return the **minimum integer** `k` such that she can eat all the bananas within `h` hours.

**LeetCode #875**

**Constraints:**
- `1 <= piles.length <= 10^4`
- `piles.length <= h <= 10^9`
- `1 <= piles[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `piles = [3,6,7,11], h = 8` | `4` | At k=4: ceil(3/4)+ceil(6/4)+ceil(7/4)+ceil(11/4) = 1+2+2+3 = 8 <= 8 |
| `piles = [30,11,23,4,20], h = 5` | `30` | With 5 piles and 5 hours, must eat one pile per hour, so k = max(piles) = 30 |
| `piles = [30,11,23,4,20], h = 6` | `23` | At k=23: 2+1+1+1+1 = 6 <= 6 |

### Real-Life Analogy
> *You run a factory that processes orders. Each order has a different size. You have a fixed deadline (h hours) and want to find the minimum processing speed (units per hour) so that all orders finish on time. Faster machines cost more, so you want the slowest machine that still meets the deadline. You do not try every possible speed linearly -- you binary search for the sweet spot.*

### Key Observations
1. If `k = max(piles)`, Koko finishes every pile in 1 hour each, so `n` hours total. Since `h >= n`, this always works.
2. If `k` is too small, the total hours exceed `h`. If `k` is large enough, it works. There is a **threshold** -- the answer space is monotonic.
3. For a given `k`, total hours = sum of `ceil(piles[i] / k)`. This is computable in O(n).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No explicit data structure needed. The search space is the range `[1, max(piles)]`.
- We apply binary search on this implicit sorted range of possible speeds.

### Pattern Recognition
- **Pattern:** Binary Search on Answer Space
- **Classification Cue:** "Find the minimum/maximum value satisfying a monotonic feasibility condition over a range of numbers --> binary search on answers."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Linear Scan
**Idea:** Try every eating speed `k` from 1 to `max(piles)`. For each `k`, compute the total hours. Return the first `k` where total hours <= `h`.

**Steps:**
1. Compute `maxPile = max(piles)`.
2. For `k` from 1 to `maxPile`:
   - Compute `hours = sum(ceil(piles[i] / k))` for all piles.
   - If `hours <= h`, return `k`.
3. Return `maxPile` (always feasible).

**Why it works:** We check every candidate in increasing order and return the first feasible one -- guaranteed minimum.

**BUD Transition -- Bottleneck:** We try up to `max(piles)` candidates, each taking O(n). Since feasibility is monotonic (once feasible, all larger k are also feasible), binary search halves the search space each step.

| Time | Space |
|------|-------|
| O(max(piles) * n) | O(1) |

### Approach 2: Optimal -- Binary Search on Answer
**What changed:** Binary search on `k` in `[1, max(piles)]`. At each `mid`, check if Koko can finish in time. If yes, try smaller `k`. If no, try larger.

**Steps:**
1. Set `lo = 1`, `hi = max(piles)`, `ans = hi`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - Compute total hours at speed `mid`.
   - If `total_hours <= h`: `ans = mid`, `hi = mid - 1` (try smaller).
   - Else: `lo = mid + 1` (need faster).
3. Return `ans`.

**Dry Run:** `piles = [3,6,7,11], h = 8`

| Step | lo | hi | mid | hours | Condition | ans | Action |
|------|----|----|-----|-------|-----------|-----|--------|
| Init | 1  | 11 | -   | -     | -         | 11  | - |
| 1    | 1  | 11 | 6   | 1+1+2+2=6 | 6<=8 | 6   | hi=5 |
| 2    | 1  | 5  | 3   | 1+2+3+4=10 | 10>8 | 6   | lo=4 |
| 3    | 4  | 5  | 4   | 1+2+2+3=8 | 8<=8 | 4   | hi=3 |
| End  | 4  | 3  | -   | -     | lo>hi   | 4   | return 4 |

**Result:** 4

| Time | Space |
|------|-------|
| O(n * log(max(piles))) | O(1) |

### Approach 3: Best -- Binary Search + Early Termination
**What changed:** Two micro-optimizations over Approach 2:
1. Use integer arithmetic `(p + k - 1) / k` instead of `ceil(p / k)` to avoid floating point.
2. Early termination in feasibility check: if accumulated hours already exceed `h`, stop iterating through remaining piles.
3. Use `lo < hi` template (no separate `ans` variable).

**Steps:**
1. Set `lo = 1`, `hi = max(piles)`.
2. While `lo < hi`:
   - `mid = lo + (hi - lo) / 2`
   - Check if Koko can finish at speed `mid` (with early exit if hours > h).
   - If yes: `hi = mid`.
   - If no: `lo = mid + 1`.
3. Return `lo`.

**Note:** The asymptotic complexity is the same as Approach 2, but the early termination provides a significant constant-factor speedup on large inputs where infeasible speeds are quickly rejected.

| Time | Space |
|------|-------|
| O(n * log(max(piles))) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n * log(max(piles))) -- "Binary search runs log(max) iterations. Each iteration scans all n piles. For max up to 10^9, that is ~30 iterations, so ~30n total work."
**Space:** O(1) -- "Only a few integer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Integer overflow:** `sum of ceil(pile/k)` can overflow int when piles are large. Use `long` in Java.
2. **Using float division for ceil:** Precision issues with large numbers. Use `(p + k - 1) / k` instead.
3. **Wrong search range:** `lo` must start at 1 (not 0, which causes division by zero). `hi` is `max(piles)` (not sum).
4. **Off-by-one in binary search:** Make sure the template correctly converges (lo <= hi with ans, or lo < hi without).

### Edge Cases to Test
- [ ] Single pile: `piles = [1], h = 1` --> return 1
- [ ] `h == n`: must eat each pile in 1 hour --> return `max(piles)`
- [ ] Very large pile: `piles = [10^9], h = 10^9` --> return 1
- [ ] All piles equal: `piles = [5,5,5,5], h = 4` --> return 5

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the minimum eating speed k so total hours = sum(ceil(pile/k)) <= h."
2. **Match:** "Feasibility is monotonic in k: if k works, all larger k also work. This is binary search on answers."
3. **Plan:** "Binary search k in [1, max(piles)]. Check feasibility in O(n) at each step."
4. **Implement:** Code with integer ceil and early termination.
5. **Review:** Walk through dry run for piles=[3,6,7,11], h=8.
6. **Evaluate:** "O(n log(max)) time, O(1) space."

### Follow-Up Questions
- "What if Koko can eat from multiple piles in one hour?" --> Different problem; greedy or different binary search predicate.
- "What if h < n?" --> Impossible, return -1 (each pile needs at least 1 hour).
- "Can you make the feasibility check faster?" --> Not in general for arbitrary piles, but if piles are sorted you can use prefix sums.

---

## CONNECTIONS
- **Prerequisite:** Binary Search basics, understanding of search on answer space
- **Same Pattern:** Minimum Days for Bouquets (LC #1482), Capacity to Ship (LC #1011), Smallest Divisor (LC #1283)
- **Harder Variant:** Split Array Largest Sum (LC #410), Minimize Max Distance to Gas Station
- **This Unlocks:** All "minimize the maximum" / "find threshold" binary search problems
