# Length of Loop in Linked List

> **Step 06.6.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given the head of a linked list, determine if a cycle exists. If it does, return the **length of the cycle** (number of nodes in the loop). If no cycle exists, return **0**.

**GFG / Striver A2Z Sheet problem.**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `1->2->3->4->5->` (tail connects to node 2) | `4` | Cycle: 2->3->4->5->2, length = 4 |
| `1->2->3->4->5->` (tail connects to node 1) | `5` | Full cycle, length = 5 |
| `1->2->3` (no cycle) | `0` | No loop |
| `1->` (self-loop) | `1` | Single node points to itself |

### Constraints
- Number of nodes: `0 <= n <= 10^4`
- Node values: `-10^5 <= val <= 10^5`
- If cycle exists, pos >= 0 (0-indexed)

### Real-Life Analogy
Imagine a circular running track where some runners enter and never leave. To find how long the loop is, you send two scouts -- one slow, one fast. When they meet, you know they're on the loop. Then you send one scout around until it comes back to measure the distance.

### 3 Key Observations
1. **"aha" -- detect first, then measure:** Floyd's algorithm detects the cycle. The measurement is a separate step: from the meeting point, walk until you return to it.
2. **"aha" -- meeting point is inside the loop:** The slow/fast pointer meeting point is always somewhere in the cycle, not at the entry point. From there, simply count steps back to the same node.
3. **"aha" -- step difference tells you the step number:** In the HashMap approach, when you re-visit a node at step `s`, the loop length is `s - first_visit_step`.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **HashMap (brute)**: O(n) space, maps node reference to step number. Simple and direct.
- **Two pointers (Floyd's)**: O(1) space. Slow pointer moves 1 step, fast moves 2. They meet inside the cycle.

### Pattern Recognition Cue
Any linked-list cycle problem starts with Floyd's cycle detection. For loop length, add a counting loop from the meeting point.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- HashMap of Visited Nodes
**Intuition:** Store each node along with the step number in a HashMap. The moment we visit a node already in the map, we found the cycle entry. The cycle length = current step - step stored for that node.

**Steps:**
1. Traverse the list, maintaining a step counter.
2. For each node: if it's in `visited`, return `current_step - visited[node]`.
3. Otherwise store `visited[node] = current_step` and advance.
4. If traversal ends (null), return 0.

**Dry Run:** List `1->2->3->4->5->2` (tail to node at index 1):
```
step=0: visit node(1), visited={node1:0}
step=1: visit node(2), visited={node1:0, node2:1}
step=2: visit node(3), ...
step=3: visit node(4), ...
step=4: visit node(5), ...
step=5: visit node(2) -- already in visited! return 5-1=4
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

### Approach 2: Optimal -- Floyd's Detection + Count from Meeting Point
**Intuition:** Phase 1 uses slow/fast pointers to detect the meeting point (inside the cycle). Phase 2 counts from the meeting point back to itself.

**Steps:**
1. **Phase 1 -- detect:** Move `slow` by 1, `fast` by 2. If they meet, a cycle exists.
2. **Phase 2 -- count:** From the meeting point, keep one pointer fixed and advance the other one step at a time until it returns. Count steps.
3. If Phase 1 ends (fast hits null), return 0.

**BUD Transition from Brute:** Eliminated the O(n) HashMap. Only two pointers needed.

**Dry Run:** `1->2->3->4->5->2`
```
Phase 1: slow: 1,2,3,4,5,2,3... fast: 1,3,5,2,4,... they meet at node(4) (or node(3))
Phase 2: from meeting, count steps until back to same node -> 4 steps
Result: 4
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Same Floyd's, Two-Phase Made Explicit
**Intuition:** Identical algorithm as Optimal, but written with the two phases separated by a boolean flag for interview clarity.

**Steps:**
1. Phase 1: detect cycle with a `hasCycle` flag.
2. Early return if no cycle.
3. Phase 2: from `meeting` node, walk `ptr` until it returns to `meeting`, counting steps.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** Floyd's Phase 1 takes at most O(n) steps to detect the cycle. Phase 2 takes at most O(cycle_length) <= O(n) steps.
- **Space O(1):** Only two pointers. No extra storage.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected | Why It Trips People |
|-----------|----------|---------------------|
| No cycle | `0` | Must check `fast != null && fast.next != null` |
| Single node, self-loop | `1` | Meeting point is the node itself; count = 1 |
| Full cycle (tail to head) | `n` | Meeting point anywhere; count wraps around all nodes |
| Empty list | `0` | Null head must be handled |

**Common Mistakes:**
- Returning from Phase 1 before incrementing count (off-by-one: count starts at 1 with `curr = slow.next`).
- Using `slow != fast` check in Phase 2 instead of `curr != slow` -- need to distinguish from the initial meeting point.
- Forgetting to check `fast.next != null` in the while condition (causes NullPointerException).

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "Detect cycle and return its length."
2. **Match:** "Floyd's cycle detection is canonical. Two phases: detect + count."
3. **Plan:** Phase 1 finds meeting point, Phase 2 counts from it.
4. **Implement:** Write both phases clearly.
5. **Review:** Trace the 1->2->3->4->5->2 example.
6. **Evaluate:** O(n) time, O(1) space.

### Follow-Up Questions
- *"How do you find the start of the loop, not just its length?"* -- After meeting, move one pointer to head; advance both by 1. They meet at the loop entry.
- *"Can fast move by 3 instead of 2?"* -- Yes, but then the meeting point analysis changes. Moving by 2 is simplest and most common.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Linked List Cycle I (LC #141) -- detect only |
| **Same Algorithm** | Linked List Cycle II (LC #142) -- find cycle entry |
| **Same Pattern** | Happy Number (LC #202) -- Floyd's on implicit linked list |
| **Harder** | Find Duplicate Number (LC #287) -- Floyd's on array |
