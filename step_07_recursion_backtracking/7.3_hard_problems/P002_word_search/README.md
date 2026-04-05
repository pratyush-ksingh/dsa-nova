# Word Search

> **Step 07.7.3** | **Difficulty:** MEDIUM | **XP:** 25 | **LeetCode:** #79

---

## 1. UNDERSTAND THE PROBLEM

### Problem Statement
Given an `m x n` grid of characters (`board`) and a string `word`, return `true` if `word` exists in the grid. The word can be constructed from letters of sequentially **adjacent** cells (horizontally or vertically). The **same cell may not be used more than once** in a single path.

### Examples

| # | Board | Word | Output | Explanation |
|---|-------|------|--------|-------------|
| 1 | `[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]]` | `"ABCCED"` | `true` | A→B→C→C→E→D forms a valid path |
| 2 | Same board | `"SEE"` | `true` | S→E→E at bottom-right |
| 3 | Same board | `"ABCB"` | `false` | B cannot be reused |
| 4 | `[["a"]]` | `"a"` | `true` | Single cell matches |

### Constraints
- `m == board.length`, `n == board[i].length`
- `1 <= m, n <= 6`
- `1 <= word.length <= 15`
- `board` and `word` consist of only lowercase and uppercase English letters

---

## 2. DS & ALGO CHOICE

| Approach | Core Idea | Data Structures |
|----------|-----------|-----------------|
| Brute Force | DFS from every cell with a visited HashSet | Set + recursion |
| Optimal | DFS with in-place `'#'` marking (no extra space for visited) | Board (in-place) + recursion |
| Best | In-place DFS + frequency pruning + direction optimization | Board (in-place) + Counter |

---

## 3. APPROACH LADDER

### Approach 1: Brute Force -- DFS with Visited Set

**Intuition:** For each cell that matches `word[0]`, launch a DFS and explore all 4 directions. Track visited cells in a `HashSet` so we don't reuse them. On backtrack, remove from the set.

**Steps:**
1. Iterate every cell `(r, c)`.
2. If `board[r][c] == word[0]`, call `dfs(r, c, 0)`.
3. In DFS: if `idx == len(word)`, return `True` (found).
4. Check bounds, visited, and character match. If all pass:
   - Add `(r, c)` to `visited`.
   - Recurse in all 4 directions with `idx + 1`.
   - Remove `(r, c)` from `visited` (backtrack).
5. Return `True` if any direction succeeds.

**Dry-Run Trace** (Board row 0: `ABCE`, word: `"ABC"`):
```
Start at (0,0)='A', idx=0 -> match
  Go right (0,1)='B', idx=1 -> match
    Go right (0,2)='C', idx=2 -> match
      idx==3 == len("ABC") -> return True
```

| Metric | Value |
|--------|-------|
| Time   | O(m * n * 4^L) where L = word length |
| Space  | O(L) for visited set + recursion stack |

---

### Approach 2: Optimal -- DFS with In-Place Marking

**Intuition:** Instead of maintaining a separate `visited` set, temporarily replace `board[r][c]` with `'#'` to mark it as visited. This saves memory and avoids HashSet overhead. Restore the cell after backtracking.

**Steps:**
1. Same outer loop as Brute Force.
2. In DFS: save `temp = board[r][c]`, set `board[r][c] = '#'`.
3. Recurse in all 4 directions.
4. Restore `board[r][c] = temp` after recursion (backtrack).
5. The `'#'` check is implicit because `board[r][c] != word[idx]` will be false for any character.

**Dry-Run Trace** (`"ABCB"` should return False):
```
(0,0)='A'->mark'#', (0,1)='B'->mark'#', (0,2)='C'->mark'#'
  Try to find 'B': right=(0,3)='E' no, down=(1,2)='C' no,
  left=(0,1)='#' no (blocked), up=out-of-bounds no
  -> backtrack, restore 'C','B','A'
-> No path found -> False
```

| Metric | Value |
|--------|-------|
| Time   | O(m * n * 4^L) |
| Space  | O(L) recursion stack only (no visited set) |

---

### Approach 3: Best -- In-Place DFS + Frequency Pruning + Direction Optimization

**Intuition:** Two additional optimizations before starting DFS:
1. **Frequency check:** Count character frequencies on the board. If the word needs more of any character than the board provides, return `False` immediately without any DFS.
2. **Direction optimization:** If `word[0]` appears more times on the board than `word[-1]`, reverse the word. The rarer character provides fewer starting points, so the DFS explores fewer branches overall.

**Steps:**
1. Build frequency maps for board and word. If any word char exceeds board count, return `False`.
2. Compare frequency of `word[0]` vs `word[-1]`. If `freq[word[0]] > freq[word[-1]]`, reverse the word.
3. Run the same in-place DFS as Approach 2.

**Why reversing helps:** Suppose `word = "AAAB"` and 'A' appears 10 times on the board while 'B' appears once. Starting from 'A' launches 10 DFS trees. Starting from 'B' launches 1 DFS tree. The reversed word `"BAAA"` finds the same path with far fewer branch explorations.

| Metric | Value |
|--------|-------|
| Time   | O(m * n * 4^L) worst case, but significantly faster in practice |
| Space  | O(L) recursion stack |

---

## 4. COMPLEXITY INTUITIVELY

**Why O(m*n * 4^L):** We start from each of the `m*n` cells. From each starting cell, DFS explores at most 4 directions at each of the L steps. However, one direction is always blocked (the cell we came from), so in practice it's closer to `3^L` for most steps.

**Space O(L):** The recursion depth is bounded by the word length L. No auxiliary data structures needed in the optimal approaches.

---

## 5. EDGE CASES & COMMON MISTAKES

| Edge Case | Expected | Why It Trips People Up |
|-----------|----------|----------------------|
| Single-character word | `True` if char exists on board | Trivial but must handle `idx == 0` -> `idx == 1 == len(word)` |
| Word longer than board | `False` | Impossible to form path |
| All same characters `"AAA"` on `[["A","A"],["A","A"]]` | `True` | Must not reuse cells even though all look same |
| Word requires diagonal | `False` | Only horizontal/vertical adjacency is allowed |
| Word wraps around a path | Depends | DFS handles this naturally with backtracking |

**Common Mistakes:**
- Forgetting to **restore** the cell after backtracking (board gets permanently modified).
- Using `visited.add()` before checking bounds/character match (wastes time).
- Off-by-one: returning `True` when `idx == len(word) - 1` instead of `idx == len(word)`.
- Not handling the case where the same letter appears multiple times on the board (the path still cannot reuse a cell).

---

## 6. REAL-WORLD USE CASE

**Boggle / Word Games:** The classic Boggle board game uses exactly this algorithm. Players look for words on a 4x4 grid where adjacent letters (including diagonal in Boggle) form valid dictionary words. Spell-checkers and word-game solvers use DFS with backtracking on a character grid.

**Protein Sequence Search:** Bioinformatics tools search for specific amino acid sequences within 2D protein folding structures, where the sequence must follow connected edges in the structure graph -- the same adjacency constraint applies.

---

## 7. INTERVIEW TIPS

- **Clarify adjacency:** "Is diagonal adjacency allowed?" (Usually no for this problem.)
- **Clarify reuse:** "Can we use the same cell twice?" (No -- the in-place marking handles this.)
- **Start with Brute Force:** Mention the visited set approach first, then optimize to in-place marking.
- **Mention the frequency optimization** as a follow-up or if asked about further improvements.
- **Time complexity caveat:** The theoretical O(4^L) is tight for pathological inputs, but on typical interview boards (6x6 max), it's very fast.
- **Follow-up: Word Search II (LeetCode 212):** Find all words from a dictionary on the board. Requires a Trie to avoid re-running DFS for each word separately.

---

## 8. CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Word Search II (LC #212) | Multiple words -- use Trie + DFS |
| Number of Islands (LC #200) | Same grid DFS/BFS pattern |
| Surrounded Regions (LC #130) | Grid traversal with marking |
| Rat in a Maze | Same backtracking on a 2D grid |
| Boggle (GFG) | Extends this with all dictionary words |

---

## Real-World Use Case

Spell-checking engines like those in Google Docs use grid-based word search algorithms to validate words against dictionaries. The backtracking approach mirrors how OCR systems scan document images character-by-character, exploring adjacent pixels to recognize words.
