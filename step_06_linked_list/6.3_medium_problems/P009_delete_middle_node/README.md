# Delete the Middle Node of a Linked List

> **LeetCode 2095** | **Step 06.6.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given the head of a linked list, **delete the middle node** and return the modified head.

The middle node is the node at **0-based index `floor(n / 2)`**, where `n` is the number of nodes.

| n | Middle index | Middle node |
|---|-------------|-------------|
| 1 | 0 | only node |
| 2 | 1 | 2nd node |
| 3 | 1 | 2nd node |
| 4 | 2 | 3rd node |
| 5 | 2 | 3rd node |

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `1 -> 3 -> 4 -> 7 -> 1 -> 2 -> 6` | `1 -> 3 -> 4 -> 1 -> 2 -> 6` | n=7, mid=3 (value 7) deleted |
| `1 -> 2 -> 3 -> 4` | `1 -> 2 -> 4` | n=4, mid=2 (value 3) deleted |
| `2 -> 1` | `2` | n=2, mid=1 (value 1) deleted |
| `1` | `[]` | n=1, single node deleted |

## Constraints

- The number of nodes is in `[1, 10^5]`.
- `1 <= Node.val <= 10^5`

---

## Approach 1: Brute Force — Two Passes

**Intuition:** Count the list length first, compute `mid = length / 2`, then walk to the predecessor and unlink the middle node. Straightforward but requires traversing the list twice.

**Steps:**
1. Traverse the list to count length `n`. Compute `mid = n / 2`.
2. Create a `dummy` node before `head`.
3. Walk `dummy` forward `mid` steps — lands at the predecessor of the middle.
4. `predecessor.next = predecessor.next.next` — unlink middle.
5. Return `dummy.next`.

| Metric | Value |
|--------|-------|
| Time   | O(2n) = O(n) |
| Space  | O(1)  |

---

## Approach 2: Optimal — Slow/Fast Pointers with Prev

**Intuition:** Use the classic slow/fast pointer technique to find the middle in one pass. `slow` advances one step at a time; `fast` advances two. When `fast` exits the list, `slow` is at the middle. A `prev` pointer trails one step behind `slow` to allow unlinking.

**Steps:**
1. Create `dummy -> head`. Set `prev = dummy`, `slow = fast = head`.
2. While `fast != null && fast.next != null`:
   - `prev = slow`
   - `slow = slow.next`
   - `fast = fast.next.next`
3. `prev.next = slow.next` — unlink the middle.
4. Return `dummy.next`.

**Why dummy?** For a single-node list, `fast` exits the loop immediately, `slow` is `head`, and `prev` stays at `dummy`. `dummy.next = head.next = null` correctly returns an empty list.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 3: Best — Slow/Fast with Fast Starting One Ahead

**Intuition:** Start `fast` one step ahead of `slow` so that `slow` naturally lands at the **predecessor** of the middle when the loop ends — no `prev` variable needed.

**Steps:**
1. If `head == null || head.next == null`, return `null`.
2. Set `slow = head`, `fast = head.next`.
3. While `fast.next != null && fast.next.next != null`:
   - `slow = slow.next`
   - `fast = fast.next.next`
4. `slow.next = slow.next.next` — `slow.next` is the middle; unlink it.
5. Return `head`.

**Why fast starts at head.next:** The loop advances `fast` two steps and `slow` one step. By giving `fast` a one-step head start, when the loop can no longer move two more steps, `slow` is at the predecessor (one before middle) rather than the middle itself. The extra head start acts as a built-in -1 offset.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Divide-and-conquer on linked lists (Merge Sort)**: Merge sort on a linked list requires splitting the list at the midpoint at each level of recursion. The fast/slow pointer approach is how every production merge-sort-for-linked-list implementation finds that split point in O(n) with O(1) space. Deleting the middle is the degenerate one-level case of that split.

---

## Interview Tips

- Know both the `prev`-tracking variant (Approach 2) and the "fast starts one ahead" variant (Approach 3). Interviewers often ask you to eliminate the extra variable.
- For the "fast starts one ahead" trick: the loop condition `fast.next != null && fast.next.next != null` checks that fast CAN take two more steps — this is subtly different from the standard middle-find condition `fast != null && fast.next != null`.
- The single-node edge case (`return null`) must be handled before entering the pointer logic.
- LeetCode 876 (Find Middle of Linked List) is the companion problem — solve that first to build intuition.
- Follow-up: "Delete the k-th node from the middle" — generalise by storing an index counter on `slow`.
