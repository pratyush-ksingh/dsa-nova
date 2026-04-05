# Fractional Knapsack

> **Batch 3 of 12** | **Topic:** Greedy Algorithms | **Difficulty:** MEDIUM | **XP:** 25

## UNDERSTAND

### Problem Statement
Given `n` items, each with a weight and value, and a knapsack with capacity `W`, maximize the total value you can carry. Unlike 0/1 knapsack, you **can take fractions** of items. Return the maximum total value (as a decimal).

### Examples

| Items (value, weight) | Capacity | Output | Explanation |
|----------------------|----------|--------|-------------|
| `[(60,10), (100,20), (120,30)]` | 50 | `240.0` | Take all of item 1 (60) + all of item 2 (100) + 2/3 of item 3 (80) = 240 |
| `[(500,30)]` | 10 | `166.67` | Take 10/30 of the item = 500*(10/30) |
| `[(60,10), (100,20), (120,30)]` | 60 | `280.0` | Take all items (total weight=60=capacity) |
| `[(10,5), (20,10)]` | 0 | `0.0` | No capacity |

### Analogy
You're at a spice market with a bag that holds 50 kg. Each spice has a different price per kilogram. The optimal strategy is obvious: buy the most expensive spice per kg first, fill up as much as you can, then move to the next most expensive, and so on. If your bag fills up mid-spice, just buy a fraction of that last spice. This is why greedy works perfectly here -- there's no penalty for partial items.

### 3 Key Observations
1. **"Aha" -- Sort by value-to-weight ratio:** The greedy strategy is to always pick the item with the highest value per unit weight. This is provably optimal for the fractional case (but NOT for 0/1 knapsack).
2. **"Aha" -- Fractions make greedy work:** In 0/1 knapsack, you can't take fractions, so a greedy approach fails. But with fractions, there's no "wasted capacity" -- you can always fill the remaining space optimally.
3. **"Aha" -- At most one item is partially taken:** All items before the "breaking point" are taken fully. At most one item is partially taken. All items after are not taken at all.

---

## DS & ALGO CHOICE

| Approach | Data Structure | Algorithm | Why? |
|----------|---------------|-----------|------|
| Brute | Generate all subsets | Try all combinations with fractions | Exponential, impractical |
| Optimal | Sorted array | Greedy by value/weight ratio | O(n log n) sort + O(n) scan |
| Best | Partial sort / nth_element | Only need to find the "breaking item" | O(n) with partition |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Try All Combinations
**Intuition:** For each item, decide how much to take (0% to 100%). Since fractions are continuous, this is theoretically an infinite search space. In practice, we can enumerate by taking items greedily in different orders.

**Steps:**
1. Try all n! permutations of items.
2. For each permutation, greedily fill the knapsack (take as much as possible of each item in order).
3. Track the maximum value across all permutations.

| Metric | Value |
|--------|-------|
| Time | O(n! * n) -- all permutations, fill each |
| Space | O(n) |

---

### Approach 2: Optimal -- Greedy by Ratio (Sort)
**Intuition:** Sort items by value/weight ratio in descending order. Iterate through sorted items: take the entire item if it fits, otherwise take the fraction that fills remaining capacity.

**BUD Optimization:**
- **B**ottleneck: The brute force tries all orderings, but the ratio sort gives the provably optimal order.
- **U**nnecessary: No need to try multiple orderings -- the highest ratio first is always best.

**Steps:**
1. Compute `ratio = value / weight` for each item.
2. Sort items by ratio in descending order.
3. For each item in sorted order:
   - If item fits entirely (`weight <= remaining_capacity`): take it all.
   - Otherwise: take fraction `remaining_capacity / weight` of it. Done.
4. Return total value.

**Dry-run trace** with `[(60,10), (100,20), (120,30)]`, W=50:
```
Ratios: item1=60/10=6.0, item2=100/20=5.0, item3=120/30=4.0
Sorted: [item1(6.0), item2(5.0), item3(4.0)]

Take item1: weight=10 <= 50. Full take. Value=60, remaining=40.
Take item2: weight=20 <= 40. Full take. Value=60+100=160, remaining=20.
Take item3: weight=30 > 20. Fraction=20/30. Value=160+120*(20/30)=160+80=240. Done.
Return 240.0
```

| Metric | Value |
|--------|-------|
| Time | O(n log n) for sorting + O(n) greedy scan |
| Space | O(1) extra (sort in-place) |

---

### Approach 3: Best -- O(n) with Partition (Quickselect-like)
**Intuition:** You don't need to fully sort. You need to find the "breaking item" -- the item where the knapsack becomes full. Use a partition approach: pick a pivot ratio, put all higher-ratio items on one side. If their total weight exceeds capacity, recurse into that side. Otherwise, take them all and recurse into the other side with reduced capacity. Like Quickselect, this is O(n) average.

**Steps:**
1. Pick random pivot item.
2. Partition items into `high_ratio` (ratio > pivot's) and `low_ratio`.
3. If total weight of `high_ratio` >= remaining capacity: answer lies in `high_ratio`. Recurse.
4. Else: take all of `high_ratio`, reduce capacity, recurse into `low_ratio` + pivot.
5. Base case: single item, take fraction that fits.

| Metric | Value |
|--------|-------|
| Time | O(n) average (like Quickselect) |
| Space | O(n) for partitions (can be O(1) with in-place partition) |

Note: In practice, O(n log n) sort is almost always used because n is typically small for knapsack problems and the sort-based approach is much simpler to implement correctly.

---

## COMPLEXITY INTUITIVELY

**Why greedy works for fractional but not 0/1:** With fractions, you can always "fill the gaps" optimally. If an item has the best ratio, taking as much of it as possible is always better than leaving capacity empty. In 0/1, you might skip a high-ratio item that's too heavy, and the greedy choice doesn't account for the combinations of smaller items that could fill the space better.

**Why O(n log n):** The sorting step dominates. The greedy scan after sorting is a single O(n) pass.

---

## EDGE CASES & MISTAKES

| Edge Case | Expected | Why Tricky |
|-----------|----------|------------|
| Capacity = 0 | `0.0` | No items can be taken |
| All items fit | Sum of all values | No fractions needed |
| Single item, doesn't fit | `value * (capacity / weight)` | Fraction of single item |
| Item with weight 0 | Infinite ratio -- take it "for free" | Avoid division by zero |
| All items have same ratio | Any order works, still take greedily |

### Common Mistakes
- Using 0/1 knapsack DP for this problem (overkill, greedy is optimal and simpler).
- Integer division when computing ratios (use floating-point).
- Forgetting to handle the fractional last item (breaking out of the loop after taking a fraction).
- Not sorting in DESCENDING order of ratio.
- Forgetting that weight=0 items are free (infinite value per weight).

---

## INTERVIEW LENS

**Frequency:** High -- classic greedy problem, often contrasted with 0/1 knapsack.
**Follow-ups the interviewer might ask:**
- "Why doesn't greedy work for 0/1 knapsack?" (Counter-example: items (60,10), (100,20) with W=20. Greedy takes item 1 (ratio=6), value=60. But taking item 2 gives value=100.)
- "What if items have dependencies?" (Knapsack with constraints -- much harder)
- "Can you do better than O(n log n)?" (Yes, O(n) with partition approach)
- "What if capacity is infinite?" (Take everything, answer = sum of all values)

**What they're really testing:** Understanding of when greedy is provably optimal, and the ability to implement it cleanly.

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| 0/1 Knapsack (DP) | Same setup, but no fractions -- requires DP |
| Activity Selection | Another classic greedy problem |
| Job Sequencing | Greedy by deadline with profit maximization |
| Assign Cookies (LC #455) | Greedy matching by size |

### Real-World Use Case
**Investment portfolio allocation:** Financial advisors use fractional knapsack logic when deciding how to allocate a budget across investment opportunities. Each opportunity has a return rate (value/weight ratio = ROI). You allocate fully to the highest-ROI investment first, then the next, and partially invest in the last one that fits your budget. This is literally how portfolio optimization works for divisible assets like stocks and bonds.
