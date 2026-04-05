# Order of People Heights

> **Step 13 | 13.2** | **Difficulty:** HARD | **XP:** 50 | **Source:** InterviewBit | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
You are given two integer arrays:
- `A[i]` = height of person `i`
- `B[i]` = number of people **in front of** person `i` who have height **greater than or equal to** `A[i]`

Reconstruct the original queue. Return an array of heights in the order they stand in the queue.

**Constraints:**
- `1 <= n <= 10^4`
- `1 <= A[i] <= 10^6`
- `0 <= B[i] < n`
- A valid solution always exists.

**Examples:**

| Heights | Infronts | Output | Explanation |
|---------|----------|--------|-------------|
| `[5,3,2,6,1,4]` | `[0,1,2,0,3,2]` | `[5,3,2,1,6,4]` | Person with h=6 is at pos 4 (0 in front≥6). Person h=5 at pos 0 (0 in front≥5), etc. |
| `[6,5,4,3,2,1]` | `[0,0,0,0,0,0]` | `[1,2,3,4,5,6]` | Strictly ascending order: no one taller stands in front of anyone |
| `[1,2]` | `[1,0]` | `[2,1]` | Person h=2 at front (0 taller), h=1 behind (1 person≥1 in front) |

### Real-Life Analogy
> *Imagine assembling a group photo by height. You process the tallest people first and seat them in position `k` (their stated "number of people taller in front"). Since everyone already seated is at least as tall, inserting at position k satisfies the constraint exactly. Shorter people added later are invisible to the already-placed taller ones.*

### Key Observations
1. Tallest people are easiest to place: when we insert a tall person, everyone already in the queue is taller or equal, so position `k` directly means `k` people in front.
2. Shorter people inserted later don't affect the count for taller people already placed.
3. This greedy ordering (tall first, insert at index `k`) is provably correct.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why Greedy (Sort + Insert)?
- The constraint for person with height `h` only involves people with height `>= h`.
- Processing by decreasing height ensures all relevant people are already placed when we insert.
- Inserting at index `k` (0-indexed) in the current list places exactly `k` already-placed (taller) people in front.

### Pattern Recognition
- **Pattern:** Greedy with sorted processing — similar to Interval Scheduling and Meeting Rooms.
- **Classification Cue:** "Reconstruct queue with constraints about relative ordering → sort by constraint dimension, then insert greedily."
- This is essentially LeetCode 406 "Queue Reconstruction by Height."

---

## APPROACH LADDER

### Approach 1: Brute Force — Try All Permutations
**Idea:** Try every possible ordering of people. For each ordering, verify that each person has the correct number of taller-or-equal people in front.

**Steps:**
1. Generate all n! permutations of people.
2. For each permutation, verify: for every position `i`, count how many positions `j < i` have height `>= heights[perm[i]]`. Check if this equals `infronts[perm[i]]`.
3. Return the first valid permutation's heights.

**BUD Transition — Bottleneck:** O(n!) is infeasible for n > 12. Need to exploit structure.

| Metric | Value |
|--------|-------|
| Time   | O(n! * n) |
| Space  | O(n) |

---

### Approach 2: Optimal — Sort Descending, Insert at Index k
**What changed:** Exploit the greedy insight: process tallest people first. When placing person `(h, k)`, all already-placed people have height `>= h`, so inserting at index `k` satisfies the constraint exactly.

**Steps:**
1. Create pairs `(height, infront)` for all people.
2. Sort by height descending. Break ties by infront ascending (fewer "taller-in-front" = place earlier among equals).
3. Maintain a result list. For each person `(h, k)` in sorted order, call `result.insert(k, h)`.
4. Return the final list.

**Correctness Proof:**
- When we insert `(h, k)` at index `k`, there are exactly `k` elements already in the list (all with height `>= h`). So exactly `k` people of height `>= h` are in front. ✓
- After inserting a shorter person later, the shorter person may appear at some index, but since they are shorter, they do NOT count toward the infronts of taller people. ✓

**Dry Run:** `heights=[5,3,2,6,1,4], infronts=[0,1,2,0,3,2]`

Sorted pairs (desc height, asc infront): `[(6,0),(5,0),(4,2),(3,1),(2,2),(1,3)]`

| Process | Action | Result |
|---------|--------|--------|
| (6,0) | insert at 0 | [6] |
| (5,0) | insert at 0 | [5,6] |
| (4,2) | insert at 2 | [5,6,4] |
| (3,1) | insert at 1 | [5,3,6,4] |
| (2,2) | insert at 2 | [5,3,2,6,4] |
| (1,3) | insert at 3 | [5,3,2,1,6,4] |

Output: `[5,3,2,1,6,4]` ✓

| Metric | Value |
|--------|-------|
| Time   | O(n² ) — n insertions each costing O(n) in a list |
| Space  | O(n) |

---

### Approach 3: Best — Same Greedy (O(n log n) with BIT)
**Intuition:** The greedy algorithm is correct; the bottleneck is `list.insert(k, h)` which is O(n) for an array. For truly O(n log n):
- Use a Binary Indexed Tree (Fenwick Tree) over n positions, initialized with all 1s (all positions free).
- For each person (sorted by height desc): find the (k+1)-th free position using binary search on BIT prefix sums → O(log n). Mark it used → O(log n).
- Total: O(n log n).

In practice, the O(n²) version with a LinkedList or ArrayList suffices for n <= 10^4 and is what interviewers expect at medium-hard level.

| Metric | Value |
|--------|-------|
| Time   | O(n²) with list; O(n log n) with BIT |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Sorting by height ascending instead of descending — the algorithm breaks completely.
2. Not handling ties: among people with the same height, sort by `infronts` ascending so the one with fewer people in front is placed first.
3. Confusing 0-indexed and 1-indexed positions when counting.

### Edge Cases to Test
- [ ] All people have the same height — sort by infronts ascending
- [ ] Only 1 person — trivially `[heights[0]]`
- [ ] All infronts = 0 — output is heights sorted descending
- [ ] Maximum infronts constraint: infronts[i] = i for all i (everyone has i taller people in front)

---

## Real-World Use Case
**Priority queue reconstruction in distributed systems:** When task logs arrive out of order with metadata about how many higher-priority tasks were pending when each task was submitted, this algorithm reconstructs the original priority order. Used in event replay systems and distributed transaction ordering.

## Interview Tips
- Immediately state the greedy insight: "Process tallest first. Inserting at position k among already-placed taller people satisfies the constraint."
- The key correctness argument: shorter people don't affect the `infronts` count of taller people.
- Tie-breaking: among equal heights, sort by infronts ascending — this ensures the person with fewer taller-in-front is inserted first (at a smaller index), leaving room for the other.
- Mention the O(n log n) BIT optimization as a follow-up if asked to improve.
- LeetCode 406 "Queue Reconstruction by Height" is essentially this same problem.
