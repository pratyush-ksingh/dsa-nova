# All Traversals in One Pass

> **Step 13 | 13.1** | **Difficulty:** HARD | **XP:** 50 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given a binary tree, compute its **preorder**, **inorder**, and **postorder** traversals simultaneously in a single iterative pass.

- **Preorder:** root → left → right
- **Inorder:** left → root → right
- **Postorder:** left → right → root

Return three lists: `(preorder, inorder, postorder)`.

**Constraints:**
- `1 <= number of nodes <= 10^5`
- `-10^9 <= node.val <= 10^9`

**Examples:**

```
Tree:     1
         / \
        2   3
       / \
      4   5
```

| Traversal | Output |
|-----------|--------|
| Preorder  | `[1, 2, 4, 5, 3]` |
| Inorder   | `[4, 2, 5, 1, 3]` |
| Postorder | `[4, 5, 2, 3, 1]` |

### Real-Life Analogy
> *Think of visiting a museum. Preorder = take a photo of the entrance hall before exploring rooms (visit before children). Inorder = explore the left wing, then the main hall, then the right wing (visit between children). Postorder = explore all rooms first, then sign the guest book at the exit (visit after all children). The "one pass" trick means one tour through the museum records all three guest books simultaneously.*

### Key Observations
1. In recursive DFS, each node is "visited" three times: once before going left, once after returning from left (before going right), and once after returning from right.
2. These three visits correspond exactly to preorder, inorder, and postorder.
3. We can simulate this with a stack by tagging each stack entry with its visit count (1, 2, or 3).

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why Stack + Visit Counter?
- Recursive DFS implicitly uses the call stack. Converting to iterative, we maintain the same three-visit structure explicitly.
- Each node goes on the stack up to 3 times (once per state). Total stack operations: 3n = O(n).
- One loop, one stack → one pass.

### Pattern Recognition
- **Pattern:** Iterative DFS with explicit state machine — applicable to any tree traversal problem that would otherwise require multiple passes.
- **Classification Cue:** "All three traversals at once → stack with (node, visit_count) where 1=pre, 2=in, 3=post."

---

## APPROACH LADDER

### Approach 1: Brute Force — Three Separate Recursive Traversals
**Idea:** Run three independent DFS calls on the tree — one for preorder, one for inorder, one for postorder.

**Steps:**
1. Recursive preorder: visit root, recurse left, recurse right.
2. Recursive inorder: recurse left, visit root, recurse right.
3. Recursive postorder: recurse left, recurse right, visit root.
4. Return all three result lists.

**BUD Transition — Bottleneck:** We traverse the tree three times and use three separate recursion stacks. While the asymptotic complexity is still O(n), we can do better by combining all three in a single pass.

| Metric | Value |
|--------|-------|
| Time   | O(3n) = O(n) |
| Space  | O(n) recursion stack depth × 3 |

---

### Approach 2: Optimal — Single Iterative Pass with (node, state) Stack
**What changed:** Simulate the recursive call stack explicitly. Each stack entry holds a node and its current visit state (1, 2, or 3). One loop handles all three traversals simultaneously.

**Steps:**
1. Push `(root, state=1)` onto the stack.
2. While stack is not empty, pop `(node, state)`:
   - **State 1 (PRE):** Append `node.val` to preorder. Push `(node, 2)` back. If left child exists, push `(left, 1)`.
   - **State 2 (IN):** Append `node.val` to inorder. Push `(node, 3)` back. If right child exists, push `(right, 1)`.
   - **State 3 (POST):** Append `node.val` to postorder.
3. Return `(preorder, inorder, postorder)`.

**Dry Run** on tree `[1, 2, 3, 4, 5]`:

| Stack (top→) | Action | Pre | In | Post |
|---|---|---|---|---|
| `[(1,1)]` | Pop (1,1): add 1 to pre, push (1,2),(2,1) | [1] | [] | [] |
| `[(1,2),(2,1)]` | Pop (2,1): add 2 to pre, push (2,2),(4,1) | [1,2] | [] | [] |
| `[(1,2),(2,2),(4,1)]` | Pop (4,1): add 4 to pre, push (4,2) [no left] | [1,2,4] | [] | [] |
| `[(1,2),(2,2),(4,2)]` | Pop (4,2): add 4 to in, push (4,3) [no right] | [1,2,4] | [4] | [] |
| `[(1,2),(2,2),(4,3)]` | Pop (4,3): add 4 to post | [1,2,4] | [4] | [4] |
| ... | (continues for node 2, 5, 1, 3) | [1,2,4,5,3] | [4,2,5,1,3] | [4,5,2,3,1] |

| Metric | Value |
|--------|-------|
| Time   | O(n) — each node processed exactly 3 times |
| Space  | O(n) — stack holds at most 3×height entries |

---

### Approach 3: Best — Same Single-Pass with Named State Constants
**Intuition:** Identical to Approach 2. Using named constants `PRE=1, IN=2, POST=3` makes the state machine explicit and readable. This is the production-quality form of the algorithm.

The visit counter trick is the core insight: it maps directly to the three "time points" in a DFS traversal (before left, between left and right, after right), allowing all three orderings to be collected simultaneously without revisiting the tree.

| Metric | Value |
|--------|-------|
| Time   | O(n) |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to push back `(node, state+1)` before pushing the child — the node must be re-processed later.
2. Pushing right child in state 1 instead of state 2 (right should be pushed when we're ready to go right, i.e., state 2).
3. Off-by-one: state goes 1→2→3, not 0→1→2.

### Edge Cases to Test
- [ ] Single node tree
- [ ] Left-skewed tree (only left children)
- [ ] Right-skewed tree (only right children)
- [ ] Perfect binary tree
- [ ] Empty tree (return three empty lists)

---

## Real-World Use Case
**Compiler/interpreter expression evaluation:** Expression trees (ASTs) are traversed in all three orders: preorder for prefix notation, inorder for infix (standard math), and postorder for postfix (used by stack-based calculators and JVM bytecode). A single-pass traversal that collects all three forms simultaneously is useful in compilers that need multiple output formats from one AST walk.

## Interview Tips
- Start by explaining the three-visit insight: "In any DFS, each node is encountered exactly three times — this is why all three traversals can be computed in one pass."
- Draw the state machine: PRE→IN→POST, with left subtree between PRE and IN, right subtree between IN and POST.
- This is considered a "hard" problem because it requires understanding why the stack-based simulation works — be ready to trace through a small example.
- This technique generalizes: any tree algorithm that requires DFS can be converted to iterative using the same (node, state) pattern.
