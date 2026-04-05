# Floor in BST

> **Batch 4 of 12** | **Topic:** Binary Search Trees | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a BST and a key value, find the **floor** of the key. The floor is the **largest value in the BST that is less than or equal to the key**. If no such value exists, return -1.

### Examples

**Example 1:**
```
       10
      /  \
     5    15
    / \   / \
   2   7 12  20

Key = 13 -> Floor = 12  (12 <= 13 and is the largest such value)
Key = 10 -> Floor = 10  (exact match)
Key = 1  -> Floor = -1  (no value <= 1)
Key = 15 -> Floor = 15  (exact match)
Key = 8  -> Floor = 7   (7 <= 8, next would be 10 > 8)
```

**Example 2:**
```
    8
   / \
  4   12

Key = 6  -> Floor = 4
Key = 15 -> Floor = 12
Key = 3  -> Floor = -1
```

### Real-Life Analogy
You have a thermostat with preset temperatures: 18, 20, 22, 25. You want the temperature "floor" for 23 degrees -- the highest preset that does not exceed 23. That would be 22. The floor function finds the "closest preset that fits under your budget."

### 3 Key Observations
1. **"Aha!" -- BST property guides the search:** If key >= node.val, this node is a candidate for floor, but a better (larger) candidate might exist in the right subtree. If key < node.val, floor must be in the left subtree.
2. **"Aha!" -- Mirror of ceil operation:** Floor goes right when node <= key (to find larger candidates). Ceil goes left when node >= key (to find smaller candidates). Exact mirror logic.
3. **"Aha!" -- One path from root to null:** We only follow one path down the tree, making this O(H) naturally. No need to explore both subtrees.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Array + Sort | Inorder traversal to sorted array, binary search | Works but overkill |
| Optimal | BST traversal (recursive) | Recursive search following BST property | Clean and intuitive |
| Best | BST traversal (iterative) | Iterative search with floor candidate tracking | O(1) space, no stack |

---

## APPROACH LADDER

### Brute Force: Inorder to Sorted Array + Binary Search
**Intuition:** BST inorder gives sorted order. Collect all values, then find the largest value <= key using binary search.

**Steps:**
1. Inorder traversal to collect sorted array
2. Binary search for key (or insertion point)
3. Floor = largest element <= key at or before the insertion point

**BUD Analysis:**
- **B**ottleneck: O(N) traversal to build the array
- **U**nnecessary: Building the whole array when we only need one value
- **D**uplicate: We're ignoring the BST structure for searching

**Dry-Run Trace (Key=13):**
```
Inorder: [2, 5, 7, 10, 12, 15, 20]
Binary search for 13: insertion point = 5 (between 12 and 15)
Floor = arr[4] = 12
```

### Optimal: Recursive BST Search
**Intuition:** At each node, decide direction based on comparison with key. Track the best floor candidate seen so far.

**Steps:**
1. If node is null, return -1 (no floor found)
2. If node.val == key, return key (exact match)
3. If node.val > key, search left subtree (current node too large)
4. If node.val < key, this node is a candidate. Search right subtree for a potentially better (larger) candidate. Return the better of the two.

### Best: Iterative BST Search
**Intuition:** Same logic as recursive but with a while loop. Track the floor candidate as we go. No recursion stack needed.

**Steps:**
1. Initialize floor = -1
2. While node is not null:
   a. If node.val == key: return key
   b. If node.val < key: update floor = node.val, go right
   c. If node.val > key: go left
3. Return floor

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N) | O(N) | Full inorder traversal + array storage |
| Optimal | O(H) | O(H) | Follow one root-to-leaf path, recursion stack |
| Best | O(H) | O(1) | Follow one path, no extra space |

*H = height of BST. For balanced BST, H = log N. For skewed, H = N.*

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| Empty tree | -1 | No values exist |
| Key smaller than minimum | -1 | No floor exists |
| Key larger than maximum | max value | Floor is the largest node |
| Exact match exists | key itself | Floor of exact match = the value |
| Key between two nodes | smaller one | E.g., key=8 in [5,7,10] -> floor=7 |

**Common Mistakes:**
- Returning the first value <= key instead of the LARGEST value <= key
- Going left when node.val < key (should go right to find larger candidates)
- Forgetting to update the floor candidate before going right

---

## INTERVIEW LENS

**Why interviewers ask this:** Fundamental BST operation. Tests whether you truly understand BST search and can adapt it beyond simple lookup.

**What they want to see:**
- Immediate O(H) approach using BST property
- Clean iterative solution preferred
- Understanding of floor vs. ceil duality

**Follow-ups to prepare for:**
- Ceil in BST (mirror operation)
- Successor and Predecessor in BST
- Closest value in BST

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Ceil in BST | Mirror: largest becomes smallest, <= becomes >= |
| Inorder Successor | Next larger value, related search pattern |
| Search in BST | Simpler version: exact match only |
| Lower Bound / Upper Bound | Same concept applied to sorted arrays |
