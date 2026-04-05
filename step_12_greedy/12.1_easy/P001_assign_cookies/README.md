# Assign Cookies

> **Batch 1 of 12** | **Topic:** Greedy | **Difficulty:** EASY | **XP:** 10

**LeetCode #455**

---

## 1. UNDERSTAND

### Problem Statement
You are a parent with `g` children and `s` cookies. Each child `i` has a greed factor `g[i]` -- the minimum cookie size that will make them content. Each cookie `j` has a size `s[j]`. A cookie `j` can satisfy child `i` only if `s[j] >= g[i]`. Each child gets at most one cookie, and each cookie goes to at most one child. **Maximize** the number of content children.

### Analogy
Imagine giving out **t-shirts** at a charity event. Each person needs at least a certain size. You sort people by required size (smallest first) and t-shirts by actual size (smallest first). Give the smallest adequate t-shirt to the least demanding person. This wastes the least "excess" and saves bigger shirts for pickier people.

### Key Observations
1. **Sorting** both arrays lets us greedily match smallest greed to smallest adequate cookie. **Aha:** If the smallest cookie cannot satisfy the least greedy child, it cannot satisfy anyone -- skip it.
2. A greedy approach works because satisfying a less greedy child with a smaller cookie never hurts -- it frees bigger cookies for greedier children. **Aha:** This is the exchange argument: swapping a big cookie from an easy child to a hard child can only help or be neutral.
3. Two pointers after sorting gives a single-pass O(n) matching. **Aha:** No need for backtracking or dynamic programming.

### Examples

| g (greed) | s (cookies) | Output | Explanation |
|-----------|-------------|--------|-------------|
| [1,2,3] | [1,1] | 1 | Only child with greed=1 gets a cookie (size 1). |
| [1,2] | [1,2,3] | 2 | Child greed=1 gets cookie 1, child greed=2 gets cookie 2. |
| [10,9,8,7] | [5,6,7,8] | 2 | After sorting: g=[7,8,9,10], s=[5,6,7,8]. 7<=7, 8<=8. |

### Constraints
- 1 <= g.length <= 3 * 10^4
- 0 <= s.length <= 3 * 10^4
- 1 <= g[i], s[j] <= 2^31 - 1

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (try all assignments) | Backtracking / bitmask | Try every permutation of cookie-child assignments. Correct but factorial time. |
| Optimal (sort + two pointers) | Sorted arrays | Greedy: match least greedy child with smallest adequate cookie. O(n log n). |
| Best (same as optimal) | Sorted arrays | Sorting is the bottleneck; the matching pass is O(n). Cannot avoid sorting. |

**Pattern cue:** "Maximize matches between two groups with constraints" -> sort both, greedy two-pointer match.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Try All Assignments)
**Intuition:** For each cookie, try giving it to each unsatisfied child. Explore all possibilities.

**Steps:**
1. For each permutation of cookie assignments to children, count satisfied children.
2. Return the maximum count.

| Metric | Value |
|--------|-------|
| Time | O(s! * g) or O(2^s * g) with bitmask |
| Space | O(s) recursion stack |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Exploring all assignments is exponential. The greedy insight is that sorting + matching smallest-to-smallest is always optimal (exchange argument), eliminating the need for backtracking.

### Approach 2 -- Optimal (Sort + Two Pointers)
**Intuition:** Sort both arrays. Use pointer `i` for children (greed) and `j` for cookies (size). If cookie j satisfies child i, match them and advance both. Otherwise, this cookie is too small for anyone remaining -- skip it.

**Steps:**
1. Sort `g` and `s` in ascending order.
2. Initialize `i = 0` (child pointer), `j = 0` (cookie pointer).
3. While `i < len(g)` and `j < len(s)`:
   - If `s[j] >= g[i]`: child i is content, increment both `i` and `j`.
   - Else: cookie j is too small, increment only `j`.
4. Return `i` (number of content children).

**Dry-Run Trace -- g=[1,2,3], s=[1,1]:**

| Step | i | j | g[i] | s[j] | s[j]>=g[i]? | Action | Content |
|------|---|---|------|------|-------------|--------|---------|
| 1 | 0 | 0 | 1 | 1 | Yes | match, i++, j++ | 1 |
| 2 | 1 | 1 | 2 | 1 | No | skip cookie, j++ | 1 |
| 3 | 1 | 2 | - | - | j out of bounds | stop | 1 |

**Dry-Run Trace -- g=[1,2], s=[1,2,3]:**

| Step | i | j | g[i] | s[j] | s[j]>=g[i]? | Action | Content |
|------|---|---|------|------|-------------|--------|---------|
| 1 | 0 | 0 | 1 | 1 | Yes | match | 1 |
| 2 | 1 | 1 | 2 | 2 | Yes | match | 2 |
| 3 | 2 | 2 | - | - | i out of bounds | stop | 2 |

| Metric | Value |
|--------|-------|
| Time | O(n log n + m log m) for sorting, then O(n + m) scan |
| Space | O(1) extra (or O(n+m) if sort is not in-place) |

### Approach 3 -- Best (Same as Optimal)
Sorting dominates at O(n log n). The two-pointer pass is already O(n + m). No further improvement possible.

| Metric | Value |
|--------|-------|
| Time | O(n log n + m log m) |
| Space | O(1) extra |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n log n):** Sorting is the most expensive step. The matching is just walking two pointers forward -- each pointer moves at most n or m times.
- **O(1) space:** We only need the two pointers. Sorting can be done in-place.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| No cookies (s is empty) | Return 0 immediately |
| No children (g is empty) | Return 0 immediately |
| All cookies too small | Every s[j] < every g[i]; j exhausts first, return 0 |
| More cookies than children | All children can potentially be satisfied |
| Single child, single cookie | Check s[0] >= g[0] |

**Common mistakes:**
- Forgetting to sort both arrays (not just one).
- Advancing the child pointer when the cookie is too small (should only advance cookie pointer).
- Trying to give multiple cookies to one child.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Prove the greedy is optimal? | Exchange argument: if solution gives big cookie to easy child and small to hard, swap them -- same or better outcome. |
| What if each child can eat multiple cookies summing to greed? | Different problem (bin packing variant); greedy may not work. |
| Can we solve without sorting? | Not efficiently. Sorting enables the two-pointer matching. |
| Time complexity lower bound? | Omega(n log n) due to sorting. The matching itself is O(n). |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| N Meetings in One Room | Greedy activity selection: sort by end time |
| Boats to Save People (LC #881) | Sort + two pointers, pairing heaviest with lightest |
| Minimum Number of Arrows (LC #452) | Sort intervals by end, greedy selection |
| Task Scheduler (LC #621) | Greedy: most frequent task first |
