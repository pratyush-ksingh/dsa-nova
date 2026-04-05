# Rotate a Linked List

> **Step 06.6.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given the `head` of a linked list, rotate the list to the right by `k` places. In one rotation, the last node moves to the front.

**LeetCode #61**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,2,3,4,5]`, k=2 | `[4,5,1,2,3]` | Rotate right 1: `[5,1,2,3,4]`. Rotate right 2: `[4,5,1,2,3]`. |
| `[0,1,2]`, k=4 | `[2,0,1]` | k=4 is equivalent to k=1 since len=3 (4%3=1). |
| `[1]`, k=0 | `[1]` | No rotation needed. |

### Constraints
- Number of nodes: `[0, 500]`
- `-100 <= Node.val <= 100`
- `0 <= k <= 2 * 10^9`

### Real-Life Analogy
Think of a necklace of beads on a circular string. "Rotating right by k" means you pick a new starting bead k positions earlier. You don't move individual beads -- you just change where you unclasp the necklace. The key insight is to make it circular first, then unclasp at the right spot.

### 3 Key Observations
1. **"aha" -- k can be huge:** k can be up to 2 billion, but rotating by the list length is a no-op. So `k = k % len` eliminates redundant full rotations.
2. **"aha" -- right rotation = break at (len-k):** Rotating right by k means the last k nodes move to the front. The break point is at position `len - k` from the head.
3. **"aha" -- make circular, then break:** Connect tail to head (circular), then walk `len - k` steps from the old tail to find the new tail. Break the circle there.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **Linked list pointer manipulation:** No extra data structure needed. Just connect tail->head (make circular), walk to the break point, and disconnect.
- The circular trick avoids the need to move nodes one by one.

### Pattern Recognition Cue
When rotating a linked list, think "make circular, then unclasp." This is a common pattern for rotation problems on linked lists.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Rotate One at a Time
**Intuition:** Perform k individual right-rotations. Each rotation: find the second-to-last node, move the last node to the front.

**Steps:**
1. For each rotation (k times):
   - Traverse to the second-to-last node.
   - Detach the last node.
   - Set last.next = head, update head = last.
2. Return the new head.

**BUD Transition:** Each rotation takes O(n) to find the tail. With k rotations, total is O(n*k) which is way too slow for k up to 2*10^9.

**Dry Run:** `[1,2,3,4,5]`, k=2
```
Rotation 1: tail=5, detach -> 5->1->2->3->4. head=5
Rotation 2: tail=4, detach -> 4->5->1->2->3. head=4
Result: [4,5,1,2,3]
```

| Metric | Value |
|--------|-------|
| Time   | O(n * k) |
| Space  | O(1)  |

---

### Approach 2: Optimal -- Find Length, Make Circular, Break
**Intuition:** Compute the length, reduce k modulo length, make the list circular, then walk `len - k` steps from the tail to find the new tail. Break the circle.

**Steps:**
1. If `head` is null or `head.next` is null or `k == 0`, return head.
2. Traverse to find `length` and the tail node.
3. `k = k % length`. If `k == 0`, return head (no rotation needed).
4. Connect `tail.next = head` (make circular).
5. Walk `length - k` steps from tail to find the new tail.
6. New head = newTail.next. Set newTail.next = null.
7. Return new head.

**BUD Transition from Brute:** Reduced from O(n*k) to O(n) by computing length and using modular arithmetic.

**Dry Run:** `[1,2,3,4,5]`, k=2
```
length=5, tail=node(5)
k = 2 % 5 = 2
Make circular: 5->1->2->3->4->5->...
Walk len-k = 3 steps from tail:
  tail(5)->1->2->3
newTail = node(3), newHead = node(4)
Break: 3.next = null
Result: 4->5->1->2->3
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

### Approach 3: Best -- Same Approach, Single Clean Pass
**Intuition:** Same algorithm as Optimal (it is already optimal). Cleanest implementation with minimal variables.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(n):** One pass to find length + tail (O(n)). One pass to walk to break point (O(n)). Total = O(n).
- **Space O(1):** Only a few pointer variables. No extra data structures.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `null` (empty) | `null` | Must handle empty list |
| `[1]`, k=99 | `[1]` | Single node, any k is a no-op |
| k=0 | Original list | No rotation |
| k = length | Original list | Full rotation = no-op |
| k > length | k % length | Must reduce k |

**Common Mistakes:**
- Forgetting `k = k % length` (TLE for huge k).
- Off-by-one: walking `len - k` vs `len - k - 1` steps.
- Not handling k=0 after modulo (would incorrectly rotate).
- Forgetting to break the circular link (infinite loop).

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I need to move the last k nodes to the front."
2. **Match:** "Make circular, break at the right point."
3. **Plan:** Find length, k%=len, circular trick, walk len-k from tail.
4. **Implement:** Write the solution.
5. **Review:** Trace through [1,2,3,4,5] k=2.
6. **Evaluate:** O(n) time, O(1) space.

### Follow-Up Questions
- *"What about rotating left by k?"* -- Same approach, but walk `k` steps instead of `len - k`.
- *"What if it were an array?"* -- Reverse-based approach: reverse all, reverse first k, reverse rest.
- *"Can you do it without making the list circular?"* -- Yes, find the (len-k)th node directly from head, adjust pointers.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Reverse Linked List (LC #206) |
| **Same Pattern** | Rotate Array (LC #189) -- similar rotation logic on array |
| **Harder** | Reverse Nodes in k-Group (LC #25) |
| **Unlocks** | Split Linked List in Parts (LC #725) |
