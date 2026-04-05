# Reverse Level Order Traversal

> **Batch 2 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a binary tree, return its **reverse level order traversal** -- the values of nodes visited level by level from **bottom to top**, left to right within each level. Return as a list of lists (each inner list represents one level).

*(InterviewBit)*

**Constraints:**
- `0 <= number of nodes <= 10^5`
- `-10^9 <= node.val <= 10^9`

**Examples:**

```
Tree:       3
           / \
          9   20
             /  \
            15   7
```

| Input | Output | Explanation |
|-------|--------|-------------|
| Above tree | `[[15, 7], [9, 20], [3]]` | Bottom level first, top level last |
| Single node `1` | `[[1]]` | One level only |
| Empty tree | `[]` | No nodes |

### Real-Life Analogy
> *Imagine photographing each floor of a building from the ground up. Normal level order is like starting from the top floor and going down. Reverse level order is starting from the basement and going up -- you see the foundation first and the penthouse last. You still photograph left to right on each floor, but the album of photos is in reverse floor order.*

### Key Observations
1. **Standard BFS first, then reverse:** Perform normal level order traversal (BFS), then reverse the result list. Simple and clean.
2. **Queue processes nodes level by level:** BFS with a queue naturally groups nodes by level when you track the level size.
3. **Reversing at the end is O(L) where L = number of levels:** The reversal cost is negligible compared to the O(n) BFS traversal. Alternatively, use a deque to avoid reversal. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **BFS** is the natural fit for level-order traversal -- it explores nodes breadth-first, level by level.
- A **queue** handles the BFS. We process nodes level by level using the `size` of the queue at each level.
- To reverse, either reverse the final list, insert at the front of a deque, or use DFS with depth tracking.

### Pattern Recognition
- **Pattern:** BFS level-order traversal + post-processing
- **Classification Cue:** "When you see _level by level in reverse_ --> think _BFS + reverse the result_"

---

## APPROACH LADDER

### Approach 1: BFS + Reverse List (Brute Force)
**Idea:** Standard level order traversal using BFS, then reverse the list of levels.

**Steps:**
1. If root is null, return empty list.
2. Initialize a queue with the root.
3. While queue is not empty:
   - `levelSize = queue.size()`.
   - Create `currentLevel` list.
   - Process `levelSize` nodes: dequeue, add value to `currentLevel`, enqueue children.
   - Add `currentLevel` to result list.
4. Reverse the result list.

**Dry Run:** Tree = `3 -> (9, 20) -> (_, _, 15, 7)`

| Step | Queue | Level Processed | Result So Far |
|------|-------|-----------------|---------------|
| 1 | [3] | [3] | [[3]] |
| 2 | [9, 20] | [9, 20] | [[3], [9, 20]] |
| 3 | [15, 7] | [15, 7] | [[3], [9, 20], [15, 7]] |
| Reverse | | | [[15, 7], [9, 20], [3]] |

| Time | Space |
|------|-------|
| O(n) | O(n) |

### Approach 2: BFS + Deque Insert at Front (Optimal)
**Idea:** Instead of reversing, insert each level at the front of a deque so the result is already in bottom-up order.

**Steps:**
1. Same BFS as above.
2. Instead of `result.append(currentLevel)`, use `result.appendleft(currentLevel)` (Python deque) or `result.addFirst(currentLevel)` (Java LinkedList).

| Time | Space |
|------|-------|
| O(n) | O(n) |

Avoids the final reverse. In Python, `deque.appendleft` is O(1). In Java, `LinkedList.addFirst` is O(1).

### Approach 3: DFS with Depth Tracking (Best)
**Idea:** Use DFS to record each node into a dictionary/map keyed by its depth. Then build the result by iterating from the maximum depth down to 0.

**Steps:**
1. If root is null, return empty list.
2. DFS from root with depth 0.
3. At each node, append its value to `levels[depth]`.
4. Recurse on left child (depth + 1) and right child (depth + 1).
5. After DFS, iterate from `max_depth` down to `0`, collecting each level's list.

| Time | Space |
|------|-------|
| O(n) | O(n) |

This approach is unique because it uses DFS instead of BFS. It naturally groups nodes by depth via the dictionary, and avoids using a queue altogether. Useful when you want to combine depth-based logic with other DFS operations.

---

## COMPLEXITY -- INTUITIVELY
**O(n) time:** "We visit every node exactly once (BFS or DFS). Any post-processing (reverse, dict iteration) is at most O(n)."
**O(n) space:** "The queue/stack holds at most one level of nodes (up to n/2 for a complete tree). The result stores all n values. The dict approach also stores all n values."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to handle the **null/empty tree** case.
2. Not tracking level boundaries -- without `levelSize`, BFS gives a flat list, not level-by-level.
3. Reversing the values within each level (wrong) instead of reversing the order of levels (correct).
4. In the DFS approach, forgetting to traverse left before right (which would reverse the within-level order).

### Edge Cases to Test
- [ ] Empty tree --> `[]`
- [ ] Single node --> `[[val]]`
- [ ] Left-skewed tree (linked list) --> each level has one node
- [ ] Right-skewed tree --> same as above
- [ ] Complete binary tree --> widest level at bottom

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Reverse the order of levels, but within each level keep left-to-right order? Return list of lists?"
2. **Approach:** "Standard BFS level order, then reverse the result list. Can optimize with deque insert-at-front."
3. **Follow-up:** "Can also do DFS with depth tracking -- groups nodes by level in a dict, then iterate from deepest level up."

### Follow-Up Questions
- "Can you do it without reversing?" --> Insert at front of a deque/LinkedList, or use DFS with depth dict.
- "What about zigzag level order?" --> Alternate left-to-right and right-to-left per level.
- "BFS vs DFS trade-offs?" --> BFS is more natural for level-order. DFS uses recursion stack (O(h) space for stack vs O(w) for queue). DFS is useful when combining with other depth-based operations.

---

## CONNECTIONS
- **Prerequisite:** Level Order Traversal (P006), BFS fundamentals
- **Same Pattern:** Level Order Traversal, Zigzag Level Order
- **This Unlocks:** Bottom-up level processing problems, Average of Levels
- **Related:** Binary Tree Right Side View, Maximum Width of Binary Tree
