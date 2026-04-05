# Number of Nice Subarrays

> **Batch 4 of 12** | **Topic:** Sliding Window | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an array of integers `nums` and an integer `k`, return the number of **nice subarrays** -- contiguous subarrays that contain **exactly `k` odd numbers**.

**LeetCode #1248**

### Examples

| Input                          | Output | Explanation                                                |
|--------------------------------|--------|------------------------------------------------------------|
| `nums=[1,1,2,1,1], k=3`       | 2      | [1,1,2,1] and [1,2,1,1] each have exactly 3 odd numbers   |
| `nums=[2,4,6], k=1`           | 0      | No odd numbers at all                                     |
| `nums=[2,2,2,1,2,2,1,2,2,2], k=2` | 16 | Many subarrays with exactly 2 odd numbers                  |

### Analogy
Imagine a shelf of books where some are red (odd) and some are blue (even). You want to count how many contiguous segments of the shelf contain exactly `k` red books. Instead of checking every possible segment, you use the trick: segments with exactly `k` reds = segments with at most `k` reds MINUS segments with at most `k-1` reds.

### 3 Key Observations
1. **"Aha" -- Exactly(k) = AtMost(k) - AtMost(k-1).** Counting "exactly k" directly with a sliding window is tricky because the window doesn't have a clean expansion/contraction rule. But "at most k" is easy with a standard sliding window.
2. **"Aha" -- Replace odd/even with 1/0.** The problem is equivalent to "count subarrays with sum exactly k" after mapping odd -> 1, even -> 0. This connects to prefix-sum and hashmap approaches.
3. **"Aha" -- Prefix sum approach also works.** Track prefix counts of odd numbers. For each right end, count how many left ends give `prefix[right] - prefix[left] = k`.

---

## DS & ALGO CHOICE

| Approach         | Data Structure     | Algorithm                     | Why?                                    |
|------------------|--------------------|-------------------------------|-----------------------------------------|
| Brute Force      | None               | Double loop                   | Check every subarray explicitly         |
| Optimal          | HashMap            | Prefix sum + freq count       | O(n) time, O(n) space                  |
| Best             | None               | atMost(k) - atMost(k-1)      | O(n) time, O(1) space, sliding window  |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Enumerate All Subarrays
**Intuition:** For every possible starting index, expand right while counting odd numbers. When the count equals `k`, increment result.

**Steps:**
1. For each `i` from 0 to n-1:
   - `oddCount = 0`
   - For each `j` from `i` to n-1:
     - If `nums[j]` is odd, increment `oddCount`.
     - If `oddCount == k`, increment `result`.
     - If `oddCount > k`, break (no point going further).
2. Return `result`.

**Dry-Run Trace** with `nums=[1,1,2,1,1], k=3`:
```
i=0: j=0 odds=1, j=1 odds=2, j=2 odds=2, j=3 odds=3 MATCH, j=4 odds=4 break -> +1
i=1: j=1 odds=1, j=2 odds=1, j=3 odds=2, j=4 odds=3 MATCH -> +1
i=2: j=2 odds=0, j=3 odds=1, j=4 odds=2 -> no match
i=3,4: not enough odds
Answer: 2
```

| Metric | Value  |
|--------|--------|
| Time   | O(n^2) |
| Space  | O(1)   |

---

### Approach 2: Optimal -- Prefix Sum + HashMap
**Intuition:** Map odds to 1, evens to 0. Compute a running prefix sum of odd counts. For each index `j`, we need the number of previous indices `i` where `prefix[j] - prefix[i] = k`, i.e., `prefix[i] = prefix[j] - k`. Use a hashmap to count prefix frequencies.

**Steps:**
1. Initialize `prefixCount = {0: 1}`, `oddCount = 0`, `result = 0`.
2. For each element in `nums`:
   - If odd, increment `oddCount`.
   - If `oddCount - k` exists in `prefixCount`, add its frequency to `result`.
   - Increment `prefixCount[oddCount]`.
3. Return `result`.

**Dry-Run Trace** with `nums=[1,1,2,1,1], k=3`:
```
prefixCount = {0:1}
j=0: num=1(odd), oddCount=1, check 1-3=-2 (no), prefixCount={0:1, 1:1}
j=1: num=1(odd), oddCount=2, check 2-3=-1 (no), prefixCount={0:1, 1:1, 2:1}
j=2: num=2(even), oddCount=2, check 2-3=-1 (no), prefixCount={0:1, 1:1, 2:2}
j=3: num=1(odd), oddCount=3, check 3-3=0 -> freq=1, result=1, prefixCount={0:1, 1:1, 2:2, 3:1}
j=4: num=1(odd), oddCount=4, check 4-3=1 -> freq=1, result=2, prefixCount={0:1, 1:1, 2:2, 3:1, 4:1}
Answer: 2
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n)  |

---

### Approach 3: Best -- Sliding Window: atMost(k) - atMost(k-1)
**Intuition:** Define `atMost(k)` = number of subarrays with at most `k` odd numbers. This is easy to compute with a standard sliding window: expand `right`, and when oddCount exceeds `k`, shrink `left` until oddCount <= `k`. At each step, subarrays ending at `right` are `right - left + 1`.

Then: `exactlyK = atMost(k) - atMost(k-1)`.

**Steps:**
1. Define helper `atMost(nums, k)`:
   - `left = 0`, `oddCount = 0`, `result = 0`
   - For each `right` from 0 to n-1:
     - If `nums[right]` is odd, increment `oddCount`.
     - While `oddCount > k`: if `nums[left]` is odd, decrement `oddCount`; increment `left`.
     - `result += right - left + 1`
   - Return `result`.
2. Return `atMost(nums, k) - atMost(nums, k - 1)`.

**Dry-Run Trace** of `atMost([1,1,2,1,1], 3)`:
```
r=0: odd, odds=1<=3, result+=1 -> result=1
r=1: odd, odds=2<=3, result+=2 -> result=3
r=2: even, odds=2<=3, result+=3 -> result=6
r=3: odd, odds=3<=3, result+=4 -> result=10
r=4: odd, odds=4>3, shrink: l=0 odd, odds=3, l=1
     result+=4 -> result=14
atMost(3) = 14

atMost(2) = 12  (computed similarly)

exactlyK = 14 - 12 = 2
```

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## COMPLEXITY INTUITIVELY

- **Why O(n):** The sliding window makes a single pass (actually two calls, but each is O(n)). Each element is added and removed from the window at most once: amortized O(1) per element.
- **Why O(1) space for Approach 3:** Only a few integer variables (left, right, oddCount, result). No hashmap needed.
- **Approach 2 vs 3:** Both are O(n) time, but Approach 3 uses O(1) space vs O(n) for the hashmap.

---

## EDGE CASES & MISTAKES

| Edge Case               | What Happens                                    |
|-------------------------|-------------------------------------------------|
| `k = 0`                 | Count subarrays with no odd numbers (all even). |
| All odd numbers          | Every subarray of length >= k counts.           |
| All even numbers         | If k > 0, answer is 0.                          |
| `k > n`                 | Impossible to have k odds, return 0.            |
| Single element, k=1     | Return 1 if odd, 0 if even.                     |

**Common Mistakes:**
- Forgetting to implement `atMost(k-1)` and only computing `atMost(k)`.
- Off-by-one in the `right - left + 1` counting formula.
- Using `==k` in the window condition instead of `>k` (which makes shrinking logic wrong).

---

## INTERVIEW LENS

- **Why interviewers ask this:** Tests the "exactly k = atMost(k) - atMost(k-1)" pattern, which is a reusable technique for many subarray counting problems.
- **Follow-ups:**
  - "Subarrays with exactly k distinct elements." (Same atMost trick, LC #992.)
  - "Binary subarrays with sum k." (Same problem with 0/1 array, LC #930.)
  - "Can you do it with a different approach?" (Prefix sum + hashmap.)
- **Communication tip:** Start by explaining why "exactly k" is hard for sliding window, then introduce the `atMost` decomposition. This shows problem-solving maturity.

---

## CONNECTIONS

| Related Problem                       | How It Connects                                 |
|--------------------------------------|------------------------------------------------|
| Subarrays with K Different Integers   | Same atMost(k) - atMost(k-1) pattern          |
| Binary Subarrays With Sum (LC #930)   | Identical problem structure with 0/1 array     |
| Subarray Sum Equals K (LC #560)       | Prefix sum + hashmap approach                  |
| Longest Substring with K Distinct     | Sliding window on "at most k" constraint       |

---

## Real-World Use Case
**Network monitoring / Anomaly detection:** Counting time windows with exactly `k` anomalous events (odd-valued sensor readings, error spikes) is a form of this problem. The sliding window approach enables real-time monitoring with O(1) processing per new data point.
