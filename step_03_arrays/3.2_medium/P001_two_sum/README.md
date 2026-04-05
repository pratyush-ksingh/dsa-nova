# Two Sum

> **Batch 1 of 12** | **Topic:** Arrays | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers `nums` and an integer `target`, return the **indices** of the two numbers such that they add up to `target`.

You may assume that each input has **exactly one solution**, and you may not use the same element twice. You can return the answer in any order.

**Constraints:**
- `2 <= nums.length <= 10^4`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`
- Only one valid answer exists.

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `nums = [2, 7, 11, 15], target = 9` | `[0, 1]` | nums[0] + nums[1] = 2 + 7 = 9 |
| `nums = [3, 2, 4], target = 6` | `[1, 2]` | nums[1] + nums[2] = 2 + 4 = 6 |
| `nums = [3, 3], target = 6` | `[0, 1]` | nums[0] + nums[1] = 3 + 3 = 6 |

### Real-Life Analogy
> *Imagine you are at a store with a gift card worth exactly $9. You walk down the aisle looking at price tags. When you pick up an item priced $2, you immediately think: "I need a $7 item to use up my gift card exactly." So you check if you have already seen a $7 item. If yes, you are done. If not, you remember the $2 item and move on. This "remember what you need" strategy is exactly how the hash map approach works.*

### Key Observations
1. For each element `x`, we need to find if `target - x` exists somewhere else in the array.
2. Brute force checks all pairs -- O(n^2). We are doing repeated lookups that a hash map can speed up.
3. A hash map lets us check "have I seen `target - x` before?" in O(1). <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- We need fast lookup of previously seen values. A **Hash Map** (dictionary) gives us O(1) average lookup.
- You might think sorting + two pointers works, but sorting destroys the original indices. You would need to store index info separately, adding complexity.

### Pattern Recognition
- **Pattern:** Complement Lookup (for each element, check if its complement exists)
- **Classification Cue:** "When you see _find two elements that satisfy a condition involving their sum/difference_ in a problem --> think _hash map for O(1) complement lookup_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Nested Loops)
**Idea:** Check every pair of elements to see if they sum to the target.

**Steps:**
1. For each index `i` from 0 to n-2:
   - For each index `j` from i+1 to n-1:
     - If `nums[i] + nums[j] == target`, return `[i, j]`.

**Why it works:** We exhaustively check all possible pairs.

**Why we move on:** **Duplicated work** -- for each element, we re-scan the rest of the array. We are repeatedly asking "does value X exist?" which is a classic use case for a hash map.

| Time | Space |
|------|-------|
| O(n^2) | O(1) |

### Approach 2: Optimal -- Hash Map (Two-Pass)
**What changed:** Build a hash map of `{value: index}` first, then scan again looking up complements.

**Steps:**
1. Pass 1: Build a hash map mapping each value to its index.
2. Pass 2: For each element `nums[i]`, compute `complement = target - nums[i]`. Check if `complement` is in the map AND its index is not `i`.
3. Return `[i, map[complement]]`.

| Time | Space |
|------|-------|
| O(n) | O(n) |

### Approach 3: Best -- Hash Map (Single-Pass)
**Core idea in one sentence:** "As we iterate, check if the complement was already seen; if not, store the current element for future lookups."

**Steps:**
1. Create an empty hash map `seen = {}`.
2. For each index `i`:
   - Compute `complement = target - nums[i]`.
   - If `complement` is in `seen`, return `[seen[complement], i]`.
   - Otherwise, store `seen[nums[i]] = i`.
3. (Problem guarantees a solution exists, so we will always return inside the loop.)

**Dry Run:** Input = `[2, 7, 11, 15]`, target = 9

| Step | i | nums[i] | complement | seen (before) | Found? | Action |
|------|---|---------|------------|---------------|--------|--------|
| 1    | 0 | 2       | 7          | {}            | No     | Store {2: 0} |
| 2    | 1 | 7       | 2          | {2: 0}        | Yes!   | Return [0, 1] |

**Result:** `[0, 1]`

**Dry Run 2:** Input = `[3, 2, 4]`, target = 6

| Step | i | nums[i] | complement | seen (before) | Found? | Action |
|------|---|---------|------------|---------------|--------|--------|
| 1    | 0 | 3       | 3          | {}            | No     | Store {3: 0} |
| 2    | 1 | 2       | 4          | {3: 0}        | No     | Store {3: 0, 2: 1} |
| 3    | 2 | 4       | 2          | {3: 0, 2: 1}  | Yes!   | Return [1, 2] |

**Result:** `[1, 2]`

| Time | Space |
|------|-------|
| O(n) | O(n) |

*Note:* Same Big-O as Approach 2, but only one pass instead of two.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We visit each element at most once. Each hash map lookup and insertion is O(1) on average."
**Space:** O(n) -- "We store up to n elements in the hash map in the worst case (answer is the last pair)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Using the same index twice --> e.g., `nums = [3, 2, 4], target = 6`. If you check `nums[0] + nums[0]`, you get 6 but that uses index 0 twice. The complement lookup must ensure different indices.
2. Forgetting negative numbers --> `target - nums[i]` can be negative; the hash map handles this naturally.
3. Duplicate values --> `[3, 3], target = 6`. The single-pass approach handles this correctly because when we process the second 3, the first 3 is already in the map with a different index.

### Edge Cases to Test
- [ ] Minimum size array `[1, 2], target = 3`
- [ ] Duplicate values `[3, 3], target = 6`
- [ ] Negative numbers `[-1, -2, -3, -4, -5], target = -8`
- [ ] Zero in array `[0, 4, 3, 0], target = 0`
- [ ] Answer at the very end of the array
- [ ] Large values near constraint boundaries

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Is there exactly one solution? Can I use the same element twice? What should I return -- values or indices?"
2. **Approach:** "Brute force checks all pairs in O(n^2). I can optimize to O(n) using a hash map: for each element, I check if its complement (target minus current) has been seen before."
3. **Code:** Narrate while writing. Say: "I iterate once. For each number, I compute the complement. If it is in my map, I have found the pair. Otherwise, I store the current number and its index."
4. **Test:** Walk through `[2, 7, 11, 15]` with target 9, showing the map grow.

### Follow-Up Questions
- "What if there are multiple valid pairs?" --> Return all pairs; same approach, but do not return early -- collect results.
- "What if the array is sorted?" --> Use two pointers: left + right, O(n) time, O(1) space.
- "What about Three Sum?" --> Sort + fix one element + two pointers on the rest, O(n^2).

---

## CONNECTIONS
- **Prerequisite:** Hash Map basics (put/get in O(1))
- **Same Pattern:** Two Sum II (sorted array, two pointers), Four Sum, Subarray Sum Equals K
- **Harder Variant:** 3Sum (LeetCode #15), 4Sum (LeetCode #18)
- **This Unlocks:** Understanding the complement-lookup pattern, foundation for countless hash map problems
