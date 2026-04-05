# Greedy Algorithm to Find Minimum Coins

> **Batch 4 of 12** | **Topic:** Greedy | **Difficulty:** Medium | **XP:** 25

## UNDERSTAND

### Problem Statement
Given an amount `V` and the Indian denomination system `{1, 2, 5, 10, 20, 50, 100, 500, 2000}`, find the **minimum number of coins/notes** needed to make the amount. Also return which denominations are used.

**Key constraint:** Greedy works correctly for the Indian (and US) denomination system because it satisfies the **canonical property** -- each denomination is at least twice the previous.

### Examples

**Example 1:**
```
V = 49
Denominations: [2000, 500, 100, 50, 20, 10, 5, 2, 1]
Use 20 (remaining 29), 20 (remaining 9), 5 (remaining 4), 2 (remaining 2), 2 (remaining 0)
Coins: [20, 20, 5, 2, 2] -> 5 coins
```

**Example 2:**
```
V = 93
Use 50 (43), 20 (23), 20 (3), 2 (1), 1 (0)
Coins: [50, 20, 20, 2, 1] -> 5 coins
```

**Example 3:**
```
V = 2000
Use 2000 (0)
Coins: [2000] -> 1 coin
```

### Real-Life Analogy
A cashier making change. When someone buys a Rs. 51 item and pays Rs. 100, the cashier gives back Rs. 49. Instinctively, the cashier grabs the largest bills/coins first: a 20, another 20, a 5, a 2, and a 2. This natural human behavior IS the greedy algorithm.

### 3 Key Observations
1. **"Aha!" -- Greedy works ONLY for canonical denomination systems:** Indian denominations {1,2,5,10,20,50,100,500,2000} form a canonical system where greedy gives optimal results. For arbitrary denominations like {1,3,4}, greedy fails (6 = 3+3, not 4+1+1).
2. **"Aha!" -- Process denominations largest-first:** Take as many of the largest denomination as possible, then move to the next. This is greedy by definition: make the locally optimal choice at each step.
3. **"Aha!" -- Division gives count, modulo gives remainder:** For each denomination d, use `V / d` coins of that denomination, then `V = V % d`.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Recursion | Try all combinations, find minimum | Exponential but always correct |
| Optimal | DP table | Bottom-up DP for general coin change | O(V*D) works for any denominations |
| Best | Array | Greedy largest-first | O(D) for canonical denominations |

---

## APPROACH LADDER

### Brute Force: Recursive Enumeration
**Intuition:** For each denomination, try using 0, 1, 2, ... coins of it. Recursively solve for the remainder. Return the combination with minimum total coins.

**Steps:**
1. For each denomination d (from largest to smallest):
   a. For count = 0 to V/d:
      b. Recursively solve for V - count*d with remaining denominations
      c. Track minimum coins
2. Return minimum

**BUD Analysis:**
- **B**ottleneck: Exponential branching factor
- **U**nnecessary: Exploring suboptimal branches when greedy works
- **D**uplicate: Same subproblems solved repeatedly

### Optimal: Dynamic Programming (General Coin Change)
**Intuition:** dp[i] = minimum coins to make amount i. For each amount, try all denominations.

**Steps:**
1. dp[0] = 0, dp[1..V] = infinity
2. For each amount i from 1 to V:
   a. For each denomination d:
      b. If d <= i: dp[i] = min(dp[i], dp[i-d] + 1)
3. Return dp[V]

### Best: Greedy Largest-First
**Intuition:** For canonical denominations, always using the largest possible denomination is optimal. Simple division and modulo.

**Steps:**
1. Sort denominations in descending order
2. For each denomination d:
   a. count = V / d
   b. Add count coins of denomination d to result
   c. V = V % d
3. Return result

**Dry-Run Trace (V=49):**
```
Denominations (desc): [2000, 500, 100, 50, 20, 10, 5, 2, 1]
2000: 49/2000=0, skip
500: 49/500=0, skip
100: 49/100=0, skip
50: 49/50=0, skip
20: 49/20=2, add [20,20], V=9
10: 9/10=0, skip
5: 9/5=1, add [5], V=4
2: 4/2=2, add [2,2], V=0
Done! Coins: [20, 20, 5, 2, 2], total = 5
```

---

## COMPLEXITY INTUITIVELY

| Approach | Time | Space | Why? |
|----------|------|-------|------|
| Brute | O(V^D) | O(D) | Exponential combinations |
| Optimal | O(V * D) | O(V) | DP table of size V, D denominations per cell |
| Best | O(D) | O(D) | One pass through D denominations |

*D = number of denominations (9 for Indian system), V = amount*

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky? |
|-----------|----------|-------------|
| V = 0 | 0 coins | No change needed |
| V = 1 | [1] | Smallest denomination |
| V = 2000 | [2000] | Single note |
| V very large | Works fine | Greedy handles any V efficiently |
| V not achievable | N/A for Indian system | Since we have denomination 1, all V > 0 are achievable |

**Common Mistakes:**
- Applying greedy to non-canonical systems (like {1, 3, 4})
- Forgetting that greedy does NOT work for arbitrary coin sets
- Not handling V = 0 case
- Using ascending order instead of descending

**WARNING:** For interview problems that say "arbitrary denominations," you MUST use DP (LeetCode #322). Greedy only works for standard denomination systems.

---

## INTERVIEW LENS

**Why interviewers ask this:** Tests greedy thinking and understanding of when greedy is provably optimal vs. when you need DP.

**What they want to see:**
- Quick greedy solution for canonical denominations
- Awareness that this does NOT generalize to arbitrary coins
- Ability to pivot to DP if denominations are arbitrary

**Follow-ups to prepare for:**
- LeetCode #322: Coin Change (arbitrary denominations, needs DP)
- LeetCode #518: Coin Change II (count combinations, DP)
- Fractional Knapsack (another greedy problem)

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Coin Change (LC #322) | Same problem but arbitrary coins -- needs DP |
| Coin Change II (LC #518) | Count number of ways instead of minimum |
| Fractional Knapsack | Another greedy-works problem with specific structure |
| Activity Selection | Classic greedy with provable optimality |
