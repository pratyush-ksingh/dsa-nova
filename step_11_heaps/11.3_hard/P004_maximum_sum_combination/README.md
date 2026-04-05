# Maximum Sum Combination

> **Step 11 | 11.3** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given two integer arrays `A` and `B` each of size `N`, find the `C` largest possible sums where each sum is formed by picking one element from `A` and one from `B` (i.e., `A[i] + B[j]`). Return the results in descending order.

**Constraints:**
- `1 <= N <= 10^3`
- `1 <= C <= N`
- `-10^9 <= A[i], B[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `A=[3,2], B=[1,4], C=2` | `[7,6]` | 3+4=7, 3+1 or 2+4=6 — top 2 are 7,6 |
| `A=[1,4,2,3], B=[2,5,1,6], C=4` | `[10,9,9,8]` | 4+6=10, 4+5=9, 3+6=9, 4+2 or 2+6... → 8 |
| `A=[1,2], B=[3,4], C=3` | `[6,5,5]` | 2+4=6, 2+3=5, 1+4=5 |

### Real-Life Analogy
> *You have N chefs (array A) and N dishes (array B). Each chef-dish pairing produces a "score" (quality). You want to find the top C chef-dish pairings without exhausting all N² combinations. By sorting both by skill/quality descending, you know the best pair is always chef[0]+dish[0], and you can intelligently expand to nearby candidates.*

### Key Observations
1. If both arrays are sorted descending, the maximum sum is always `A[0] + B[0]`.
2. The "sum grid" `A[i] + B[j]` decreases as `i` or `j` increases.
3. BFS over this grid via a max-heap, expanding only valid neighbors, yields sums in decreasing order without visiting all N² cells.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why Max-Heap + Visited Set?
- We do not need all N² sums — only the top C of them.
- The sum grid has a monotonic decreasing property (after sorting both arrays desc).
- A max-heap extracts the current maximum, and we expand to neighbors that could be the next maximum.
- Visited set prevents duplicate heap entries.

### Pattern Recognition
- **Pattern:** BFS on an implicit graph (sum grid) using a max-heap — same as "K Closest Pairs" or "Kth Smallest in Sorted Matrix".
- **Classification Cue:** "Top-K results from a sorted 2D grid → max-heap BFS from (0,0)."

---

## APPROACH LADDER

### Approach 1: Brute Force — Generate All N² Sums
**Idea:** Compute every possible `A[i] + B[j]`, collect all N² values, sort descending, and return the first C.

**Steps:**
1. Nested loop over all (i, j) pairs; compute `A[i] + B[j]` and append to list.
2. Sort the list in descending order.
3. Return the first C elements.

**BUD Transition — Bottleneck:** We compute and store N² sums even though we only need C of them. When N=1000 and C=10, we do 10^6 work for 10 answers.

| Metric | Value |
|--------|-------|
| Time   | O(N² log N) |
| Space  | O(N²) |

---

### Approach 2: Optimal — Sort + Max-Heap BFS
**What changed:** Sort both arrays descending. Then do BFS from (0,0) — the cell with the largest sum — expanding each extracted cell to (i+1,j) and (i,j+1). A max-heap gives the largest unexplored sum at each step.

**Steps:**
1. Sort `A` and `B` in descending order.
2. Push `(A[0]+B[0], 0, 0)` onto a max-heap; mark (0,0) as visited.
3. Repeat C times:
   - Pop `(sum, i, j)` from the heap and add `sum` to result.
   - If `(i+1, j)` not visited: push `(A[i+1]+B[j], i+1, j)` and mark visited.
   - If `(i, j+1)` not visited: push `(A[i]+B[j+1], i, j+1)` and mark visited.
4. Return result.

**Dry Run:** `A=[3,2], B=[4,1], C=2`

| Step | Heap | Pop | Result |
|------|------|-----|--------|
| Init | [(7,0,0)] | — | [] |
| 1 | [(6,1,0),(4,0,1)] | (7,0,0) | [7] |
| 2 | [(4,0,1),(3,1,1)] | (6,1,0) | [7,6] |

| Metric | Value |
|--------|-------|
| Time   | O(N log N + C log C) |
| Space  | O(N + C) for visited set and heap |

---

### Approach 3: Best — Same Max-Heap (clean production form)
**Intuition:** Identical to Approach 2. The algorithm is already optimal. This version uses a `while len(result) < C` loop style which is more readable, and uses `seen.add()` return value to avoid separate containment checks. The key correctness argument: after sorting both arrays descending, the sum grid is "doubly sorted" — every move right or down decreases the sum. BFS from the top-left corner via max-heap visits cells in non-increasing sum order.

| Metric | Value |
|--------|-------|
| Time   | O(N log N + C log C) |
| Space  | O(N + C) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to sort both arrays — the visited-set BFS only works because the grid is monotone.
2. Not using a visited set — causes duplicate entries and wrong answers.
3. Encoding visited pairs as a set of tuples vs. a single long (for Java performance).

### Edge Cases to Test
- [ ] C = 1 (only the maximum sum)
- [ ] C = N² (need all sums — brute force is equivalent)
- [ ] Arrays with all identical elements
- [ ] Negative numbers in both arrays
- [ ] One array has a single element

---

## Real-World Use Case
**Top product recommendations:** In a recommendation engine, array A could be user preference scores and array B could be item quality scores. Finding the top C (user, item) pairings by combined score uses exactly this max-heap BFS — critical when N is large and you need only the top few results without ranking all N² pairs.

## Interview Tips
- This is a natural extension of "Kth Largest Element in a Sorted Matrix."
- Clearly state why sorting is necessary before the BFS — the BFS correctness depends on the grid being monotone.
- Explain the visited set: without it, both (i+1,j) and (i,j+1) can both expand to (i+1,j+1), causing duplicates.
- Complexity breakdown: O(N log N) for sorting + O(C log C) for extracting C elements from a heap that grows by at most 2 per step.
- Similar problems: LeetCode 373 "Find K Pairs with Smallest Sums" (same pattern, min-heap).
