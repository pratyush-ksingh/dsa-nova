# Remove Nth Node From End of List

> **LeetCode 19** | **Step 06.6.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given the head of a linked list, **remove the n-th node from the end** of the list and return its head.

The constraint guarantees `1 <= n <= length of list`, so n is always valid.

The challenge: can you do it in **a single pass**?

## Examples

| Input | n | Output | Explanation |
|-------|---|--------|-------------|
| `1 -> 2 -> 3 -> 4 -> 5` | 2 | `1 -> 2 -> 3 -> 5` | 2nd from end is 4 |
| `1` | 1 | `[]` | Only node removed |
| `1 -> 2` | 1 | `1` | Last node removed |
| `1 -> 2` | 2 | `2` | First node removed |

## Constraints

- The number of nodes is in `[1, 30]`.
- `0 <= Node.val <= 100`
- `1 <= n <= sz`

---

## Approach 1: Brute Force — Two Passes

**Intuition:** First pass counts the total length `L`. The n-th node from the end is the `(L - n + 1)`-th node from the front (1-based). Walk to its predecessor and unlink it. Requires two traversals of the list.

**Steps:**
1. Traverse the list to count length `L`.
2. Create a `dummy` node pointing to `head` (handles deletion of head when `n == L`).
3. Walk `dummy` forward `(L - n)` steps — this lands on the predecessor.
4. `predecessor.next = predecessor.next.next` unlinks the target.
5. Return `dummy.next`.

| Metric | Value |
|--------|-------|
| Time   | O(2n) = O(n) |
| Space  | O(1)  |

---

## Approach 2: Optimal — Two Pointers with n+1 Gap

**Intuition:** Maintain two pointers both starting from a dummy node. Move `fast` exactly `n + 1` steps ahead of `slow`. Then advance both until `fast` is null. Because the gap is `n + 1`, `slow` stops at the **predecessor** of the target node.

**Steps:**
1. Create `dummy -> head`. Set `fast = slow = dummy`.
2. Advance `fast` by `n + 1` steps.
3. Advance both `fast` and `slow` together until `fast == null`.
4. `slow.next = slow.next.next` — unlink target.
5. Return `dummy.next`.

**Why n+1?** We want `slow` at the predecessor, not the target. If we moved `fast` only `n` steps, `slow` would land on the target itself with no way to unlink it without a prev pointer.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 3: Best — Two Pointers with Explicit n Gap

**Intuition:** A subtle variant: create a gap of exactly `n` (not `n+1`). Then advance both until `right.next == null` (i.e., `right` is at the last node). At that point `left` is the predecessor of the target.

**Steps:**
1. Create `dummy -> head`. Set `left = right = dummy`.
2. Advance `right` by `n` steps.
3. Advance both until `right.next == null`.
4. `left.next = left.next.next` — unlink target.
5. Return `dummy.next`.

**Head deletion edge case:** When `n == length`, after step 2 `right` points to the last node. The loop in step 3 doesn't execute (right.next is already null), so `left` stays at `dummy`. `dummy.next = dummy.next.next` correctly removes head.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Rolling log buffer management**: In a fixed-depth circular log where you always want to drop the n-th most recent entry (because it's outside the retention window), the two-pointer technique models exactly one-pass maintenance of that window without reading the entire log twice. The same pattern appears in streaming data pipelines where you cannot buffer the full input.

---

## Interview Tips

- Always use a **dummy node** before head. Without it, removing head (`n == length`) requires a special case that clutters the code.
- The two most common bugs: off-by-one in how far `fast` is advanced, and forgetting to return `dummy.next` instead of `head`.
- Distinguish between "gap of n" (loop until `right.next == null`) and "gap of n+1" (loop until `fast == null`); both are correct with different termination conditions — pick one and be consistent.
- Interviewer follow-up: "Can you do it recursively?" — yes, recurse to the end, return the depth count back up, unlink when the count equals `n`.
- Test with `n == 1` (remove tail), `n == length` (remove head), and a two-node list to cover all edge cases.
