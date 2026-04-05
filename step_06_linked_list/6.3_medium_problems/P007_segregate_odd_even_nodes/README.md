# Odd Even Linked List

> **LeetCode 328** | **Step 06.6.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given the head of a singly linked list, group all nodes with **odd indices** together followed by all nodes with **even indices**, and return the reordered list.

- The **first** node is considered odd (index 1), the **second** is even (index 2), and so on.
- "Odd/even" refers to the **node position/index**, NOT the node's value.
- The relative order within the odd group and within the even group must be preserved.
- Must run in **O(n) time** and **O(1) extra space**.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `1 -> 2 -> 3 -> 4 -> 5` | `1 -> 3 -> 5 -> 2 -> 4` | Odd indices: 1,3,5. Even indices: 2,4 |
| `2 -> 1 -> 3 -> 5 -> 6 -> 4 -> 7` | `2 -> 3 -> 6 -> 7 -> 1 -> 5 -> 4` | Odd: 2,3,6,7. Even: 1,5,4 |
| `1` | `1` | Single node, no change |

## Constraints

- The number of nodes is in the range `[0, 10^4]`.
- `-10^6 <= Node.val <= 10^6`

---

## Approach 1: Brute Force — Collect Values, Rebuild

**Intuition:** Walk the list once, collecting values at 1-based odd positions into one list and even positions into another. Then build a brand-new linked list by concatenating them. Simple to reason about but uses O(n) extra memory.

**Steps:**
1. Traverse the original list, keeping a 1-based index counter.
2. Append `node.val` to `odd_vals` if index is odd, else to `even_vals`.
3. Build a new list: iterate `odd_vals + even_vals` and create new nodes.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) — value arrays + new nodes |

---

## Approach 2: Optimal — Two-Chain Pointer Re-linking

**Intuition:** Rather than creating new nodes, rewire the existing `.next` pointers in a single pass. Maintain two "tail" pointers — one for the odd chain, one for the even chain. After the loop, attach the even chain to the end of the odd chain.

**Steps:**
1. `odd = head` (tail of odd chain, starts at node 1).
2. `even = head.next` (tail of even chain, starts at node 2). Save `even_head = even`.
3. Loop while `even != null && even.next != null`:
   - `odd.next = even.next` — odd tail skips the even node.
   - `odd = odd.next` — advance odd tail.
   - `even.next = odd.next` — even tail skips the newly advanced odd node.
   - `even = even.next` — advance even tail.
4. `odd.next = even_head` — stitch chains together.
5. Return `head`.

**Why it terminates:** Each iteration advances the `even` pointer by one node; the loop exits when `even` or `even.next` is null (handles both even-length and odd-length lists).

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 3: Best — Same Algorithm, Named Variables

**Intuition:** Identical to Approach 2 but with explicitly named intermediate variables (`nextOdd`, `nextEven`) for each pointer step. Preferred for whiteboard presentations where clarity is paramount.

**Steps:**
Same as Approach 2, with each pointer dereference stored in a named local variable before assignment, making the pointer diagram step-by-step obvious.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Task scheduling with priority tiers**: In an OS ready-queue implemented as a linked list, tasks at odd positions might represent high-priority processes and even positions normal-priority ones. Segregating the list ensures high-priority tasks run first without needing a separate data structure — the same nodes are just re-linked in O(n) time with O(1) overhead.

---

## Interview Tips

- The key insight is that you only need two tail pointers — you do NOT need to know the total length.
- Common mistake: forgetting to save `even_head` before the loop begins (it gets overwritten during traversal).
- Common mistake: not setting `odd.next = even_head` at the end — this leaves the odd and even chains disconnected.
- The loop condition `even != null && even.next != null` handles both odd-length and even-length lists cleanly; verify both cases mentally.
- Follow-up: "What if you need to do this by value (odd/even values)?" — same two-chain idea, just change the partition condition from index parity to value parity.
