# Min Steps in Infinite Grid

> **Batch 2 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
You are given a sequence of `n` points on a 2D infinite grid. Starting from the first point, you must visit each subsequent point in order. In each step you can move to any of the 8 adjacent cells (up, down, left, right, and the 4 diagonals). Return the **minimum total number of steps** required to visit all points in the given order.

**Input:** Two arrays `X` and `Y` of length `n`, where `(X[i], Y[i])` is the i-th point.

**Constraints:**
- `1 <= n <= 10^5`
- `-10^9 <= X[i], Y[i] <= 10^9`

**Examples:**

| Input (X, Y) | Output | Explanation |
|---------------|--------|-------------|
| `X=[0,1,1], Y=[0,1,5]` | `5` | (0,0)->(1,1) = 1 step diagonal, (1,1)->(1,5) = 4 steps up. Total = 5 |
| `X=[0,3], Y=[0,4]` | `4` | Move diagonally 3 times to (3,3), then 1 step up to (3,4). max(3,4) = 4 |
| `X=[0,0], Y=[0,0]` | `0` | Already at destination |

### Real-Life Analogy
> *Think of a king on an infinite chessboard. A king can move one square in any direction -- horizontally, vertically, or diagonally. If the king needs to go 3 squares right and 5 squares up, it moves diagonally for 3 steps (covering both horizontal and vertical distance simultaneously) and then straight up for the remaining 2 steps. The minimum moves equal the larger of the horizontal and vertical distances. This is called Chebyshev distance.*

### Key Observations
1. Each step covers at most 1 unit of horizontal distance AND 1 unit of vertical distance simultaneously (diagonal moves).
2. We can always move diagonally to cover both dimensions at once, so the min steps between two points is `max(|dx|, |dy|)`, not `|dx| + |dy|`. <-- This is the "aha" insight (Chebyshev distance)
3. Since we must visit points in order, the total is just the sum of Chebyshev distances between consecutive pairs.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We only need sequential iteration over the points -- a simple **Array** is perfect.
- No need for sorting, hashing, or advanced data structures; the problem is purely mathematical once you recognize the distance formula.

### Pattern Recognition
- **Pattern:** Pairwise Consecutive Computation (compute something for each adjacent pair and sum)
- **Classification Cue:** "When you see _minimum steps on a grid with 8-directional movement_ --> think _Chebyshev distance = max(|dx|, |dy|)_"

---

## APPROACH LADDER

### Approach 1: Brute Force -- BFS/Simulation
**Idea:** For each pair of consecutive points, simulate the movement step by step, always moving diagonally toward the target when possible, then straight when one axis is aligned.

**Steps:**
1. For each pair of consecutive points `(x1,y1)` to `(x2,y2)`:
   - While current position != target: move one step closer (diagonally if both dx and dy are nonzero, else straight).
   - Count each step.
2. Sum all steps.

**Why it works:** Greedy diagonal movement is provably optimal for 8-directional grids.

**Why we move on:** **Unnecessary work** -- simulating each individual step is O(distance) per pair. If coordinates are up to 10^9, a single pair could require billions of iterations.

| Time | Space |
|------|-------|
| O(sum of distances) -- up to O(n * 10^9) | O(1) |

### Approach 2: Optimal -- Chebyshev Distance Formula
**What changed (BUD -- Bottleneck):** Instead of simulating step-by-step, we compute the answer for each pair in O(1) using the formula `max(|dx|, |dy|)`.

**Steps:**
1. Initialize `totalSteps = 0`.
2. For each consecutive pair `(X[i], Y[i])` to `(X[i+1], Y[i+1])`:
   - `dx = |X[i+1] - X[i]|`
   - `dy = |Y[i+1] - Y[i]|`
   - `totalSteps += max(dx, dy)`
3. Return `totalSteps`.

**Why `max(|dx|, |dy|)` works:**
- Diagonal steps reduce both |dx| and |dy| by 1 each.
- We take `min(|dx|, |dy|)` diagonal steps, after which one axis is zeroed out.
- We then take `|max(|dx|,|dy|) - min(|dx|,|dy|)|` straight steps for the remaining axis.
- Total = `min(|dx|,|dy|) + (max(|dx|,|dy|) - min(|dx|,|dy|))` = `max(|dx|, |dy|)`.

**Dry Run:** `X=[0,1,1], Y=[0,1,5]`

| Pair | dx | dy | max(dx,dy) | Running Total |
|------|----|----|------------|---------------|
| (0,0)->(1,1) | 1 | 1 | 1 | 1 |
| (1,1)->(1,5) | 0 | 4 | 4 | 5 |

**Result:** 5

| Time | Space |
|------|-------|
| O(n) | O(1) |

*Note:* This is already the best possible approach -- we must examine every consecutive pair at least once, so O(n) is the lower bound. No Approach 3 needed.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We iterate through n-1 consecutive pairs, doing O(1) math per pair."
**Space:** O(1) -- "We only maintain a running sum, no extra storage."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Using Manhattan distance `|dx| + |dy|` instead of Chebyshev `max(|dx|, |dy|)` -- forgets that diagonal moves cover both axes.
2. Integer overflow -- coordinates can be up to 10^9, differences can be up to 2*10^9. Use `long` in Java.
3. Off-by-one: forgetting there are `n-1` pairs for `n` points.

### Edge Cases to Test
- [ ] Single point (n=1) --> return 0 (no movement needed)
- [ ] Two identical points --> return 0
- [ ] Pure horizontal move `(0,0)->(5,0)` --> 5
- [ ] Pure vertical move `(0,0)->(0,7)` --> 7
- [ ] Pure diagonal move `(0,0)->(4,4)` --> 4
- [ ] Negative coordinates `(-3,-3)->(3,3)` --> 6
- [ ] Large coordinates (overflow check)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "So we have a sequence of grid points, and we can move in 8 directions. I need the total minimum steps to visit them all in order."
2. **Match:** "Moving in 8 directions on a grid is the classic Chebyshev distance setup."
3. **Plan:** "For each consecutive pair, the answer is max(|dx|, |dy|). I'll sum these up."
4. **Implement:** Write clean code. Mention overflow risk in Java.
5. **Review:** Walk through the example dry run.
6. **Evaluate:** "O(n) time, O(1) space. We can't do better since we must read all points."

### Follow-Up Questions
- "What if you could visit points in any order?" --> This becomes the Travelling Salesman Problem (NP-hard).
- "What if you can only move in 4 directions (no diagonals)?" --> Use Manhattan distance `|dx| + |dy|`.
- "What if some cells are blocked?" --> BFS/A* needed.

---

## CONNECTIONS
- **Prerequisite:** Basic array traversal, absolute value
- **Same Pattern:** Manhattan distance problems, grid traversal
- **Related Concept:** Chebyshev distance vs Manhattan distance vs Euclidean distance
- **This Unlocks:** Understanding grid distance metrics for pathfinding problems
