# Kth Node From Middle

> **Batch 4 of 12** | **Topic:** Linked List (Medium Problems) | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given a singly linked list and an integer `k`, find the **kth node from the middle towards the beginning** of the list. If the list has `n` nodes and the middle node is at 0-based index `m = (n-1)/2` (integer division, picking the left-middle for even-length lists), return the node at index `m - k`. If `m - k < 0`, return `-1` (invalid).

**Clarification on "middle":** For a list of length `n`, the middle index is `(n-1)/2`. For n=5 (indices 0-4), middle is at index 2. For n=6 (indices 0-5), middle is at index 2 (left of center).

### Examples

| # | List | n | Middle Index | k | Target Index | Output |
|---|------|---|-------------|---|-------------|--------|
| 1 | 1->2->3->4->5 | 5 | 2 | 0 | 2 | 3 |
| 2 | 1->2->3->4->5 | 5 | 2 | 1 | 1 | 2 |
| 3 | 1->2->3->4->5 | 5 | 2 | 2 | 0 | 1 |
| 4 | 1->2->3->4->5 | 5 | 2 | 3 | -1 | -1 (invalid) |
| 5 | 1->2->3->4 | 4 | 1 | 0 | 1 | 2 |
| 6 | 1->2->3->4 | 4 | 1 | 1 | 0 | 1 |
| 7 | 5 | 1 | 0 | 0 | 0 | 5 |
| 8 | 5 | 1 | 0 | 1 | -1 | -1 (invalid) |

### Real-Life Analogy
Imagine you are standing in the **middle of a queue** of people. Someone asks you: "Who is standing 3 places behind you (towards the entrance)?" You know your position, so you simply count backwards. If there are not enough people behind you, you say "nobody." This problem is exactly that -- find the middle, then step `k` places backward.

### Three Key Observations (the "Aha!" Moments)
1. **Two-step problem** -- First find the middle, then find the kth node before it. These can be decomposed.
2. **Middle index = (n-1)/2** -- You need either the length or the slow/fast pointer technique to locate the middle.
3. **"Before middle" means a lower index** -- Since linked lists are forward-only, you cannot walk backward. You must compute the target index `m - k` and walk forward to it from the head.

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Two passes: count length, compute target, walk to it | Counter |
| Optimal | Store nodes in array, direct index access | ArrayList / array |
| Best | Single computation: target = (n-1)/2 - k, walk to it | Counter only |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Three Passes

**Intuition:** Pass 1 counts the length. Compute middle index `m = (n-1)/2` and target `t = m - k`. If `t < 0`, return -1. Pass 2 walks to index `t`.

**BUD Analysis:**
- **B**ottleneck: Two traversals of the list.
- **U**nnecessary work: Walking from head to target could be combined with counting if we are clever.
- **D**uplicate work: Both passes traverse the same prefix.

**Steps:**
1. Traverse the list, count `n`.
2. Compute `m = (n - 1) / 2`, `target = m - k`.
3. If `target < 0`, return -1.
4. Walk `target` steps from head, return that node's value.

**Dry-Run Trace** (list: 1->2->3->4->5, k=1):
```
Pass 1: n = 5
m = (5-1)/2 = 2
target = 2 - 1 = 1
Pass 2: head(1) -> step 1 -> node(2)
Return: 2
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- two passes |
| Space | O(1) |

---

### Approach 2: Optimal -- Array Storage

**Intuition:** Traverse the list once and store all node values in an array. Then directly index `array[(n-1)/2 - k]`.

**BUD Analysis:**
- **B**ottleneck: Single pass for building array, O(1) for indexing.
- Trade-off: Uses O(n) space but gives instant index access.

**Steps:**
1. Traverse the list, push each value into an array.
2. `n = array.length`, `m = (n-1)/2`, `target = m - k`.
3. If `target < 0`, return -1.
4. Return `array[target]`.

**Dry-Run Trace** (list: 1->2->3->4->5, k=1):
```
array = [1, 2, 3, 4, 5]
n=5, m=2, target=1
array[1] = 2
Return: 2
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- single pass |
| Space | O(n) -- array storage |

---

### Approach 3: Best -- Two-Pass with Minimal Computation

**Intuition:** We realize that the target index is `(n-1)/2 - k`. After counting `n` in the first pass, we compute this index directly and walk to it in the second pass. This is the same as Approach 1 but written cleanly with no wasted operations. It is O(n) time and O(1) space -- theoretically optimal for a singly linked list since you cannot avoid at least one full traversal to find `n`.

**Steps:**
1. Count `n` in one pass.
2. `target = (n - 1) / 2 - k`.
3. If `target < 0`, return -1.
4. Walk `target` steps from head, return value.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(1) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n) is the minimum:** You must know `n` (or equivalently, the middle) before you can compute the target position. Finding the middle requires visiting at least half the nodes, and the target could be anywhere in the first half. In the worst case you traverse the entire list.

**Space comparison:** The array approach trades O(n) space for conceptual simplicity. The two-pass approach achieves O(1) space because it only stores counters, not node values.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| k = 0 | Middle node itself | Forgetting this means returning the middle directly |
| k > middle index | -1 | Target index goes negative |
| Single node, k=0 | Node value | Middle is index 0, target is 0 |
| Single node, k=1 | -1 | Target = -1, invalid |
| Even-length list | Left-middle | Must be clear on which middle to use |
| Empty list | -1 | No nodes to process |

**Common Mistakes:**
- Using `n/2` instead of `(n-1)/2` for the middle index.
- Off-by-one: walking `target` steps but starting the count at 1 instead of 0.
- Not checking `target < 0` before attempting to traverse.

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** Combines two fundamental skills -- finding the middle of a linked list and index-based traversal. Tests methodical decomposition of a multi-step problem.

**Follow-ups to expect:**
- "What if you want kth from middle towards the end?" -> Target = m + k, check bounds against n.
- "Can you do it with a slow/fast pointer instead of counting?" -> Yes, slow finds middle, but you still need to walk backward which requires counting anyway in a singly linked list.
- "What about a doubly linked list?" -> Find middle with slow/fast, then walk backward k steps directly.

**Talking points:**
- Discuss why singly linked lists cannot efficiently go backward.
- Mention that converting to an array solves it in one pass but costs O(n) space.

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Middle of Linked List | First half of this problem |
| Kth Node From End | Similar concept but from the other direction |
| Nth Node From End (LC #19) | Two-pointer gap technique, related flavor |
| Remove Middle Element | Find middle + deletion instead of kth offset |
