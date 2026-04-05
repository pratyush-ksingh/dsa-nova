# Course Schedule II

> **Step 15.3** | **Difficulty:** MEDIUM | **XP:** 25 | **Status:** UNSOLVED

## Problem Statement

**(LeetCode #210)** There are a total of `numCourses` courses you have to take, labeled from `0` to `numCourses - 1`. You are given an array `prerequisites` where `prerequisites[i] = [ai, bi]` indicates that you **must** take course `bi` before course `ai`.

Return the ordering of courses you should take to finish all courses. If there are multiple valid answers, return any of them. If it is impossible to finish all courses (i.e., there is a cycle), return an **empty array**.

## Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| numCourses=4, prerequisites=[[1,0],[2,0],[3,1],[3,2]] | [0,1,2,3] or [0,2,1,3] | Take 0 first, then 1 and 2 in any order, then 3 |
| numCourses=2, prerequisites=[[1,0]] | [0,1] | Must take 0 before 1 |
| numCourses=2, prerequisites=[[1,0],[0,1]] | [] | Cycle: 0 needs 1, 1 needs 0 |
| numCourses=1, prerequisites=[] | [0] | Single course, no dependencies |

## Constraints

- 1 <= numCourses <= 2000
- 0 <= prerequisites.length <= numCourses * (numCourses - 1)
- prerequisites[i].length == 2
- 0 <= ai, bi < numCourses
- ai != bi
- All pairs [ai, bi] are distinct

---

## Approach 1: Brute Force (DFS-based Topological Sort)

**Intuition:** Topological sort via DFS works by processing nodes in reverse post-order. We run DFS from each unvisited node. When a node's DFS completes (all descendants processed), we push it to a stack. The stack order gives the topological sort. We use 3-state coloring (white/gray/black) to detect cycles: if DFS reaches a gray (in-progress) node, a cycle exists.

**Why 3 colors?** White = unvisited, Gray = currently being explored (on the DFS stack), Black = fully done. A gray-to-gray edge means we found a back edge (cycle). White-to-gray and black encounters are safe.

**Steps:**
1. Build adjacency list from prerequisites: `b -> a` (b is prerequisite of a)
2. Initialize `color[]` array (all white = 0)
3. For each unvisited node, call `dfs(node)`
4. `dfs(node)`:
   - Set color to gray (1)
   - For each neighbor: if gray -> cycle; if white -> recurse
   - Set color to black (2), append to order list
5. Reverse the order list to get topological order
6. If cycle detected at any point, return empty array

**Dry-Run Trace:**
```
numCourses=4, prerequisites=[[1,0],[2,0],[3,1],[3,2]]
adj: 0->[1,2], 1->[3], 2->[3], 3->[]

dfs(0): color[0]=gray
  dfs(1): color[1]=gray
    dfs(3): color[3]=gray
      no neighbors -> color[3]=black, order=[3]
    color[1]=black, order=[3,1]
  dfs(2): color[2]=gray
    neighbor 3: black, skip
    color[2]=black, order=[3,1,2]
  color[0]=black, order=[3,1,2,0]

Reverse: [0,2,1,3]  -- valid topological order!
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Visit each node once, traverse each edge once |
| Space  | O(V + E) | Adjacency list + color array + recursion stack |

---

## Approach 2: Optimal (Kahn's Algorithm - BFS with indegree)

**Intuition:** Kahn's algorithm works by repeatedly removing nodes with no incoming edges (indegree = 0). These nodes have no unmet prerequisites, so they can be taken first. After removing a node, decrement the indegree of its dependents. If a dependent reaches indegree 0, it is now ready. If we process all nodes, we have a valid order. If not, a cycle prevents some nodes from ever reaching indegree 0.

**Why Kahn's is natural here:** The problem literally asks "which courses can I take?" -- courses with no remaining prerequisites. Kahn's directly models this: take all available courses, unlock new ones, repeat.

**Steps:**
1. Build adjacency list and compute `indegree[]` for each node
2. Add all nodes with `indegree == 0` to a queue
3. While queue is not empty:
   - Dequeue node, add to result
   - For each neighbor: decrement indegree; if reaches 0, enqueue
4. If result size == numCourses: return result. Otherwise: cycle exists, return []

**Dry-Run Trace:**
```
numCourses=4, prereqs=[[1,0],[2,0],[3,1],[3,2]]
adj: 0->[1,2], 1->[3], 2->[3]
indegree: [0, 1, 1, 2]

Queue: [0] (indegree 0)
  Process 0: order=[0], decrement 1->0, 2->0
  Queue: [1, 2]
  Process 1: order=[0,1], decrement 3->1
  Queue: [2]
  Process 2: order=[0,1,2], decrement 3->0
  Queue: [3]
  Process 3: order=[0,1,2,3]

len(order)=4 == numCourses -> return [0,1,2,3]
```

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Process each node and edge once |
| Space  | O(V + E) | Adjacency list + indegree array + queue |

---

## Approach 3: Best (Kahn's - clean implementation)

**Intuition:** Same Kahn's algorithm with a cleaner, more concise implementation. Kahn's is the preferred approach for Course Schedule II because:
1. It naturally produces the order in the correct direction (no reversal needed, unlike DFS)
2. Cycle detection is trivial (just check if all nodes were processed)
3. It is iterative, avoiding recursion stack overflow on large inputs
4. The "process nodes with no remaining prerequisites" model directly matches the problem's semantics

**Steps:**
1. Same as Approach 2 with streamlined code
2. Use `--indegree[next] == 0` inline check for conciseness

| Metric | Value | Reasoning |
|--------|-------|-----------|
| Time   | O(V + E) | Same as Kahn's |
| Space  | O(V + E) | Same as Kahn's |

---

## Real-World Use Case

**Build Systems and Package Managers:** Tools like `make`, `gradle`, `npm`, and `apt` use topological sorting to determine the order in which to build/install packages. Each package may depend on others (prerequisites). The build system computes a topological order of the dependency graph, and if a circular dependency exists, it reports an error. Course Schedule II is exactly this problem: find a valid build order or report that it is impossible.

## Interview Tips

- Start by identifying this as a topological sort problem -- any "ordering with dependencies" problem is topo sort
- Know BOTH DFS and Kahn's approaches; interviewers may ask for both
- Kahn's is generally easier to implement correctly in an interview and handles cycle detection more cleanly
- The edge direction matters: `[a, b]` means b -> a (b is prerequisite of a), NOT a -> b
- Common mistake: building the graph in the wrong direction (swapping source and destination)
- If asked "what if there are multiple valid orderings?" -- both approaches can be modified (e.g., use min-heap in Kahn's for lexicographically smallest order)
- Cycle detection: in DFS use 3 colors; in Kahn's just check if processed count equals numCourses
- Follow-up: "Can you find the minimum number of semesters?" -- this becomes a BFS level-order problem (parallel scheduling)
