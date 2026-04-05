# Sort Linked List of 0s, 1s, and 2s

> **Step 06.3** | **Difficulty:** MEDIUM | **XP:** 25

## Problem Statement

Given a linked list where every node has a data value of `0`, `1`, or `2`, sort the linked list. The list should be sorted in non-decreasing order.

**Note:** You should not change the structure of the linked list. Only change the data values, or alternatively, relink the nodes.

## Examples

| Input | Output |
|-------|--------|
| `1 -> 2 -> 0 -> 2 -> 1 -> 0` | `0 -> 0 -> 1 -> 1 -> 2 -> 2` |
| `0 -> 1 -> 2` | `0 -> 1 -> 2` |
| `2 -> 1 -> 0` | `0 -> 1 -> 2` |
| `0 -> 0 -> 1` | `0 -> 0 -> 1` |

## Constraints

- `1 <= N <= 10^6` (number of nodes)
- Each node's data is `0`, `1`, or `2`

---

## Approach 1: Brute Force (Count and Overwrite)

**Intuition:** Since values are limited to {0, 1, 2}, a simple frequency count followed by value overwrite is perfectly valid. No comparison-based sort is needed.

**Steps:**
1. Traverse the list and count occurrences of `0`, `1`, and `2` into `count[3]`.
2. Traverse again: overwrite the first `count[0]` nodes with `0`, next `count[1]` nodes with `1`, remaining with `2`.
3. Return head.

| Metric | Value   |
|--------|---------|
| Time   | O(2n)   |
| Space  | O(1)    |

---

## Approach 2: Optimal (Three Dummy Lists — Partition and Link)

**Intuition:** Create three sub-lists — one for 0s, one for 1s, one for 2s — each with a dummy sentinel head. Traverse the original list once, detach each node, and append it to the appropriate sub-list. Finally, chain the three sub-lists: `tail0 -> head1 -> tail1 -> head2 -> tail2 -> null`. Return `dummy0.next`.

This approach relinks actual nodes (no value overwriting) and makes only a single pass.

**Steps:**
1. Create dummies `d0`, `d1`, `d2` and tails `t0 = d0`, `t1 = d1`, `t2 = d2`.
2. Traverse original list:
   - For each node, detach it (`cur.next = null`) and append to the matching tail.
3. Link the three lists:
   - `t0.next = d1.next` if non-empty, else `d2.next`.
   - `t1.next = d2.next`.
   - `t2.next = null`.
4. Return `d0.next`.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 3: Best (Three-Bucket Array of Dummies)

**Intuition:** Identical logic to Approach 2 but uses arrays of dummy heads and tails (`heads[3]`, `tails[3]`). This makes the bucket dispatching `tails[v].next = cur` more systematic and generalizes cleanly to any `k`-valued sort.

**Steps:**
1. Allocate `heads[0..2]` and `tails[0..2]` as ListNode arrays.
2. Walk the list: `tails[cur.val].next = cur; tails[cur.val] = cur`.
3. Connect buckets: `tails[0].next = heads[1].next ?? heads[2].next`, `tails[1].next = heads[2].next`, `tails[2].next = null`.
4. Return first non-empty bucket head.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Dutch National Flag problem — three-way partitioning:** Dijkstra's original Dutch National Flag problem is exactly this: partition an array (or list) of three-colored elements in a single pass. This technique is used in:
- **3-way quicksort**: when many duplicate keys exist, 3-way partitioning avoids O(n^2) degeneration.
- **Task scheduling**: categorize tasks by priority (low/medium/high) in a single scan.
- **Network packet routing**: categorize packets by QoS class in a router's forwarding table.

## Interview Tips

- The brute force (count and overwrite) is perfectly valid and O(n) — don't be shy about stating it first. It shows you recognized the constraint that values are only {0, 1, 2}.
- The three-dummy-list approach is the "linked list native" solution — it relinks nodes without modifying values, which may matter if node identity is important.
- This is a direct application of the **Dutch National Flag** algorithm. Mention Dijkstra if you want bonus points.
- Common interview extension: "Sort a list of 0s and 1s" — same idea with two buckets.
- Another extension: "Sort a list of k distinct values" — use k buckets (bucket sort / counting sort on linked list).
- Be careful when connecting the three sub-lists: if the `1s` list is empty, `t0.next` should go directly to `d2.next`, not `d1.next` (which still points to `d1` itself, not a real node).
