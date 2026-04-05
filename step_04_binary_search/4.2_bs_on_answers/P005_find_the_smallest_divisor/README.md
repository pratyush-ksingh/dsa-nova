# Find the Smallest Divisor

> **Step 04 | 4.2** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** #1283 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an array of integers `nums` and an integer `threshold`, find the **smallest positive integer divisor** such that the sum of `ceil(nums[i] / divisor)` for all `i` is **less than or equal to** `threshold`.

Each result of division is rounded up to the nearest integer greater than or equal to that element's result.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums=[1,2,5,9], threshold=6` | `5` | d=5: ceil(1/5)+ceil(2/5)+ceil(5/5)+ceil(9/5) = 1+1+1+2 = 5 <= 6 |
| `nums=[44,22,33,11,1], threshold=5` | `44` | Need largest element as divisor; sum = 5 elements each ceil -> 5 |
| `nums=[1,1,1,1], threshold=4` | `1` | All 1s, any divisor gives sum=4; smallest is 1 |
| `nums=[2,3,5,7,11], threshold=11` | `3` | d=3: 1+1+2+3+4=11 <= 11 |

**Constraints:**
- `1 <= nums.length <= 5 * 10^4`
- `1 <= nums[i] <= 10^6`
- `nums.length <= threshold <= 10^6`

### Real-Life Analogy
> *You run a data center processing jobs. Each job has a certain workload (nums[i]). You set a processing batch size (divisor). With batch size d, job i takes ceil(workload/d) time slots. The total time slots used must not exceed your slot budget (threshold). You want to use the smallest batch size (most granular processing) that still fits. Binary searching on the batch size is far better than trying every batch size from 1 upward.*

### Key Observations
1. The sum function `f(d) = sum(ceil(nums[i]/d))` is **monotonically non-increasing** in `d`: larger divisor -> smaller or equal sum.
2. So there is a threshold divisor `d*` -- for all `d >= d*`, `f(d) <= threshold`; for all `d < d*`, `f(d) > threshold`.
3. We binary search for the left boundary of the "feasible zone."
4. The answer is in `[1, max(nums)]`: at `d=max(nums)`, every ceil rounds down to 1, giving sum = n <= threshold (guaranteed by constraints).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No extra data structure. We binary search an implicit integer range.
- The search space `[1, max(nums)]` can be up to 10^6. Binary search reduces this to ~20 iterations.

### Pattern Recognition
- **Pattern:** Binary Search on Answer Space (minimize threshold-satisfying value)
- **Classification Cue:** "Find smallest value in a range satisfying a monotonic condition --> binary search on answers."

---

## APPROACH LADDER

### Approach 1: Brute Force (Linear scan from 1 to max)
**Idea:** Try every divisor `d` from 1 upward. For each `d`, compute the sum of ceil divisions. Return the first `d` where sum <= threshold.

**Steps:**
1. Compute `maxVal = max(nums)`.
2. For `d` from 1 to `maxVal`:
   - `total = sum(ceil(nums[i] / d))` for all i.
   - If `total <= threshold`, return `d`.
3. Return `maxVal` (always feasible by constraint).

**Why it works:** We iterate divisors in increasing order; the first feasible one is the smallest.

**Why we move on:** For max(nums) up to 10^6, and n up to 5*10^4, this is 5*10^10 operations. Way too slow.

| Time | Space |
|------|-------|
| O(max(nums) * n) | O(1) |

---

### Approach 2: Optimal (Binary search on divisor)
**What changed:** Binary search on `d` in `[1, max(nums)]`. At each midpoint, check if the sum is feasible. If yes, try smaller d; if no, try larger.

**Steps:**
1. `lo = 1`, `hi = max(nums)`, `ans = hi`.
2. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`.
   - Compute `total = sum(ceil(nums[i] / mid))`.
   - If `total <= threshold`: `ans = mid`, `hi = mid - 1` (try smaller divisor).
   - Else: `lo = mid + 1` (need larger divisor).
3. Return `ans`.

**Dry Run:** `nums = [1, 2, 5, 9], threshold = 6`

| lo | hi | mid | sum | Condition | ans | Action |
|----|----|-----|-----|-----------|-----|--------|
| 1  | 9  | 5   | 1+1+1+2=5 | 5<=6 | 5   | hi=4 |
| 1  | 4  | 2   | 1+1+3+5=10 | 10>6 | 5  | lo=3 |
| 3  | 4  | 3   | 1+1+2+3=7 | 7>6 | 5   | lo=4 |
| 4  | 4  | 4   | 1+1+2+3=7 | 7>6 | 5   | lo=5 |
| lo>hi | - | - | - | done | 5 | return 5 |

**Result:** 5

| Time | Space |
|------|-------|
| O(n * log(max(nums))) | O(1) |

---

### Approach 3: Best (Binary search + integer ceil + early exit)
**What changed:** Two micro-optimizations:
1. **Integer ceil:** Use `(x + d - 1) / d` instead of `math.ceil(x / d)` to avoid floating point and precision issues.
2. **Early exit:** If the running total exceeds `threshold` during sum computation, stop early.
3. Use `lo < hi` template for cleaner convergence without a separate `ans` variable.

**Steps:**
1. `lo = 1`, `hi = max(nums)`.
2. While `lo < hi`:
   - `mid = lo + (hi - lo) / 2`.
   - Check `feasible(mid)`: compute sum with early exit if total > threshold.
   - If feasible: `hi = mid`.
   - Else: `lo = mid + 1`.
3. Return `lo`.

| Time | Space |
|------|-------|
| O(n * log(max(nums))) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n * log(max(nums))) -- "Binary search runs ~log(10^6) = ~20 iterations. Each iteration scans n elements."
**Space:** O(1) -- "Only a few integer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Float precision with ceil:** `math.ceil(1000000 / 3)` is fine in Python but Java's `Math.ceil(double)` can have float representation issues. Use integer formula `(x + d - 1) / d` instead.
2. **Integer overflow:** Sum can be up to n * max(nums) = 5*10^4 * 10^6 = 5*10^10. Use `long` in Java.
3. **Setting hi to sum(nums):** The valid range for d is `[1, max(nums)]`, not `[1, sum(nums)]`. `max(nums)` is sufficient and smaller.
4. **threshold < n is impossible by constraints:** The problem guarantees `threshold >= n`, so `d = max(nums)` always produces a feasible sum.

### Edge Cases to Test
- [ ] Single element: `nums=[10^6], threshold=1` --> return `10^6`
- [ ] All same elements: `nums=[5,5,5], threshold=3` --> return 5
- [ ] threshold equals n: must achieve sum = n, so divisor >= max element
- [ ] nums has 1s only: any divisor works; return 1

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "Find smallest d such that sum(ceil(a[i]/d)) <= threshold."
2. **Match:** "Sum is monotonically decreasing in d -- binary search on answers."
3. **Plan:** "Binary search d in [1, max(nums)]. Feasibility check in O(n)."
4. **Implement:** Integer ceil `(x+d-1)/d`, early exit if sum exceeds threshold.
5. **Review:** Dry run with `[1,2,5,9], threshold=6`.
6. **Evaluate:** "O(n log(max)) time, O(1) space."

### Follow-Up Questions
- "Why is hi = max(nums) and not sum(nums)?" --> At d=max(nums), every element divided gives at most 1 (or exactly 1 for max element itself), so sum = n <= threshold. Going higher is wasteful.
- "Can this be solved with a different approach?" --> Conceptually, no. Feasibility requires O(n) per check; binary search is optimal for the outer search.
- "What if threshold = 1?" --> Only valid if nums = [1]; otherwise impossible (n >= 1 elements each contribute >= 1).

---

## CONNECTIONS
- **Prerequisite:** Koko Eating Bananas (same pattern, different feasibility function)
- **Same Pattern:** Koko Eating Bananas (LC #875), Capacity to Ship (LC #1011), Min Days for Bouquets (LC #1482)
- **Harder Variant:** Split Array Largest Sum (LC #410)
- **This Unlocks:** All "minimize divisor/speed/capacity satisfying a constraint" binary search problems
