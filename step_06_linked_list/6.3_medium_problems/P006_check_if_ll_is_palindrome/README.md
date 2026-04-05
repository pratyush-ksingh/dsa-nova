# Check if Linked List is Palindrome

> **Step 06.6.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given the head of a singly linked list, return `true` if it is a **palindrome** (reads the same forwards and backwards), and `false` otherwise.

**LeetCode #234**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `1->2->2->1` | `true` | Reads same from both ends |
| `1->2` | `false` | `1 != 2` |
| `1` | `true` | Single element is always a palindrome |
| `1->2->3->2->1` | `true` | Symmetric around center |
| `1->2->3->4->5` | `false` | Not symmetric |

### Constraints
- Number of nodes: `1 <= n <= 10^5`
- Node values: `0 <= val <= 9`

### Real-Life Analogy
Imagine a train where each car has a number painted on it. You want to check if the sequence of numbers reads the same from the front as from the back. The simplest check is to write down all numbers, then compare the list with its reverse.

### 3 Key Observations
1. **"aha" -- arrays make palindrome checking trivial:** Once values are in an array, two-pointer comparison is O(n). The catch is O(n) extra space.
2. **"aha" -- reverse the second half:** We can compare without extra space by finding the middle, reversing the back half, then walking both halves simultaneously.
3. **"aha" -- restoration matters:** After checking, reverse the second half back to restore the original list. Good practice (and expected in interviews).

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **ArrayList (brute)**: simple, O(n) space, O(1) per comparison.
- **In-place reverse (optimal)**: O(1) space using slow/fast pointer to find mid and pointer reversal.

### Pattern Recognition Cue
Any "palindrome in linked list" problem has the same three-step template: find middle, reverse second half, compare. Memorize this pattern.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Copy to Array
**Intuition:** The array random-access property makes palindrome checking trivial. Collect all values, use two pointers moving from both ends inward.

**Steps:**
1. Traverse the list, appending each value to a list/array.
2. Use left=0, right=n-1 pointers.
3. While left < right: if `vals[left] != vals[right]` return false; advance both.
4. Return true.

**Dry Run:** `1->2->2->1`
```
vals = [1, 2, 2, 1]
l=0, r=3: 1==1, l=1, r=2
l=1, r=2: 2==2, l=2, r=1, l>=r -> true
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

### Approach 2: Optimal -- Find Mid, Reverse Second Half, Compare
**Intuition:** Use the "hare and tortoise" technique to find the midpoint. Reverse the second half in-place. Compare node by node. Restore the list.

**Steps:**
1. **Find middle:** slow/fast pointers. After loop, `slow` is at end of first half.
2. **Cut and reverse:** set `slow.next = null`, reverse from `slow.next` onward.
3. **Compare:** two pointers `p1=head`, `p2=reversed_second`. Walk until `p2` is null. Check values.
4. **Restore:** reverse the second half again, reconnect to `slow.next`.
5. Return the comparison result.

**BUD Transition from Brute:** Eliminated the O(n) extra array. Only O(1) pointers.

**Dry Run:** `1->2->2->1`
```
Find mid: slow stops at node(2) [first half end]
Cut: first half = 1->2, second half start = 2->1
Reverse second: 1->2
Compare: 1==1, 2==2 -> true
Restore: second half = 2->1, reconnect
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Same as Optimal with Explicit Restoration
**Intuition:** Identical to Optimal. Written with explicit, named restoration steps to make it 100% interview-ready. The list is always left in its original state.

**Steps:**
1-4: Identical to Optimal.
5. Explicitly reverse the second half back and reconnect.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** One pass to find mid (n/2 steps), one pass to reverse (n/2 steps), one pass to compare (n/2 steps), one pass to restore (n/2 steps). Total = 2n steps.
- **Space O(1):** Only pointer variables. No auxiliary data structure.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected | Why It Trips People |
|-----------|----------|---------------------|
| Single node | `true` | Base case must return before find-mid |
| Two nodes, equal | `true` | Even-length; slow ends at first node |
| Two nodes, unequal | `false` | Same |
| Odd length `1->2->1` | `true` | Middle node is skipped in comparison |
| Even length `1->2->3->1` | `false` | Second half = 3->1 reversed = 1->3; compare with 1->2: 1==1 but 2!=3 |

**Common Mistakes:**
- Not handling the case where `slow.next` after finding mid differs for odd vs even length -- the condition `fast.next && fast.next.next` ensures slow lands correctly.
- Forgetting to set `slow.next = null` before reversing, which creates a loop in the second half.
- Not restoring the list (acceptable in interviews where mutation is allowed, but bad practice).
- Comparing while `p1 != null` instead of `p2 != null` -- p2 (shorter half) is the correct termination condition.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "Check if a linked list is a palindrome."
2. **Match:** "Three-step pattern: find mid, reverse second half, compare. O(1) space."
3. **Plan:** Slow/fast for mid. Reverse second half. Walk both halves simultaneously.
4. **Implement:** Show the reversal helper separately.
5. **Review:** Trace `1->2->2->1`.
6. **Evaluate:** O(n) time, O(1) space.

### Follow-Up Questions
- *"What if we can't modify the list?"* -- Must use O(n) extra space (array or recursion stack).
- *"Can you solve it recursively?"* -- Yes: use the call stack as an implicit stack for the first half. O(n) space (stack frames). Elegant but space-heavy.
- *"What if the list has cycles?"* -- Undefined input; assume no cycles.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Reverse a Linked List (LC #206) |
| **Prereq** | Middle of the Linked List (LC #876) |
| **Same Pattern** | Reorder List (LC #143) -- same find-mid + reverse technique |
| **Same Concept** | Valid Palindrome (LC #125) -- array version |
| **Harder** | Palindrome Linked List with Random Pointer |
