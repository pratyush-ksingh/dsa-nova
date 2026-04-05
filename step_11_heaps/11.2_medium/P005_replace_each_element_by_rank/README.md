# Replace Each Element by Rank

> **Step 11.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an array `arr` of integers, replace each element with its **rank**.

The rank represents how large the element is. The rules for assigning ranks are:
- Rank is **1-indexed** (smallest element gets rank 1).
- The larger the element, the larger the rank.
- If two elements are **equal**, their rank must be the **same**.
- Ranks should be as **small as possible** (no gaps).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[100, 37, 15, 1, 900, 2, 2]` | `[5, 4, 3, 1, 6, 2, 2]` | 1→1, 2→2, 15→3, 37→4, 100→5, 900→6; both 2s get rank 2 |
| `[40, 10, 20, 30]` | `[4, 1, 2, 3]` | All distinct; sorted order gives ranks 1-4 |
| `[5, 5, 5]` | `[1, 1, 1]` | All equal → all rank 1 |

## Constraints

- `1 <= arr.length <= 10^5`
- `-10^9 <= arr[i] <= 10^9`

---

## Approach 1: Brute Force

**Intuition:** For any element `x`, its rank equals the number of **distinct** values in the array that are strictly less than `x`, plus 1. We can compute this for every element with nested loops.

**Steps:**
1. For each element `arr[i]`, iterate over the entire array.
2. Collect all values strictly less than `arr[i]` into a set (to deduplicate).
3. The rank of `arr[i]` is `set.size() + 1`.
4. Store result and move to the next element.

| Metric | Value |
|--------|-------|
| Time   | O(n²) |
| Space  | O(n) for the set per element |

---

## Approach 2: Optimal (Sort with Index Tracking)

**Intuition:** Sort the array while remembering original indices. Walk the sorted list assigning ranks sequentially; only increment the rank when the value actually changes. Then place each rank back at its original position.

**Steps:**
1. Create an array of indices `[0, 1, ..., n-1]` and sort them by `arr[index]`.
2. Initialize `rank = 1`.
3. Walk the sorted indices list:
   - If current value differs from previous value, increment `rank`.
   - Assign `result[original_index] = rank`.
4. Return result.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Approach 3: Best (Sorted Unique Values Map)

**Intuition:** Extract unique values, sort them, and assign ranks 1, 2, 3, ... via enumeration. Build a value→rank dictionary, then map the original array in one pass. Cleanest implementation with the same asymptotic complexity.

**Steps:**
1. Compute `sorted(set(arr))` — unique values in ascending order.
2. Build `rank_map = {value: rank}` using enumerate starting at 1.
3. Return `[rank_map[x] for x in arr]`.

This is essentially the same as Approach 2 but leverages Python's `set`/Java's `TreeMap` for more readable code.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n) |

---

## Real-World Use Case

**Percentile Ranking in Competitive Exams:** When millions of students take a standardized test, their raw scores are converted to percentile ranks. Equal scores receive the same percentile rank, and ranks are dense (no gaps). This is exactly the "replace by rank" operation — used in GATE, JEE, GRE result processing.

Another use case: **Database ORDER BY with DENSE_RANK()** — SQL's `DENSE_RANK()` window function implements this exact algorithm for ranking rows within partitions.

## Interview Tips

- Clarify: "Is rank 0-indexed or 1-indexed?" — this problem uses **1-indexed**.
- Clarify: "Are equal elements allowed? What rank do they get?" — same rank, dense ranking (no gaps).
- The key insight is that rank = position in the sorted unique values list + 1.
- Brute force mention shows understanding; pivot quickly to the O(n log n) sort-based solution.
- In Python, `enumerate(sorted(set(arr)), start=1)` is the idiomatic one-liner for building the rank map.
- In Java, `TreeMap` naturally maintains sorted order, making it clean for this problem.
- Distinguish from **RANK** (with gaps) vs **DENSE_RANK** (no gaps) — this problem asks for **dense rank**.
- Follow-up: "What if we want fractional ranks for ties (average rank)?" — common in statistics.
