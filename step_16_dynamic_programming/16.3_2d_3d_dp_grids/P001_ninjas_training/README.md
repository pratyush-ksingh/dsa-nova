# Ninja's Training

> **Batch 2 of 12** | **Topic:** Dynamic Programming | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
A ninja has `N` days of training ahead. Each day offers **3 activities** (indexed 0, 1, 2), each with certain **merit points**. The ninja **cannot perform the same activity on two consecutive days**. Find the **maximum total merit points** the ninja can earn over all N days.

You are given a 2D array `points[N][3]` where `points[i][j]` represents merit points for activity `j` on day `i`.

*(Coding Ninjas / Striver SDE Sheet)*

**Constraints:**
- `1 <= N <= 10^5`
- `1 <= points[i][j] <= 100`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[[10,40,70],[20,50,80],[30,60,90]]` | `210` | Day 0: activity 2 (70), Day 1: activity 1 (50), Day 2: activity 2 (90). Total = 210 |
| `[[10,50,1],[5,100,11]]` | `110` | Day 0: activity 0 (10), Day 1: activity 1 (100) = 110 |
| `[[18,11,28],[72,13,91],[55,12,99],[64,80,78],[22,77,44]]` | `345` | Optimal selection across 5 days |

### Real-Life Analogy
> *Imagine a martial artist training for a tournament. Each day offers three dojos: Strength, Speed, and Technique. Training at the same dojo two days in a row causes diminishing returns (the sensei kicks you out). So you must alternate strategically. You want to maximize your total skill points by the end of training camp. The key insight: your choice today constrains tomorrow's options, so you must plan backwards from the end.*

### Key Observations
1. **State needs two dimensions:** To know what choices are available on day `i`, we need to know what activity was done on day `i-1`. So our state is `(day, lastActivity)`. <-- This is the "aha" insight
2. **Recurrence:** `dp[day][last] = max over all activities j != last of (points[day][j] + dp[day-1][j])`. For the first call, `last = 3` means "no activity done yet".
3. **Handling "no constraint" for day 0:** Use `last = 3` as a sentinel meaning "any activity is valid on day 0". This avoids special-casing the first day. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Overlapping subproblems:** `dp[day][0]` and `dp[day][1]` both need `dp[day-1][2]`. States are recomputed across branches.
- **Optimal substructure:** The best total for (day, last) is built from optimal solutions for earlier days.
- This is a classic **2D DP** problem where the second dimension tracks the "last choice" constraint.

### Pattern Recognition
- **Pattern:** 2D DP with constraint on consecutive choices
- **Classification Cue:** "When you see _maximize over N stages_ with _can't repeat the same choice consecutively_ --> think _dp[stage][lastChoice]_"

---

## APPROACH LADDER

### Approach 1: Plain Recursion
**Idea:** Start from the last day. For each day, try all activities except the one done on the next day (since we recurse backwards). Take the maximum.

**State Definition:** `solve(day, last)` = max points from day 0 to `day`, given that activity `last` was done on day `day+1` (so `last` is forbidden today).

**Recurrence:**
```
solve(day, last) = max over j in {0,1,2} where j != last of (points[day][j] + solve(day-1, j))
Base case: day == 0 --> max of points[0][j] for j != last
```

**Steps:**
1. Call `solve(N-1, 3)` where 3 means "no activity done yet".
2. On day 0, pick the best activity that is not `last`.
3. On other days, try all 3 activities except `last`, recurse to `day-1` with the chosen activity as the new `last`.

| Time | Space |
|------|-------|
| O(3 * 2^N) ~ O(2^N) | O(N) recursion stack |

**BUD Transition:** Many (day, last) pairs are recomputed. Cache them.

### Approach 2: Memoization (Top-Down DP)
**What changed:** Store results in `dp[day][last]` (size `N x 4` since last ranges 0-3).

**Dry Run:** `points = [[10,40,70],[20,50,80],[30,60,90]]`

| Call | day | last | Compute | Result |
|------|-----|------|---------|--------|
| solve(2,3) | 2 | 3 | max(30+solve(1,0), 60+solve(1,1), 90+solve(1,2)) | |
| solve(1,0) | 1 | 0 | max(50+solve(0,1), 80+solve(0,2)) | |
| solve(0,1) | 0 | 1 | max(pts[0][0], pts[0][2]) = max(10,70) = 70 | 70 |
| solve(0,2) | 0 | 2 | max(pts[0][0], pts[0][1]) = max(10,40) = 40 | 40 |
| back(1,0) | | | max(50+70, 80+40) = max(120, 120) = 120 | 120 |
| solve(1,1) | 1 | 1 | max(20+solve(0,0), 80+solve(0,2)) | |
| solve(0,0) | 0 | 0 | max(pts[0][1], pts[0][2]) = max(40,70) = 70 | 70 |
| solve(0,2) | | | **Cached** = 40 | 40 |
| back(1,1) | | | max(20+70, 80+40) = max(90, 120) = 120 | 120 |
| solve(1,2) | 1 | 2 | max(20+70, 50+70) = max(90, 120) = 120 | 120 |
| back(2,3) | | | max(30+120, 60+120, 90+120) = max(150, 180, 210) | **210** |

| Time | Space |
|------|-------|
| O(N * 4 * 3) = O(N) | O(N * 4) = O(N) |

**BUD Transition:** Convert to bottom-up to eliminate recursion overhead.

### Approach 3: Tabulation (Bottom-Up DP)
**What changed:** Build `dp[day][last]` iteratively from day 0 upwards.

**Steps:**
1. Fill `dp[0][last]` for each `last` in {0,1,2,3}: max of `points[0][j]` for `j != last`.
2. For day 1 to N-1:
   - For each `last` in {0,1,2,3}:
     - `dp[day][last] = max over j != last of (points[day][j] + dp[day-1][j])`
3. Return `dp[N-1][3]`.

**Dry Run:** `points = [[10,40,70],[20,50,80],[30,60,90]]`

Day 0 base cases:
| last | Valid activities | dp[0][last] |
|------|-----------------|-------------|
| 0 | {1,2} | max(40,70) = 70 |
| 1 | {0,2} | max(10,70) = 70 |
| 2 | {0,1} | max(10,40) = 40 |
| 3 | {0,1,2} | max(10,40,70) = 70 |

Day 1:
| last | Best j != last | dp[1][last] |
|------|----------------|-------------|
| 0 | max(50+70, 80+40) = 120 | 120 |
| 1 | max(20+70, 80+40) = 120 | 120 |
| 2 | max(20+70, 50+70) = 120 | 120 |
| 3 | max(20+70, 50+70, 80+40) = 120 | 120 |

Day 2: dp[2][3] = max(30+120, 60+120, 90+120) = **210**

| Time | Space |
|------|-------|
| O(N * 4 * 3) = O(N) | O(N * 4) = O(N) |

**BUD Transition:** Each day only depends on the previous day. Keep one row.

### Approach 4: Space Optimized
**What changed:** Only store `prev[4]` (previous day's dp values) instead of full 2D table.

**Steps:**
1. Compute `prev[0..3]` for day 0.
2. For each subsequent day, compute `curr[0..3]` using `prev[]`, then set `prev = curr`.
3. Return `prev[3]`.

| Time | Space |
|------|-------|
| O(N * 4 * 3) = O(N) | **O(1)** -- just 4 values |

---

## COMPLEXITY -- INTUITIVELY
**Recursion:** "At each of N days we try up to 3 activities, and each branches further -- exponential blowup."
**Memo/Tab:** "There are N * 4 = 4N states, each computed in O(3) work. Total O(12N) = O(N)."
**Space Optimized:** "Same O(N) time. Only the previous day's 4 values are needed, so O(1) extra space."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Forgetting the `last = 3` sentinel** -- without it, you need special logic for the first call where no activity is restricted.
2. **Using dp[day][activity] instead of dp[day][last]** -- the state must encode the *constraint* (what was last done), not what you are about to do.
3. **Off-by-one in tabulation** -- day 0 is the base case, iteration starts from day 1.

### Edge Cases to Test
- [ ] Single day `[[10,20,30]]` --> 30 (pick max)
- [ ] Two days, forced alternation `[[100,1,1],[1,1,100]]` --> 200
- [ ] All same points `[[5,5,5],[5,5,5]]` --> 10
- [ ] Large N (10^5 days) -- must be O(N), no recursion stack overflow

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "3 activities, can't repeat same on consecutive days, maximize total points?"
2. **State:** "I need `(day, lastActivity)` because today's options depend on yesterday's choice."
3. **Recurrence:** Write `dp[day][last] = max(points[day][j] + dp[day-1][j]) for j != last`.
4. **Progression:** Recursion -> Memo -> Tab -> Space. Code the space-optimized version.

### Follow-Up Questions
- "What if there are K activities instead of 3?" --> Same approach, dp[day][last] with K+1 values for last. Time O(N*K^2). Can optimize to O(N*K) with prefix/suffix max.
- "What if you can skip a day entirely?" --> Add a "no activity" option (activity 3) with 0 points.
- "What if certain activities are unavailable on certain days?" --> Check availability before including in the max.

---

## CONNECTIONS
- **Prerequisite:** Climbing Stairs, Max Sum Non-Adjacent (1D DP pick/skip pattern)
- **Same Pattern:** Paint House (LeetCode #256), Paint Fence -- constraint on consecutive choices
- **This Unlocks:** Grid DP (Unique Paths, Min Path Sum), 3D DP (Cherry Pickup)
- **Harder Variant:** N activities with O(N*K) optimization using prefix max
