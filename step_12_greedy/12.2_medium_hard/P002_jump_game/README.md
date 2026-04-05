# Jump Game

> **Batch 3 of 12** | **Topic:** Greedy Algorithms | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Given an integer array `nums` where each element represents the **maximum jump length** from that position, determine if you can reach the **last index** starting from index 0. *(LeetCode #55)*

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `[2,3,1,1,4]` | `true` | Jump 1 step from 0->1, then 3 steps from 1->4 |
| `[3,2,1,0,4]` | `false` | Always land on index 3 (value 0) -- stuck |
| `[0]` | `true` | Already at last index |
| `[2,0,0]` | `true` | Jump 2 from index 0 directly to index 2 |
| `[1,0,1]` | `false` | Can reach index 1 (value 0), stuck there |

### Analogy
Imagine you're hopping across stepping stones in a river. Each stone has a number painted on it telling you the MAXIMUM number of stones you can jump forward. You want to reach the last stone. At each point, you're tracking: "what's the farthest stone I could possibly reach from here?" If that farthest reachable stone is ever beyond the last stone, you win. If you reach a stone that's beyond your farthest reach, you're stuck in the water.

### 3 Key Observations
1. **"Aha" -- Track max reachable position:** You don't need to find the actual path. Just track the farthest index reachable so far. If `maxReach >= last index`, return true.
2. **"Aha" -- If current index > maxReach, you're stuck:** This means there's a "gap" -- no previous position could jump far enough to reach where you are. This is the termination condition.
3. **"Aha" -- Greedy works because we only care about reachability:** We don't need the shortest path or the exact sequence of jumps. We just need to know if the last index is reachable. The farthest-reach greedy approach captures all possible paths in a single forward scan.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Recursion / backtracking | Try all jumps from each position | Exponential |
| Optimal | Boolean DP array | Bottom-up DP from end | O(n^2) time, O(n) space |
| Best | Single variable | Greedy max-reach scan | O(n) time, O(1) space |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Recursive Backtracking
**Intuition:** From each position, try every possible jump length (1 to `nums[i]`). If any jump eventually reaches the end, return true. This explores all possible paths.

**Steps:**
1. Start at index 0.
2. For each jump length `j` from 1 to `nums[i]`:
   - Recursively check if `canReach(i + j)` is true.
3. If any recursive call returns true, return true.
4. Base case: if `i >= n-1`, return true. If `i >= n` or `nums[i] == 0`, return false.

**Dry-run trace** with `[2,3,1,1,4]`:
```
canReach(0): nums[0]=2. Try j=1: canReach(1). Try j=2: canReach(2).
  canReach(1): nums[1]=3. Try j=1: canReach(2). Try j=2: canReach(3). Try j=3: canReach(4).
    canReach(4): 4 >= 4 (last index). Return true!
  Return true.
Return true.
```

| Metric | Value |
|--------|-------|
| Time | O(2^n) worst case -- exponential branching |
| Space | O(n) -- recursion depth |

---

### Approach 2: Optimal -- Bottom-Up DP
**Intuition:** Create a boolean array `dp` where `dp[i]` = "can I reach the last index from position i?" Start from the end: `dp[n-1] = true`. For each position from `n-2` to 0, check if any jump lands on a position where `dp` is true.

**BUD Optimization:**
- **B**ottleneck: Recursion explores the same subproblems multiple times. DP with memoization eliminates redundancy.
- Still O(n^2) because for each position, we scan up to `nums[i]` forward positions.

**Steps:**
1. `dp[n-1] = true`.
2. For `i` from `n-2` down to 0:
   - For `j` from 1 to `nums[i]`:
     - If `i + j < n` and `dp[i + j]` is true, set `dp[i] = true` and break.
3. Return `dp[0]`.

**Dry-run trace** with `[3,2,1,0,4]`:
```
dp = [F,F,F,F,T]
i=3: nums[3]=0, no jumps. dp[3]=F.
i=2: nums[2]=1. Check dp[3]=F. dp[2]=F.
i=1: nums[1]=2. Check dp[2]=F, dp[3]=F. dp[1]=F.
i=0: nums[0]=3. Check dp[1]=F, dp[2]=F, dp[3]=F. dp[0]=F.
Return false.
```

| Metric | Value |
|--------|-------|
| Time | O(n^2) -- for each index, scan up to n forward |
| Space | O(n) -- dp array |

---

### Approach 3: Best -- Greedy Max-Reach
**Intuition:** Maintain a single variable `maxReach` = the farthest index reachable so far. Scan left to right: at each index `i`, update `maxReach = max(maxReach, i + nums[i])`. If at any point `i > maxReach`, we can't reach index `i` -- return false. If `maxReach >= n-1`, return true.

**BUD Optimization:**
- **B**ottleneck: DP scans forward for each position. But we don't need to check each reachable position -- just tracking the FARTHEST reachable is enough.
- **U**nnecessary: The entire DP array. A single variable suffices.

**Steps:**
1. `maxReach = 0`.
2. For each index `i` from 0 to `n-1`:
   - If `i > maxReach`, return false (can't reach this index).
   - `maxReach = max(maxReach, i + nums[i])`.
   - If `maxReach >= n-1`, return true (can already reach the end).
3. Return true.

**Dry-run trace** with `[2,3,1,1,4]`:
```
maxReach=0
i=0: 0<=0. maxReach=max(0, 0+2)=2. 2<4.
i=1: 1<=2. maxReach=max(2, 1+3)=4. 4>=4. Return true!
```

**Dry-run trace** with `[3,2,1,0,4]`:
```
maxReach=0
i=0: 0<=0. maxReach=max(0, 0+3)=3. 3<4.
i=1: 1<=3. maxReach=max(3, 1+2)=3. 3<4.
i=2: 2<=3. maxReach=max(3, 2+1)=3. 3<4.
i=3: 3<=3. maxReach=max(3, 3+0)=3. 3<4.
i=4: 4>3. Return false! (can't reach index 4)
```

| Metric | Value |
|--------|-------|
| Time | O(n) -- single pass |
| Space | O(1) -- one variable |

---

## COMPLEXITY INTUITIVELY

**Why O(n) is possible:** Reachability is monotonic -- if you can reach index `i`, you can reach all indices before it (since you passed through them). The `maxReach` variable captures all the information needed: we don't care HOW we got somewhere, just IF we can.

**Why greedy is correct:** At each step, extending `maxReach` as far as possible is always optimal for reachability. Taking a shorter jump can never help you reach farther than a longer one from the same position. So the greedy choice (maximize reach) is provably optimal.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| `[0]` | `true` | Already at last index (length 1) |
| `[0, ...]` | `false` (if len > 1) | Stuck at index 0 |
| `[1,1,1,1]` | `true` | Exactly enough to reach end |
| `[10,0,0,0,0]` | `true` | Big first jump clears everything |
| All zeros except first | Depends on first value vs length |
| `[2,0,0]` | `true` | Can jump directly from 0 to 2 |

### Common Mistakes
- Returning true when `maxReach >= n` instead of `n-1` (off-by-one).
- Forgetting the early termination: `if i > maxReach, return false`.
- For the DP approach: checking `dp[i+j]` out of bounds.
- Confusing this with Jump Game II (minimum number of jumps -- different problem).
- Processing index `n-1` unnecessarily (if maxReach already >= n-1, stop early).

---

## INTERVIEW LENS

**Frequency:** Very high -- one of the most-asked greedy problems.
**Follow-ups the interviewer might ask:**
- "What if you need the minimum number of jumps?" (Jump Game II, LC #45 -- BFS or greedy with window)
- "What about backward jumps?" (BFS, no greedy shortcut)
- "Can you find the actual path?" (Track parent pointers during the greedy scan)
- "What if jump values can be negative?" (Completely different problem -- need BFS/DP)

**What they're really testing:** Greedy intuition. Can you identify that tracking a single "max reachable" is sufficient? Can you prove it works?

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Jump Game II (LC #45) | Extension: minimum jumps to reach end |
| Jump Game III (LC #1306) | Can jump both directions -- BFS needed |
| Gas Station (LC #134) | Similar circular reachability with greedy |
| Farthest Building (LC #1642) | Greedy resource allocation |

### Real-World Use Case
**Network reachability analysis:** In network routing, each router has a "reach" -- how many hops its packets can travel. Determining if a packet can traverse from source to destination through a series of routers with varying TTL (time-to-live) values is structurally identical to Jump Game. Network monitoring tools use this greedy reachability check to identify dead zones where packets can't reach their destination.
