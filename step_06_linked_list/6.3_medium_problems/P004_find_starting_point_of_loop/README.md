# Find Starting Point of Loop

> **Step 06.6.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given the `head` of a linked list, return the node where the cycle begins. If there is no cycle, return `null`. A cycle exists if some node in the list can be reached again by continuously following the `next` pointer. Do **not** modify the linked list.

**LeetCode #142 -- Linked List Cycle II**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `[3,2,0,-4]`, tail connects to node 1 | Node with val `2` | The cycle starts at the node with value 2 (index 1). |
| `[1,2]`, tail connects to node 0 | Node with val `1` | The cycle starts at head (index 0). |
| `[1]`, no cycle | `null` | No cycle exists. |

### Constraints
- Number of nodes: `[0, 10^4]`
- `-10^5 <= Node.val <= 10^5`
- `pos` (the index where tail connects) is `-1` (no cycle) or a valid index

### Real-Life Analogy
Imagine two runners on a circular track that has a straight entrance path. One runs at 2x speed. They will eventually meet inside the loop. To find where the loop entrance is: after they meet, send one runner back to the starting line while the other stays at the meeting point. Both run at the same speed -- they will meet exactly at the loop entrance. This is Floyd's algorithm.

### 3 Key Observations
1. **"aha" -- slow/fast always meet inside loop:** If a cycle exists, a slow (1-step) and fast (2-step) pointer will always meet somewhere inside the cycle.
2. **"aha" -- distance math:** Let `d` = distance from head to cycle start, `k` = distance from cycle start to meeting point, `C` = cycle length. At meeting: slow traveled `d + k`, fast traveled `d + k + nC`. Since fast = 2*slow: `d + k = nC`, so `d = nC - k`. Moving `d` steps from the meeting point lands at the cycle start.
3. **"aha" -- two-pointer reset trick:** After meeting, reset one pointer to head. Move both one step at a time. They meet at the cycle start because both travel distance `d`.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **Two pointers (Floyd's):** The slow/fast pointer technique detects cycles in O(1) space. The mathematical property that `d = nC - k` allows finding the exact start node.
- A HashSet works but uses O(n) extra space.

### Pattern Recognition Cue
Whenever you need to detect or find properties of a cycle in a linked list, Floyd's tortoise-and-hare algorithm is the canonical approach. The "reset one pointer to head" trick for finding the cycle start is a classic follow-up.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- HashSet
**Intuition:** Traverse the list and store each visited node in a set. The first node you encounter that is already in the set is the cycle start.

**Steps:**
1. Initialize an empty `HashSet<ListNode>`.
2. Traverse the list from `head`.
3. For each node:
   - If node is in the set, return it (cycle start found).
   - Otherwise, add it to the set.
4. If you reach `null`, return `null` (no cycle).

**BUD Transition:** The HashSet uses O(n) space. Floyd's algorithm achieves O(1) space.

**Dry Run:** `[3,2,0,-4]`, tail->node1
```
visited = {}
3 -> add {3}
2 -> add {3,2}
0 -> add {3,2,0}
-4 -> add {3,2,0,-4}
next is node 2, already in set -> return node 2
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

### Approach 2: Optimal -- Floyd's Cycle Detection
**Intuition:** Use slow/fast pointers to find a meeting point inside the cycle. Then reset one pointer to head and advance both at speed 1. They will meet at the cycle start.

**Steps:**
1. `slow = head`, `fast = head`.
2. Move `slow` one step, `fast` two steps until they meet (or `fast` reaches null).
3. If `fast` is null or `fast.next` is null, return `null` (no cycle).
4. Reset `slow = head`. Keep `fast` at meeting point.
5. Move both one step at a time until they meet.
6. Return the meeting node (cycle start).

**BUD Transition from Brute:** Eliminated the HashSet. O(1) space.

**Dry Run:** `[3->2->0->-4->2...]`
```
Phase 1 (detect):
  slow: 3->2->0->-4
  fast: 3->0->-4->0   (wait, let me be precise)
  slow=3, fast=3
  slow=2, fast=0
  slow=0, fast=-4 (next is 2, then fast goes to 0)
  Hmm, let me index: nodes [0]=3, [1]=2, [2]=0, [3]=-4, -4.next=node[1]
  slow=node[0], fast=node[0]
  slow=node[1], fast=node[2]
  slow=node[2], fast=node[2] (fast: node[2]->node[3]->node[1]; next: node[1]->node[2])
  Wait: fast=node[0], step: node[0]->node[1]->node[2]. So fast=node[2].
  slow=node[1], fast=node[2]. Not equal.
  slow=node[2], fast goes node[2]->node[3]->node[1]. fast=node[1]. Not equal.
  slow=node[3], fast goes node[1]->node[2]->node[3]. fast=node[3]. MEET at node[3].

Phase 2 (find start):
  slow=head=node[0], fast=node[3]
  slow=node[1], fast=node[1] (fast: node[3]->node[1])
  MEET at node[1] which is value 2. Correct!
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Floyd's (Clean Implementation)
**Intuition:** Same Floyd's algorithm (it is provably optimal for O(1) space). Cleanest possible implementation with minimal code.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** Phase 1: slow travels at most `d + C` steps before meeting (where d = tail length, C = cycle length). Phase 2: both travel exactly `d` steps. Total is O(n).
- **Space O(1):** Only two pointer variables. No extra data structures.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `null` (empty list) | `null` | Must handle empty input |
| `[1]` no cycle | `null` | Single node, no cycle |
| `[1]` tail->node0 | Node with val 1 | Self-loop: cycle start is head |
| `[1,2]` tail->node0 | Node with val 1 | Cycle starts at head |
| Long tail, short cycle | Correct node | Phase 2 distance = tail length regardless of cycle size |

**Common Mistakes:**
- Not handling the no-cycle case (fast reaches null).
- Resetting the wrong pointer (must reset one to head, keep the other at meeting point).
- Confusing this with just cycle detection (LC #141) -- that only needs to detect, not find the start.
- Off-by-one: starting both pointers at head, not at head.next.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I need to find where a cycle starts, or return null if no cycle."
2. **Match:** "Floyd's cycle detection with the phase-2 trick to find the start."
3. **Plan:** Phase 1: slow/fast meet inside cycle. Phase 2: reset one to head, walk both at speed 1.
4. **Implement:** Write the two-phase solution.
5. **Review:** Trace through the example showing both phases.
6. **Evaluate:** O(n) time, O(1) space. Explain the math if asked.

### Follow-Up Questions
- *"Prove why the phase-2 trick works."* -- slow traveled d+k, fast traveled d+k+nC. Since fast=2*slow: d+k=nC, so d=nC-k. Starting from meeting point and walking d steps = walking nC-k steps = arriving at cycle start.
- *"What if you can modify the list?"* -- Mark visited nodes by negating values or setting a flag (not recommended, destructive).
- *"Find the cycle length."* -- After meeting in phase 1, keep one pointer still, advance the other counting steps until they meet again.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Linked List Cycle (LC #141) -- just detection |
| **Same Pattern** | Find the Duplicate Number (LC #287) -- Floyd's on array |
| **Harder** | Intersection of Two Linked Lists (LC #160) -- two-pointer meet trick |
| **Unlocks** | Happy Number (LC #202) -- cycle detection in number sequence |
