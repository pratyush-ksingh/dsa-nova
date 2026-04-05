# Reverse an Array

> **Batch 2 of 12** | **Topic:** Basic Recursion | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
Given an array of integers, reverse the array in-place. Show the recursive two-pointer swap approach.

**Constraints:**
- `0 <= arr.length <= 10^5`
- `-10^9 <= arr[i] <= 10^9`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[1, 2, 3, 4, 5]` | `[5, 4, 3, 2, 1]` | Full reversal |
| `[1, 2, 3, 4]` | `[4, 3, 2, 1]` | Even-length array |
| `[1]` | `[1]` | Single element, already reversed |
| `[]` | `[]` | Empty array, nothing to do |
| `[1, 2]` | `[2, 1]` | Minimal swap |

### Real-Life Analogy
> *Imagine a row of students standing in line. To reverse the line, the first and last students swap positions. Then the second and second-to-last swap. Continue until you reach the middle. Each swap brings two elements to their final positions, and you only need N/2 swaps total. The recursive version is like telling the two outermost students "swap, then tell the inner pair to do the same thing."*

### Key Observations
1. Reversing is equivalent to swapping mirror elements: `arr[i]` swaps with `arr[n-1-i]` for all `i < n/2`. Only N/2 swaps are needed. <-- This is the "aha" insight
2. The recursive approach uses two pointers (`left`, `right`) and calls itself with `left+1, right-1`. Base case: when `left >= right`, the pointers have crossed and we stop.
3. In-place reversal uses O(1) extra space (just a temp variable for swapping). Creating a new reversed array uses O(n) extra space.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Data Structure?
- An **array** supports O(1) random access, so swapping `arr[left]` and `arr[right]` is instant.
- In-place modification avoids allocating a second array.

### Pattern Recognition
- **Pattern:** Two-Pointer (converging from both ends)
- **Classification Cue:** "Operate on both ends of an array and move inward --> two pointers."

---

## APPROACH LADDER

### Approach 1: Brute Force -- New Array (Extra Space)
**Idea:** Create a new array and fill it by reading the original array backwards.

**Steps:**
1. Create a new array `reversed` of size n.
2. For `i` from 0 to n-1: `reversed[i] = arr[n - 1 - i]`.
3. Copy `reversed` back to `arr` (or return `reversed`).

**Why it is suboptimal:** Uses O(n) extra space. We can reverse in-place.

**BUD Transition -- Unnecessary Work:** We allocate a whole new array. Swapping pairs in-place eliminates this extra space.

| Time | Space |
|------|-------|
| O(n) | O(n) |

### Approach 2: Optimal -- Iterative Two-Pointer Swap
**What changed:** Use two pointers, `left = 0` and `right = n - 1`. Swap `arr[left]` and `arr[right]`, then move both pointers inward. Stop when they meet.

**Steps:**
1. Set `left = 0`, `right = n - 1`.
2. While `left < right`:
   - Swap `arr[left]` and `arr[right]`.
   - `left++`, `right--`.
3. Array is reversed in-place.

**Dry Run:** `arr = [1, 2, 3, 4, 5]`

| Step | left | right | Swap | Array After |
|------|------|-------|------|-------------|
| 1    | 0    | 4     | arr[0] <-> arr[4] | [5, 2, 3, 4, 1] |
| 2    | 1    | 3     | arr[1] <-> arr[3] | [5, 4, 3, 2, 1] |
| 3    | 2    | 2     | left >= right, stop | [5, 4, 3, 2, 1] |

**Result:** [5, 4, 3, 2, 1]

| Time | Space |
|------|-------|
| O(n) | O(1) |

### Approach 3: Best -- Recursive Two-Pointer Swap
**What changed:** Same two-pointer logic expressed recursively. The recursive call replaces the loop.

**Steps:**
1. Define `reverse(arr, left, right)`.
2. Base case: if `left >= right`, return (pointers crossed or met).
3. Swap `arr[left]` and `arr[right]`.
4. Recurse: `reverse(arr, left + 1, right - 1)`.

**Trace:** `reverse([1,2,3,4,5], 0, 4)`

| Call | left | right | Swap | Array |
|------|------|-------|------|-------|
| 1    | 0    | 4     | 1 <-> 5 | [5, 2, 3, 4, 1] |
| 2    | 1    | 3     | 2 <-> 4 | [5, 4, 3, 2, 1] |
| 3    | 2    | 2     | base case | [5, 4, 3, 2, 1] |

**Note:** The recursive version uses O(n/2) = O(n) stack frames. The iterative version is preferred for large arrays, but the recursive version beautifully illustrates how recursion replaces loops.

| Time | Space |
|------|-------|
| O(n) | O(n/2) = O(n) recursion stack |

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We perform n/2 swaps. Each swap is O(1). Total: O(n)."
**Space:** O(1) iterative, O(n) recursive -- "Iterative uses one temp variable. Recursive uses n/2 stack frames."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. **Using `left <= right` instead of `left < right`:** When left == right (odd-length middle element), no swap needed. Using `<=` would try to swap an element with itself -- harmless but wasteful.
2. **Forgetting to move both pointers:** Must increment left AND decrement right.
3. **Off-by-one in new-array approach:** Indexing `arr[n-i]` instead of `arr[n-1-i]` causes out-of-bounds.

### Edge Cases to Test
- [ ] Empty array (nothing to reverse)
- [ ] Single element (already reversed)
- [ ] Two elements (one swap)
- [ ] Odd-length array (middle element stays)
- [ ] Even-length array (all elements swap)
- [ ] Array with all identical elements (should work, no visible change)
- [ ] Array with negative numbers

---

## INTERVIEW LENS

### How to Present (UMPIRE)
1. **Understand:** "Reverse an array in-place."
2. **Match:** "Two pointers from both ends, swapping and converging. Can be iterative or recursive."
3. **Plan:** "Start pointers at both ends. Swap. Move inward. Stop when they cross."
4. **Implement:** Write the iterative version first (O(1) space), then show the recursive version.
5. **Review:** Dry-run with [1,2,3,4,5] showing three steps.
6. **Evaluate:** "O(n) time, O(1) space iterative. The recursive version is O(n) space due to the call stack."

### Follow-Up Questions
- "Can you reverse a linked list?" --> Two approaches: iterative (prev, curr, next pointers) or recursive. Very common follow-up.
- "Reverse a subarray from index i to j?" --> Same two-pointer approach, just start at `left = i` and `right = j`.
- "Reverse in groups of k?" --> LC #25 (Reverse Nodes in k-Group). Apply reversal to each chunk.

---

## CONNECTIONS
- **Prerequisite:** Array basics, swap operation, recursion basics
- **Same Pattern:** Palindrome check (two pointers converging), Two Sum (sorted array variant)
- **Harder Variant:** Reverse Linked List (LC #206), Rotate Array (LC #189), Reverse Words in a String (LC #151)
- **This Unlocks:** Two-pointer technique confidence; recursive thinking for divide-and-conquer problems
