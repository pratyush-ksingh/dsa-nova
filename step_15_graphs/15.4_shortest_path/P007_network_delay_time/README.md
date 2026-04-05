# Network Delay Time

> **Step 15.4** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

---

## UNDERSTAND

### Problem Statement
**(LeetCode #743)** You have a network of `n` nodes labeled `1` to `n`. You are given a list of travel times as directed edges `times[i] = [u_i, v_i, w_i]` where `u_i` is the source, `v_i` is the target, and `w_i` is the travel time. A signal is sent from a given node `k`. Return the **minimum time it takes for all `n` nodes to receive the signal**. If it is impossible for all nodes to receive the signal, return `-1`.

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| times=[[2,1,1],[2,3,1],[3,4,1]], n=4, k=2 | 2 | From 2: node1=1, node3=1, node4=2. Max=2 |
| times=[[1,2,1]], n=2, k=1 | 1 | Direct edge, both nodes reached |
| times=[[1,2,1]], n=2, k=2 | -1 | Node 1 unreachable from 2 |

```
Example 1 visualization:
     1
   ↗
2       (2->3->4, cost=2)
   ↘
     3 -> 4

Signal from 2: reaches 1 at t=1, reaches 3 at t=1, reaches 4 at t=2
Answer = max(1, 1, 2) = 2
```

### Constraints
- `1 <= k <= n <= 100`
- `1 <= times.length <= 6000`
- `times[i].length == 3`
- `1 <= u_i, v_i <= n`
- `u_i != v_i`
- `0 <= w_i <= 100`
- All `(u_i, v_i)` pairs are unique

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Single source | SSSP from node k | Signal originates at one node |
| Non-negative weights | Dijkstra | Optimal for non-negative edge weights |
| Answer | max(dist[1..n]) | Slowest node determines total delay |
| Unreachable | Return -1 | Any INF distance means some node never receives signal |

---

## APPROACH LADDER

### Approach 1: Brute Force -- Bellman-Ford

**Intuition:** Relax all edges V-1 times. After `i` relaxations, `dist[v]` holds the shortest path using at most `i` edges. For V nodes, V-1 relaxations are sufficient.

**Steps:**
1. Initialize `dist[k] = 0`, all others `INF`
2. Repeat V-1 times:
   a. For each edge `(u, v, w)`: if `dist[u] + w < dist[v]`, update
   b. If no update in a round, break early
3. `max(dist[1..n])`. If any is INF, return -1

| Metric | Value |
|--------|-------|
| Time   | O(V * E) -- up to n * E relaxations |
| Space  | O(V) -- dist array only |

---

### Approach 2: Optimal -- Dijkstra with Min-Heap

**Intuition:** Since all edge weights are non-negative, Dijkstra is optimal. The signal spreading from `k` is exactly single-source shortest paths. The answer is the maximum shortest-path distance (slowest node to receive signal).

**Steps:**
1. Build adjacency list from `times`
2. Initialize `dist[k] = 0`, all others `INF`
3. Push `(0, k)` into min-heap
4. Pop `(d, u)`:
   - If `d > dist[u]`, skip (stale)
   - For each neighbor `(v, w)`: if `dist[u] + w < dist[v]`, update and push
5. Return `max(dist[1..n])`, or -1 if any INF remains

```
Dry-run: times=[[2,1,1],[2,3,1],[3,4,1]], k=2

Init: dist=[INF,INF,0,INF,INF] (1-indexed), heap=[(0,2)]

Pop (0,2): relax (2->1, w=1): dist[1]=1, push (1,1)
           relax (2->3, w=1): dist[3]=1, push (1,3)
heap=[(1,1),(1,3)]

Pop (1,1): no outgoing edges from node 1
Pop (1,3): relax (3->4, w=1): dist[4]=2, push (2,4)
heap=[(2,4)]

Pop (2,4): no outgoing edges from 4

dist = [INF, 1, 0, 1, 2]
max(dist[1..4]) = 2 ✓
```

| Metric | Value |
|--------|-------|
| Time   | O((V + E) log V) -- each node processed once, each edge relaxed |
| Space  | O(V + E) -- adjacency list + dist array + heap |

---

### Approach 3: Best -- Dijkstra with Early Termination

**Intuition:** Same as Approach 2 but with an optimization: track how many nodes have been finalized. When the n-th node is popped from the heap, its distance is the maximum shortest distance, which IS the answer. No need to scan the dist array afterward.

**Steps:** Same as Approach 2, but:
- Increment `visited` counter when a node is popped (non-stale)
- When `visited == n`, return the current distance immediately

This also naturally handles the unreachable case: if the heap empties before `visited == n`, return -1.

| Metric | Value |
|--------|-------|
| Time   | O((V + E) log V) -- same asymptotically, slightly faster constant |
| Space  | O(V + E) |

---

## COMPLEXITY INTUITIVELY

**Why is the answer the max shortest distance?** The signal spreads optimally (Dijkstra gives shortest times). Each node receives the signal at its shortest-path time from k. The last node to receive it determines the total delay. So the answer = max over all nodes of their shortest-path distance from k.

**Why does Dijkstra beat Bellman-Ford here?** With n <= 100 and E <= 6000, both are fast. But Dijkstra is O((V+E) log V) vs O(VE). For dense graphs Bellman-Ford degrades badly.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| n=1 | dist[k]=0, max=0, return 0 | Single node, instant |
| Disconnected graph | Some dist[i]=INF | Return -1 |
| k has no outgoing edges | Only dist[k]=0 | Return -1 (others unreachable) |
| Multiple edges between same pair | Take minimum | Bellman-Ford handles naturally; Dijkstra's lazy deletion handles it |

**Common Mistakes:**
- Nodes are 1-indexed (1..n), not 0-indexed. Allocate dist of size n+1.
- Forgetting to check for -1 (unreachable nodes) -- just checking max without INF check
- Using unweighted BFS (only works for uniform-weight graphs)

---

## Real-World Use Case

**Network Broadcasting:** A router broadcasts a packet to all nodes. The "network delay time" tells network engineers the worst-case propagation time. Used in network timing analysis (NTA) and SLA verification.

**Emergency Alert Systems:** Signal sent from command center. How long until every district (node) receives the alert? Dijkstra gives the answer; the max distance is the "all-clear" time.

---

## Interview Tips
- This is a direct application of Dijkstra's SSSP. State this immediately.
- The answer being `max(dist)` is the non-obvious part -- explain it clearly: the signal reaches everyone only when even the slowest path completes.
- Mention the -1 case: if any node has dist=INF after Dijkstra, the graph is disconnected from k.
- Follow-up: "What if edges can have negative weights?" -- Use Bellman-Ford (O(VE)).
- Follow-up: "What's the minimum number of edges to guarantee all nodes receive signal?" -- This is the MST/spanning tree problem.
