# Find Two Numbers Appearing Once

> **Step 08 — 8.2** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given an integer array `nums` where every element appears **exactly twice** except for **two elements** which appear exactly once, find the two elements that appear only once. Return them in any order.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| [1, 2, 3, 2, 1, 4] | [3, 4] | 3 and 4 appear once; rest appear twice |
| [4, 1, 2, 1, 2, 3] | [3, 4] | Same unique pair, different order |
| [2, 1, 3, 2] | [1, 3] | 1 and 3 appear once |

## Constraints

- `2 <= nums.length <= 3 * 10^4`
- `-2^31 <= nums[i] <= 2^31 - 1`
- Each element in `nums` appears exactly twice except for two elements which appear exactly once.

---

## Approach 1: Brute Force — HashMap Frequency Count

**Intuition:** Count occurrences of each number. Elements with count = 1 are the answer.

**Steps:**
1. Traverse the array, building a frequency map.
2. Return all keys with value = 1.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(n) — storing all unique elements |

---

## Approach 2: Optimal — XOR + Partition by Differentiating Bit

**Intuition:** XOR has a magical property: `x XOR x = 0` and `x XOR 0 = x`. XOR-ing all elements cancels duplicates, leaving `a XOR b`. Since a != b, at least one bit differs — use that bit to split all numbers into two groups, XOR each group to recover a and b.

**Steps:**
1. XOR all elements: `xor_all = a XOR b`.
2. Find the **rightmost set bit** of `xor_all`: `diff_bit = xor_all & (-xor_all)`.
   - This bit is 1 in exactly one of a or b (they differ at this position).
3. Partition: for each number, if `num & diff_bit != 0` add to group A, else group B.
4. XOR group A separately -> gives `a`. XOR group B separately -> gives `b`.

**Why rightmost set bit:** `x & (-x)` in two's complement flips all bits after the lowest set bit and adds 1, which isolates exactly the lowest set bit. Example: 6 (110) -> -6 in two's complement = 010, 6 & (-6) = 010 = 2 (bit 1).

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Approach 3: Best — XOR + Explicit Bit Scan

**Intuition:** Same XOR partition strategy as Approach 2, but locates the differentiating bit by scanning bit positions 0 through 31 until a set bit is found. Avoids the two's-complement trick, making the logic more transparent for learning.

**Steps:**
1. XOR all elements: `xor_all = a XOR b`.
2. Scan bit positions 0, 1, 2, ... until `(xor_all >> bit_pos) & 1 == 1`.
3. Partition numbers by that bit and XOR each group.

| Metric | Value |
|--------|-------|
| Time   | O(n)  |
| Space  | O(1)  |

---

## Real-World Use Case

**Data deduplication audit:** In storage systems, checksums for file blocks are stored twice (for redundancy). A bit-corruption audit XORs all checksums; if everything is fine, the result is 0. If two blocks are corrupted exactly once, XOR reveals their combined checksum — this approach isolates the two corrupted blocks.

**Network packet analysis:** In reliable streaming protocols, sequence numbers appear once for sent and once for acknowledged packets. Two missing acks correspond to two numbers appearing once — XOR partition identifies them in O(n) without extra memory.

## Interview Tips

- Start by explaining the simpler problem: "Find one number appearing once" (just XOR everything). Then build up to two.
- The key insight is that `a XOR b != 0` since a != b, so there exists a bit where they differ. That bit is your partition key.
- `x & (-x)` is a standard bit trick for isolating the lowest set bit. Know it cold.
- The partition step ensures each group contains exactly one unique number (plus some duplicates that cancel), so XOR within each group gives the unique number.
- Generalizing: if there are k numbers appearing once, this approach works only for k=1 or k=2. For k=3+, different techniques are needed.
