# Middle of Linked List

> **Batch 1 of 12** | **Topic:** Linked List | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given the `head` of a singly linked list, return the **middle node**. If there are two middle nodes (even-length list), return the **second middle** node.

**LeetCode #876**

### Analogy
Two friends walk down a hallway. One walks at **normal speed**, the other at **double speed**. When the fast friend reaches the end, the slow friend is exactly at the middle. This is the famous **tortoise and hare** technique.

### Key Observations
1. **You don't know the length upfront.** Unlike arrays, there's no `.length` property. *Aha: You either count first (two passes) or use the two-pointer trick (one pass).*
2. **"Second middle" for even length.** For `[1,2,3,4]`, the answer is node 3, not node 2. *Aha: This detail changes when the fast pointer should stop -- it matters whether you check `fast != null` or `fast.next != null`.*
3. **Two-pointer pattern appears everywhere.** This simple problem teaches the slow/fast pointer technique used in cycle detection, finding the start of a cycle, and finding the kth-from-end node. *Aha: Master this and you unlock a family of linked list problems.*

---

## 2. DS & ALGO CHOICE

| Aspect | Details |
|--------|---------|
| **Data Structure** | Singly Linked List |
| **Algorithm** | Two pointers (slow + fast) |
| **Why** | Achieves O(n) time in a single pass with O(1) space |
| **Pattern cue** | "Find middle," "find cycle," "find kth from end" -- think slow/fast pointers. |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Count then Traverse
**Intuition:** First pass counts the nodes. Second pass walks to `count / 2`.

**Steps:**
1. Traverse the list, count nodes: `n`.
2. Traverse again, stop at node `n / 2` (0-indexed).
3. Return that node.

**Dry Run Trace:**
```
List: 1 -> 2 -> 3 -> 4 -> 5

Pass 1: count = 5
Middle index = 5 / 2 = 2

Pass 2: index 0 -> node 1
         index 1 -> node 2
         index 2 -> node 3  <-- return this

Output: Node(3)
```

```
List: 1 -> 2 -> 3 -> 4

Pass 1: count = 4
Middle index = 4 / 2 = 2

Pass 2: index 0 -> node 1
         index 1 -> node 2
         index 2 -> node 3  <-- return this (second middle)

Output: Node(3)
```

### Approach 2: Optimal -- Slow and Fast Pointers (Tortoise & Hare)
**Intuition:** Move `slow` one step and `fast` two steps. When `fast` reaches the end, `slow` is at the middle.

**Steps:**
1. Initialize `slow = head`, `fast = head`.
2. While `fast != null` and `fast.next != null`: move `slow` one step, `fast` two steps.
3. Return `slow`.

**BUD Analysis:**
- **B**ottleneck: Must visit O(n) nodes -- can't avoid this.
- **U**nnecessary work: Brute force traverses twice. Two-pointer does one pass.
- **D**uplicate work: Brute force revisits nodes. Two-pointer doesn't.

**Dry Run Trace:**
```
List: 1 -> 2 -> 3 -> 4 -> 5

         slow  fast
Start:    1     1
Step 1:   2     3
Step 2:   3     5
Step 3:   --    fast.next is null, STOP

Return slow = Node(3) ✓
```

```
List: 1 -> 2 -> 3 -> 4

         slow  fast
Start:    1     1
Step 1:   2     3
Step 2:   3     null (fast went past end)

fast == null, STOP
Return slow = Node(3) ✓  (second middle)
```

### Approach 3: Best -- Same as Optimal
The slow/fast pointer approach IS the best. O(n) time and O(1) space is optimal since you must read every node at least once.

---

## 4. COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why |
|----------|------|-------|-----|
| Brute Force | O(n) | O(1) | Two passes of n nodes = 2n = O(n) |
| Slow/Fast Pointer | O(n) | O(1) | One pass -- fast pointer traverses n nodes, slow traverses n/2 |

*"Both are O(n), but the two-pointer approach is strictly faster in practice (one pass vs two) and more elegant. The constant factor matters."*

---

## 5. EDGE CASES & MISTAKES

| Edge Case | What Happens | How to Handle |
|-----------|-------------|---------------|
| Single node `[1]` | fast = head, fast.next = null, loop doesn't execute | Return head immediately |
| Two nodes `[1, 2]` | slow moves to 2, fast becomes null | Return node 2 (second middle) |
| Odd length `[1,2,3]` | Exact middle exists | Return node 2 |
| Even length `[1,2,3,4]` | Two middles: 2 and 3 | Return node 3 (second middle) |

**Common Mistakes:**
- Using `while fast.next != null and fast.next.next != null` -- this returns the FIRST middle for even-length lists, not the second.
- Null pointer exception by not checking `fast != null` before `fast.next`.
- Confusing 0-indexed vs 1-indexed middle calculation in brute force.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| **Why not store in array and index?** | That uses O(n) extra space. The two-pointer approach uses O(1). |
| **What if you want the first middle?** | Change condition to `while fast.next != null and fast.next.next != null`. |
| **Follow-up: Delete the middle node** | Find middle, then adjust pointers to skip it. Need reference to node before middle. |
| **Follow-up: Split list at middle** | Find middle, break the link. First half ends at middle, second half starts at middle.next. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Linked List Cycle (LC #141) | Same slow/fast pointer technique |
| Cycle Start (LC #142) | Extends tortoise & hare with math |
| Reorder List (LC #143) | Step 1 is finding the middle |
| Sort List (LC #148) | Merge sort on LL needs middle for splitting |
| Palindrome Linked List (LC #234) | Find middle, reverse second half, compare |

---

## Real-World Use Case
**Load balancer health checks:** AWS Elastic Load Balancer maintains a list of backend servers. Finding the middle node (via the slow/fast pointer technique) is analogous to identifying the median-latency server for smart routing decisions, enabling balanced traffic distribution across instances.
