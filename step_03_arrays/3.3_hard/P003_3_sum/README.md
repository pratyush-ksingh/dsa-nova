# 3 Sum

> **LeetCode 15** | **Step 03 - Arrays (Hard)** | **Difficulty:** HARD | **XP:** 30 | **Status:** UNSOLVED

## Problem Statement

Given an integer array `nums`, return all unique triplets `[nums[i], nums[j], nums[k]]` such that `i != j`, `i != k`, `j != k`, and `nums[i] + nums[j] + nums[k] == 0`.

The solution set **must not contain duplicate triplets**.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[-1, 0, 1, 2, -1, -4]` | `[[-1,-1,2],[-1,0,1]]` | Two valid unique triplets |
| `[0, 1, 1]` | `[]` | No triplet sums to zero |
| `[0, 0, 0]` | `[[0,0,0]]` | Only one unique triplet |
| `[-2, 0, 0, 2, 2]` | `[[-2,0,2]]` | Duplicates must be skipped |

## Constraints

- `3 <= nums.length <= 3000`
- `-10^5 <= nums[i] <= 10^5`
- Answer must not contain duplicate triplets.

---

## Approach 1: Brute Force — Triple Nested Loops

**Intuition:** Try every combination of three distinct indices. Sort each found triplet before inserting it into a set to deduplicate results.

**Steps:**
1. Use three nested loops over indices `i < j < k`.
2. If `nums[i] + nums[j] + nums[k] == 0`, sort the triplet.
3. Convert sorted triplet to a canonical key (tuple/string) and add to a set.
4. Build the result list from the set.

| Metric | Value |
|--------|-------|
| Time   | O(n³) |
| Space  | O(k) — k = number of unique triplets stored |

---

## Approach 2: Optimal — Sort + Fix One + Two Pointers

**Intuition:** Sort the array. Fix the first element `nums[i]` and reduce the problem to "find two numbers in `nums[i+1..n-1]` that sum to `-nums[i]`" — a classic two-pointer problem. Skipping duplicates at each pointer position guarantees uniqueness.

**Steps:**
1. Sort `nums`.
2. Iterate `i` from `0` to `n-3`:
   - If `nums[i] > 0`, break (all subsequent sums will be positive).
   - If `i > 0` and `nums[i] == nums[i-1]`, skip (duplicate first element).
3. Set `lo = i+1`, `hi = n-1`.
4. While `lo < hi`:
   - `sum = nums[i] + nums[lo] + nums[hi]`
   - If `sum == 0`: record triplet, advance `lo` past duplicates, retreat `hi` past duplicates, then `lo++`, `hi--`.
   - If `sum < 0`: `lo++`.
   - If `sum > 0`: `hi--`.

| Metric | Value |
|--------|-------|
| Time   | O(n²) — O(n log n) sort + O(n²) two-pointer |
| Space  | O(1) extra — sort in place, output not counted |

---

## Approach 3: Best — Sort + Fix One + HashSet Inner Pass

**Intuition:** Same outer loop as Approach 2. For the inner search, use a hash set instead of a second pointer. As we scan rightward from `i+1`, store each `nums[j]` in the set. Before storing, check if its complement (`-nums[i] - nums[j]`) is already in the set.

**Steps:**
1. Sort `nums`; iterate `i` with same duplicate-skip and early-exit logic.
2. For each `i`, create an empty `seen` set.
3. For `j` from `i+1` to `n-1`:
   - `complement = -nums[i] - nums[j]`
   - If `complement` is in `seen`: record `[nums[i], complement, nums[j]]`, skip duplicate `nums[j]` values.
   - Add `nums[j]` to `seen`.

| Metric | Value |
|--------|-------|
| Time   | O(n²) |
| Space  | O(n) — inner hash set |

---

## Real-World Use Case

**Financial portfolio balancing / arbitrage detection:** Given a list of currency exchange rates or profit/loss values, find three assets whose combined net position equals zero (balanced exposure). The same O(n²) two-pointer pattern is used in computational chemistry to find three particles whose interaction energy sums to a target value, and in recommendation systems to find three items that together match a composite user preference score.

## Interview Tips

- The two-pointer solution is the expected answer — memorise the duplicate-skipping logic exactly.
- Three common mistakes: (1) forgetting to skip duplicates for `i`; (2) forgetting to skip duplicates for `lo`/`hi` after recording a match; (3) not handling `nums[i] > 0` early exit.
- If asked for 4-Sum (LeetCode 18), extend by fixing two pointers `i` and `j` in O(n³) with the same two-pointer inner loop.
- If the interviewer allows O(n²) space and asks for a simpler approach, hash-set per row is acceptable.
- Sorting is key: it allows both duplicate skipping and the two-pointer narrowing argument to work.
