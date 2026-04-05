# Container With Most Water

> **LeetCode 11** | **Step 10 | 10.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

You are given an integer array `height` of length `n`. There are `n` vertical lines drawn such that the two endpoints of the `i`-th line are at `(i, 0)` and `(i, height[i])`.

Find two lines that, together with the x-axis, form a container such that the container holds the **most water**.

Return the **maximum amount of water** a container can store.

`area(i, j) = min(height[i], height[j]) * (j - i)` where `i < j`.

The container cannot be tilted.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,8,6,2,5,4,8,3,7]` | `49` | Lines at index 1 (h=8) and index 8 (h=7): min(8,7)*7 = 49 |
| `[1,1]` | `1` | Only pair: min(1,1)*1 = 1 |
| `[4,3,2,1,4]` | `16` | Lines at index 0 and 4: min(4,4)*4 = 16 |
| `[1,2,1]` | `2` | Lines at index 0 and 2: min(1,1)*2 = 2 |

## Constraints

- `n == height.length`
- `2 <= n <= 10^5`
- `0 <= height[i] <= 10^4`

---

## Approach 1: Brute Force — All Pairs

**Intuition:** Try every possible pair of lines `(i, j)` where `i < j`, compute the water volume, and track the maximum. Simple and correct, but O(n²) — too slow for n = 10^5.

**Steps:**
1. For each `i` from `0` to `n-1`:
2.   For each `j` from `i+1` to `n-1`:
3.     `area = min(height[i], height[j]) * (j - i)`.
4.     Update `max_water` if `area` is larger.
5. Return `max_water`.

| Metric | Value |
|--------|-------|
| Time   | O(n²) |
| Space  | O(1)  |

---

## Approach 2: Optimal — Two Pointers from Both Ends

**Intuition:** Start with the widest possible container (`left = 0`, `right = n-1`). At each step, the area is `min(h[l], h[r]) * (r - l)`. Moving inward always decreases the width. To have any chance of increasing the area, we must increase the minimum height — so always move the pointer at the **shorter line** inward.

**Greedy correctness:** When we move `left` (the shorter side), we discard all pairs `(left, j')` for `j' < right`. This is safe: `area(left, j') <= h[left] * (right - left)` since `j' - left <= right - left` and `h[left]` is the bottleneck. So none of those pairs can beat the current candidate.

**Steps:**
1. Set `left = 0`, `right = n - 1`, `max_water = 0`.
2. While `left < right`:
   - Compute `area = min(h[left], h[right]) * (right - left)`.
   - Update `max_water`.
   - If `h[left] <= h[right]`: `left += 1` (move shorter side inward).
   - Else: `right -= 1`.
3. Return `max_water`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 3: Best — Two Pointers with Skip Optimisation

**Intuition:** Extend Approach 2 with an inner skip: after deciding which pointer to move, keep advancing that pointer past all lines that are **no taller** than the boundary we just left. Those lines cannot possibly improve the area (shorter height AND narrower width). Same worst-case complexity but significantly faster in practice.

**Steps:**
1. Same initialisation as Approach 2.
2. After deciding to move `left` (shorter side, height `hl`):
   - Keep advancing `left` while `h[left] <= hl`.
3. After deciding to move `right` (shorter side, height `hr`):
   - Keep advancing `right` while `h[right] <= hr`.
4. Return `max_water`.

| Metric | Value |
|--------|-------|
| Time   | O(n) worst case; faster in practice |
| Space  | O(1)  |

---

## Real-World Use Case

**Water reservoir engineering**: Engineers modelling water retention in terrain cross-sections use exactly this problem — given a height profile of land ridges, find the two walls that form the largest natural reservoir. The two-pointer approach mirrors how a physical survey narrows candidates: start with the widest possible span and eliminate sides that are too short to hold more water than the current best.

**Software context**: In image processing, finding the largest bounding rectangle that fits within a histogram of feature heights reduces to this same problem.

---

## Interview Tips

- The O(n²) brute force is trivial; interviewers want the two-pointer solution and, crucially, the **proof of correctness**.
- The proof: when we move the shorter pointer, we are NOT missing any better answer using that pointer's current position — the current shorter side is the binding constraint, and any other pairing with it produces equal or smaller area.
- Common bug: moving the **taller** pointer instead of the shorter one. Always move the side with the smaller height.
- Tie case: when `h[left] == h[right]`, moving either side is correct. Use `h[left] <= h[right]` to move left.
- This problem is a gateway to "Trapping Rain Water" (LeetCode 42), which is harder but uses the same two-pointer idea extended to per-unit water accumulation.
- Always say "we never miss the optimal pair" as the core justification — stating correctness explicitly is what separates a good answer from a great one.
