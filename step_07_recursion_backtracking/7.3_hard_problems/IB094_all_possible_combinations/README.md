# All Possible Combinations (Power Set)

> **Batch 3 of 12** | **Topic:** Recursion & Backtracking | **Difficulty:** EASY | **XP:** 10

---

## 1. UNDERSTAND

### Problem Statement
Given an array of **distinct** integers (or characters), generate **all possible subsets** (the power set). Each subset is a combination of elements from the input. The output should include the empty subset and the full set itself. Return the subsets in any order.

**Example:**
```
Input: [1, 2, 3]
Output: [[], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]]

Input: [1, 2]
Output: [[], [1], [2], [1,2]]
```

| Input | Output | Explanation |
|-------|--------|-------------|
| [1,2,3] | [[], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]] | 2^3 = 8 subsets |
| [1,2] | [[], [1], [2], [1,2]] | 2^2 = 4 subsets |
| [1] | [[], [1]] | 2^1 = 2 subsets |
| [] | [[]] | 2^0 = 1 subset (just the empty set) |

### Real-Life Analogy
Imagine a **pizza menu** with 3 toppings: pepperoni, mushrooms, and olives. For each topping, you have a binary choice: include it or not. "All possible combinations" is the complete menu of every pizza you could order: plain, just pepperoni, just mushrooms, just olives, pepperoni+mushrooms, pepperoni+olives, mushrooms+olives, and all three. With N toppings, there are 2^N possible pizzas. The include/exclude decision for each topping is exactly the binary choice tree that generates the power set.

### Key Observations
1. For N elements, there are exactly 2^N subsets. Each element is either included or excluded -- N binary choices.
2. The problem has a natural recursive structure: for each element, branch into "include it" and "exclude it", then solve the remaining elements.
3. **Aha moment:** Each subset maps to an N-bit binary number. Subset `{1,3}` of `[1,2,3]` maps to binary `101` (include 1, exclude 2, include 3). This gives rise to the bitmask approach: iterate integers from 0 to 2^N - 1 and use their binary representation to select elements.

### Constraints
- 0 <= nums.length <= 15 (InterviewBit) or <= 10 (some variants)
- All elements are distinct
- 2^15 = 32768 subsets maximum

---

## 2. DATA STRUCTURE & ALGORITHM CHOICE

### Why Backtracking?
We need to explore all possible include/exclude decisions for each element. Backtracking naturally models this as a decision tree, where each level corresponds to one element, and branches represent include vs. exclude.

### Pattern Recognition
**Classification cue:** "Generate all subsets / combinations / power set" --> backtracking with include/exclude at each step, OR bitmask enumeration. This is the foundational backtracking pattern. Every combination/permutation/partition problem builds on this skeleton.

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Iterative Subset Building
**Intuition:** Start with the empty set. For each element in the input, take all existing subsets and create new subsets by appending the current element to each. This "cascading" approach doubles the number of subsets at each step.

**Steps:**
1. Initialize `result = [[]]`.
2. For each element `num` in the input:
   - For each existing subset `s` in result, create `s + [num]`.
   - Add all new subsets to result.
3. Return result.

**Dry Run Trace (nums = [1, 2, 3]):**

| Step | Current element | Existing subsets | New subsets added | Result |
|------|----------------|-----------------|-------------------|--------|
| Init | - | - | - | [[]] |
| 1 | 1 | [[]] | [[1]] | [[], [1]] |
| 2 | 2 | [[], [1]] | [[2], [1,2]] | [[], [1], [2], [1,2]] |
| 3 | 3 | [[], [1], [2], [1,2]] | [[3], [1,3], [2,3], [1,2,3]] | [[], [1], [2], [1,2], [3], [1,3], [2,3], [1,2,3]] |

| Metric | Value |
|--------|-------|
| Time   | O(N * 2^N) -- 2^N subsets, each up to N elements to copy |
| Space  | O(N * 2^N) for output |

**BUD Transition:** This works but is not the classic backtracking approach. Let us express it recursively.

---

### Approach 2: Optimal -- Backtracking (Include / Exclude)
**Intuition:** Use recursion with an index. At each index, make two choices: include the element or exclude it. When the index reaches the end of the array, the current path is a complete subset.

**Steps:**
1. Define `backtrack(index, current)`.
2. If `index == len(nums)`, add a copy of `current` to result and return.
3. **Exclude:** call `backtrack(index + 1, current)`.
4. **Include:** add `nums[index]` to current, call `backtrack(index + 1, current)`, then remove it (backtrack).

**Dry Run Trace (nums = [1, 2, 3]):**
```
backtrack(0, [])
  Exclude 1 -> backtrack(1, [])
    Exclude 2 -> backtrack(2, [])
      Exclude 3 -> backtrack(3, []) => add []
      Include 3 -> backtrack(3, [3]) => add [3]
    Include 2 -> backtrack(2, [2])
      Exclude 3 -> backtrack(3, [2]) => add [2]
      Include 3 -> backtrack(3, [2,3]) => add [2,3]
  Include 1 -> backtrack(1, [1])
    Exclude 2 -> backtrack(2, [1])
      Exclude 3 -> backtrack(3, [1]) => add [1]
      Include 3 -> backtrack(3, [1,3]) => add [1,3]
    Include 2 -> backtrack(2, [1,2])
      Exclude 3 -> backtrack(3, [1,2]) => add [1,2]
      Include 3 -> backtrack(3, [1,2,3]) => add [1,2,3]
```

Result: [[], [3], [2], [2,3], [1], [1,3], [1,2], [1,2,3]]

| Metric | Value |
|--------|-------|
| Time   | O(N * 2^N) |
| Space  | O(N) recursion depth + O(N * 2^N) output |

---

### Approach 3: Best -- Bitmask Enumeration
**Intuition:** There are 2^N subsets. Each can be represented by an N-bit integer where bit `j` being 1 means "include nums[j]". Iterate from 0 to 2^N - 1 and decode each bitmask into a subset.

**Steps:**
1. Compute `total = 2^N`.
2. Loop `mask` from 0 to total - 1.
3. For each mask, iterate bits: if bit `j` is set, include `nums[j]`.
4. Add the decoded subset to result.

**Dry Run Trace (nums = [1,2,3], N=3):**

| mask (binary) | Bits set | Subset |
|---------------|----------|--------|
| 000 (0) | none | [] |
| 001 (1) | bit 0 | [1] |
| 010 (2) | bit 1 | [2] |
| 011 (3) | bits 0,1 | [1,2] |
| 100 (4) | bit 2 | [3] |
| 101 (5) | bits 0,2 | [1,3] |
| 110 (6) | bits 1,2 | [2,3] |
| 111 (7) | bits 0,1,2 | [1,2,3] |

| Metric | Value |
|--------|-------|
| Time   | O(N * 2^N) |
| Space  | O(N * 2^N) output (no recursion stack) |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(N * 2^N)?** There are 2^N subsets. Generating each subset requires up to N operations (copying elements or checking bits). The output itself has O(N * 2^N) total elements across all subsets. You cannot do better because the output size is the bottleneck.

**Why is backtracking elegant?** It naturally extends to constrained problems (e.g., subsets of a given sum, combinations of size k) by adding pruning. The bitmask approach is cleaner for unconstrained power sets but harder to prune.

---

## 5. EDGE CASES & MISTAKES

### Common Mistakes
| Mistake | Why it happens | Fix |
|---------|---------------|-----|
| Forgetting to copy `current` before adding | All subsets point to same mutable list | Add `list(current)` or `new ArrayList<>(current)` |
| Not backtracking (forgetting to remove element) | Subset keeps growing, never shrinks | Always remove last element after recursive call |
| Missing empty subset | Starting with first element included | Empty set is valid; base case adds whatever current is |
| Duplicate subsets when elements are not distinct | Problem states distinct, but watch for variants | Sort + skip duplicates in variants |

### Edge Cases Checklist
- Empty input [] --> [[]]
- Single element [5] --> [[], [5]]
- N=15 --> 32768 subsets (verify time is acceptable)

---

## 6. INTERVIEW LENS

### How to Present (UMPIRE)
1. **U**nderstand: "Generate all subsets. Elements distinct? Order of subsets matter? Order within subsets?"
2. **M**atch: "Power set -- backtracking or bitmask."
3. **P**lan: "I'll use backtracking: at each index, include or exclude, recurse. Base case: index == n."
4. **I**mplement: Show clean backtracking with add/remove.
5. **R**eview: Trace [1,2] to show 4 subsets.
6. **E**valuate: "O(N * 2^N) time and space. Cannot improve -- output size is 2^N."

### Follow-Up Questions
- "What if elements can be duplicated?" --> Sort + skip adjacent duplicates (LeetCode 90 Subsets II).
- "Only subsets of size k?" --> Combination (LeetCode 77). Add pruning to backtracking.
- "Subsets that sum to target?" --> Subset Sum problem (DP or backtracking with pruning).

---

## 7. CONNECTIONS

| Relationship | Problem |
|-------------|---------|
| **Prerequisite** | Basic recursion, include/exclude decision pattern |
| **Same pattern** | Subsets II (with duplicates), Combinations (size k) |
| **Harder variant** | Subset Sum, Combination Sum, Permutations |
| **Unlocks** | All backtracking problems; bitmask DP; combinatorial generation |
