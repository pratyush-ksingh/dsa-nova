# Sort Binary Linked List

> **Batch 4 of 12** | **Topic:** Linked List (Medium Problems) | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given a singly linked list where each node's value is either **0** or **1**, sort the list so that all 0s come before all 1s. Return the head of the sorted list.

### Examples

| # | Input List | Output | Explanation |
|---|-----------|--------|-------------|
| 1 | 1 -> 0 -> 1 -> 0 | 0 -> 0 -> 1 -> 1 | Two 0s grouped first, then two 1s |
| 2 | 0 -> 0 -> 0 | 0 -> 0 -> 0 | Already sorted |
| 3 | 1 -> 1 -> 1 | 1 -> 1 -> 1 | All same value |
| 4 | 1 | 1 | Single element |
| 5 | (empty) | (empty) | Nothing to sort |
| 6 | 0 -> 1 -> 0 -> 1 -> 0 | 0 -> 0 -> 0 -> 1 -> 1 | General interleaved case |

### Real-Life Analogy
Imagine a row of **light switches** -- each is either ON (1) or OFF (0). You want to slide all OFF switches to the left and all ON switches to the right. You could either (a) count how many are OFF, then just overwrite them in order, or (b) physically collect OFF ones into one chain and ON ones into another, then join the chains.

### Three Key Observations (the "Aha!" Moments)
1. **Only two distinct values** -- This is not a general sorting problem. With just 0 and 1, comparison-based O(n log n) is overkill. Counting sort runs in O(n).
2. **Counting suffices for value overwrite** -- Count the 0s, then walk the list filling the first `count0` nodes with 0, the rest with 1.
3. **Node rearrangement preserves satellite data** -- If nodes carry additional fields beyond the value, you cannot just overwrite. Splitting into two sub-lists and reconnecting solves this without touching values.

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Count 0s and 1s, overwrite values | Counter variable |
| Optimal | Two dummy lists for 0-nodes and 1-nodes, reconnect | Two dummy head nodes |
| Best | Single-pass split with clean edge-case handling | Two dummy heads + tail pointers |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Count and Overwrite

**Intuition:** Since there are only two possible values, count how many 0s exist. Then overwrite the first `count0` nodes with 0 and the remainder with 1.

**BUD Analysis:**
- **B**ottleneck: Two full passes over the list -- O(2n) = O(n).
- **U**nnecessary work: Overwrites nodes that already have the correct value.
- **D**uplicate work: None.

**Steps:**
1. Traverse the list, count nodes with value 0 (`count0`).
2. Traverse again: for the first `count0` nodes set val=0, for the rest set val=1.

**Dry-Run Trace** (list: 1->0->1->0):
```
Pass 1: count0 = 2
Pass 2:
  Node 1: count0=2 > 0 -> val=0, count0=1
  Node 2: count0=1 > 0 -> val=0, count0=0
  Node 3: count0=0     -> val=1
  Node 4: count0=0     -> val=1
Result: 0 -> 0 -> 1 -> 1
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- two passes |
| Space | O(1) |

---

### Approach 2: Optimal -- Split into Two Lists

**Intuition:** Create two sub-lists using dummy heads -- one collects all 0-nodes, the other all 1-nodes. After traversing, connect the 0-list tail to the 1-list head. This avoids modifying node values.

**BUD Analysis:**
- Single pass, no value modification.
- Preserves original node identity (important when nodes have extra data).

**Steps:**
1. Create `dummy0`, `dummy1` with tail pointers `tail0`, `tail1`.
2. For each node in the original list:
   - If `val == 0`: `tail0.next = node`, advance `tail0`.
   - If `val == 1`: `tail1.next = node`, advance `tail1`.
3. Connect: `tail0.next = dummy1.next`.
4. Terminate: `tail1.next = null` (prevent cycle).
5. Return `dummy0.next`.

**Dry-Run Trace** (list: 1->0->1->0):
```
Node(1): -> 1-list.   0-list: []     1-list: [1]
Node(0): -> 0-list.   0-list: [0]    1-list: [1]
Node(1): -> 1-list.   0-list: [0]    1-list: [1,1]
Node(0): -> 0-list.   0-list: [0,0]  1-list: [1,1]
Connect tail of 0-list to head of 1-list
Result: 0 -> 0 -> 1 -> 1
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- single pass |
| Space | O(1) |

---

### Approach 3: Best -- Clean Single-Pass Split

**Intuition:** Same algorithm as Approach 2, but with meticulous edge-case handling in compact code. The critical detail: when all nodes are 1 (0-list is empty), return `dummy1.next` directly.

**Steps:**
1. Same split logic as Approach 2.
2. After the loop, if `dummy0.next == null`, return `dummy1.next`.
3. Otherwise, `tail0.next = dummy1.next`, `tail1.next = null`, return `dummy0.next`.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is the floor:** Every node must be inspected at least once -- you cannot know a node's value without visiting it. So Omega(n) is the lower bound and all our approaches achieve it.

**Why O(1) extra space:** We rearrange existing nodes (or overwrite values). No new nodes are created. The two dummy nodes are constant overhead.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Empty list | null | Dereferencing null head |
| All 0s | Unchanged | 1-list is empty, connecting to null must work |
| All 1s | Unchanged | 0-list is empty, must return 1-list head |
| Single node | Unchanged | Trivially sorted |
| Two nodes [1,0] | [0,1] | Simplest non-trivial swap |

**Common Mistakes:**
- Forgetting to set `tail1.next = null` -- creates a cycle when the last original node was a 0.
- Returning `dummy0.next` without checking if the 0-list is empty (returns null instead of the 1-list).
- In the count approach: off-by-one in the second pass.

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** Tests exploitation of constraints (binary values), linked-list pointer manipulation, and awareness of data integrity (overwrite vs. rearrange).

**Follow-ups to expect:**
- "What if values are 0, 1, 2?" -> Three dummy lists (Dutch National Flag on linked list).
- "Nodes carry extra data beyond 0/1?" -> Must use rearrangement, not overwrite.
- "Can you do it in-place for an array of 0s and 1s?" -> Two-pointer swap from both ends.

**Talking points:**
- Mention counting sort and its O(n+k) nature for small k.
- Discuss stability: the split approach preserves relative order within each group.

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Sort List (0, 1, 2) | Extension to 3 values, same split technique with 3 lists |
| Partition List (LC #86) | Split around a pivot value, identical two-list pattern |
| Segregate Even/Odd Nodes | Binary classification of nodes, same approach |
| Dutch National Flag | Array version of the same idea |
