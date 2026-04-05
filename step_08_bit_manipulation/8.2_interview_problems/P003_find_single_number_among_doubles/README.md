# Find Single Number Among Doubles

> **Batch 2 of 12** | **Topic:** Bit Manipulation | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given a non-empty array of integers `nums` where every element appears **exactly twice** except for one element which appears **exactly once**, find that single element.

You must implement a solution with **O(n)** time and **O(1)** extra space.

**LeetCode #136**

**Constraints:**
- `1 <= nums.length <= 3 * 10^4`
- `-3 * 10^4 <= nums[i] <= 3 * 10^4`
- Every element appears twice except for one

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[2, 2, 1]` | `1` | 1 appears once, 2 appears twice |
| `[4, 1, 2, 1, 2]` | `4` | 4 appears once |
| `[1]` | `1` | Only one element |

### Real-Life Analogy
> *Imagine you have a bag of socks. Every sock has an identical twin somewhere in the bag -- except one lonely sock. You need to find the unpaired sock. One way is to pair them up (sorting or hashing). But the XOR trick is like a magic pairing machine: you feed socks in one at a time, and whenever a matching pair enters, they cancel each other out and vanish. Whatever remains at the end is the unpaired sock.*

### Key Observations
1. Sorting would group duplicates together, but that costs O(n log n).
2. A hash map can count occurrences in O(n) time but uses O(n) space.
3. XOR has the property: `a ^ a = 0` and `a ^ 0 = a`. XOR-ing all elements cancels every pair, leaving only the single number. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- XOR is **commutative** (`a ^ b = b ^ a`) and **associative** (`(a ^ b) ^ c = a ^ (b ^ c)`), so the order does not matter.
- Every paired element XORs to 0, and `0 ^ single = single`.
- No extra data structure needed -- just one variable.

### Pattern Recognition
- **Pattern:** XOR Cancellation (pair elimination)
- **Classification Cue:** "When you see _every element appears twice except one_ --> think _XOR all elements_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Nested Loop)
**Idea:** For each element, scan the entire array to check if it has a duplicate.

**Steps:**
1. For each element `nums[i]`, iterate through the array to count its occurrences.
2. If an element appears exactly once, return it.

**Why we move on:** **Bottleneck** -- O(n^2) time due to the nested loop.

| Time | Space |
|------|-------|
| O(n^2) | O(1) |

### Approach 2: Sorting
**Idea:** Sort the array. Pairs sit adjacent. The unpaired element stands alone.

**Steps:**
1. Sort `nums`.
2. Check pairs at indices (0,1), (2,3), (4,5), ...
3. If `nums[i] != nums[i+1]`, then `nums[i]` is the single element.
4. If no mismatch found in pairs, the last element is the answer.

**Why we move on:** **Unnecessary work** -- sorting is O(n log n) and we can do better.

| Time | Space |
|------|-------|
| O(n log n) | O(1) if in-place sort |

### Approach 3: Hash Map
**Idea:** Count occurrences using a hash map.

**Steps:**
1. Build a frequency map of all elements.
2. Return the element with frequency 1.

**Why we move on:** **Duplicate work** -- we store every element just to find one. Uses O(n) extra space.

| Time | Space |
|------|-------|
| O(n) | O(n) |

### Approach 4: Best -- XOR Cancellation
**What changed:** Exploit the mathematical properties of XOR to eliminate pairs without any extra storage.

**Steps:**
1. Initialize `result = 0`.
2. For each element `x` in `nums`: `result = result ^ x`.
3. Return `result`.

**Why it works:**
- `a ^ a = 0` (same numbers cancel)
- `a ^ 0 = a` (XOR with 0 is identity)
- Order does not matter (commutative + associative)
- After XOR-ing all elements, every pair cancels to 0, leaving only the single number.

**Dry Run:** Input = `[4, 1, 2, 1, 2]`

| Step | x | result (before) | result ^ x | result (after) | Binary |
|------|---|-----------------|------------|----------------|--------|
| Init | - | 0               | -          | 0              | `000` |
| 1    | 4 | 0               | 0 ^ 4     | 4              | `100` |
| 2    | 1 | 4               | 4 ^ 1     | 5              | `101` |
| 3    | 2 | 5               | 5 ^ 2     | 7              | `111` |
| 4    | 1 | 7               | 7 ^ 1     | 6              | `110` |
| 5    | 2 | 6               | 6 ^ 2     | 4              | `100` |

**Result:** 4

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We iterate through the array once, performing a single XOR per element."
**Space:** O(1) -- "We use one integer variable `result`, regardless of input size."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Initializing `result` to `nums[0]` instead of `0` and then starting the loop from index 1 -- works but less clean.
2. Thinking XOR only works for positive numbers -- it works for negative numbers too, since XOR operates on the bit representation.
3. Forgetting the constraint that every other element appears exactly twice -- XOR cancellation does NOT work if elements appear 3 or more times.

### Edge Cases to Test
- [ ] Single element array `[42]` --> return 42
- [ ] Single element is negative `[-1, 3, 3]` --> return -1
- [ ] Large array where single is at the very end
- [ ] Large array where single is at the very start

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Every element appears exactly twice except one? Can elements be negative?"
2. **Approach:** "I could sort (O(n log n)) or use a hash map (O(n) space), but XOR gives us O(n) time and O(1) space. Because `a ^ a = 0`, all pairs cancel, leaving the lone element."
3. **Code:** Write the three-line solution. Emphasize starting result at 0.
4. **Test:** Walk through `[4, 1, 2, 1, 2]` showing XOR at each step.

### Follow-Up Questions
- "What if every element appears three times except one?" --> Use bit counting: count 1s at each bit position modulo 3.
- "What if there are two unique elements?" --> XOR all to get `a ^ b`, then split into two groups by a set bit. (LeetCode #260)

---

## CONNECTIONS
- **Prerequisite:** XOR properties (identity, self-inverse, commutativity, associativity)
- **Same Pattern:** Missing Number (XOR with indices), Single Number II (modulo 3 bit counting)
- **Harder Variant:** Single Number II (every element 3x except one), Single Number III (two unique elements)
- **This Unlocks:** Understanding XOR as a pairing/cancellation tool, applicable in many bit manipulation problems
