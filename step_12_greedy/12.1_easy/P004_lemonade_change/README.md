# Lemonade Change

> **Batch 2 of 12** | **Topic:** Greedy | **Difficulty:** EASY | **XP:** 10

---

## UNDERSTAND

### Problem Statement
At a lemonade stand, each lemonade costs **$5**. Customers stand in line and each pays with a **$5**, **$10**, or **$20** bill. You must provide correct change to each customer using only the bills you have collected from previous customers. You start with no change. Return `true` if you can provide correct change to every customer, `false` otherwise.

*(LeetCode #860)*

**Constraints:**
- `1 <= bills.length <= 10^5`
- `bills[i]` is either `5`, `10`, or `20`

**Examples:**

| Input | Output | Explanation |
|-------|--------|-------------|
| `[5, 5, 5, 10, 20]` | `true` | Give $5 change for $10, give $10+$5 for $20 |
| `[5, 5, 10, 10, 20]` | `false` | Two $10s used up $5s; can't make change for $20 |
| `[5, 5, 10]` | `true` | Give $5 change for $10 |

### Real-Life Analogy
> *You are a cashier at a lemonade stand on the beach. Every glass costs $5. Each customer hands you a bill and you must give back the right change from your cash register. The trick: when someone pays $20, you prefer to give back a $10 + $5 rather than three $5s, because $5 bills are more versatile (they work for both $10 and $20 customers). This greedy choice -- always preserve $5 bills when possible -- is the key to success.*

### Key Observations
1. **Only three bill types matter:** $5 (no change needed), $10 (need one $5 back), $20 (need $15 back).
2. **For $20 change, prefer $10+$5 over $5+$5+$5:** The $5 bill is more flexible -- it helps with both $10 and $20 customers. Using a $10 bill for $20 change preserves $5 bills.
3. **Just track counts of $5 and $10 bills:** You never need to give $20 as change, so tracking $20 bills is unnecessary. <-- This is the "aha" insight

---

## DATA STRUCTURE & ALGORITHM CHOICE

### Why This Technique?
- **Greedy works** because at each step there is a locally optimal choice (prefer $10+$5 over three $5s) that leads to a globally optimal outcome.
- No need for backtracking or DP -- the problem has the **greedy choice property**: giving change greedily never leads to a worse outcome.
- Two integer counters are all we need.

### Pattern Recognition
- **Pattern:** Greedy simulation with resource management
- **Classification Cue:** "When you see _sequential decisions with limited resources and no backtracking needed_ --> think _greedy simulation_"

---

## APPROACH LADDER

### Approach 1: Greedy Simulation (Optimal)
**Idea:** Process customers left to right. Track $5 and $10 bill counts. For $20, prefer $10+$5; fallback to $5+$5+$5.

**Steps:**
1. Initialize `fives = 0`, `tens = 0`.
2. For each bill in the queue:
   - **$5:** `fives++` (no change needed).
   - **$10:** Need $5 back. If `fives > 0`, give one $5. Else return `false`.
   - **$20:** Need $15 back. **Prefer** $10 + $5 (if available). **Else** try $5 + $5 + $5. If neither works, return `false`.
3. If all customers served, return `true`.

**Dry Run:** `bills = [5, 5, 5, 10, 20]`

| Customer | Bill | Action | fives | tens |
|----------|------|--------|-------|------|
| 1 | $5 | Collect | 1 | 0 |
| 2 | $5 | Collect | 2 | 0 |
| 3 | $5 | Collect | 3 | 0 |
| 4 | $10 | Give $5 back | 2 | 1 |
| 5 | $20 | Give $10+$5 back | 1 | 0 |

All served. Return `true`.

**Dry Run:** `bills = [5, 5, 10, 10, 20]`

| Customer | Bill | Action | fives | tens |
|----------|------|--------|-------|------|
| 1 | $5 | Collect | 1 | 0 |
| 2 | $5 | Collect | 2 | 0 |
| 3 | $10 | Give $5 back | 1 | 1 |
| 4 | $10 | Give $5 back | 0 | 2 |
| 5 | $20 | Want $10+$5: have $10 but no $5. Want $5x3: have 0 $5s. FAIL |

Return `false`.

| Time | Space |
|------|-------|
| O(n) | O(1) |

This is already optimal -- we must process every customer at least once.

---

## COMPLEXITY -- INTUITIVELY
**O(n) time:** "We process each of n customers once, doing O(1) work per customer."
**O(1) space:** "We only track two counters: fives and tens."

---

## EDGE CASES & MISTAKES

### Common Mistakes
1. Forgetting to **prefer** $10+$5 over $5+$5+$5 for $20 change. Using three $5s when a $10 is available wastes precious $5 bills.
2. Not checking `fives > 0` when giving change for $10.
3. Tracking $20 bills (unnecessary -- you never give $20 as change).

### Edge Cases to Test
- [ ] First customer pays $10 or $20 --> immediately `false`
- [ ] All $5 bills --> always `true`
- [ ] Alternating $5 and $10 --> works as long as $5s keep coming
- [ ] Single customer with $5 --> `true`

---

## INTERVIEW LENS

### How to Present
1. **Clarify:** "Customers arrive in order? Start with no change? Only $5, $10, $20 denominations?"
2. **Greedy Insight:** "The key is prioritizing $10+$5 over $5+$5+$5 for $20 change. $5 bills are more versatile."
3. **Code:** Simple if-else chain. Very clean.

### Follow-Up Questions
- "What if we had $50 bills too?" --> More cases but same greedy approach: use largest denominations first for change.
- "What if customers could pay any amount?" --> Becomes a general change-making problem (may need DP/greedy depending on denominations).
- "Prove the greedy choice is correct." --> If you use $5+$5+$5 when $10+$5 was available, you lose two extra $5 bills but save a $10 that is less useful (only helps with $20 change, and even then needs a $5).

---

## CONNECTIONS
- **Prerequisite:** Basic greedy strategy
- **Same Pattern:** Assign Cookies (LeetCode #455), Gas Station (LeetCode #134)
- **This Unlocks:** More complex greedy resource allocation problems
- **Related:** Coin Change (DP version when greedy fails)
