# Combination Sum

> **LeetCode 39** | **Step 07 — 7.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an array of **distinct** integers `candidates` and a target integer `target`, return a list of all **unique combinations** of `candidates` where the chosen numbers sum to `target`. The same number may be chosen from `candidates` an **unlimited number of times**. Two combinations are unique if the frequency of at least one of the chosen numbers is different. The answer may be returned in any order.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| candidates = [2,3,6,7], target = 7 | [[2,2,3],[7]] | 2+2+3=7 and 7=7 |
| candidates = [2,3,5], target = 8 | [[2,2,2,2],[2,3,3],[3,5]] | All unique combos summing to 8 |
| candidates = [2], target = 1 | [] | No combination sums to 1 |

## Constraints

- `1 <= candidates.length <= 30`
- `2 <= candidates[i] <= 40`
- All elements of `candidates` are **distinct**.
- `1 <= target <= 40`

---

## Approach 1: Brute Force — Generate All Combinations

**Intuition:** Recursively pick each candidate starting from index `start`, allowing re-picking the same element (unlimited reuse). No early termination — explore all branches even past the target.

**Steps:**
1. For each index `i` from `start` to end:
   - Add `candidates[i]` to current combination.
   - Recurse with same `start = i` (allowing reuse) and `remaining - candidates[i]`.
   - Backtrack (remove last element).
2. If `remaining == 0`, record the combination.
3. If `remaining < 0`, return (over-shot).

| Metric | Value |
|--------|-------|
| Time   | O(2^t * k) where t = target, k = avg combo length |
| Space  | O(2^t * k) for output |

---

## Approach 2: Optimal — Backtracking with Sort + Early Break

**Intuition:** Sort the candidates first. When iterating, if `candidates[i] > remaining`, we can **break** immediately because all subsequent candidates (being larger) will also exceed the remaining target. This prunes the search tree substantially.

**Steps:**
1. Sort `candidates`.
2. For each index `i` from `start`:
   - If `candidates[i] > remaining`, **break** (not continue).
   - Add `candidates[i]`, recurse with same `i`, backtrack.
3. If `remaining == 0`, record the combination.

| Metric | Value |
|--------|-------|
| Time   | O(2^(t/min_c)) where min_c = smallest candidate |
| Space  | O(t/min_c) recursion depth |

---

## Approach 3: Best — Pick/Skip Decision Tree

**Intuition:** At each index, make a binary choice: **pick** `candidates[idx]` (stay at same index to allow reuse) or **skip** it (advance to next index). This two-branch model is elegant and avoids duplicates structurally — we never revisit a prior index so order is preserved.

**Steps:**
1. Sort `candidates`.
2. At each call with `(idx, current, remaining)`:
   - **Pick:** append `candidates[idx]`, recurse with same `idx`, pop.
   - **Skip:** recurse with `idx + 1`, unchanged `current`.
3. Base cases: `remaining == 0` (record) or `idx out of bounds` or `remaining < 0` (return).

**Decision tree for [2,3], target=4:**
```
                 (idx=0, rem=4)
              pick 2 /        \ skip 2
        (idx=0, rem=2)         (idx=1, rem=4)
       pick 2/   \skip 2     pick 3/  \skip 3
  (0,rem=0) (1,rem=2)     (1,rem=1) (end)
  FOUND[2,2] pick3/skip3   ret(neg)
           (1,rem=-1)(end)
```

| Metric | Value |
|--------|-------|
| Time   | O(2^(t/min_c)) |
| Space  | O(t/min_c) |

---

## Real-World Use Case

**Change-making problem:** Given coin denominations, find all ways to make exact change for a given amount. Each denomination can be used unlimited times — exactly this problem.

**Resource allocation:** In cloud computing, assign VM sizes (candidates) to fill a quota (target) exactly. All valid combinations help the scheduler choose the most cost-effective option.

## Interview Tips

- Always sort candidates before backtracking. The `break` (not `continue`) on `candidates[i] > remaining` is crucial for efficiency.
- Key detail: passing `i` (not `i+1`) to the recursive call allows re-using the same element. Passing `i+1` would give Combination Sum II behavior.
- Distinguish from **Combination Sum II** (each number used once, may have duplicates in input) — different problem with different handling.
- The pick/skip model (Approach 3) is conceptually cleaner for teaching; the loop model (Approach 2) is typically faster in practice due to the early break.
- Space complexity counts only the recursion depth (O(t/min_c)), not the output size. Clarify this when discussing complexity.
