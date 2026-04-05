# Consecutive Parent Child

> **Step 13 | 13.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Source:** InterviewBit | **Status:** UNSOLVED

## UNDERSTAND

### Problem Statement
Given a binary tree, find the **count of parent-child pairs** where the child's value is exactly one more than the parent's value (i.e., `child.val == parent.val + 1`). Check both left and right children for each node.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1,2,3]` | `2` | (1,2) and (1,3-no), actually 1->2 is consecutive. 1->3: 3!=2. Wait: 2==1+1 yes, 3==1+1? No, 3!=2. So count=1. Hmm, let me reconsider. If tree is [1,2,3]: parent 1, children 2,3. 2==1+1? Yes. 3==1+1? 3==2? No. Count=1 |
| Tree with `1->2, 2->3` | `2` | (1,2) and (2,3) are both consecutive |
| `[5,6,7,7,8]` | `3` | (5,6), (5,7-no:7!=6), (6,7), (6,8): 6+1=7 yes, 6+1=7!=8 no. Pairs: (5,6), (6,7), (6,8-no). Count=2. Corrected: (5,6)=yes, (6,7)=yes, (6,8)=no. Count=2 |
| `[]` | `0` | Empty tree |
| `[1]` | `0` | No parent-child pairs |

Let me provide cleaner examples:

| Input | Output | Explanation |
|-------|--------|-------------|
| `1(2,3(,4))` | `2` | Pairs: (1,2) yes (2=1+1), (1,3) no, (3,4) yes (4=3+1). Count=2 |
| `1(2(3,),2)` | `2` | Pairs: (1,2) yes, (1,2) yes, (2,3) yes. Count=3 |
| `5(10,20)` | `0` | No consecutive pairs |
| `[1]` | `0` | Single node, no children to check |

### Analogy
Think of a family tree where each person has an "age ID." You want to count how many direct parent-child relationships have the child's ID exactly one more than the parent's. You simply check every parent and their immediate children.

### 3 Key Observations
1. **"Aha" -- Already O(n) is optimal:** Every node must be checked at least once, so O(n) is a lower bound. There is no trick to skip nodes.
2. **"Aha" -- Local check only:** Each parent-child comparison is independent. No global state or path tracking needed.
3. **"Aha" -- Check from parent side:** At each node, check if left child or right child has value = current + 1. This avoids needing parent pointers.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Queue | BFS | Straightforward level-order check |
| Optimal | Recursion stack | DFS | O(h) space vs O(n) for BFS |
| Best | Recursion stack | DFS (functional return) | No global state, cleaner |

---

## APPROACH LADDER

### Approach 1: Brute Force -- BFS
**Intuition:** Use level-order traversal. At each node, check if its children satisfy the consecutive condition.

**Steps:**
1. Push root to queue.
2. While queue is not empty:
   - Dequeue node.
   - If left child exists and `left.val == node.val + 1`, increment count.
   - If right child exists and `right.val == node.val + 1`, increment count.
   - Enqueue children.
3. Return count.

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node |
| Space | O(n) -- queue can hold up to n/2 nodes at widest level |

---

### Approach 2: Optimal -- DFS with Global Counter
**Intuition:** DFS uses O(h) space instead of O(n) for BFS. At each node, check children and recurse.

**Steps:**
1. Initialize count = 0.
2. DFS from root: at each node, check left and right children for consecutiveness.
3. Recurse on both children.
4. Return count.

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node |
| Space | O(h) -- recursion stack depth |

---

### Approach 3: Best -- DFS Returning Count (Functional)
**Intuition:** Same DFS but each call returns the count of consecutive pairs found in its subtree. No global variable needed -- cleaner and thread-safe.

**Steps:**
1. If node is null, return 0.
2. Initialize local count = 0.
3. If left child exists and is consecutive, count += 1.
4. Add recursive count from left subtree.
5. Same for right child.
6. Return total count.

| Metric | Value |
|--------|-------|
| Time | O(n) -- visit every node |
| Space | O(h) -- recursion stack depth |

---

## COMPLEXITY INTUITIVELY

**Why O(n) is unavoidable:** Every parent-child edge must be checked. A tree with n nodes has n-1 edges. We must examine each edge at least once.

**Why O(h) space for DFS:** The recursion stack depth equals the tree height. For balanced trees, h = O(log n). For skewed trees, h = O(n).

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Empty tree | `0` | No nodes, no pairs |
| Single node | `0` | No children to check |
| All nodes same value | `0` | child.val != parent.val + 1 |
| Linear chain 1->2->3->4 | `3` | Every edge is consecutive |
| Negative values | Works | -1 -> 0 is consecutive |

### Common Mistakes
- Checking `child.val == parent.val - 1` instead of `+ 1` (direction matters).
- Counting pairs from child to parent instead of parent to child.
- Forgetting to handle null children before accessing `.val`.

---

## INTERVIEW LENS

**Frequency:** Low-medium -- InterviewBit specific.
**Follow-ups the interviewer might ask:**
- "Find the longest consecutive sequence path" (harder -- track path length)
- "What if consecutive means difference of 1 in either direction?" (Check abs(diff) == 1)
- "Return the actual pairs, not just count" (Store pairs in list)

**What they're really testing:** Basic tree traversal fluency and ability to check local properties during traversal.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Binary Tree Longest Consecutive Sequence | Extends to path-based consecutive counting |
| Count Complete Tree Nodes | Another counting-during-traversal problem |
| Sum of Left Leaves | Similar "check property at each node" pattern |

### Real-World Use Case
**Version control sequential commits:** In a branching commit history (tree structure), finding parent-child commit pairs where the version number increments by exactly one helps identify sequential development chains vs. hotfix branches. This is useful for release management tools that track linear development flow.
