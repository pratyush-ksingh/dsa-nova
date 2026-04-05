# Intersection of Two Linked Lists

> **LeetCode 160** | **Step 06 — 6.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given the heads of two singly linked lists `headA` and `headB`, return the node at which the two lists intersect. If the two linked lists have no intersection at all, return `null`.

The intersection is defined based on **reference**, not value — the lists must share the exact same node object from some point onward.

The test cases are generated such that there are no cycles anywhere in the entire linked structure.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| A = [4,1,8,4,5], B = [5,6,1,8,4,5], intersectVal = 8, skipA = 2, skipB = 3 | Node with val = 8 | Lists merge at node 8 |
| A = [1,9,1,2,4], B = [3,2,4], intersectVal = 2, skipA = 3, skipB = 1 | Node with val = 2 | Lists merge at node 2 |
| A = [2,6,4], B = [1,5], intersectVal = 0 | null | No intersection |

## Constraints

- The number of nodes of `listA` is in the range `[1, 3 * 10^4]`
- The number of nodes of `listB` is in the range `[1, 3 * 10^4]`
- `-10^5 <= Node.val <= 10^5`
- `skipA` is in `[0, lenA]`, `skipB` is in `[0, lenB]`
- `intersectVal` is `0` if there is no intersection, else equals the value of the intersecting node
- If there is no intersection, the two lists do not intersect at all

---

## Approach 1: Brute Force — HashSet

**Intuition:** Store all node references of list A in a HashSet. Then walk list B; the first node whose reference appears in the set is the intersection node.

**Steps:**
1. Traverse list A, adding each node reference to a `HashSet`.
2. Traverse list B; for each node check if it exists in the set.
3. Return the first matching node, or `null` if none found.

| Metric | Value |
|--------|-------|
| Time   | O(m + n) |
| Space  | O(m) — storing all of list A |

---

## Approach 2: Optimal — Length Difference + Align

**Intuition:** If two lists intersect, their suffixes from the intersection point are identical. If we align the two pointers so they are equidistant from the end, they will reach the intersection simultaneously.

**Steps:**
1. Compute lengths `lenA` and `lenB`.
2. Advance the pointer of the longer list by `|lenA - lenB|` steps.
3. Walk both pointers one step at a time until `pA == pB`.
4. That node is the intersection (or `null` if lists never meet).

| Metric | Value |
|--------|-------|
| Time   | O(m + n) |
| Space  | O(1) |

---

## Approach 3: Best — Two Pointer Switch Heads

**Intuition:** Both pointers travel `lenA + lenB` steps total. Pointer A walks A then switches to B; pointer B walks B then switches to A. They cover the same total distance and meet at the intersection because the "offset" cancels out automatically.

**Steps:**
1. Initialize `pA = headA`, `pB = headB`.
2. While `pA != pB`:
   - Advance `pA`; if `pA` is `null`, redirect it to `headB`.
   - Advance `pB`; if `pB` is `null`, redirect it to `headA`.
3. Return `pA` (either the intersection node or `null`).

**Why it works:** After the switch, `pA` has traveled `lenA + lenB` and `pB` has traveled `lenB + lenA`. The offset difference is eliminated by the cross-traversal.

| Metric | Value |
|--------|-------|
| Time   | O(m + n) |
| Space  | O(1) |

---

## Real-World Use Case

**Version control / file systems:** Two branches of a file-system tree that share a common sub-tree (e.g., hard-linked directories in Linux). Finding where they converge tells you the shared ancestor node without scanning the entire tree twice.

**Network routing:** Two network paths (sequences of routers) that merge into a common backbone. The intersection node is the first shared router — useful for fault-domain analysis.

## Interview Tips

- The problem is about **node identity** (same memory reference), not value equality. Make this clear upfront.
- The two-pointer trick (Approach 3) is the expected "aha" answer. Derive it by noting that pA covers `a + c` and pB covers `b + c`, then after the switch both cover `a + c + b` = `b + c + a`.
- Always handle the edge case where one or both lists are `null`.
- Do **not** modify the lists (no reversals, no length changes) — the problem expects the original structure to be preserved.
- If asked "what if there are cycles?", note that Floyd's cycle detection would be needed first — but LeetCode 160 guarantees no cycles.
