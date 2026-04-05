# Asteroid Collision

> **LeetCode 735** | **Step 09 — 9.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

We are given an array `asteroids` of integers representing asteroids in a row. For each asteroid:
- The **absolute value** represents its **size**.
- The **sign** represents its **direction**: positive = moving **right**, negative = moving **left**.

All asteroids move at the same speed. Find out the state of the asteroids after all collisions.

**Collision rules:**
- Two asteroids collide only when one moves **right** (+) and the next moves **left** (-).
- The **smaller** one explodes.
- If they are the **same size**, both explode.
- Two asteroids moving in the **same direction** never collide.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [5, 10, -5] | [5, 10] | -5 collides with 10, -5 explodes (10 > 5) |
| [8, -8] | [] | Equal size, both explode |
| [10, 2, -5] | [10] | -5 destroys 2, then 10 destroys -5 |
| [-2, -1, 1, 2] | [-2, -1, 1, 2] | No collision (left-moving never catch right-moving ahead) |
| [1, -1, -2] | [-2] | 1 and -1 cancel, -2 survives |

## Constraints

- `2 <= asteroids.length <= 10^4`
- `-1000 <= asteroids[i] <= 1000`
- `asteroids[i] != 0`

---

## Approach 1: Brute Force — Repeated Simulation

**Intuition:** Repeatedly scan the array for any adjacent pair where a right-moving asteroid is immediately followed by a left-moving asteroid. Resolve that collision and rescan from the beginning. Repeat until no collision is found.

**Steps:**
1. Copy the array into a mutable list.
2. Set `changed = true`. While `changed`:
   - `changed = false`.
   - Scan pairs (i, i+1). If `list[i] > 0` and `list[i+1] < 0`:
     - Set `changed = true`.
     - Resolve: pop larger survivor or both if equal.
     - Break inner loop and restart scan.
3. Return the final list.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) — up to n collisions each requiring a full scan |
| Space  | O(n)  |

---

## Approach 2: Optimal — Single-Pass Stack Simulation

**Intuition:** Process asteroids left to right. A stack holds "surviving" asteroids seen so far. A collision can only happen between the top of the stack (moving right, +) and the current asteroid (moving left, -). Resolve it greedily:
- Current wins (bigger): pop stack top, keep processing current.
- Equal: pop both, current is done.
- Stack top wins: current is destroyed, move on.

**Steps:**
1. Initialize empty `stack`.
2. For each `asteroid`:
   - Mark it `alive = true`.
   - While `alive` and stack top > 0 and current < 0 (collision condition):
     - If `top < |current|`: pop top (top destroyed), continue.
     - If `top == |current|`: pop top, `alive = false` (both destroyed).
     - Else: `alive = false` (current destroyed, top survives).
   - If still `alive`: push to stack.
3. Return stack as array.

| Metric | Value |
|--------|-------|
| Time   | O(n) — each asteroid is pushed and popped at most once |
| Space  | O(n)  |

---

## Approach 3: Best — Stack with Explicit Collision Logic

**Intuition:** Same stack algorithm as Approach 2, reorganized with a `while-break` pattern that explicitly handles the three cases (top explodes, both explode, current explodes). Uses a list as the stack for direct index-based construction of the result.

**Steps:**
1. Use `List` as stack (append/remove last).
2. For each asteroid, loop while collision condition holds:
   - `top < |current|`: remove top, continue inner while.
   - `top == |current|`: remove top, `currentAlive = false`, break.
   - `top > |current|`: `currentAlive = false`, break.
3. If `currentAlive`: append to stack.

This maps directly to the clean interview whiteboard code.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

## Real-World Use Case

**Packet collision in network switches:** Packets routed in opposite directions on a shared medium simulate asteroid collision — the smaller/lower-priority packet is dropped, or both are dropped on equal priority. A stack-based simulation models this in one pass.

**Game physics / particle simulation:** In 2D game engines, projectiles moving in opposite directions are checked for collision in a single sweep using a stack — an O(n) pass replaces the naive O(n^2) pair-check for linear tracks.

## Interview Tips

- The key insight is that collisions only happen between a **right-mover on the stack** and a **left-mover being processed**. Two right-movers or two left-movers can never collide.
- The `while` inner loop handles chain reactions correctly: after destroying the stack top, the current asteroid must be checked against the new top.
- Common mistake: forgetting to continue checking after the top is destroyed. The current asteroid can destroy multiple stack entries.
- Equal-size destruction: both must be removed. Mark `alive = false` AND pop the stack top.
- Edge cases:
  - All positive: no collisions, return as-is.
  - All negative: no collisions, return as-is.
  - `[-2, -1, 1, 2]`: left-movers came first — they can never hit the right-movers that follow (already past them).
