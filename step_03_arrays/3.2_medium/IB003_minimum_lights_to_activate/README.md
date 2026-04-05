# Minimum Lights to Activate

> **Step 03 | 3.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## Problem Statement

You are given an array `A` of length `n` where `A[i]` is either `0` or `1`:
- `1` means a light is installed at position `i`.
- `0` means no light at position `i`.

Each light at position `i`, when turned on, illuminates all positions in the range `[i - B, i + B]` (clamped to `[0, n-1]`).

Find the **minimum number of lights** to turn on so that every position in the array is illuminated. Return `-1` if it is impossible to illuminate all positions.

## Examples

| Input A            | B | Output | Explanation |
|--------------------|---|--------|-------------|
| [0,0,1,0,0]        | 2 | 1      | Light at index 2 covers [0,4] — all 5 positions |
| [1,0,0,1,1,0,0,1]  | 2 | 3      | Light at idx 0 covers [0..2], idx 4 covers [2..6], idx 7 covers [5..7] |
| [0,0,0,1,0,0,0]    | 1 | -1     | Positions 0,1 can never be reached (no light within B=1 of them) |
| [1,1,1,1,1]        | 0 | 5      | B=0: each light covers only itself |
| [1,0,1,0,1]        | 1 | 3      | Need idx 0 (covers [0..1]), idx 2 (covers [1..3]), idx 4 (covers [3..4]) |

## Constraints

- 1 <= n <= 10^5
- 0 <= B <= n
- A[i] in {0, 1}

---

## Approach 1: Brute Force

**Intuition:** Try every possible subset of installed lights. For each subset, check whether the union of their coverage intervals covers all positions. Return the size of the smallest valid subset. Exponential in the number of lights — only practical for tiny inputs.

**Steps:**
1. Collect indices of all lights (positions where A[i] == 1). Let there be k such positions.
2. For each size s from 0 to k:
   a. Enumerate all subsets of the k lights of size s.
   b. For each subset, compute the union of coverage intervals.
   c. If all n positions are covered, return s.
3. If no subset works, return -1.

| Metric | Value |
|--------|-------|
| Time   | O(2^k * n) |
| Space  | O(k) for recursion |

---

## Approach 2: Optimal (Greedy)

**Intuition:** This is a classic interval covering / jump game problem. We want the minimum number of intervals (light coverages) to cover [0, n-1]. The greedy strategy: always pick the light that extends our coverage the furthest to the right.

At each step, `pos` = the leftmost uncovered position. Any light within `[pos-B, pos+B]` can cover `pos`. Among all such lights, pick the one with the largest index (rightmost), because its coverage `[light-B, light+B]` extends the farthest right. After activating it, advance `pos` to `light+B+1`.

**Steps:**
1. Initialize `count = 0`, `pos = 0`.
2. While `pos < n`:
   a. Define search window `left = max(0, pos-B)`, `right = min(n-1, pos+B)`.
   b. Find the rightmost index in `[left, right]` where `A[i] == 1`. Call it `bestLight`.
   c. If `bestLight == -1`: return -1 (impossible).
   d. Increment count. Set `pos = bestLight + B + 1`.
3. Return count.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Approach 3: Best

**Intuition:** Identical greedy logic as Optimal, written with a loop that starts from `min(pos+B, n-1)` and scans leftward to `max(pos-B, 0)`. The result is exactly the same algorithm — this variant makes it visually clear that we are scanning from the farthest possible right to find the rightmost light.

**Steps:**
1. Initialize `count = 0`, `pos = 0`.
2. While `pos < n`:
   a. Scan from `min(pos+B, n-1)` down to `max(pos-B, 0)`.
   b. First index with `A[i] == 1` is `found`.
   c. If none found, return -1.
   d. Increment count, set `pos = found + B + 1`.
3. Return count.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(1) |

---

## Real-World Use Case

**Street lighting and wireless access point placement:** Given a road of length n, some fixed lamp post positions, and the illumination radius of each lamp, find the minimum lamps to switch on for full coverage. Equivalently, this models **Wi-Fi router activation** in a corridor — given routers at fixed positions with range B, activate the minimum number to ensure full signal coverage. It's also used in **sensor network coverage** problems and **1D interval covering** for scheduling algorithms.

## Interview Tips

- Recognize this as the "minimum interval cover" or "jump game" greedy pattern — always extend coverage as far right as possible.
- The key insight: for uncovered position `pos`, any light in `[pos-B, pos+B]` can cover `pos`, but the rightmost light maximises how far right we jump.
- Don't confuse the window: a light at index `i` covers `pos` if `|i - pos| <= B`, which means the light must be in `[pos-B, pos+B]`.
- The -1 case occurs when no light exists within B distance of some position — handle it by checking `bestLight == -1`.
- This is related to LeetCode 45 "Jump Game II" — same greedy principle.
- Time complexity is O(n) even though there's a nested loop, because the outer pointer `pos` only ever moves forward.
