# Connect N Ropes with Minimum Cost

> **Batch 4 of 12** | **Topic:** Heaps | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given `n` ropes of different lengths, connect all ropes into one rope. The **cost** of connecting two ropes is the **sum of their lengths**. Find the **minimum total cost** to connect all ropes.

### Examples

**Example 1:**
```
ropes = [4, 3, 2, 6]

Greedy (always connect two shortest):
  Connect 2+3 = 5, cost = 5.   Remaining: [4, 5, 6]
  Connect 4+5 = 9, cost = 9.   Remaining: [6, 9]
  Connect 6+9 = 15, cost = 15. Remaining: [15]
  Total cost = 5 + 9 + 15 = 29

Wrong order (connect longest first):
  Connect 4+6 = 10, cost = 10. Remaining: [3, 2, 10]
  Connect 3+10 = 13, cost = 13. Remaining: [2, 13]
  Connect 2+13 = 15, cost = 15.
  Total cost = 10 + 13 + 15 = 38 (worse!)
```

**Example 2:**
```
ropes = [1, 2, 3]
Connect 1+2 = 3, cost = 3.  Remaining: [3, 3]
Connect 3+3 = 6, cost = 6.
Total = 9
```

### Real-Life Analogy
You are at a lumber yard merging wooden planks. To join two planks, the carpenter charges by total length of the two planks being joined. A longer combined plank costs more in every future merge it participates in. So you want to delay merging long pieces -- always join the two shortest pieces first, like Huffman coding minimizes weighted path length.

### 3 Key Observations
1. **"Aha!" -- Longer ropes participate in more future costs:** A rope connected early gets its length added to every subsequent merge. So shorter ropes should be merged first to minimize their contribution to future costs.
2. **"Aha!" -- This is Huffman coding:** The optimal merge order builds a Huffman tree. Always merge the two smallest frequencies (lengths) first. The total cost equals the weighted external path length.
3. **"Aha!" -- Min-heap makes this efficient:** We need to repeatedly extract the two smallest ropes and insert their sum back. A min-heap does both operations in O(log N).

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Sorted array | Sort, merge two smallest, re-sort or re-insert | Correct but slow re-sorting |
| Optimal | Min-Heap | Extract min twice, insert sum, repeat | O(N log N) total |
| Best | Min-Heap (same) | Same as optimal with minor optimizations | Already optimal |

---

## APPROACH LADDER

### Brute Force: Sort + Linear Insert
**Intuition:** Sort the array. Merge the two smallest (first two). Insert the result back in sorted position. Repeat.

**Steps:**
1. Sort the array
2. While more than 1 rope:
   a. Remove first two elements (smallest)
   b. Compute cost = sum of the two
   c. Add cost to total
   d. Insert the merged rope back into sorted position
3. Return total cost

**BUD Analysis:**
- **B**ottleneck: Re-inserting into sorted position is O(N) per merge
- **U**nnecessary: Re-sorting when a heap can maintain order efficiently
- **D**uplicate: None

**Dry-Run Trace ([4,3,2,6]):**
```
Sort: [2, 3, 4, 6]
Merge 2+3=5, total=5. Insert 5: [4, 5, 6]
Merge 4+5=9, total=14. Insert 9: [6, 9]
Merge 6+9=15, total=29. Done!
```

### Optimal: Min-Heap Greedy
**Intuition:** Use a min-heap. Extract-min twice, merge, add cost, insert back. Heap maintains the "two smallest" invariant efficiently.

**Steps:**
1. Build min-heap from all rope lengths
2. While heap size > 1:
   a. Extract two minimums: a = extractMin(), b = extractMin()
   b. merged = a + b
   c. totalCost += merged
   d. Insert merged back into heap
3. Return totalCost

### Best: Same Min-Heap (Already Optimal)
**Intuition:** The min-heap greedy is already the optimal algorithm. We can add a small optimization: use heapify O(N) instead of N insertions O(N log N) for initial build.

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N^2) | O(1) | N-1 merges, each needs O(N) insertion into sorted array |
| Optimal | O(N log N) | O(N) | N-1 merges, each with O(log N) heap operations |
| Best | O(N log N) | O(N) | Same as optimal (this IS the lower bound for comparison-based) |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| Single rope | 0 | No merging needed |
| Two ropes | sum of both | Just one merge |
| All ropes same length | Still use greedy | Order doesn't matter when equal |
| One rope of length 0 | Works normally | Merging with 0-length rope costs 0+other |
| Very large N | Heap handles it | O(N log N) scales well |

**Common Mistakes:**
- Forgetting to insert the merged rope back into the heap
- Using max-heap instead of min-heap (would give worst cost, not best)
- Returning the final rope length instead of the accumulated cost
- Not handling single rope case (answer is 0, not the rope length)

---

## INTERVIEW LENS

**Why interviewers ask this:** Tests greedy intuition and heap fluency. The connection to Huffman coding is a great "aha" moment that shows deep algorithmic understanding.

**What they want to see:**
- Greedy insight: always merge the two shortest
- Heap as the tool for efficient repeated min-extraction
- Proof of correctness via exchange argument or Huffman connection

**Follow-ups to prepare for:**
- Huffman Encoding
- Merge K Sorted Lists (similar heap pattern)
- Minimum Cost to Merge Stones (DP variant with constraint k)

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Huffman Coding | Identical algorithm -- merge two lowest frequencies |
| Merge K Sorted Lists | Same heap pattern: repeatedly merge smallest |
| Minimum Cost to Merge Stones | Generalized version (merge k at a time) -- needs DP |
| Optimal BST | Both involve minimizing weighted costs in a tree structure |
