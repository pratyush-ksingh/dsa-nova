# Hand of Straights

> **Step 11 - 11.2 Medium Heaps** | **LeetCode 846** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Alice has a hand of cards represented by integers in `hand[]`. She wants to rearrange all cards into groups where each group has `groupSize` consecutive numbers. Return `true` if this is possible, `false` otherwise.

Note: LeetCode 1296 "Divide Array in Sets of K Consecutive Numbers" is the exact same problem.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `hand = [1,2,3,6,2,3,4,7,8]`, `groupSize = 3` | `true` | Groups: [1,2,3], [2,3,4], [6,7,8] |
| `hand = [1,2,3,4,5]`, `groupSize = 4` | `false` | Cannot form groups of 4 |
| `hand = [1]`, `groupSize = 1` | `true` | Single card forms a group |

## Constraints

- `1 <= hand.length <= 10^4`
- `0 <= hand[i] <= 10^9`
- `1 <= groupSize <= hand.length`

---

## Approach 1: Brute Force — Sort + List Removal

**Intuition:** Sort the hand. Repeatedly take the smallest card and try to complete a consecutive group of length `groupSize` by removing required cards one by one. Use `list.remove()` which searches linearly.

**Steps:**
1. If `len(hand) % groupSize != 0`, return false immediately.
2. Sort the hand into a list.
3. While the list is not empty: take `start = list[0]`. For each card in `[start, start+groupSize)`, find and remove it (O(n) each). If any card is missing, return false.
4. Return true.

| Metric | Value |
|--------|-------|
| Time   | O(n^2) |
| Space  | O(n)   |

---

## Approach 2: Optimal — Counter + Sorted Keys (TreeMap)

**Intuition:** Build a frequency map. Process cards in sorted order of their values. For each card value `c` with remaining count `cnt > 0`, we must start `cnt` groups from `c`. This requires `cnt` copies of each of `c, c+1, ..., c+groupSize-1`. Deduct from the frequency map; if any required value is insufficient, return false.

**Steps:**
1. If `len(hand) % groupSize != 0`, return false.
2. Build `freq: card -> count`. Sort unique cards.
3. For each `card` in sorted order with `freq[card] = cnt > 0`:
   - For `i` in `[0, groupSize)`: if `freq[card+i] < cnt`, return false; else `freq[card+i] -= cnt`.
4. Return true.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n)       |

---

## Approach 3: Best — Same Greedy with Early Exit

**Intuition:** Identical to Optimal. The minimum complexity is O(n log n) because we must process keys in sorted order. A minor enhancement: set `freq[card] = 0` after processing and decrement successors incrementally, returning false immediately when any frequency drops below zero.

**Steps:**
1. Same as Optimal, but exit as soon as `freq[card + step] < 0` (after decrement).
2. Zero out `freq[card]` explicitly after handling it.

| Metric | Value |
|--------|-------|
| Time   | O(n log n) |
| Space  | O(n)       |

---

## Real-World Use Case

**Card game engine / tile-based puzzle validation:** A Mahjong or Rummy game engine must verify whether a player's hand can be arranged into valid sequences (consecutive runs). The greedy frequency-map approach validates this in O(n log n) — efficient enough for real-time gameplay on mobile devices.

## Interview Tips

- The first key insight: `len(hand) % groupSize != 0` immediately returns false (pigeonhole).
- The core greedy insight is the "always start from smallest" rule. This is provable: if the smallest card `c` cannot start a group, it can never be placed (no earlier group can include it), so the answer is false.
- A TreeMap/sorted Counter gives O(log n) access to the smallest key, making the overall algorithm O(n log n).
- Don't sort the hand and try to form groups sequentially — duplicates make this error-prone. The frequency map approach handles duplicates cleanly by consuming multiple groups at once.
- This problem is often compared to LeetCode 659 (Split Array into Consecutive Subsequences) which is harder because groups don't have a fixed size.
