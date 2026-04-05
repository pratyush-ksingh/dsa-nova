# BST Iterator

> **Step 14 | 14.2** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** #173 | **Status:** UNSOLVED

---

## 1. UNDERSTAND

### Problem Statement
Implement the `BSTIterator` class that represents an iterator for the inorder traversal of a BST:

- `BSTIterator(root)`: Initializes the object with the `root` of a BST.
- `boolean hasNext()`: Returns `true` if there exists a next smallest number.
- `int next()`: Moves to the next smallest number and returns it.

**Constraints on complexity:**
- `next()` and `hasNext()` must run in **average O(1) time**.
- Use only **O(h) memory** where h is the height of the tree.

### Analogy
Think of a sorted music playlist. Instead of loading all songs into memory (O(n)), you keep a bookmark at your current position. `next()` plays the next song and advances the bookmark. `hasNext()` checks if there are more songs. The "bookmark" in a BST is a stack that remembers your path.

### Key Observations
1. **Inorder of BST = sorted sequence.** We're essentially providing lazy sorted access to BST elements.
2. **Flatten approach is O(n) space** -- simple but wastes memory for large trees when only a few elements are needed.
3. **Stack-based approach is the key insight:** Maintain a stack of "pending" nodes -- specifically the left spine from the current position. When we call `next()`, pop the smallest, then push the left spine of its right child.
4. **Amortized O(1):** Each node is pushed exactly once and popped exactly once across all `next()` calls -> total work = O(n) for n calls -> amortized O(1) per call.

### Examples

```
BST:
      7
     / \
    3   15
       /  \
      9   20

BSTIterator(root)
next()    -> 3   (leftmost)
next()    -> 7
hasNext() -> true
next()    -> 9
hasNext() -> true
next()    -> 15
hasNext() -> true
next()    -> 20
hasNext() -> false
```

| Call | Returns | Stack state after |
|------|---------|-------------------|
| init | - | [7, 3] (left spine) |
| next() | 3 | [7] (3 has no right) |
| next() | 7 | [15, 9] (push left spine of 15) |
| hasNext() | true | [15, 9] |
| next() | 9 | [15] (9 has no right) |
| next() | 15 | [20] (push left spine of 20) |
| next() | 20 | [] |
| hasNext() | false | [] |

### Constraints
- 1 <= number of nodes <= 10^5
- 0 <= Node.val <= 10^6
- At most 10^5 calls to `next()` and `hasNext()`
- `next()` is always called when `hasNext()` is true

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (flatten) | Array/List + index | Pre-compute all inorder values; O(1) operations but O(n) space |
| Optimal (stack-based) | Explicit stack | O(h) space; amortized O(1) per next(); lazy evaluation |
| Best (same as Optimal) | Explicit stack | Identical algorithm, clearer method naming ("advanceLeft") |

**Pattern cue:** "Iterator over sorted structure" -> think lazy controlled traversal. Stack naturally simulates the call stack of recursive inorder traversal, paused between yields.

---

## 3. APPROACH LADDER

### Approach 1 -- Brute Force (Flatten to Array)
**Intuition:** Do a complete inorder traversal at initialization, store all values in a list. `next()` and `hasNext()` are simple list accesses.

**Steps:**
1. In constructor: traverse tree with DFS inorder, append each value to `inorder[]`.
2. `hasNext()`: return `idx < inorder.length`.
3. `next()`: return `inorder[idx++]`.

| Metric | Value |
|--------|-------|
| Time | O(n) constructor, O(1) next/hasNext |
| Space | O(n) -- full inorder array stored |

**When acceptable:** When you know you'll call `next()` for all n elements and have the memory budget. Very simple to implement.

---

### BUD Transition (Brute -> Optimal)
**Bottleneck:** O(n) space is wasteful if only a few `next()` calls are made. The constraint says O(h).
**Insight:** Simulate the recursive inorder call stack explicitly. The stack never needs more than h entries (one per level from root to the "current" node's leftmost descendant).

### Approach 2 -- Optimal (Controlled Stack Traversal)
**Intuition:** The stack holds the "left spine" of the subtree rooted at the current position -- all nodes we will visit before moving to any right subtrees. Calling `next()` pops the top (smallest), then loads the left spine of its right child.

**Steps:**
1. Constructor: call `pushLeft(root)` to load the initial left spine onto the stack.
2. `pushLeft(node)`: while `node != null`, push it and go left.
3. `next()`:
   - Pop top of stack (this is the next smallest value).
   - Call `pushLeft(poppedNode.right)` to prepare the right subtree's left spine.
   - Return `poppedNode.val`.
4. `hasNext()`: return `!stack.isEmpty()`.

**Dry-Run on [7, 3, 15, null, null, 9, 20]:**
```
init: pushLeft(7) -> stack=[7, 3] (push 7, push 3, 3.left=null stop)

next(): pop 3, 3.right=null (no pushLeft), return 3
        stack=[7]

next(): pop 7, pushLeft(7.right=15) -> push 15, push 9, 9.left=null stop
        stack=[15, 9], return 7

next(): pop 9, 9.right=null, return 9
        stack=[15]

next(): pop 15, pushLeft(15.right=20) -> push 20, 20.left=null stop
        stack=[20], return 15

next(): pop 20, 20.right=null, return 20
        stack=[]

hasNext(): false
```

| Metric | Value |
|--------|-------|
| Time | O(h) constructor, amortized O(1) next(), O(1) hasNext() |
| Space | O(h) -- stack depth bounded by tree height |

**Why amortized O(1)?** Each node is pushed once and popped once. n total `next()` calls do 2n stack operations total -> O(1) per call amortized.

---

### Approach 3 -- Best (Same Stack, "advanceLeft" Naming)
**Intuition:** Exactly the same algorithm as Approach 2. The naming `advanceLeft` makes it clear that we're lazily "advancing" along the inorder sequence by exploring the left spine incrementally.

**Steps:** Identical to Approach 2. Method name `advanceLeft` instead of `pushLeft`.

| Metric | Value |
|--------|-------|
| Time | Amortized O(1) next(), O(1) hasNext() |
| Space | O(h) |

*Note:* For this problem, Optimal and Best are the same algorithm. The stack-based approach IS the best possible solution given the constraints.

---

## 4. COMPLEXITY INTUITIVELY

- **Brute O(n) space:** Stores every node's value. Useful when n is small.
- **Stack O(h) space:** For a balanced BST, h = O(log n). Even a skewed BST has h = O(n), but real BSTs are typically balanced -> O(log n).
- **Amortized O(1) next():** Think of it as pre-paying. The constructor "pre-pays" for pushing the left spine (O(h) work). Each subsequent pushLeft call on right subtrees also pre-pays for those nodes. Total pushes across all calls = n = total pops. Cost per next() = amortized O(1).

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Single node BST | pushLeft pushes just root; next() returns root.val; hasNext() false |
| Right-skewed BST | pushLeft pushes only root initially; each next() loads one more node |
| Left-skewed BST | pushLeft loads all n nodes at init; subsequent next() calls do no pushLeft |
| Empty tree (root=null) | Constructor: pushLeft(null) does nothing; hasNext() immediately false |

**Common mistakes:**
- Calling `pushLeft(node.right)` but forgetting to check if `node.right != null` -- actually fine since pushLeft handles null gracefully.
- In the brute approach: using `remove(0)` from ArrayList (O(n)); use index pointer instead.
- Forgetting that the stack ordering is correct: the **top** of the stack is always the next smallest.

---

## 6. REAL-WORLD USE CASE

**Database cursor / lazy query execution:** When a database executes `SELECT * FROM table ORDER BY id`, it doesn't load all rows into memory. Instead, it uses a cursor (iterator) that fetches the next row on demand. BST iterators model this exactly -- the BST represents the B-Tree index, and the iterator lazily traverses it in sorted order.

**Stream processing:** When streaming sorted data from a large BST-based index (e.g., time-series data), the O(h) stack iterator allows processing elements one at a time without loading the entire dataset -- critical for memory-constrained systems.

---

## 7. INTERVIEW TIPS

- **Name the pattern:** "Controlled inorder traversal using a stack -- we maintain the left spine of the current subtree."
- **Explain the amortized analysis:** "Each node is pushed once and popped once, so across n next() calls, total work is O(n) -> O(1) amortized."
- **Draw the stack state** after each operation -- it makes the invariant clear.
- **Follow-ups to expect:**
  - "Can you implement a reverse iterator (largest to smallest)?" -> Push the RIGHT spine instead; go right in `next()`.
  - "What if the tree is modified during iteration?" -> Add a version counter (fail-fast iterator pattern).
  - "What is the space complexity for a perfectly balanced BST?" -> O(log n) stack depth.
- **LeetCode follow-up (LC #173):** "You may assume `next()` call will always be valid." -- no need to guard against empty stack in `next()`.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Inorder Traversal (LC #94) | BST Iterator is essentially a "pausable" inorder traversal |
| Kth Smallest in BST (LC #230) | Call `next()` k times with the iterator |
| Flatten BST to Sorted List | Iterator visits elements in sorted order; similar controlled traversal |
| Two Sum in BST (LC #653) | Use two BST iterators (one forward, one reverse) simultaneously |
| Design Twitter Feed (system design) | Iterator/cursor pattern for lazy data loading |

---

## Real-World Use Case

Database cursor implementations in PostgreSQL and MongoDB use BST iterator patterns to traverse B-tree indexes row-by-row without loading the entire index into memory. This controlled traversal enables efficient pagination in REST APIs serving millions of sorted records.
