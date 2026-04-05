# 4 Sum

> **Step 03 | 3.3** | **Difficulty:** HARD | **XP:** 35 | **LeetCode:** 18 | **Status:** UNSOLVED

## Problem Statement

Given an integer array `nums` of `n` integers and an integer `target`, return all **unique quadruplets** `[nums[a], nums[b], nums[c], nums[d]]` such that:
- `a`, `b`, `c`, `d` are distinct indices
- `nums[a] + nums[b] + nums[c] + nums[d] == target`

The answer must not contain duplicate quadruplets (two quadruplets with the same values in any order are duplicates).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| nums=[1,0,-1,0,-2,2], target=0 | [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]] | Three unique quadruplets summing to 0 |
| nums=[2,2,2,2,2], target=8 | [[2,2,2,2]] | Only one unique quadruplet |
| nums=[1,2,3,4], target=10 | [[1,2,3,4]] | Entire array |
| nums=[-3,-2,-1,0,0,1,2,3], target=0 | Multiple | Several quadruplets |

## Constraints

- 1 <= nums.length <= 200
- -10^9 <= nums[i] <= 10^9
- -10^9 <= target <= 10^9
- **Watch out for integer overflow!** Use long arithmetic in Java when summing 4 large values.

---

## Approach 1: Brute Force

**Intuition:** Check every possible combination of 4 distinct indices (a < b < c < d). For each combination, check if the sum equals target. Use a Set of sorted tuples to automatically eliminate duplicates.

**Steps:**
1. Use four nested loops for indices a < b < c < d
2. If `nums[a] + nums[b] + nums[c] + nums[d] == target`, sort the four values and add to a Set
3. Convert the Set to a list and return

| Metric | Value |
|--------|-------|
| Time   | O(n^4) |
| Space  | O(k) where k = number of unique quadruplets |

---

## Approach 2: Optimal — Sort + Fix Two + Two Pointers

**Intuition:** This generalizes the 3Sum two-pointer technique by adding one more outer loop. Sort the array first. Fix the first element (index i) and the second element (index j > i). Then use two pointers (lo = j+1, hi = n-1) to find pairs summing to `target - nums[i] - nums[j]`. Skip duplicate values at each level to avoid duplicate quadruplets in the output.

**Steps:**
1. Sort `nums`
2. Loop `i` from 0 to n-4:
   - Skip if `nums[i] == nums[i-1]` (duplicate first element)
   - Loop `j` from i+1 to n-3:
     - Skip if `nums[j] == nums[j-1]` (duplicate second element)
     - Set `lo = j+1`, `hi = n-1`
     - While `lo < hi`:
       - Compute `total = nums[i] + nums[j] + nums[lo] + nums[hi]`
       - If `total == target`: add quadruplet, skip duplicates on both sides, move both pointers
       - If `total < target`: increment `lo`
       - If `total > target`: decrement `hi`
3. Return result

| Metric | Value |
|--------|-------|
| Time   | O(n^3) |
| Space  | O(k) |

---

## Approach 3: Best — Sort + Fix Two + Two Pointers + Pruning

**Intuition:** Same as Optimal, but add constant-time pruning conditions at each outer loop level:
- If the minimum possible quadruplet sum with the current `i` (four smallest remaining elements) exceeds `target`, **break** — no solutions exist for any larger i.
- If the maximum possible quadruplet sum with the current `i` (three largest elements) is below `target`, **continue** — current i is too small, try the next one.
Apply the same two prunings for the j loop. These allow early termination without changing worst-case complexity but drastically reduce constant factors on many inputs.

**Steps:**
1. Sort `nums`
2. Loop `i` with pruning:
   - Break if `nums[i] + nums[i+1] + nums[i+2] + nums[i+3] > target`
   - Continue if `nums[i] + nums[n-1] + nums[n-2] + nums[n-3] < target`
3. Loop `j` with pruning:
   - Break if `nums[i] + nums[j] + nums[j+1] + nums[j+2] > target`
   - Continue if `nums[i] + nums[j] + nums[n-1] + nums[n-2] < target`
4. Two-pointer inner loop (same as Optimal)

| Metric | Value |
|--------|-------|
| Time   | O(n^3) worst case, much faster in practice |
| Space  | O(k) |

---

## Real-World Use Case

**Combinatorial Drug Discovery:** In bioinformatics, finding combinations of 4 compounds whose combined effect (represented as a sum of effect scores) meets a target efficacy value is analogous to 4-sum. Pharmaceutical companies use constraint-based combinatorial search — the sorted two-pointer optimization is a direct application.

**Financial Portfolio Optimization:** Finding four assets whose expected returns sum exactly to a target return, or whose combined risk score meets a threshold. The pruning in Approach 3 maps to eliminating asset classes that can't possibly meet the target.

## Interview Tips

- 4Sum is a natural extension of 3Sum. If you can explain 3Sum with two pointers clearly, extending it is straightforward. Show that pattern.
- **Duplicate skipping:** Must skip duplicates at all four levels (i, j, lo, hi). A very common bug is skipping only at the outer levels.
- **Integer overflow:** In Java/C++, `nums[i] + nums[j] + nums[lo] + nums[hi]` can overflow int. Cast to long before summing.
- The brute force is O(n^4); mention the progression: O(n^4) brute → O(n^3) two-pointer → O(n^3) with pruning.
- **Generalization:** kSum can be solved recursively — reduce to (k-1)Sum by fixing one element. Know this pattern for follow-ups asking about 5Sum or kSum.
- If asked about uniqueness: after sorting, adjacent duplicate skipping is cleaner and more efficient than using a Set.
