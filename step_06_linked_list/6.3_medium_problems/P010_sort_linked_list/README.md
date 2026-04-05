# Sort Linked List

> **Step 06.3** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** 148

## Problem Statement

Given the `head` of a linked list, return the list after sorting it in **ascending order**.

You are expected to achieve `O(n log n)` time complexity. A follow-up asks for `O(1)` space (excluding recursion stack).

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[4,2,1,3]` | `[1,2,3,4]` | Sorted ascending |
| `[-1,5,3,4,0]` | `[-1,0,3,4,5]` | Negative values handled |
| `[]` | `[]` | Empty list |
| `[1]` | `[1]` | Single element |

## Constraints

- The number of nodes in the list is in the range `[0, 5 * 10^4]`.
- `-10^5 <= Node.val <= 10^5`

---

## Approach 1: Brute Force (Copy to Array, Sort, Rebuild)

**Intuition:** The simplest approach is to disconnect the problem from the linked list structure: collect all values, use a standard sort, write them back. This trades space for simplicity.

**Steps:**
1. Traverse the list and collect all values into an array.
2. Sort the array using a standard sorting algorithm (e.g., Timsort in Python/Java).
3. Traverse the list again, overwriting each node's value with the sorted values in order.
4. Return the original head.

| Metric | Value        |
|--------|--------------|
| Time   | O(n log n)   |
| Space  | O(n)         |

---

## Approach 2: Optimal (Bottom-up Merge Sort — O(1) Space)

**Intuition:** Top-down merge sort uses O(log n) stack space for recursion. Bottom-up merge sort eliminates this by iterating: first merge all pairs of size 1 into sorted pairs of size 2, then merge pairs of size 2 into size 4, and so on, until the whole list is sorted. This runs entirely with pointer manipulation and a fixed number of variables.

**Steps:**
1. Count the list length `n`.
2. Attach a dummy head for simplified splicing.
3. For `size = 1, 2, 4, ... < n`:
   - Walk through the list in chunks of `2 * size`.
   - For each chunk, `split(left, size)` cuts off `size` nodes as the left part; `split(right, size)` gets the right part; remaining list continues.
   - `merge(left, right)` returns `(head, tail)` of the merged portion; attach to the running tail.
4. Return `dummy.next`.

| Metric | Value      |
|--------|------------|
| Time   | O(n log n) |
| Space  | O(1)       |

---

## Approach 3: Best (Top-down Merge Sort — O(log n) Stack)

**Intuition:** The classic recursive merge sort is the most readable and interview-friendly. Find the midpoint with slow/fast pointers, recursively sort each half, then merge. Uses O(log n) stack space which is acceptable for most constraints (up to ~50,000 nodes → 16 levels).

**Steps:**
1. Base case: if `head == null` or `head.next == null`, return `head`.
2. Find midpoint using slow (1 step) and fast (2 steps) pointers. Stop when fast reaches the end.
3. Cut the list at midpoint: `mid = slow.next; slow.next = null`.
4. Recursively sort `left = sort(head)` and `right = sort(mid)`.
5. Merge the two sorted halves using a dummy head and return.

| Metric | Value      |
|--------|------------|
| Time   | O(n log n) |
| Space  | O(log n)   |

---

## Real-World Use Case

**External sort in databases:** When a database table is too large to fit in memory, it is sorted using external merge sort — which is structurally identical to bottom-up merge sort on a linked list (blocks on disk = "nodes"). Each pass merges increasingly larger sorted runs. Understanding linked list merge sort is the conceptual foundation for understanding how databases sort large datasets efficiently.

## Interview Tips

- The problem is famous for requiring **merge sort specifically** — quicksort needs random access for efficient pivot selection, and heapsort requires a heap (array). Merge sort works naturally on linked lists.
- For finding the midpoint: use slow/fast pointers. A subtle bug: `slow = head, fast = head.next` (not `fast = head`) avoids infinite recursion on a 2-node list where both halves need to be different.
- In the merge step, always use a dummy head to avoid special-casing the first node.
- Bottom-up merge sort impresses interviewers because it demonstrates deep understanding. Mention it as a follow-up: "If O(1) space is required, I'd use bottom-up iterative merge sort."
- Edge cases: empty list, single node, two nodes in reverse order, already sorted list, all same values.
- The `split()` helper in bottom-up sort must carefully return `null` if the sublist is smaller than `size`, not crash.
