# Flattening a Linked List

> **Step 06.6.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Given a linked list where every node has two pointers:
1. **`next`** -- points to the next node in the main horizontal list.
2. **`bottom`** -- points to a sub-list hanging below that node.

Each sub-list (following `bottom` pointers) is **sorted**. Flatten the entire structure into a single sorted linked list using `bottom` pointers. The `next` pointers of all nodes in the result should be `null`.

**GeeksForGeeks Classic Problem**

### Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| `5->10->19->28` with bottom lists `5->[7,8,30]`, `10->[20]`, `19->[22,50]`, `28->[35,40,45]` | `5->7->8->10->19->20->22->28->30->35->40->45->50` | All bottom lists merged into one sorted list. |

### Constraints
- Number of nodes: `[0, 200]`
- `1 <= Node.data <= 10^5`
- Each sub-list is sorted in non-decreasing order
- The main list (following `next`) may or may not be sorted

### Real-Life Analogy
Imagine a multi-floor building directory. Each floor has a sorted list of room numbers going downward. You need to merge all floors into one master sorted directory. You start from the rightmost floor and keep merging pairs of sorted lists, working your way left, like a tournament bracket for sorted sequences.

### 3 Key Observations
1. **"aha" -- each bottom list is already sorted:** This means we can efficiently merge two bottom lists in O(m+n) time, just like merge sort's merge step.
2. **"aha" -- merge from right to left:** Start with the rightmost two lists, merge them, then merge the result with the next list to the left. This avoids re-merging already-merged data.
3. **"aha" -- this is merge sort's merge step on k lists:** The structure is k sorted lists that need to be merged. We can either merge pairwise or use a min-heap.

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why This DS?
- **Two-pointer merge (like merge sort):** Since each bottom list is sorted, merging two lists is O(m+n). We repeatedly merge pairs.
- A min-heap could merge all k lists simultaneously in O(N log k), but the recursive pairwise approach is simpler and preferred in interviews for this problem.

### Pattern Recognition Cue
"Multiple sorted lists to merge" = merge sort's merge step. The recursive right-to-left approach mimics merge sort's divide-and-conquer strategy.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Collect All Values, Sort, Rebuild
**Intuition:** Traverse the entire structure, collect all node values into an array, sort the array, and build a new linked list from it.

**Steps:**
1. Traverse all nodes (both `next` and `bottom` pointers), collect values into a list.
2. Sort the list.
3. Build a new linked list using `bottom` pointers from the sorted values.

**BUD Transition:** Sorting is O(N log N) where N is total nodes. We can exploit the fact that sub-lists are already sorted to achieve O(N * k) or O(N log k).

**Dry Run:** For the example:
```
Collect: [5,7,8,30, 10,20, 19,22,50, 28,35,40,45]
Sort:    [5,7,8,10,19,20,22,28,30,35,40,45,50]
Build:   5->7->8->10->...->50 (using bottom pointers)
```

| Metric | Value |
|--------|-------|
| Time   | O(N log N) where N = total nodes |
| Space  | O(N) -- array of all values |

---

### Approach 2: Optimal -- Iterative Merge from Right
**Intuition:** Start with the rightmost list. Merge it with the second-rightmost. Take the result and merge with the next list to the left. Continue until all lists are merged. Each merge of two sorted lists is O(m+n).

**Steps:**
1. Start from the last node in the `next` chain.
2. Merge the last two bottom-lists into one sorted list.
3. Move one step left, merge again.
4. Repeat until the head is processed.
5. Return the merged head.

**BUD Transition from Brute:** Exploits the sorted property of each sub-list. Avoids the global sort.

**Dry Run:**
```
Lists: [5,7,8,30] -> [10,20] -> [19,22,50] -> [28,35,40,45]
Merge [19,22,50] + [28,35,40,45] = [19,22,28,35,40,45,50]
Merge [10,20] + [19,22,28,35,40,45,50] = [10,19,20,22,28,35,40,45,50]
Merge [5,7,8,30] + [10,19,20,22,28,35,40,45,50] = [5,7,8,10,19,20,22,28,30,35,40,45,50]
```

| Metric | Value |
|--------|-------|
| Time   | O(N * k) where k = number of top-level nodes, N = total nodes |
| Space  | O(1) -- in-place merge (O(k) if using recursion stack) |

---

### Approach 3: Best -- Recursive Merge from Right
**Intuition:** Same pairwise merge logic but expressed recursively. Recursively flatten from the right, then merge the current list with the already-flattened right portion. Elegant and clean.

**Steps:**
1. Base case: if `head` is null or `head.next` is null, return `head`.
2. Recursively flatten `head.next`.
3. Merge `head`'s bottom list with the flattened result.
4. Return the merged list.

| Metric | Value |
|--------|-------|
| Time   | O(N * k) |
| Space  | O(k) -- recursion stack depth |

---

## 4. COMPLEXITY INTUITIVELY

- **Time O(N * k):** In the worst case, each of the k merge operations processes up to N elements total. More precisely, each element is involved in at most k merges, but the total work across all merges is bounded by O(sum of all list lengths * number of merges involving that element).
- **Space O(1) iterative / O(k) recursive:** The merge is in-place (rearranging pointers). Recursion uses O(k) stack frames where k = number of top-level nodes.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | Expected Output | Why It Trips People |
|-----------|-----------------|---------------------|
| `null` | `null` | Empty input |
| Single node, no bottom | That node | Nothing to merge |
| Single column (one top node with bottom list) | Same list | No `next` nodes to merge |
| All values equal | All nodes in a single chain | Merge should handle equal values |

**Common Mistakes:**
- Forgetting to set `next = null` for all nodes in the result (the result uses only `bottom` pointers).
- Modifying the `next` pointer chain before processing all nodes (lose access to remaining lists).
- Not handling the case where one list is exhausted before the other during merge.

---

## 6. INTERVIEW LENS (UMPIRE)

### How to Present
1. **Understand:** "I have k sorted vertical lists connected horizontally. I need to merge them into one sorted vertical list."
2. **Match:** "This is merging k sorted lists -- I can merge pairwise from right to left."
3. **Plan:** Recursive flatten: flatten(head.next) first, then merge head's bottom list with the result.
4. **Implement:** Write the merge helper and the recursive flatten.
5. **Review:** Trace through the example showing each merge step.
6. **Evaluate:** O(N*k) time, O(k) space for recursion.

### Follow-Up Questions
- *"Can you do better than O(N*k)?"* -- Use a min-heap to merge all k lists in O(N log k). Same as Merge K Sorted Lists.
- *"What if the bottom lists are not sorted?"* -- Must sort each first, or collect all and sort globally.
- *"Can you do it iteratively?"* -- Yes, iterate through the `next` chain from right to left, merging as you go.

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prereq** | Merge Two Sorted Lists (LC #21) -- the merge helper |
| **Same Pattern** | Merge K Sorted Lists (LC #23) -- same concept with a heap |
| **Harder** | Flatten a Multilevel Doubly Linked List (LC #430) |
| **Unlocks** | Sort List (LC #148) -- merge sort on linked list |
