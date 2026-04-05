# Jump Game II

> **Batch 4 of 12** | **Topic:** Greedy | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given a 0-indexed array `nums` where `nums[i]` represents the maximum jump length from index `i`, return the **minimum number of jumps** to reach the last index. You are guaranteed to be able to reach the last index.

**LeetCode #45**

### Examples

**Example 1:**
```
nums = [2, 3, 1, 1, 4]

Jump 1: index 0 -> index 1 (jump length 1, using nums[0]=2 allows up to index 2)
Jump 2: index 1 -> index 4 (jump length 3, using nums[1]=3)
Minimum jumps = 2
```

**Example 2:**
```
nums = [2, 3, 0, 1, 4]
Jump 1: 0 -> 1
Jump 2: 1 -> 4
Minimum jumps = 2
```

**Example 3:**
```
nums = [1, 1, 1, 1]
Jump 1: 0->1, Jump 2: 1->2, Jump 3: 2->3
Minimum jumps = 3
```

### Real-Life Analogy
Imagine crossing a river by stepping on stones. Each stone tells you the maximum distance you can leap from it. You want to cross in the fewest leaps possible. At each leap, you look at all reachable stones and pick the one that gives you the best "runway" for your next leap -- like a BFS where each "level" is one jump.

### 3 Key Observations
1. **"Aha!" -- BFS level analogy:** Think of jump #1 as reaching all indices reachable from index 0. Jump #2 reaches all indices reachable from those. Each "level" is one jump. The answer is the number of levels to reach the end.
2. **"Aha!" -- Greedy window expansion:** Maintain the current reachable range [start, end]. From this range, compute the farthest index reachable. That farthest becomes the new end. Each expansion = one jump.
3. **"Aha!" -- No need to track which index we actually jump to:** We only need to count jumps. The "window" approach counts jumps implicitly.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | DP array | dp[i] = min jumps to reach i | Correct but O(N^2) |
| Optimal | BFS-like greedy | Level-by-level expansion | O(N) single pass |
| Best | Greedy (refined) | Track currentEnd and farthest in one pass | O(N), cleanest code |

---

## APPROACH LADDER

### Brute Force: Dynamic Programming
**Intuition:** dp[i] = minimum number of jumps to reach index i. For each index i, look back at all indices j that can reach i (where j + nums[j] >= i) and take min(dp[j]) + 1.

**Steps:**
1. dp[0] = 0, dp[1..n-1] = infinity
2. For each index i from 1 to n-1:
   a. For each j from 0 to i-1:
      b. If j + nums[j] >= i: dp[i] = min(dp[i], dp[j] + 1)
3. Return dp[n-1]

**BUD Analysis:**
- **B**ottleneck: O(N^2) nested loops
- **U**nnecessary: Checking all previous indices when we can use greedy levels
- **D**uplicate: Recomputing reachability for overlapping ranges

**Dry-Run Trace ([2,3,1,1,4]):**
```
dp = [0, INF, INF, INF, INF]
i=1: j=0, 0+2>=1 -> dp[1]=1. dp=[0,1,INF,INF,INF]
i=2: j=0, 0+2>=2 -> dp[2]=1. j=1, 1+3>=2 -> dp[2]=min(1,2)=1. Hmm wait...
     Actually: dp[2] = min(dp[0]+1, dp[1]+1) = min(1, 2) = 1
i=3: j=1, 1+3>=3 -> dp[3]=dp[1]+1=2. j=2, 2+1>=3 -> dp[3]=min(2,dp[2]+1)=2
i=4: j=1, 1+3>=4 -> dp[4]=dp[1]+1=2. j=3, 3+1>=4 -> dp[4]=min(2,dp[3]+1)=2
Answer: 2
```

### Optimal: BFS Level-by-Level
**Intuition:** Treat it as BFS. Level 0 = {index 0}. Level 1 = all indices reachable in 1 jump. Level 2 = all indices reachable in 2 jumps. Count levels until we reach the end.

**Steps:**
1. jumps = 0, currentEnd = 0, farthest = 0
2. For i from 0 to n-2 (don't process last index):
   a. farthest = max(farthest, i + nums[i])
   b. If i == currentEnd (end of current BFS level):
      - jumps++
      - currentEnd = farthest
      - If currentEnd >= n-1: break
3. Return jumps

### Best: Same Greedy (Cleanest Form)
**Intuition:** Identical to optimal. The key insight is that `i == currentEnd` detects when we have exhausted all positions reachable in the current number of jumps, forcing us to take another jump.

**Dry-Run Trace ([2,3,1,1,4]):**
```
jumps=0, currentEnd=0, farthest=0

i=0: farthest = max(0, 0+2) = 2. i==currentEnd(0)! jumps=1, currentEnd=2
i=1: farthest = max(2, 1+3) = 4.
i=2: farthest = max(4, 2+1) = 4. i==currentEnd(2)! jumps=2, currentEnd=4. 4>=4, break!

Return 2
```

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(N^2) | O(N) | DP with nested loop checking all predecessors |
| Optimal | O(N) | O(1) | Single pass, three variables |
| Best | O(N) | O(1) | Same as optimal |

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| Single element [0] | 0 | Already at the end |
| Two elements [1,0] | 1 | One jump needed |
| All ones [1,1,1,1] | n-1 | Must visit every index |
| First element covers all [n,0,0,...] | 1 | One giant jump |
| Decreasing [5,4,3,2,1] | 1 or 2 | First jump covers far |

**Common Mistakes:**
- Iterating to n-1 instead of n-2 (processing the last index triggers an extra jump)
- Not recognizing this as a BFS problem
- Implementing actual BFS with a queue (correct but unnecessary overhead)
- Confusing Jump Game I (can you reach?) with Jump Game II (minimum jumps)

---

## INTERVIEW LENS

**Why interviewers ask this:** Tests greedy thinking and the ability to see BFS in a non-graph problem. The O(N) solution is elegant and tests whether you can identify the "level boundary" pattern.

**What they want to see:**
- DP solution as starting point
- Optimization insight: BFS levels = jumps
- Clean O(N) greedy with currentEnd/farthest
- Understanding why the loop goes to n-2 (not n-1)

**Follow-ups to prepare for:**
- Jump Game I (LeetCode #55): Can you reach the end?
- Jump Game III (LeetCode #1306): Can you reach index with value 0?
- Video Stitching (LeetCode #1024): Same greedy pattern

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Jump Game I (LC #55) | Simpler: just check reachability, not count jumps |
| Jump Game III (LC #1306) | BFS/DFS on jump graph, bidirectional jumps |
| Video Stitching (LC #1024) | Same interval-covering greedy pattern |
| Minimum Taps to Water Garden (LC #1326) | Interval covering, same greedy technique |
