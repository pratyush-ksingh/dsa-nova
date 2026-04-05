# Fruits into Baskets

> **Batch 3 of 12** | **Topic:** Sliding Window | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
You are visiting a farm with a row of fruit trees represented by an integer array `fruits`, where `fruits[i]` is the type of fruit the `i`-th tree produces. You have **two baskets**, and each basket can only hold **one type** of fruit (unlimited quantity). Starting from any tree, you pick exactly one fruit from each tree moving to the right, and you must stop when you encounter a third type of fruit. Return the **maximum number of fruits** you can pick.

Equivalently: find the length of the longest contiguous subarray that contains **at most 2 distinct** values.

**LeetCode #904**

**Constraints:**
- `1 <= fruits.length <= 10^5`
- `0 <= fruits[i] < fruits.length`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `fruits = [1,2,1]` | `3` | Pick all trees: types {1, 2} |
| `fruits = [0,1,2,2]` | `3` | Pick trees [1,2,2]: types {1, 2} |
| `fruits = [1,2,3,2,2]` | `4` | Pick trees [2,3,2,2]: types {2, 3} |
| `fruits = [3,3,3,1,2,1,1,2,3,3,4]` | `5` | Pick [1,2,1,1,2]: types {1, 2} |

### Real-Life Analogy
> *You are walking along a buffet table with two plates. Each plate can hold only one type of dish. You move left to right, scooping one item from each station. The moment you hit a third type, you have to stop (or go back and restart from a better position). Your goal is to find the longest stretch of the buffet where only 2 dish types appear.*

### Key Observations
1. This is exactly the "longest subarray with at most K distinct elements" problem with K=2. <-- This is the "aha" insight
2. A sliding window with a hashmap tracking frequencies of each fruit type in the window is the natural approach.
3. When the number of distinct types exceeds 2, shrink from the left until we are back to 2 types.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- A **HashMap** (or frequency array) tracks how many of each fruit type are in the current window. Adding/removing and counting distinct types are all O(1).
- A **sliding window** (two pointers) processes each element at most twice, giving O(n) overall.

### Pattern Recognition
- **Pattern:** Sliding Window with at most K distinct elements
- **Classification Cue:** "Whenever you see _longest subarray with at most K distinct_ --> sliding window + hashmap."

---

## APPROACH LADDER

### Approach 1: Brute Force -- Check All Subarrays
**Idea:** For every starting index, extend as far as possible while using at most 2 types.

**Steps:**
1. For each `i` from 0 to n-1:
   - Track distinct types using a set.
   - For each `j >= i`:
     - Add `fruits[j]` to the set.
     - If the set size > 2, break.
     - Update answer: `ans = max(ans, j - i + 1)`.
2. Return `ans`.

**BUD Transition -- Bottleneck:** When we break out of the inner loop, we start fresh at `i+1`, re-counting everything. The sliding window avoids this by reusing the left pointer.

| Time | Space |
|------|-------|
| O(n^2) | O(1) since at most 3 types |

### Approach 2: Optimal -- Sliding Window with HashMap
**What changed:** Maintain a window `[left, right]` and a frequency map. Expand `right`, and when distinct types exceed 2, shrink from `left`.

**Steps:**
1. Initialize `freq = {}`, `left = 0`, `ans = 0`.
2. For each `right` from 0 to n-1:
   - `freq[fruits[right]]++`.
   - While `len(freq) > 2`:
     - `freq[fruits[left]]--`. If it becomes 0, remove the key.
     - `left++`.
   - `ans = max(ans, right - left + 1)`.
3. Return `ans`.

**Dry Run:** `fruits = [1, 2, 3, 2, 2]`

| right | fruit | freq | distinct | left | window | ans |
|-------|-------|------|----------|------|--------|-----|
| 0 | 1 | {1:1} | 1 | 0 | [1] | 1 |
| 1 | 2 | {1:1,2:1} | 2 | 0 | [1,2] | 2 |
| 2 | 3 | {1:1,2:1,3:1} | 3 | 0 | shrink! | 2 |
| | | {2:1,3:1} | 2 | 1 | [2,3] | 2 |
| 3 | 2 | {2:2,3:1} | 2 | 1 | [2,3,2] | 3 |
| 4 | 2 | {2:3,3:1} | 2 | 1 | [2,3,2,2] | 4 |

**Result:** 4

| Time | Space |
|------|-------|
| O(n) | O(1) -- at most 3 keys in map |

### Approach 3: Best -- Non-Shrinking Sliding Window
**What changed:** Same as the non-shrinking optimization from character replacement. Instead of a `while` loop to shrink, use an `if` to slide by 1. The window never shrinks below a previous best, so the final answer is `n - left`.

**Steps:**
1. Initialize `freq = {}`, `left = 0`.
2. For each `right` from 0 to n-1:
   - `freq[fruits[right]]++`.
   - If `len(freq) > 2`:
     - `freq[fruits[left]]--`. If 0, remove key.
     - `left++`.
3. Return `n - left`.

**Why this works:** We only care about whether a larger valid window exists. If the current window is invalid, we slide it (maintaining its size) rather than shrinking. Once a valid window of size W is found, we never go smaller.

| Time | Space |
|------|-------|
| O(n) | O(1) |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "Each element is added to the window once (right pointer) and removed at most once (left pointer). Total work is 2n = O(n)."
**Space:** O(1) -- "The hashmap holds at most 3 entries at any time (we shrink when it exceeds 2)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to remove a key from the map when its count drops to 0 -- this inflates the distinct count.
2. Using a set instead of a frequency map -- a set cannot tell you when a fruit type fully leaves the window.
3. Off-by-one: the window length is `right - left + 1`, not `right - left`.

### Edge Cases to Test
- [ ] All same fruit type (answer is n)
- [ ] Exactly 2 types (answer is n)
- [ ] Alternating types: `[1,2,1,2,1]` (answer is n)
- [ ] Single element array (answer is 1)
- [ ] Every element is different: `[1,2,3,4,5]` (answer is 2)

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "I need the longest contiguous subarray with at most 2 distinct values."
2. **Match:** "At most K distinct in a subarray --> sliding window + hashmap, K=2."
3. **Plan:** "Expand the window with the right pointer. When distinct types exceed 2, shrink from the left. Track max window size."
4. **Implement:** Use a frequency map. Be careful to remove keys when count hits 0.
5. **Review:** Walk through the dry run.
6. **Evaluate:** "O(n) time, O(1) space since the map has at most 3 entries."

### Follow-Up Questions
- "What if you had K baskets instead of 2?" --> Same algorithm, just change the threshold from 2 to K. This is LC #340 (Longest Substring with At Most K Distinct Characters).
- "What if you need the starting index too?" --> Track `bestLeft` and `bestRight` alongside `ans`.
- "What if the array is very large and streamed?" --> Sliding window works perfectly with streaming since we only look at the current window.

---

## CONNECTIONS
- **Prerequisite:** Two-pointer technique, HashMap basics
- **Same Pattern:** Longest Substring with At Most K Distinct Characters (LC #340), Longest Substring Without Repeating Characters (LC #3)
- **Harder Variant:** Minimum Window Substring (LC #76), Subarrays with K Different Integers (LC #992)
- **This Unlocks:** All "at most K distinct" sliding window problems
