# Children Sum Property

> **Step 13 - Hard** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given a binary tree, convert it so that **every node's value equals the sum of its two children's values**.

Rules:
- You can **only increase** node values (never decrease them).
- Leaf nodes trivially satisfy the property.
- After the operation the property must hold for **every** internal node.

This is equivalent to the GFG problem "Children Sum Property" / Striver Tree Hard #4.

## Examples

**Example 1 - Validate:**
```
Input:
        10
       /  \
      8    2
     / \  / \
    3   5 1  1

Output: True  (8+2=10, 3+5=8, 1+1=2 — all satisfied)
```

**Example 2 - Convert:**
```
Input:
        2
       / \
      35  10
     / \ /  \
    2  3 5   2

Output (one valid answer):
        45
       /  \
      35   10
     / \  /  \
    30  5 8   2

Explanation: going down we inflate children; coming back up we fix parents.
```

## Constraints

- Number of nodes: `1 <= N <= 10^5`
- Node values: `1 <= val <= 10^4`
- You may only **increase** values.
- No upper bound on how large values can grow.

---

## Approach 1: Brute Force — Validate Only (Multiple DFS passes)

**Intuition:** Simply recurse through the tree checking the invariant at every internal node. If any node fails, return false. This does NOT convert the tree; it only validates.

**Steps:**
1. Base cases: `null` returns `True`; leaf nodes return `True`.
2. Compute `child_sum = left.val + right.val` (use 0 for absent children).
3. If `node.val != child_sum`, return `False`.
4. Recursively check left and right subtrees.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(h) — recursion stack, h = tree height |

**Limitation:** Only validates; conversion requires the approach below.

---

## Approach 2: Optimal — Single DFS (Down-inflate then Up-correct)

**Intuition:** Since we can only increase values, we greedily push the parent's value down to its children on the way **in** (ensuring children are large enough), then on the way **out** we set the parent equal to the true sum of its children.

**Key insight:** At any node, if `child_sum < parent.val`, we push the deficit to the larger child (or left by default). This ensures children are never smaller than needed. After recursing, the children's subtrees are all correct, so we safely update the parent.

**Steps:**
1. If node is `null` or a leaf, return.
2. Compute `child_sum = left.val + right.val`.
3. If `child_sum < node.val`: set `left.val = node.val` (or `right.val` if no left). This inflates the child so the constraint can be satisfied.
4. Recurse into left child, then right child.
5. After both children return, set `node.val = left.val + right.val` (pulling the corrected sum up).

| Metric | Value |
|--------|-------|
| Time   | O(n) — each node visited exactly once |
| Space  | O(h) — recursion stack |

---

## Approach 3: Best — Same DFS with Deficit Propagation

**Intuition:** Identical to Approach 2 but written with explicit `diff` calculation for clarity. Instead of setting `left.val = node.val` (which could overshoot), we compute the exact deficit and add only that to the chosen child.

**Steps:**
1. Compute `diff = node.val - child_sum`.
2. If `diff > 0`, add `diff` to left child (or right if no left).
3. Recurse, then pull sum back up.

This is the **same asymptotic complexity** — there is no strictly "better" algorithm. The improvement is in code clarity.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(h) |

---

## Real-World Use Case

**Organizational budgets / HR cost roll-ups:** In a company org-chart tree, a manager's total budget should equal the sum of all direct reports' budgets. If the board approves a larger budget for a VP, the system needs to redistribute (inflate) team budgets downward and then re-aggregate upward — exactly what this algorithm does. The constraint "only increase" mirrors that budget approvals can never be rescinded mid-cycle.

---

## Interview Tips

- Clarify the "only increase" rule upfront — it's the key constraint that makes the greedy push-down safe.
- The direction matters: push down on the way **in**, correct on the way **out**. Doing it backwards breaks correctness.
- Mention that this is a **post-order traversal with pre-processing** — a rare pattern worth knowing.
- Edge cases: single node (trivially valid), all-zero tree, tree where root must be inflated because both children are 0.
- Follow-up: "What if we could also decrease?" → Then a simpler bottom-up sum assignment works, but the problem becomes trivial.
