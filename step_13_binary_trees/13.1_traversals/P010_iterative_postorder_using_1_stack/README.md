# Iterative Postorder using 1 Stack

> **Batch 3 of 12** | **Topic:** Binary Trees -- Traversals | **Difficulty:** HARD | **XP:** 50

## UNDERSTAND

### Problem Statement
Given the root of a binary tree, return its **postorder traversal** (Left -> Right -> Root) using an **iterative approach with only one stack**. No recursion allowed. Return the values as a list.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,2,3,4,5]` | `[4,5,2,3,1]` | Left subtree [4,5,2] fully, then right 3, then root 1 |
| `[1,null,2,3]` | `[3,2,1]` | No left, visit 3 (left of 2), then 2, then 1 |
| `[]` | `[]` | Empty tree |
| `[1]` | `[1]` | Single node |

### Analogy
Imagine you are a project manager who must sign off on tasks. You can only sign off on a parent task AFTER all sub-tasks are complete. You keep a to-do stack, always diving into left sub-tasks first. Before signing off, you peek: "Is there an unfinished right sub-task?" If yes, dive into that first. You track your "last signed-off task" so you know when you are revisiting a parent whose right side is already done.

### 3 Key Observations
1. **"Aha" -- The challenge is knowing when to process:** In postorder, a node is processed only after BOTH children are done. With one stack, you must distinguish between "going down" and "coming back up."
2. **"Aha" -- Track last visited:** By remembering the last node you processed, you can tell if you are returning from the right child (safe to process parent) or still need to visit the right child.
3. **"Aha" -- Go left first, then check right:** Push all left nodes first. At each peek, check if right child exists and is unvisited. If so, go right. Otherwise, process the node.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Recursion (call stack) | Recursive DFS | Simple baseline |
| Optimal | Single stack + prev pointer | Iterative DFS | O(h) space, no reversal needed |
| Best | Single stack + prev pointer | Same, streamlined | Same complexity, cleaner code |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursive Postorder
**Intuition:** The natural recursive solution. Recurse left, recurse right, process node. Baseline to understand the pattern.

**Steps:**
1. If node is null, return.
2. Recurse on left child.
3. Recurse on right child.
4. Add node's value to result.

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node once |
| Space | O(h) -- recursion stack depth |

---

### Approach 2: Optimal -- Single Stack with Last Visited Tracking
**Intuition:** Simulate recursion with one stack. The key insight: after pushing all left nodes, peek at the top. If it has an unvisited right child, go right. Otherwise, pop and process it. Track `lastVisited` to distinguish "returning from right" vs "need to go right."

**BUD Optimization:**
- **B**ottleneck: The 2-stack approach uses O(n) space. This uses O(h) -- much better for balanced trees.
- **U**nnecessary: No reversal pass needed.

**Steps:**
1. Initialize `curr = root`, `lastVisited = null`, empty stack.
2. While `curr` is not null OR stack is not empty:
   a. Push all left nodes: while `curr` not null, push `curr`, go left.
   b. Peek at stack top.
   c. If top has right child AND right child is not `lastVisited`, set `curr = top.right`.
   d. Else, pop top, add to result, set `lastVisited = popped node`.
3. Return result.

**Dry-run trace** with `[1,2,3,4,5]`:
```
Push left: 1 -> 2 -> 4. Stack: [1,2,4]
Peek 4: no right, no unvisited right -> pop 4, add 4. last=4. Stack: [1,2]
Peek 2: right=5, 5 != last(4) -> go right. curr=5
Push left from 5: just 5. Stack: [1,2,5]
Peek 5: no right -> pop 5, add 5. last=5. Stack: [1,2]
Peek 2: right=5, 5 == last(5) -> pop 2, add 2. last=2. Stack: [1]
Peek 1: right=3, 3 != last(2) -> go right. curr=3
Push left from 3: just 3. Stack: [1,3]
Peek 3: no right -> pop 3, add 3. last=3. Stack: [1]
Peek 1: right=3, 3 == last(3) -> pop 1, add 1. last=1. Stack: []
Result: [4,5,2,3,1]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node pushed and popped once |
| Space | O(h) -- stack holds at most height of tree |

---

### Approach 3: Best -- Single Stack (streamlined)
**Intuition:** Same algorithm as Optimal with a slightly different loop structure. Instead of two nested loops, use a single while loop with an if-else: if `curr` is not null, push and go left; else peek and decide.

**Steps:**
1. If `curr` is not null, push it and go left.
2. Else, peek top. If right exists and unvisited, go right. Else pop, add, set `prev`, set `curr = null`.

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node processed once |
| Space | O(h) -- same single stack |

---

## COMPLEXITY INTUITIVELY

**Why O(n) time:** Each node is pushed once and popped once. The "go left" inner loop visits each node at most once across the entire execution.

**Why O(h) space:** At any point, the stack contains nodes along the path from root to the current position -- bounded by tree height. This is a significant improvement over the 2-stack approach's O(n).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `[]` | Check for null root first |
| Single node | `[val]` | Immediately processed |
| Left-skewed tree | Bottom to top | Stack grows to O(n), all pushed before any popped |
| Right-skewed tree | Bottom to top | Each node: push, peek, go right, come back |
| Complete binary tree | Standard postorder | O(log n) stack depth |

### Common Mistakes
- Forgetting to set `curr = null` after processing a node (causes infinite loop of re-pushing left).
- Using `==` instead of `is` (Python) for node comparison -- use identity check for object references.
- Not handling the case where right child was already visited (must check `lastVisited`).

---

## INTERVIEW LENS

**Frequency:** High -- often asked as a follow-up to the 2-stack version.
**Follow-ups the interviewer might ask:**
- "Why is 1 stack better than 2?" (O(h) vs O(n) space)
- "Can you explain the state transitions?" (Going down-left, going right, coming back up)
- "How does this compare to iterative inorder?" (Similar skeleton, but inorder processes when popping, postorder delays processing)

**What they're really testing:** Deep understanding of stack-based tree traversal states and the ability to handle the most complex iterative traversal (postorder) with minimal auxiliary space.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Iterative Postorder (2 Stacks) | Simpler but uses O(n) space |
| Iterative Inorder | Same "push all left" pattern, simpler processing |
| Iterative Preorder | Simplest iterative traversal, process on push |
| Binary Tree Maximum Path Sum | Also needs post-order style bottom-up processing |

### Real-World Use Case
**Expression tree evaluation:** Compilers evaluate expression trees in postorder (evaluate operands before operators). In embedded systems with limited memory, using a single stack is critical. The 1-stack postorder traversal directly maps to how a compiler might evaluate `(a + b) * (c - d)` by processing leaves first, then combining results upward.
