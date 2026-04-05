# Shortest Distance in Binary Maze

> **Batch 4 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
Given an `n x n` binary grid where `0` represents a passable cell and `1` represents a wall, find the **shortest path** from a `source` cell to a `destination` cell. You can move in 4 directions (up, down, left, right), and each move costs 1 unit. Return the shortest distance. If no path exists, return `-1`.

The source and destination are given as `[row, col]` coordinates.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| grid=[[0,0,0],[0,1,0],[0,0,0]], src=[0,0], dst=[2,2] | 4 | Path: (0,0)->(1,0)->(2,0)->(2,1)->(2,2) |
| grid=[[0,1],[1,0]], src=[0,0], dst=[1,1] | -1 | No path exists (walls block) |
| grid=[[0,0],[0,0]], src=[0,0], dst=[0,0] | 0 | Already at destination |

```
Example 1: grid = [[0,0,0],[0,1,0],[0,0,0]], src=(0,0), dst=(2,2)

Grid:                  BFS levels (distance from source):
+---+---+---+          +---+---+---+
| 0 | 0 | 0 |          | 0 | 1 | 2 |
+---+---+---+          +---+---+---+
| 0 | 1 | 0 |          | 1 | X | 3 |
+---+---+---+          +---+---+---+
| 0 | 0 | 0 |          | 2 | 3 | 4 |  <-- dst=(2,2), distance=4
+---+---+---+          +---+---+---+

Shortest path (one of several):
  (0,0) -> (0,1) -> (0,2) -> (1,2) -> (2,2)  = 4 steps
  (0,0) -> (1,0) -> (2,0) -> (2,1) -> (2,2)  = 4 steps
```

### Real-World Analogy
Imagine navigating a maze in a video game. You are at the entrance (source) and need to reach the exit (destination). Some tiles are walls (1) and some are open (0). Each step takes the same amount of time. BFS explores all tiles reachable in 1 step, then 2 steps, etc. -- like a flood filling the maze. The first time the flood reaches the exit, that is the shortest path.

### Three Key Observations

1. **All edge weights are 1, so BFS gives shortest path** -- In an unweighted graph, BFS naturally finds shortest paths because it explores level by level. No need for Dijkstra or any priority queue.
   - *Aha:* BFS on a grid = BFS on an implicit graph where each cell is a node and edges connect adjacent passable cells.

2. **Visit each cell at most once** -- Once a cell is visited at distance `d`, no shorter path to it exists (BFS guarantee). So we mark cells visited immediately upon discovery.
   - *Aha:* This is why we mark visited when ENQUEUING, not when DEQUEUING. Marking on dequeue can lead to duplicate work.

3. **Early termination: stop when destination is reached** -- We do not need to complete the entire BFS. The moment we dequeue (or discover) the destination, we have the answer.
   - *Aha:* In the worst case we still visit all cells, but on average early termination helps.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Traversal | BFS | Unit-weight edges -> BFS = shortest path |
| Queue | Standard FIFO queue | O(1) enqueue/dequeue |
| Visited tracking | Modify grid in-place (mark as 1) or use dist[][] | Avoid revisiting |
| Distance tracking | Level count or dist[][] array | Track shortest distance |

---

## APPROACH LADDER

### Approach 1: Brute Force -- DFS Explore All Paths

**Intuition:** Use DFS to explore every possible path from source to destination. Track the minimum path length among all complete paths.

**Steps:**
1. DFS from source, tracking current path length
2. At each step, try all 4 directions
3. If we reach destination, update minimum distance
4. Backtrack (unmark visited) to explore other paths

**BUD Analysis:**
- **Bottleneck:** Explores ALL paths, exponential in the worst case
- **Unnecessary:** Many paths are longer than the optimal; no pruning
- **Duplicated:** Same cells visited in different DFS branches

| Metric | Value |
|--------|-------|
| Time   | O(4^(n*n)) worst case -- exponential |
| Space  | O(n * n) -- recursion stack |

---

### Approach 2: Optimal -- BFS (Standard)

**Intuition:** BFS explores cells level by level. Level 0 = source, Level 1 = cells 1 step away, etc. The first time we reach the destination, we have the shortest path.

**Steps:**
1. If source == destination, return 0
2. If source or destination is a wall (1), return -1
3. Enqueue source, mark as visited
4. BFS level by level:
   a. For each cell at current level, check 4 neighbors
   b. If neighbor is destination, return current distance + 1
   c. If neighbor is passable and unvisited, mark visited and enqueue
5. If BFS completes without finding destination, return -1

```
Dry-run: grid=[[0,0,0],[0,1,0],[0,0,0]], src=(0,0), dst=(2,2)

Queue: [(0,0)]  dist=0
  Visit: {(0,0)}

Level 0: process (0,0)
  Neighbors: (0,1)ok, (1,0)ok
  Queue: [(0,1),(1,0)]  dist=1

Level 1: process (0,1),(1,0)
  (0,1) neighbors: (0,2)ok
  (1,0) neighbors: (2,0)ok
  Queue: [(0,2),(2,0)]  dist=2

Level 2: process (0,2),(2,0)
  (0,2) neighbors: (1,2)ok
  (2,0) neighbors: (2,1)ok
  Queue: [(1,2),(2,1)]  dist=3

Level 3: process (1,2),(2,1)
  (1,2) neighbors: (2,2) = DESTINATION! return 3+1 = 4 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(n * n) -- each cell visited at most once |
| Space  | O(n * n) -- queue + visited |

---

### Approach 3: Best -- BFS with In-Place Marking + Early Exit

**Intuition:** Same BFS but with optimizations:
- Mark cells as visited by changing grid value to 1 (wall) in-place, avoiding extra visited array
- Use flat index encoding for reduced memory overhead
- Check destination immediately when discovering (not when dequeuing)
- Bidirectional BFS could halve the search space but adds complexity

| Metric | Value |
|--------|-------|
| Time   | O(n * n) |
| Space  | O(n * n) for queue, O(1) extra with in-place marking |

---

## COMPLEXITY INTUITIVELY

**Why O(n*n)?** The grid has n*n cells. BFS visits each cell at most once (marked visited upon enqueue). Each cell has at most 4 neighbors to check. Total work: 4 * n*n = O(n*n).

**Why BFS and not Dijkstra?** All edge weights are 1 (unit weight). BFS is a special case of Dijkstra for unit weights. Dijkstra with a heap would add O(log(n*n)) overhead per operation -- unnecessary when all weights are equal.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| src == dst | Return 0 | Check before starting BFS |
| src is wall (1) | Return -1 | Can't start from a wall |
| dst is wall (1) | Return -1 | Can't reach a wall |
| 1x1 grid, src=dst=(0,0) | Return 0 | Grid value must be 0 |
| Completely blocked | Return -1 | BFS exhausts queue without reaching dst |
| Large open grid | BFS explores all cells | O(n*n) is the best we can do |

**Common Mistakes:**
- Not checking if source/destination is blocked (grid value = 1)
- Marking visited on DEQUEUE instead of ENQUEUE (causes duplicate processing)
- Off-by-one in distance counting (forgetting the +1 when reaching destination)
- Modifying grid when the caller expects it preserved (use a copy or restore)

---

## INTERVIEW LENS

**Why interviewers ask this:**
- Classic BFS-on-grid problem -- fundamental pattern
- Tests understanding of when BFS vs Dijkstra is appropriate
- Grid traversal with obstacles is extremely common in interviews

**Follow-ups to expect:**
1. "What if diagonal moves are allowed?" -- 8 directions instead of 4
2. "What if some cells have different weights?" -- Use Dijkstra instead of BFS
3. "What if you can break one wall?" -- BFS with state `(r, c, walls_broken)`
4. "Find the actual path, not just distance" -- Track parent pointers, backtrack from dst
5. "What about 0-1 BFS?" -- For graphs with weights 0 and 1, use deque (front/back)

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Dijkstra's Algorithm | Generalization for weighted graphs; BFS is Dijkstra for unit weights |
| Rotten Oranges (LC 994) | Multi-source BFS on grid, same traversal pattern |
| 01 Matrix (LC 542) | Multi-source BFS for shortest distance to 0 |
| Shortest Path in Grid with Obstacles (LC 1293) | BFS with extra state dimension (walls broken) |
| Maze I/II (LC 490/505) | BFS variant where you slide until hitting a wall |
| A* Search | BFS + heuristic for goal-directed shortest path |
