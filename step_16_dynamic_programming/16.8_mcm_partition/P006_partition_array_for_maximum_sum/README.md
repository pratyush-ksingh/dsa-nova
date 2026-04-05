# Partition Array for Maximum Sum (LeetCode 1043)

> **Step 16.8** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
Given an integer array `arr` and integer `k`, partition the array into contiguous subarrays of length **at most k**. After partitioning, replace each element with the **maximum value** of its subarray, then return the **largest possible sum** of the array after partitioning.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| arr=[1,15,7,9,2,5,10], k=3 | 84 | Partition: [1,15,7] [9] [2,5,10] → [15,15,15,9,10,10,10] → sum=84 |
| arr=[1,4,1,5,7,3,6,1,9,9,3], k=4 | 83 | Optimal partitioning gives sum=83 |
| arr=[1], k=1 | 1 | Single element, value 1 |

### Constraints
- `1 <= arr.length <= 500`
- `0 <= arr[i] <= 10^9`
- `1 <= k <= arr.length`

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursion

**Intuition:** This is a matrix chain multiplication (MCM) style partition problem. From position `idx`, try all possible first partition lengths from 1 to k. The subarray maximum replaces all elements, contributing `max_val * length` to the sum. Recurse on the rest.

**Steps:**
1. `solve(idx)`: max sum for arr[idx..n-1]
2. Base: idx == n → return 0
3. For length from 1 to k (while in bounds):
   - Update `max_val = max(arr[idx..idx+length-1])`
   - `curr = max_val * length + solve(idx + length)`
   - Track max curr
4. Return max curr

```
Dry-run: arr=[1,15,7], k=3
solve(0):
  len=1: max=1, curr=1*1 + solve(1)
    solve(1): len=1: 15+solve(2)=15+7=22; len=2: 15*2+solve(3)=30; → 30
  len=1: 1+30 = 31
  len=2: max(1,15)=15, 15*2+solve(2)=30+7=37
  len=3: max(1,15,7)=15, 15*3+solve(3)=45+0=45 ✓
solve(0) = 45
```

| Metric | Value |
|--------|-------|
| Time   | O(k^(n/k)) -- exponential |
| Space  | O(n) -- recursion depth |

---

### Approach 2: Optimal -- DP Tabulation

**Intuition:** `dp[i]` = max sum for `arr[i..n-1]`. Fill right-to-left. For each position i, try all partition lengths 1..k. Maintain running max as we extend the window.

**Steps:**
1. `dp[n] = 0` (base: empty suffix)
2. For i from n-1 down to 0:
   - `max_val = 0`, `best = 0`
   - For length from 1 to k (while `i + length <= n`):
     - `max_val = max(max_val, arr[i + length - 1])`
     - `curr = max_val * length + dp[i + length]`
     - `best = max(best, curr)`
   - `dp[i] = best`
3. Return `dp[0]`

```
Dry-run: arr=[1,15,7,9,2,5,10], k=3
dp[7]=0
dp[6]=10*1+0=10
dp[5]=max(5*1+10=15, max(5,10)*2+0=20) = 20
dp[4]=max(2*1+20=22, max(2,5)*2+10=20, max(2,5,10)*3+0=30) = 30
dp[3]=max(9+30=39, 9*2+20=38, 9*3+10=37) = 39
dp[2]=max(7+39=46, max(7,9)*2+30=48, max(7,9,2)*3+20=47) = 48
dp[1]=max(15+48=63, 15*2+39=69, 15*3+30=75) = 75
dp[0]=max(1+75=76, 15*2+48=78, 15*3+39=84) = 84 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(n * k) |
| Space  | O(n) |

---

### Approach 3: Best -- DP Forward Direction

**Intuition:** Same O(n*k) but fill left-to-right. `dp[i]` = max sum for `arr[0..i-1]` (first i elements). For each position i, look back up to k steps to find where the last partition started.

**Steps:**
1. `dp[0] = 0` (base: empty prefix)
2. For i from 1 to n:
   - `max_val = 0`, `best_val = 0`
   - For length from 1 to k (while `i - length >= 0`):
     - `max_val = max(max_val, arr[i - length])`
     - `curr = max_val * length + dp[i - length]`
     - `best_val = max(best_val, curr)`
   - `dp[i] = best_val`
3. Return `dp[n]`

| Metric | Value |
|--------|-------|
| Time   | O(n * k) |
| Space  | O(n) |

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| k = 1 | Sum equals original array sum | Each element its own partition |
| k >= n | Can merge all into one partition | Answer = max(arr) * n |
| Single element | Return arr[0] | Trivial |
| All equal elements | Any partitioning gives same result | Still correct |

**Common Mistakes:**
- Forgetting to maintain running max within each partition window (re-computing from scratch is O(k^2) total extra)
- Confusing the forward vs backward DP indexing
- Off-by-one when checking `i + length <= n` or `i - length >= 0`

---

## Real-World Use Case
**Image compression blocks:** In JPEG-like compression, an image row is divided into blocks of at most size k. Each block is approximated by its maximum intensity value. The partition for maximum sum models finding the block decomposition that maximizes the total represented intensity — useful in adaptive compression quality settings.

## Interview Tips
- Recognize this as an MCM/interval DP variant: "partition into contiguous groups with a constraint"
- The running max trick is key: update `max_val` incrementally as you extend the window (O(k) per position, not O(k^2))
- Compare with: Jump Game (greedy, not DP), Matrix Chain Multiplication (arbitrary ranges), Burst Balloons (interval DP)
- Common follow-up: what if you minimize instead of maximize? (Just change max to min in dp update)
- The O(n * k) complexity is optimal for this problem; no known O(n log n) solution exists
