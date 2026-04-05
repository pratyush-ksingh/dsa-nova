# Max Points from Cards

> **Batch 2 of 12** | **Topic:** Sliding Window | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
There are several cards arranged in a row, and each card has an associated number of points given in the integer array `cardPoints`. In one step, you can take one card from the **beginning** or from the **end** of the row. You have to take exactly `k` cards.

Your score is the sum of the points of the cards you have taken. Return the **maximum score** you can obtain.

**LeetCode #1423**

**Constraints:**
- `1 <= cardPoints.length <= 10^5`
- `1 <= cardPoints[i] <= 10^4`
- `1 <= k <= cardPoints.length`

**Examples:**

| Input | k | Output | Explanation |
|-------|---|--------|-------------|
| `[1, 2, 3, 4, 5, 6, 1]` | 3 | `12` | Take 1 from right + 6 from right + 5 from right = 12 |
| `[2, 2, 2]` | 2 | `4` | Any 2 cards give 4 |
| `[9, 7, 7, 9, 7, 7, 9]` | 7 | `55` | Take all cards |
| `[1, 1000, 1]` | 1 | `1` | Best single card from ends is 1 |
| `[1, 79, 80, 1, 1, 1, 200, 1]` | 3 | `202` | Take 1 from left + 1 from right + 200 from right |

### Real-Life Analogy
> *Imagine a row of face-up playing cards on a table. You can only pick cards from the left end or the right end -- never from the middle. You must pick exactly k cards and want to maximize your total. Instead of thinking about which k cards to pick (which seems hard), flip the problem: the n-k cards you leave behind form a contiguous block in the middle. Minimize that block's sum, and you have maximized the picked cards' sum.*

### Key Observations
1. You pick some cards from the left and the rest from the right. If you pick `i` from the left, you pick `k - i` from the right, where `0 <= i <= k`.
2. The cards you do NOT pick form a contiguous subarray of length `n - k` in the middle. <-- This is the "aha" insight
3. Maximizing the sum of picked cards = Total sum - minimum sum of a contiguous subarray of length `n - k`.

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Approach?
- The "inverse window" insight converts the problem from choosing from two ends (complex) to finding a minimum-sum contiguous subarray of fixed size (classic sliding window).
- A **fixed-size sliding window** of length `n - k` slides across the array, tracking the minimum window sum.

### Pattern Recognition
- **Pattern:** Fixed-Size Sliding Window (on the complement)
- **Classification Cue:** "When you see _pick from both ends_ --> think _what remains in the middle is a contiguous subarray --> fixed-size sliding window on the complement_"

---

## APPROACH LADDER

### Approach 1: Brute Force (Try All Splits)
**Idea:** Try every possible split: take `i` cards from the left and `k - i` from the right, compute the sum.

**Steps:**
1. Compute prefix sums for the left and suffix sums for the right.
2. For `i` from 0 to `k`:
   - `leftSum = sum of first i elements`
   - `rightSum = sum of last (k - i) elements`
   - `maxScore = max(maxScore, leftSum + rightSum)`
3. Return `maxScore`.

**Note:** This is already O(k) with prefix sums, which is quite good. But we can also frame it as a sliding window for conceptual clarity.

| Time | Space |
|------|-------|
| O(k) | O(1) with running sums |

### Approach 2: Optimal -- Sliding Window on Minimum Subarray
**What changed:** Instead of iterating over splits, find the minimum-sum contiguous subarray of length `n - k`. The answer is `totalSum - minWindowSum`.

**Steps:**
1. Compute `totalSum = sum of all elements`.
2. If `k == n`, return `totalSum` (take all cards).
3. Set `windowSize = n - k`.
4. Compute `windowSum` = sum of first `windowSize` elements.
5. Set `minWindowSum = windowSum`.
6. Slide the window from index 1 to `n - windowSize`:
   - `windowSum = windowSum + cardPoints[right] - cardPoints[right - windowSize]`
   - `minWindowSum = min(minWindowSum, windowSum)`
7. Return `totalSum - minWindowSum`.

**Dry Run:** Input = `[1, 2, 3, 4, 5, 6, 1]`, k = 3

- `totalSum = 22`, `windowSize = 7 - 3 = 4`
- Initial window `[1, 2, 3, 4]` sum = 10, `minWindowSum = 10`

| Slide | Window | windowSum | minWindowSum |
|-------|--------|-----------|--------------|
| Start | `[1,2,3,4]` | 10 | 10 |
| 1     | `[2,3,4,5]` | 10 - 1 + 5 = 14 | 10 |
| 2     | `[3,4,5,6]` | 14 - 2 + 6 = 18 | 10 |
| 3     | `[4,5,6,1]` | 18 - 3 + 1 = 16 | 10 |

**Result:** `22 - 10 = 12`

| Time | Space |
|------|-------|
| O(n) | O(1) |

*Note:* Approach 1 (prefix/suffix split) and Approach 2 (sliding window) are both O(n) time and O(1) space. They are two equivalent ways to think about the same problem. The sliding window framing is more generalizable.

---

## COMPLEXITY -- INTUITIVELY
**Time:** O(n) -- "We compute the total sum in one pass and slide a window of fixed size in another pass. Each element is visited at most twice."
**Space:** O(1) -- "We use only a few integer variables (totalSum, windowSum, minWindowSum)."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Trying to solve with a variable-size sliding window -- this is a fixed-size window problem (window size = `n - k`).
2. Forgetting the `k == n` case -- when you take all cards, the answer is the total sum (no window to slide).
3. Off-by-one errors in the sliding window bounds.
4. Thinking you can use a greedy "pick the larger end" strategy -- this is WRONG. Example: `[1, 1000, 1]`, k=1. Greedy picks 1, but the answer is still 1 (both ends are 1). A more subtle failure: the greedy approach does not account for future picks.

### Edge Cases to Test
- [ ] `k == n` --> take all cards, return total sum
- [ ] `k == 1` --> return max(first, last)
- [ ] All elements equal `[5, 5, 5, 5]`, k=2 --> return 10
- [ ] Best strategy is all from left
- [ ] Best strategy is all from right
- [ ] Best strategy is a mix of left and right

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "I must take exactly k cards from the ends? Can I take 0 from one end and k from the other?"
2. **Insight:** "The key insight is that the cards I do NOT pick form a contiguous subarray of length n-k. So maximizing my score is equivalent to minimizing the sum of that middle subarray."
3. **Code:** Write the sliding window version. Or write the prefix-suffix split -- both are clean.
4. **Test:** Walk through `[1, 2, 3, 4, 5, 6, 1]`, k=3.

### Follow-Up Questions
- "What if you could pick from the middle too?" --> This becomes a different problem (knapsack-style or segment selection).
- "What if you want to minimize the score?" --> Maximize the window sum of the remaining `n - k` elements.

---

## CONNECTIONS
- **Prerequisite:** Prefix sums, fixed-size sliding window
- **Same Pattern:** Maximum Average Subarray I (#643), Minimum Size Subarray Sum
- **Harder Variant:** Minimum Window Substring, Sliding Window Maximum
- **This Unlocks:** The "complement window" technique -- a powerful reframing strategy for problems involving selections from both ends
