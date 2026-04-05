# Generate All Subsequences

> **Batch 4 of 12** | **Topic:** Recursion & Backtracking (Subsequences) | **Difficulty:** MEDIUM | **XP:** 25

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given an array (or string) of `n` elements, generate **all** possible subsequences. A subsequence is any subset of elements taken in their original relative order (but not necessarily contiguous). The empty subsequence is included. There are exactly `2^n` subsequences.

### Examples

| # | Input | All Subsequences | Count |
|---|-------|-----------------|-------|
| 1 | [1, 2, 3] | [], [1], [2], [3], [1,2], [1,3], [2,3], [1,2,3] | 8 (2^3) |
| 2 | [1, 2] | [], [1], [2], [1,2] | 4 (2^2) |
| 3 | [5] | [], [5] | 2 (2^1) |
| 4 | [] | [[]] | 1 (2^0) |
| 5 | "ab" | "", "a", "b", "ab" | 4 |

### Real-Life Analogy
You are at a **buffet** with n dishes. For each dish, you make a binary decision: **take it** or **leave it**. Every possible combination of dishes you could put on your plate is a subsequence. With 3 dishes, you have 2x2x2 = 8 possible plates (including the empty plate). The order you pick them up stays the same as the order they are laid out on the buffet.

### Three Key Observations (the "Aha!" Moments)
1. **Include/Exclude pattern** -- At each index, you have exactly two choices: include the current element in the subsequence or exclude it. This binary decision tree has 2^n leaves, one per subsequence.
2. **Recursion depth = n** -- You process n elements, making one decision per element. The recursion tree is a complete binary tree of depth n.
3. **Bit manipulation as alternative** -- Each subsequence corresponds to an n-bit binary number. Bit `j` being set means "include element j." Iterate from 0 to 2^n - 1 to enumerate all subsequences.

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | Iterative bit masking (0 to 2^n - 1) | Bitmask loop |
| Optimal | Recursive include/exclude with backtracking | Recursion + list |
| Best | Recursive with explicit list building (no backtracking) | Recursion + immutable copies |

All three generate the same 2^n subsequences. The "best" is about clean code and ease of understanding, not asymptotic improvement (2^n is inherent).

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- Iterative Bit Masking

**Intuition:** There are 2^n subsequences. Each integer from 0 to 2^n - 1, when written in binary, represents one subsequence. If bit `j` is set, include `arr[j]`.

**BUD Analysis:**
- **B**ottleneck: 2^n subsequences, each up to length n -> O(n * 2^n) total work.
- **U**nnecessary work: None -- every subsequence must be generated.
- **D**uplicate work: None.

**Steps:**
1. For `mask` from 0 to `2^n - 1`:
   - Initialize empty list `subseq`.
   - For `j` from 0 to n-1:
     - If bit `j` of `mask` is set, append `arr[j]` to `subseq`.
   - Add `subseq` to the result.

**Dry-Run Trace** (arr=[1,2,3]):
```
mask=000(0): []         mask=001(1): [1]
mask=010(2): [2]        mask=011(3): [1,2]
mask=100(4): [3]        mask=101(5): [1,3]
mask=110(6): [2,3]      mask=111(7): [1,2,3]
```

| Metric | Value |
|--------|-------|
| Time | O(n * 2^n) |
| Space | O(n * 2^n) for storing all subsequences |

---

### Approach 2: Optimal -- Recursive Include/Exclude with Backtracking

**Intuition:** At each index `i`, make two recursive calls: one that includes `arr[i]` and one that excludes it. Use a mutable `current` list and backtrack (remove the element) after the include-call returns.

**BUD Analysis:**
- Same time complexity as bit masking: O(n * 2^n).
- Space: O(n) for the recursion stack + O(n) for the `current` list (not counting output storage).
- Backtracking reuses the same list, reducing memory allocation.

**Steps:**
1. Define `generate(i, current)`:
   - Base case: `i == n` -> add a copy of `current` to results. Return.
   - **Include:** Append `arr[i]` to `current`. Call `generate(i+1, current)`. Remove last element (backtrack).
   - **Exclude:** Call `generate(i+1, current)` without appending.
2. Call `generate(0, [])`.

**Dry-Run Trace** (arr=[1,2,3]):
```
generate(0, [])
├── Include 1: generate(1, [1])
│   ├── Include 2: generate(2, [1,2])
│   │   ├── Include 3: generate(3, [1,2,3]) -> ADD [1,2,3]
│   │   └── Exclude 3: generate(3, [1,2])   -> ADD [1,2]
│   └── Exclude 2: generate(2, [1])
│       ├── Include 3: generate(3, [1,3])    -> ADD [1,3]
│       └── Exclude 3: generate(3, [1])      -> ADD [1]
└── Exclude 1: generate(1, [])
    ├── Include 2: generate(2, [2])
    │   ├── Include 3: generate(3, [2,3])    -> ADD [2,3]
    │   └── Exclude 3: generate(3, [2])      -> ADD [2]
    └── Exclude 2: generate(2, [])
        ├── Include 3: generate(3, [3])      -> ADD [3]
        └── Exclude 3: generate(3, [])       -> ADD []
```

| Metric | Value |
|--------|-------|
| Time | O(n * 2^n) |
| Space | O(n) recursion stack (not counting output) |

---

### Approach 3: Best -- Cascading (Iterative Build-Up)

**Intuition:** Start with `result = [[]]` (just the empty subsequence). For each element in the array, take every existing subsequence and create a new one by appending the current element. Add all new subsequences to the result.

This is neither recursive nor bit-based -- it builds subsequences incrementally. It is intuitive and avoids recursion overhead.

**Steps:**
1. `result = [[]]`.
2. For each element `x` in `arr`:
   - `new_subseqs = [subseq + [x] for subseq in result]`.
   - `result.extend(new_subseqs)`.
3. Return `result`.

**Dry-Run Trace** (arr=[1,2,3]):
```
Start: [[]]
After x=1: [[], [1]]
After x=2: [[], [1], [2], [1,2]]
After x=3: [[], [1], [2], [1,2], [3], [1,3], [2,3], [1,2,3]]
```

| Metric | Value |
|--------|-------|
| Time | O(n * 2^n) |
| Space | O(n * 2^n) for all subsequences |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(n * 2^n) is unavoidable:** There are 2^n subsequences. The average subsequence length is n/2 (each element has a 50% chance of being included). So the total output size is O(n/2 * 2^n) = O(n * 2^n). Any algorithm that produces all subsequences must do at least this much work.

**Why 2^n grows fast:** For n=20, 2^n = ~1 million (feasible). For n=30, 2^n = ~1 billion (likely too slow). This problem is inherently exponential -- there is no polynomial-time shortcut.

**Recursion depth:** O(n), which is fine for n up to a few thousand (stack limit).

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Empty array | [[]] | Must include the empty subsequence |
| Single element [5] | [[], [5]] | Two subsequences, not one |
| Duplicate elements [1,1] | [[], [1], [1], [1,1]] | Subsequences may have duplicates (this is correct) |
| Large n (e.g., 20) | 2^20 = 1M subsequences | Memory and time limits |
| String input "abc" | "", "a", "b", "c", "ab", "ac", "bc", "abc" | Same logic, just with characters |

**Common Mistakes:**
- Forgetting to copy the `current` list before adding to results (mutation bug -- all results point to the same list).
- Not including the empty subsequence.
- Confusing subsequences with subsets (subsequences preserve order, but for arrays they are equivalent).
- Confusing subsequences with subarrays (subarrays are contiguous).

---

## 6. INTERVIEW LENS

**Why interviewers ask this:** It is the foundation for a huge family of problems: subset sum, combination sum, partition problems, and more. If you master the include/exclude pattern here, you can solve dozens of variations.

**Follow-ups to expect:**
- "Generate only subsequences of length k." -> Add a `remaining` parameter; prune when too few or too many elements are left.
- "Generate subsequences with a target sum." -> Add a `currentSum` parameter; prune when sum exceeds target.
- "What about duplicates in the input?" -> Sort the array first and skip consecutive duplicates at each level (Subsets II, LC #90).
- "Print subsequences in lexicographic order." -> Sort the input, then the include/exclude order naturally produces sorted output.

**Talking points:**
- Explain the binary decision tree clearly -- this is the mental model for all backtracking.
- Time complexity: emphasize that exponential is inherent, not a sign of a bad algorithm.
- Mention the bit-masking approach as an iterative alternative.

---

## 7. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Subsets (LC #78) | Identical problem (subsets = subsequences for arrays) |
| Subsets II (LC #90) | Handle duplicates: sort + skip |
| Combination Sum (LC #39) | Subsequences with a target sum constraint |
| Power Set | Mathematical name for the set of all subsequences |
| Generate Parentheses (LC #22) | Same include/exclude recursion structure |
| Letter Combinations of Phone (LC #17) | Multi-way branching instead of binary |
