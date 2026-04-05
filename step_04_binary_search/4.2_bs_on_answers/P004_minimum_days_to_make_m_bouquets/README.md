# Minimum Days to Make M Bouquets

> **Batch 1 of 12** | **Topic:** Binary Search on Answers | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
You are given an integer array `bloomDay`, an integer `m` (number of bouquets needed), and an integer `k` (number of adjacent flowers per bouquet). You want to make `m` bouquets. To make a bouquet, you need `k` **adjacent** flowers from the garden.

The garden has `n` flowers, and the `i`-th flower will bloom on `bloomDay[i]`. A flower can only be used in one bouquet.

Return the **minimum number of days** you need to wait to make `m` bouquets. If it is impossible to make `m` bouquets, return `-1`.

**LeetCode #1482**

**Constraints:**
- `1 <= bloomDay.length <= 10^5`
- `1 <= bloomDay[i] <= 10^9`
- `1 <= m <= 10^6`
- `1 <= k <= bloomDay.length`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `bloomDay = [1,10,3,10,2], m = 3, k = 1` | `3` | After day 3: flowers at indices 0, 2, 4 have bloomed. 3 bouquets of 1 flower each. |
| `bloomDay = [1,10,3,10,2], m = 3, k = 2` | `-1` | Need 3*2 = 6 flowers but only 5 exist. Impossible. |
| `bloomDay = [7,7,7,7,12,7,7], m = 2, k = 3` | `12` | After day 7: [B,B,B,B,_,B,B]. Only 1 bouquet from [0-2]. After day 12: all bloom, get [0-2] and [4-6]. |

### Real-Life Analogy
> *You are a wedding planner who needs m identical flower arrangements, each requiring k consecutive flowers from a garden row. Each flower blooms on a specific day. You want to find the earliest date you can schedule the wedding so that enough adjacent flowers have bloomed to make all the arrangements. Rather than checking every single calendar day, you narrow down by testing the middle date -- if you can make enough bouquets, try earlier; if not, wait longer.*

### Key Observations
1. If `m * k > n`, it is impossible regardless of days -- return -1.
2. As days increase, more flowers bloom, so the number of possible bouquets is **monotonically non-decreasing**.
3. The answer lies in the range `[min(bloomDay), max(bloomDay)]`.
4. For a given day, counting bouquets is O(n): scan left to right, track consecutive bloomed flowers, and count groups of size k.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- No extra data structure needed. The search space is the range of days `[min(bloomDay), max(bloomDay)]`.
- The feasibility check is a simple linear scan counting consecutive bloomed flowers.

### Pattern Recognition
- **Pattern:** Binary Search on Answer Space
- **Classification Cue:** "Find the minimum day (value) such that a monotonic feasibility condition is met --> binary search on answers."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Try Every Day
**Idea:** Try each day from 1 to `max(bloomDay)`. For each day, count how many bouquets of k adjacent bloomed flowers exist. Return the first day with >= m bouquets.

**Steps:**
1. If `m * k > n`, return -1.
2. For `day` from 1 to `max(bloomDay)`:
   - Scan the array, counting consecutive flowers where `bloomDay[i] <= day`.
   - Every time the consecutive count reaches `k`, increment bouquet count and reset.
   - If bouquets >= m, return `day`.
3. Return -1.

**BUD Transition -- Bottleneck:** We try up to `max(bloomDay)` days, each costing O(n). Since feasibility is monotonic, binary search reduces the number of days checked to O(log(max - min)).

| Time | Space |
|------|-------|
| O(n * max(bloomDay)) | O(1) |

### Approach 2: Optimal -- Binary Search on Days
**What changed:** Binary search on the day in `[min(bloomDay), max(bloomDay)]`. At each mid-day, check if we can make m bouquets. If yes, try fewer days. If no, try more.

**Steps:**
1. If `m * k > n`, return -1.
2. Set `lo = min(bloomDay)`, `hi = max(bloomDay)`, `ans = -1`.
3. While `lo <= hi`:
   - `mid = lo + (hi - lo) / 2`
   - Count bouquets possible on day `mid`.
   - If bouquets >= m: `ans = mid`, `hi = mid - 1`.
   - Else: `lo = mid + 1`.
4. Return `ans`.

**Dry Run:** `bloomDay = [1,10,3,10,2], m = 3, k = 1`

| Step | lo | hi | mid | Bouquets | Condition | ans | Action |
|------|----|----|-----|----------|-----------|-----|--------|
| Init | 1  | 10 | -   | -        | -         | -1  | - |
| 1    | 1  | 10 | 5   | 3 (idx 0,2,4) | 3>=3 | 5   | hi=4 |
| 2    | 1  | 4  | 2   | 2 (idx 0,4) | 2<3  | 5   | lo=3 |
| 3    | 3  | 4  | 3   | 3 (idx 0,2,4) | 3>=3 | 3   | hi=2 |
| End  | 3  | 2  | -   | -        | lo>hi   | 3   | return 3 |

**Result:** 3

| Time | Space |
|------|-------|
| O(n * log(max - min)) | O(1) |

### Approach 3: Best -- Binary Search + Early Termination
**What changed:** Two optimizations:
1. Early termination: once we count `m` bouquets during the scan, return `true` immediately without scanning the rest.
2. Use `lo < hi` template (no separate `ans` variable, cleaner code).
3. Use `(long) m * k` to prevent integer overflow when checking impossibility.

**Steps:**
1. If `m * k > n`, return -1.
2. Set `lo = min(bloomDay)`, `hi = max(bloomDay)`.
3. While `lo < hi`:
   - `mid = lo + (hi - lo) / 2`
   - Check feasibility with early termination.
   - If feasible: `hi = mid`.
   - Else: `lo = mid + 1`.
4. Return `lo` (verify feasibility for safety).

| Time | Space |
|------|-------|
| O(n * log(max - min)) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n * log(max - min)) -- "Binary search does ~30 iterations for max up to 10^9. Each iteration scans n flowers. Total ~30n."
**Space:** O(1) -- "Only a few integer variables."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Integer overflow:** `m * k` can overflow int. Cast to `long` before multiplying.
2. **Forgetting the impossible case:** If `m * k > n`, must return -1 immediately.
3. **Adjacency requirement:** Flowers must be adjacent. A gap resets the consecutive count.
4. **Reusing flowers:** Once k consecutive flowers form a bouquet, they cannot be reused. Reset the counter to 0, not just decrement.

### Edge Cases to Test
- [ ] Impossible: `m * k > n` --> return -1
- [ ] Single flower, single bouquet: `[5], m=1, k=1` --> return 5
- [ ] All bloom same day: `[3,3,3], m=1, k=3` --> return 3
- [ ] Need all flowers: `[1,2,3], m=1, k=3` --> return 3

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the earliest day when I can gather m bouquets, each from k adjacent bloomed flowers."
2. **Match:** "As days increase, more flowers bloom -- monotonic. Binary search on the day."
3. **Plan:** "Binary search day in [min, max]. Feasibility check: linear scan counting consecutive bloomed flowers."
4. **Implement:** Code with overflow check and early termination.
5. **Review:** Walk through the dry run above.
6. **Evaluate:** "O(n log(max)) time, O(1) space."

### Follow-Up Questions
- "What if flowers can be non-adjacent?" --> Simple counting: sort bloomDay, answer is bloomDay[m*k - 1].
- "What if each bouquet can have different sizes?" --> More complex greedy or DP approach.
- "What if flowers regrow?" --> Different problem entirely, possibly sliding window.

---

## CONNECTIONS
- **Prerequisite:** Binary Search basics, Koko Eating Bananas (LC #875)
- **Same Pattern:** Capacity to Ship (LC #1011), Smallest Divisor (LC #1283), Split Array Largest Sum (LC #410)
- **Harder Variant:** Magnetic Force Between Two Balls (LC #1552)
- **This Unlocks:** Confidence with "binary search on answer" where the feasibility check involves adjacency/contiguity constraints
