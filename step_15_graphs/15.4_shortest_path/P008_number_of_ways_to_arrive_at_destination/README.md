# Number of Ways to Arrive at Destination

> **Step 15.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #1976)** You are in a city of `n` intersections numbered `0` to `n-1`. You are given an array `roads` where `roads[i] = [u_i, v_i, time_i]` represents a **bidirectional** road between intersections `u_i` and `v_i` with travel time `time_i`.

You want to go from intersection `0` to intersection `n-1` in the **shortest possible time**. Return the **number of ways** to reach your destination in the shortest time. Since the answer may be large, return it **modulo 10^9 + 7**.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| n=7, roads=[[0,6,7],[0,1,2],[1,2,3],[1,3,3],[6,3,3],[3,5,1],[6,5,1],[2,5,1],[0,4,5],[4,6,2]] | 4 | There are 4 shortest paths of length 7 from 0 to 6 |
| n=2, roads=[[0,1,5]] | 1 | Only one direct road |
| n=4, roads=[[0,1,1],[0,2,1],[1,3,1],[2,3,1]] | 2 | Two equal-length paths: 0->1->3 and 0->2->3 |

```
Example 1 (n=7):
Shortest path from 0 to 6 has length 7.
Four paths of length 7:
  0 -> 6           (direct, cost 7)
  0 -> 4 -> 6     (5 + 2 = 7)
  0 -> 1 -> 3 -> 6 (2 + 3 + ... ) -- check actual edges
  0 -> 1 -> 2 -> 5 -> 6  etc.
Answer = 4
```

### Constraints
- `1 <= n <= 200`
- `n - 1 <= roads.length <= n * (n - 1) / 2`
- `roads[i].length == 3`
- `0 <= u_i, v_i <= n - 1`
- `1 <= time_i <= 10^9`
- The graph is connected, no self-loops, no duplicate edges

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Shortest path | Dijkstra | Non-negative weights |
| Count paths | ways[] alongside dist[] | Update ways when equal-cost path found |
| Large numbers | Modulo 10^9+7 | Prevent integer overflow |
| Large weights | long/int64 dist | time_i up to 10^9; sums can overflow 32-bit int |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Dijkstra then DFS path counting

**Intuition:** Two-phase approach: (1) compute shortest path distance using Dijkstra; (2) DFS from 0 to n-1, counting paths whose total cost exactly equals the shortest distance.

**Steps:**
1. Run Dijkstra from node 0 to find `shortest_dist = dist[n-1]`
2. DFS from 0 with `(current_node, current_cost)` state
3. If `current_cost == shortest_dist` and `node == n-1`, increment count
4. Prune if `current_cost >= shortest_dist`
5. Return count mod 10^9+7

**Why this is brute force:** DFS may explore exponentially many paths in dense graphs. Works for small inputs but doesn't scale.

| Metric | Value |
|--------|-------|
| Time   | O(2^V) worst case for DFS + O((V+E) log V) for Dijkstra |
| Space  | O(V + E) -- adjacency list and call stack |

---

### Approach 2: Optimal -- Dijkstra with ways[] tracking

**Intuition:** Piggyback the path count onto Dijkstra itself. Maintain two arrays:
- `dist[v]` = shortest distance from 0 to v (standard Dijkstra)
- `ways[v]` = number of shortest paths from 0 to v

**Key insight:** When processing a neighbor `v` from node `u`:
- If `dist[u] + w < dist[v]`: found a strictly shorter path. **Reset** `ways[v] = ways[u]`
- If `dist[u] + w == dist[v]`: found an equally short path. **Add** `ways[v] += ways[u]`

**Steps:**
1. Initialize `dist[0] = 0`, `ways[0] = 1`; all others `dist = INF`, `ways = 0`
2. Push `(0, 0)` into min-heap
3. Pop `(d, u)`:
   - Skip if stale
   - For each neighbor `(v, w)`:
     - `new_dist = d + w`
     - `< dist[v]`: update dist, reset ways, push to heap
     - `== dist[v]`: add to ways (no push -- dist unchanged)
4. Return `ways[n-1] % MOD`

```
Dry-run: n=4, roads=[[0,1,1],[0,2,1],[1,3,1],[2,3,1]]

Init: dist=[0,INF,INF,INF], ways=[1,0,0,0], heap=[(0,0)]

Pop (0,0):
  neighbor 1, w=1: new_dist=1 < INF => dist[1]=1, ways[1]=1, push (1,1)
  neighbor 2, w=1: new_dist=1 < INF => dist[2]=1, ways[2]=1, push (1,2)

Pop (1,1):
  neighbor 3, w=1: new_dist=2 < INF => dist[3]=2, ways[3]=ways[1]=1, push (2,3)

Pop (1,2):
  neighbor 3, w=1: new_dist=2 == dist[3]=2 => ways[3] += ways[2] => ways[3]=2

Pop (2,3): node 3 is destination.

Return ways[3] = 2 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O((V + E) log V) -- Dijkstra with one extra O(1) per edge |
| Space  | O(V + E) -- dist[], ways[], heap, adj list |

---

### Approach 3: Best -- Same Dijkstra with array adjacency

**Intuition:** Identical algorithm to Approach 2. Minor optimization: use array-of-lists adjacency (pre-allocated) instead of a HashMap for O(1) neighbor access. Also ensures `long`-typed distance accumulation to handle `time_i` up to 10^9 (cumulative sums can exceed 32-bit int).

**Steps:** Same as Approach 2.

| Metric | Value |
|--------|-------|
| Time   | O((V + E) log V) |
| Space  | O(V + E) -- slightly more cache-friendly |

---

## COMPLEXITY INTUITIVELY

**Why reset vs add ways?** If `new_dist < dist[v]`, the old ways to reach v are all suboptimal now -- they don't achieve the new shortest distance. Start fresh with `ways[u]` paths. If `new_dist == dist[v]`, both the old and new paths are equally optimal, so we add them together.

**Why mod only on addition?** The count only grows during `==` updates. `ways[v] = ways[u]` (reset on shorter path) doesn't need mod since `ways[u]` is already taken mod.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| n=1 (src == dst) | ways[0] = 1, return 1 | Handled by init |
| Single path | ways[n-1] = 1 | No equal-cost branches |
| Parallel edges | Multiple equal-cost paths | ways[] accumulation handles correctly |
| Large weights (10^9) | Sum overflows int | Use long for dist accumulation |

**Common Mistakes:**
- Using int for distances (overflow with time up to 10^9, path lengths sum can be ~200*10^9)
- Forgetting to mod in the `ways[v] += ways[u]` step (count grows exponentially)
- Applying mod to dist[] (dist must be exact for correct comparison)
- Pushing to heap when `new_dist == dist[v]` (unnecessary; dist doesn't change)

---

## Real-World Use Case

**Navigation Apps (Route Alternatives):** Google Maps / Waze find the fastest route but also show "N alternative routes with the same estimated time." This is exactly counting shortest paths.

**Network Reliability:** Given the number of equally-short paths, network engineers know how many independent routes exist. More paths = more fault tolerance. If k paths exist, the network survives k-1 failures while maintaining optimal latency.

---

## Interview Tips
- This is Dijkstra + path counting. State the two-array approach (dist + ways) immediately.
- Explicitly explain the "reset vs add" rule for `ways[]` -- this is what interviewers want to hear.
- Mention modular arithmetic: only apply mod to `ways[]`, never to `dist[]`.
- Use `long` for distances -- easy mistake with large weights that TLEs or gives wrong answers due to overflow.
- Follow-up: "What if you want paths within 10% of optimal?" -- Extend to track all paths where `new_dist <= 1.1 * dist[v]`.
- Related: LC 743 (Network Delay Time) -- same Dijkstra but without path counting.
