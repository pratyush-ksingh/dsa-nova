# Iterative Postorder using 2 Stacks

> **Batch 3 of 12** | **Topic:** Binary Trees -- Traversals | **Difficulty:** HARD | **XP:** 50

## UNDERSTAND

### Problem Statement
Given the root of a binary tree, return its **postorder traversal** (Left -> Right -> Root) using an **iterative approach with two stacks**. No recursion allowed. Return the values as a list.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,2,3,4,5]` | `[4,5,2,3,1]` | Left subtree [4,5,2] fully, then right 3, then root 1 |
| `[1,null,2,3]` | `[3,2,1]` | No left, visit 3 (left of 2), then 2, then 1 |
| `[]` | `[]` | Empty tree |
| `[1]` | `[1]` | Single node |

### Analogy
Imagine you are cleaning up a multi-floor building. You need to clean every room, but you must clean child rooms before parent rooms (postorder). You first make a "visit plan" by walking through the building noting rooms in a special reverse order (stack 1), then you transfer that plan to a "cleanup queue" (stack 2). When you read the cleanup queue back, it naturally gives you the bottom-up order you need.

### 3 Key Observations
1. **"Aha" -- Postorder is reverse of modified preorder:** If you do Root -> Right -> Left (modified preorder), then reverse it, you get Left -> Right -> Root (postorder). The second stack provides that reversal.
2. **"Aha" -- Stack 1 produces reverse postorder:** By pushing left before right to s1, when you pop from s1 and push to s2, s2 accumulates nodes in reverse postorder.
3. **"Aha" -- Two stacks avoid the "when to process" dilemma:** Postorder is hard iteratively because you must process a node AFTER both children. Two stacks sidestep this by separating traversal from output.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Recursion (call stack) | Recursive DFS | Simple baseline, uses implicit stack |
| Optimal | Two explicit stacks | Modified preorder + reversal | Avoids complex "visited" tracking |
| Best | Stack + LinkedList | Same logic, addFirst for reversal | Slightly cleaner with O(1) front insertion |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursive Postorder
**Intuition:** The natural recursive solution. Recurse left, recurse right, then process current node. This is our baseline to understand the pattern.

**Steps:**
1. If node is null, return.
2. Recurse on left child.
3. Recurse on right child.
4. Add node's value to result.

**Dry-run trace** with `[1,2,3,4,5]`:
```
postorder(1): go left
  postorder(2): go left
    postorder(4): left=null, right=null, add 4
  back to 2, go right
    postorder(5): left=null, right=null, add 5
  add 2
back to 1, go right
  postorder(3): left=null, right=null, add 3
add 1
Result: [4,5,2,3,1]
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node once |
| Space | O(h) -- recursion stack depth (h = tree height) |

---

### Approach 2: Optimal -- Two Stacks
**Intuition:** Postorder (L, R, Root) is the reverse of a modified preorder (Root, R, L). Use stack 1 to perform Root -> Right -> Left traversal, pushing each popped node onto stack 2. When done, stack 2 contains nodes in postorder.

**BUD Optimization:**
- **B**ottleneck: Recursion can overflow on deep trees. Two stacks avoid that.
- **U**nnecessary: Each node is pushed/popped exactly once from each stack.

**Steps:**
1. If root is null, return empty list.
2. Push root onto stack 1.
3. While stack 1 is not empty:
   - Pop node from s1, push it onto s2.
   - If node has left child, push to s1.
   - If node has right child, push to s1.
4. Pop all from s2 into result (this is postorder).

**Dry-run trace** with `[1,2,3,4,5]`:
```
s1: [1], s2: []
Pop 1 from s1 -> s2: [1]. Push left=2, right=3 to s1. s1: [2,3]
Pop 3 from s1 -> s2: [1,3]. No children. s1: [2]
Pop 2 from s1 -> s2: [1,3,2]. Push left=4, right=5. s1: [4,5]
Pop 5 from s1 -> s2: [1,3,2,5]. No children. s1: [4]
Pop 4 from s1 -> s2: [1,3,2,5,4]. No children. s1: []
Pop s2 -> [4,5,2,3,1] (postorder!)
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node pushed/popped twice total |
| Space | O(n) -- both stacks together hold all n nodes |

---

### Approach 3: Best -- Two Stacks (compact with addFirst)
**Intuition:** Same two-stack logic, but instead of a second stack, use a LinkedList and insert each node at the front. This effectively reverses the order as you go, eliminating the explicit second pass.

**Steps:**
1. Push root onto stack.
2. While stack is not empty:
   - Pop node, add its value to the FRONT of result list.
   - Push left child, then right child to stack.
3. Return result (already in postorder).

| Metric | Value |
|--------|-------|
| Time | O(n) -- each node processed once |
| Space | O(n) -- stack + result list |

---

## COMPLEXITY INTUITIVELY

**Why O(n) time:** Every node must be visited exactly once to collect its value. No shortcuts possible.

**Why O(n) space:** Unlike preorder/inorder iterative (O(h) stack), here the second stack must accumulate ALL nodes before we can start reading the result. Hence O(n) worst and average case.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `[]` | Check for null root first |
| Single node | `[val]` | Push/pop once from each stack |
| Left-skewed tree | All nodes, leaves first | Both stacks grow to O(n) |
| Right-skewed tree | All nodes, leaf first | Same O(n) behavior |

### Common Mistakes
- Pushing right before left to s1 (reverses the Left-Right order in postorder).
- Forgetting to reverse s2 (or pop from it) -- you'd get reverse postorder.
- Confusing this with the 1-stack approach which requires tracking the last visited node.

---

## INTERVIEW LENS

**Frequency:** High -- classic follow-up to iterative preorder/inorder.
**Follow-ups the interviewer might ask:**
- "Can you do it with just 1 stack?" (Harder -- track last visited node)
- "What's the relationship to preorder?" (Postorder = reverse of Root-Right-Left preorder)
- "Can you do O(1) space?" (Morris postorder -- very advanced)

**What they're really testing:** Understanding of how stack operations map to traversal order, and the insight that postorder is reverse-modified-preorder.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Iterative Preorder | Postorder is reverse of modified preorder |
| Iterative Postorder (1 Stack) | Harder variant with less space overhead |
| Iterative Inorder | Same family of iterative tree traversals |
| Reverse Level Order | Also uses reversal trick for bottom-up processing |

### Real-World Use Case
**Dependency cleanup / garbage collection:** When deleting a tree structure (like a file directory), you must delete children before parents. Postorder traversal ensures you process leaf nodes first, then work your way up -- exactly how `rm -rf` works internally. The two-stack approach maps to first building a deletion plan, then executing it in reverse.
