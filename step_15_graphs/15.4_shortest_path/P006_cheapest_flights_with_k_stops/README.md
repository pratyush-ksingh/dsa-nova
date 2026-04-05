# Cheapest Flights Within K Stops

> **Step 15.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #787)** There are `n` cities numbered `0` to `n-1`. You are given an array `flights` where `flights[i] = [from_i, to_i, price_i]` representing directed flights. Find the **cheapest price** from `src` to `dst` with **at most `k` stops** (intermediate cities). Return `-1` if no such route exists.

Note: `k` stops means `k+1` edges in the path.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| n=4, flights=[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], src=0, dst=3, k=1 | 700 | 0->1->3 costs 100+600=700; 0->1->2->3 uses 2 stops (too many) |
| n=3, flights=[[0,1,100],[1,2,100],[0,2,500]], src=0, dst=2, k=1 | 200 | 0->1->2 costs 200, cheaper than direct 0->2=500 |
| n=3, flights=[[0,1,100],[1,2,100],[0,2,500]], src=0, dst=2, k=0 | 500 | k=0 means direct only; only 0->2 is valid |

### Constraints
- `1 <= n <= 100`
- `0 <= flights.length <= (n * (n-1) / 2)`
- `flights[i].length == 3`
- `0 <= from_i, to_i < n`
- `1 <= price_i <= 10^4`
- `0 <= k < n`
- There are no duplicate flights and no self-loops

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Constraint on path length | k+1 relaxation rounds | Bellman-Ford naturally limits hops per round |
| Shortest path | Bellman-Ford / Dijkstra with stops | Standard SSSP adapted for hop limit |
| Why not plain Dijkstra | Stops matter, not just cost | A cheaper path may use more stops than allowed |

---

## APPROACH LADDER

### Approach 1: Brute Force -- BFS/DFS exploring all paths

**Intuition:** Enumerate every possible path from `src` to `dst` with at most `k` stops using BFS. No sophisticated pruning; just enforce the stop limit and track minimum cost.

**Steps:**
1. Build adjacency list
2. BFS queue: `(current_node, total_cost, stops_used)`
3. At each step, expand all outgoing flights
4. Skip if `stops > k`; update best cost when `node == dst`
5. Use a `min_cost[node][stops]` table to prune revisits

| Metric | Value |
|--------|-------|
| Time   | O(n^k) -- exponential in k, due to trying all combinations |
| Space  | O(n*k) -- queue and min-cost table |

---

### Approach 2: Optimal -- Bellman-Ford with k+1 Relaxation Rounds

**Intuition:** The classic Bellman-Ford algorithm relaxes all edges V-1 times. Here we only need `k+1` relaxations because each round allows exactly one more edge (= one more stop). **Critical:** use a snapshot copy of `dist` before each round to prevent chaining multiple hops within the same round.

**Steps:**
1. Initialize `dist[src] = 0`, all others `INF`
2. For `i = 0` to `k` (k+1 iterations):
   a. `temp = dist.copy()`
   b. For each flight `(u, v, w)`:  if `dist[u] + w < temp[v]`, update `temp[v]`
   c. `dist = temp`
3. Return `dist[dst]` or `-1`

```
Dry-run: n=3, flights=[[0,1,100],[1,2,100],[0,2,500]], src=0, dst=2, k=1

Initial: dist = [0, INF, INF]

Round 1 (i=0), temp starts as [0, INF, INF]:
  flight (0,1,100): dist[0]=0, 0+100=100 < INF  => temp[1] = 100
  flight (1,2,100): dist[1]=INF, skip
  flight (0,2,500): dist[0]=0, 0+500=500 < INF  => temp[2] = 500
  dist = [0, 100, 500]

Round 2 (i=1), temp starts as [0, 100, 500]:
  flight (0,1,100): 0+100=100, not < 100, skip
  flight (1,2,100): dist[1]=100, 100+100=200 < 500 => temp[2] = 200
  flight (0,2,500): 0+500=500, not < 200, skip
  dist = [0, 100, 200]

dist[2] = 200 ✓
```

**Why the snapshot copy matters:** Without it, in Round 1 we'd update `temp[1]=100` then immediately use that to set `temp[2]=200`, effectively completing a 2-hop path in 1 round (wrong).

| Metric | Value |
|--------|-------|
| Time   | O(k * E) -- k+1 rounds, each scanning all E edges |
| Space  | O(n) -- dist and temp arrays |

---

### Approach 3: Best -- Dijkstra with (cost, node, stops) State

**Intuition:** Extend standard Dijkstra with `stops_used` as part of the state. A node can be visited multiple times at different stop counts. Use `min_cost[node][stops]` to prune stale heap entries. This approach is faster on sparse graphs because it skips irrelevant states eagerly.

**Steps:**
1. Build adjacency list
2. `min_cost[node][stops] = INF` for all; `min_cost[src][0] = 0`
3. Push `(0, src, 0)` into min-heap
4. Pop `(cost, node, stops)`:
   - If `node == dst`, return `cost`
   - If `stops > k` or `cost > min_cost[node][stops]`, skip
   - Relax neighbors: push `(cost+price, neighbor, stops+1)` if it improves `min_cost[neighbor][stops+1]`
5. Return `-1` if heap exhausted

| Metric | Value |
|--------|-------|
| Time   | O(k * E * log(k*n)) -- heap operations bounded by k*n states |
| Space  | O(k*n) -- min_cost table and heap |

---

## COMPLEXITY INTUITIVELY

**Why Bellman-Ford not plain Dijkstra?** Standard Dijkstra finds the minimum-cost path regardless of hop count. Here we have a hard constraint of k stops. Bellman-Ford's round-based structure naturally enforces this: after `i` rounds, `dist[v]` is the cheapest path using at most `i` edges.

**Why is Dijkstra-with-stops valid?** Dijkstra works here because all edge weights (prices) are non-negative. We just expand the state space to include stop count.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| k=0 | Only direct flights src->dst are valid | Bellman-Ford runs 1 round |
| No path | Returns -1 | dist[dst] remains INF |
| src == dst | Distance is 0 | Works naturally (dist[src]=0) |
| Multiple paths same cost | Any valid one is returned | Both algorithms handle this |

**Common Mistakes:**
- Forgetting the temp copy in Bellman-Ford -- leads to multi-hop updates in one round
- Confusing k stops vs k+1 edges (k stops = k+1 edges = k+1 Bellman-Ford rounds)
- Using plain Dijkstra without stop tracking -- finds cheapest path but may exceed k stops

---

## Real-World Use Case

**Flight Booking with Layover Limits:** Airlines limit the number of connections (layovers) on a ticket. This problem directly models finding the cheapest itinerary with at most k layovers. Used in booking engines for airlines, trains, and bus networks.

**Network Routing with TTL:** In networking, IP packets have a Time-To-Live (TTL) that decrements at each hop. Finding the cheapest (lowest-latency) route within TTL hops is this exact problem.

---

## Interview Tips
- Start with Bellman-Ford; it's the cleanest solution for this problem. Mention the snapshot copy explicitly -- interviewers often ask why it's needed.
- Dijkstra with stops is faster on sparse graphs; explain the state-space expansion `(node, stops)`.
- The phrase "k stops = k+1 edges" trips up many people. Clarify it immediately.
- Follow-up: "What if you want exactly k stops?" -- Initialize dist after exactly k+1 Bellman-Ford rounds; don't take min across rounds.
- Connect to: Bellman-Ford (LC P009), Dijkstra (LC P003).
