# Floyd Warshall Algorithm

> **Step 15.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

Given a weighted directed graph with **V** vertices represented as a **V x V adjacency matrix**, find the **shortest path between every pair of vertices** (all-pairs shortest paths).

- `matrix[i][j]` = weight of the direct edge from vertex `i` to vertex `j`.
- `matrix[i][j] = INF` (use `1e8`) means no direct edge exists.
- `matrix[i][i] = 0` (distance from a vertex to itself is 0).
- Edge weights can be **negative**, but the graph must contain **no negative-weight cycles** for the result to be meaningful.

Return the updated matrix where `matrix[i][j]` holds the shortest distance from `i` to `j`.

## Examples

**Example 1:**
```
Input (V=4):
       0    1    2    3
  0 [  0,   3, INF,   7 ]
  1 [  8,   0,   2, INF ]
  2 [  5, INF,   0,   1 ]
  3 [  2, INF, INF,   0 ]

Output:
       0    1    2    3
  0 [  0,   3,   5,   6 ]
  1 [  5,   0,   2,   3 ]
  2 [  3,   6,   0,   1 ]
  3 [  2,   5,   7,   0 ]
```
**Explanation:** e.g., shortest path 1->0 is 1->2->3->0 = 2+1+2 = 5 (not the direct 8).

**Example 2 (Negative Cycle):**
```
Input (V=3):
  0->1 (1), 1->2 (-2), 2->0 (-1)
  Cycle sum: 1 + (-2) + (-1) = -2 < 0 → negative cycle!
```

## Constraints

- `1 <= V <= 100`
- `-1000 <= matrix[i][j] <= 1000` (when edge exists)
- `matrix[i][i] = 0` always
- Use `INF = 1e8` (not `Integer.MAX_VALUE`) to avoid integer overflow on addition
- The graph may have negative edges but the problem assumes **no negative cycles** for a valid answer

---

## Approach 1: Brute Force — Dijkstra from Every Source

**Intuition:** The simplest way to get all-pairs shortest paths is to run a single-source shortest path algorithm (Dijkstra) from every vertex. This reuses known-correct logic but runs it V times.

**Steps:**
1. Convert the adjacency matrix to an adjacency list (skip INF entries).
2. For each source vertex `src` from `0` to `V-1`:
   - Run Dijkstra with `src` as the starting node.
   - Store the resulting distance array as row `src` of the answer.
3. Return the filled answer matrix.

**Limitation:** Dijkstra requires non-negative weights. Does NOT handle negative edges.

| Metric | Value |
|--------|-------|
| Time   | O(V * (V + E) * log V) |
| Space  | O(V^2) for result + O(V + E) for adjacency list |

---

## Approach 2: Optimal — Classic Floyd-Warshall

**Intuition:** Instead of running separate shortest-path searches, use dynamic programming. Define `dp[i][j]` as the shortest path from `i` to `j` using only vertices `{0, 1, ..., k}` as intermediate nodes. Iterate `k` from `0` to `V-1`, progressively "unlocking" each vertex as an allowed intermediate. The recurrence is:

```
dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j])
```

This means: "Can we do better by going from `i` to `k`, then `k` to `j`?"

**Steps:**
1. Copy the input matrix into `dp[][]`. Set `dp[i][i] = 0` for all `i`.
2. Outer loop: `k` from `0` to `V-1` (intermediate vertex).
3. Inner loops: `i` from `0` to `V-1`, `j` from `0` to `V-1`.
4. If `dp[i][k] != INF` and `dp[k][j] != INF`:
   - `dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j])`
5. Return `dp`.

**Key insight:** The order of the outer `k` loop is critical. When processing intermediate `k`, `dp[i][k]` and `dp[k][j]` already hold optimal values for the sub-problem with intermediates `{0..k-1}`, which is exactly what we need.

| Metric | Value |
|--------|-------|
| Time   | O(V^3) |
| Space  | O(V^2) |

---

## Approach 3: Best — Floyd-Warshall + Negative Cycle Detection

**Intuition:** Same algorithm as Optimal, with one critical addition: after the triple loop finishes, inspect the diagonal. If `dp[i][i] < 0` for any vertex `i`, a negative-weight cycle passes through `i`. In that case, the shortest paths involving nodes on that cycle are `-infinity` — the result is undefined. Production code must detect and report this.

**Steps:**
1. Run the exact same Floyd-Warshall triple loop as in Optimal.
2. After the loop completes, scan the diagonal:
   - For each `i`, check if `dp[i][i] < 0`.
   - If yes, a negative cycle exists — raise an error or return a sentinel.
3. Otherwise return `dp` as the valid all-pairs shortest path matrix.

**Why does this work?** Initially `dp[i][i] = 0`. A negative cycle `i -> ... -> i` with total weight < 0 means we can make `dp[i][i]` strictly negative by going around the cycle. Floyd-Warshall propagates this correctly.

| Metric | Value |
|--------|-------|
| Time   | O(V^3) |
| Space  | O(V^2) |

---

## Dry Run (Optimal / Best, V=4)

```
Initial dp:
  [0,   3, INF,  7]
  [8,   0,   2, INF]
  [5, INF,   0,  1]
  [2, INF, INF,  0]

k=0 (intermediate = vertex 0):
  dp[1][3] = min(INF, dp[1][0]+dp[0][3]) = min(INF, 8+7) = 15
  dp[2][1] = min(INF, dp[2][0]+dp[0][1]) = min(INF, 5+3) = 8
  dp[2][3] = min(1,   dp[2][0]+dp[0][3]) = min(1,  5+7) = 1  (no change)
  dp[3][1] = min(INF, dp[3][0]+dp[0][1]) = min(INF, 2+3) = 5
  dp[3][2] = min(INF, dp[3][0]+dp[0][2]) = min(INF, 2+INF) = INF (no change)
  dp[3][3] = min(0,   dp[3][0]+dp[0][3]) = min(0, 2+7) = 0  (no change)

... (continue for k=1,2,3)

Final dp:
  [0, 3,  5,  6]
  [5, 0,  2,  3]
  [3, 6,  0,  1]
  [2, 5,  7,  0]
```

---

## Real-World Use Case

**Network routing (OSPF / BGP):** In a computer network, routers need to know the shortest latency or cost path between every pair of routers — not just from one source. Floyd-Warshall is used in **link-state routing protocols** and network simulators. Other applications:

- **Road network analysis:** Find the fastest route between every city pair for trip-planning APIs.
- **Transitive closure:** Set `INF` detection to compute reachability (can vertex `i` reach `j`?). Used in dependency resolution (package managers, build systems).
- **Arbitrage detection in currency exchange:** Negative cycles in a log-weight graph indicate a sequence of currency conversions that yields a profit — exactly what Floyd-Warshall's negative cycle detection catches.

---

## Interview Tips

- Always clarify whether edge weights can be negative. Dijkstra fails with negative edges; Floyd-Warshall handles them (as long as no negative cycle exists).
- Use `INF = 1e8` (not `Integer.MAX_VALUE` / `float('inf')`) to avoid overflow when computing `dp[i][k] + dp[k][j]`.
- The order of loops is `k` (intermediate) outermost, then `i`, then `j`. Swapping `k` with `i` or `j` breaks correctness.
- The diagonal check (`dp[i][i] < 0`) is the canonical negative-cycle detection — mention it proactively.
- Floyd-Warshall works in-place on a single 2D matrix (no 3D array needed) because when we process intermediate `k`, both `dp[i][k]` and `dp[k][j]` are already finalized for all intermediates `< k`.
- Space can be reduced to O(V^2) — already optimal since we must store all-pairs distances.
- Compare complexities: Dijkstra repeated = O(V*(V+E)logV); Floyd-Warshall = O(V^3). For dense graphs (E ~ V^2), both are O(V^3), but Floyd-Warshall has smaller constants and simpler code.
