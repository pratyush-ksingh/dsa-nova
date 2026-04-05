# Postorder Traversal

> **Batch 1 of 12** | **Topic:** Binary Trees | **Difficulty:** EASY | **XP:** 10

**LeetCode #145**

---

## 1. UNDERSTAND

### Problem Statement
Given the `root` of a binary tree, return its **postorder** traversal as a list. Postorder means: **Left, Right, Root** -- visit the left subtree, then the right subtree, then the current node.

### Analogy
Think of **cleaning up after a party** in a building. You clean the leftmost rooms first, then the rightmost rooms, and only then clean the hallway (root). You always finish everything below before dealing with the current level. This is why postorder is used for **deletion** and **bottom-up computation** -- you must process children before the parent.

### Key Observations
1. Postorder processes children before the parent. **Aha:** This is the natural order for deletion (free children before parent) and for computing properties that depend on subtree results (like height).
2. Recursive postorder is straightforward. **Aha:** Iterative is the trickiest of the three traversals because the root must be visited last.
3. A clever iterative trick: modified preorder (Root-R-L) then reverse gives L-R-Root (postorder). **Aha:** This avoids the complexity of the "two-stack" or "prev pointer" approaches.

### Examples

```
    1            Postorder: [3,2,1]
     \
      2
     /
    3
```

```
      1          Postorder: [4,5,2,6,3,1]
     / \
    2   3
   / \   \
  4   5   6
```

| Input (tree) | Output |
|-------------|--------|
| [] | [] |
| [1] | [1] |
| [1,null,2,3] | [3,2,1] |
| [1,2,3,4,5,null,6] | [4,5,2,6,3,1] |

### Constraints
- 0 <= number of nodes <= 100
- -100 <= Node.val <= 100

---

## 2. DS & ALGO CHOICE

| Approach | Data Structure | Why |
|----------|---------------|-----|
| Brute (recursive) | Call stack | Direct L-R-Root definition. |
| Optimal (iterative -- reverse trick) | Stack + reverse | Modified preorder (Root-R-L) reversed = L-R-Root. |
| Best (iterative -- single stack with prev) | Stack + prev pointer | True postorder without reversal. O(h) space. |

**Pattern cue:** Postorder = bottom-up processing. Think of problems where you need subtree results first.

---

## 3. APPROACH LADDER

### Approach 1 -- Recursive
**Intuition:** Visit left, visit right, then add root.

**Steps:**
1. If node is null, return.
2. Recurse on `node.left`.
3. Recurse on `node.right`.
4. Add `node.val` to result.

**Dry-Run Trace -- Tree [1,2,3,4,5,null,6]:**

| Call | Node | Action | Result |
|------|------|--------|--------|
| post(1) | 1 | go left to 2 | |
| post(2) | 2 | go left to 4 | |
| post(4) | 4 | left null, right null, add 4 | [4] |
| post(2) | 2 | go right to 5 | |
| post(5) | 5 | left null, right null, add 5 | [4,5] |
| post(2) | 2 | add 2 | [4,5,2] |
| post(1) | 1 | go right to 3 | |
| post(3) | 3 | left null, go right to 6 | |
| post(6) | 6 | add 6 | [4,5,2,6] |
| post(3) | 3 | add 3 | [4,5,2,6,3] |
| post(1) | 1 | add 1 | [4,5,2,6,3,1] |

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) recursion stack |

### BUD Transition (Brute -> Optimal)
**Bottleneck:** Recursion depth risk. The iterative reverse trick is simple: do Root-R-L (easy with a stack), then reverse the result to get L-R-Root.

### Approach 2 -- Optimal (Iterative -- Reverse Trick)
**Intuition:** Do a modified preorder: Root -> Right -> Left (push left first, then right). This gives Root-R-L. Reversing this list yields L-R-Root (postorder).

**Steps:**
1. Push root. While stack not empty:
   - Pop node, add to result.
   - Push LEFT first, then RIGHT (so right is processed first = Root-R-L).
2. Reverse the result.

**Dry-Run Trace -- Tree [1,2,3]:**

| Step | Pop | Stack | Result |
|------|-----|-------|--------|
| 1 | 1 | [2, 3] | [1] |
| 2 | 3 | [2] | [1,3] |
| 3 | 2 | [] | [1,3,2] |

Reverse: [2, 3, 1] -- correct postorder.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(n) for the result (reversal) + O(h) stack |

### Approach 3 -- Best (Iterative -- Single Stack with Prev Pointer)
**Intuition:** Use a `prev` variable to track the last visited node. Only pop and visit a node when both its children have been processed (or it has no children).

**Steps:**
1. Push root. Set `prev = null`.
2. While stack not empty:
   - Peek at top.
   - If top has left/right unvisited, push the unvisited child.
   - Else: pop, visit, set prev = popped node.

| Metric | Value |
|--------|-------|
| Time | O(n) |
| Space | O(h) -- no reversal needed |

---

## 4. COMPLEXITY INTUITIVELY

- **O(n):** Each node processed once.
- **O(h) stack:** Same as other traversals.
- Reverse trick uses O(n) extra for the reversal but is simpler to code.

---

## 5. EDGE CASES & MISTAKES

| Edge Case | How to Handle |
|-----------|---------------|
| Empty tree | Return [] |
| Single node | Return [val] |
| Left-only chain | Postorder visits bottom to top |
| Right-only chain | Same: bottom to top |

**Common mistakes:**
- Reverse trick: pushing right before left (gives preorder, not modified preorder).
- Prev pointer approach: not correctly checking if children have been visited.
- Returning Root-R-L without reversing.

---

## 6. INTERVIEW LENS

| Question | Answer |
|----------|--------|
| Why is postorder the hardest iteratively? | Root must be visited last, but it is encountered first. Need extra logic to defer processing. |
| When is postorder useful? | Tree deletion, expression evaluation, computing heights, any bottom-up aggregation. |
| Can you do postorder with Morris? | Yes, but significantly more complex. Rarely asked. |
| Postorder + Inorder = unique tree? | Yes. Same reconstruction capability as Preorder + Inorder. |

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Inorder Traversal (LC #94) | L-Root-R; compare with L-R-Root |
| Preorder Traversal (LC #144) | Root-L-R; reverse trick leverages modified preorder |
| Delete Nodes (LC #1110) | Postorder ensures children processed before parent deletion |
| Expression Tree Evaluation | Postorder evaluates operands before operator |
| Height of Binary Tree (LC #104) | Height is a postorder computation: max(left, right) + 1 |

---

## Real-World Use Case
**Dependency cleanup and garbage collection:** When deleting a directory tree, `rm -rf` uses postorder traversal -- it deletes all child files before removing the parent directory. Java's garbage collector similarly frees child objects before their parent, ensuring no dangling references remain.
