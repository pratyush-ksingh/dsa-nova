# Course Schedule I

> **Batch 4 of 12** | **Topic:** Graphs | **Difficulty:** MEDIUM | **XP:** 25

---

## UNDERSTAND

### Problem Statement
**(LeetCode #207)** There are `numCourses` courses labeled `0` to `numCourses - 1`. You are given an array `prerequisites` where `prerequisites[i] = [a, b]` means you must take course `b` **before** course `a` (i.e., `b -> a` is a directed edge).

Return `true` if you can finish all courses. Return `false` if there is a **cycle** in the prerequisite graph (making it impossible to complete all courses).

### Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| numCourses=2, prereqs=[[1,0]] | true | Take 0 then 1 |
| numCourses=2, prereqs=[[1,0],[0,1]] | false | 0 requires 1, 1 requires 0 -- cycle! |
| numCourses=4, prereqs=[[1,0],[2,0],[3,1],[3,2]] | true | Order: 0->1->2->3 or 0->2->1->3 |

```
Example 3: numCourses=4, prereqs=[[1,0],[2,0],[3,1],[3,2]]

Directed graph:           In-degrees:
    (0) ---> (1)           0: 0  (no prereqs)
     |        |            1: 1  (needs 0)
     v        v            2: 1  (needs 0)
    (2) ---> (3)           3: 2  (needs 1 and 2)

Topological order: 0 -> 1 -> 2 -> 3  (valid, no cycle)

Example 2: numCourses=2, prereqs=[[1,0],[0,1]]

    (0) ----> (1)
     ^         |
     |_________|      <- CYCLE! Cannot topologically sort.
```

### Real-World Analogy
Think of a university degree plan. Some courses have prerequisites: you cannot take "Data Structures" before "Intro to Programming." If two courses are prerequisites of each other (a cycle), you are stuck in a deadlock -- you can never start either one. The question is: given all prerequisite rules, is it possible to plan a valid schedule?

### Three Key Observations

1. **This is cycle detection in a directed graph** -- If the prerequisite graph has a cycle, at least one group of courses forms a circular dependency, making completion impossible.
   - *Aha:* "Can you finish all courses?" is equivalent to "Does the directed graph have NO cycles?" which is equivalent to "Does a valid topological ordering exist?"

2. **Kahn's Algorithm (BFS Topo Sort): process courses with 0 in-degree** -- Start with courses that have no prerequisites. After "taking" them, reduce in-degrees of dependent courses. If a course reaches in-degree 0, it can be taken next.
   - *Aha:* If we process all reachable courses and the count < numCourses, there must be a cycle (the remaining courses are stuck in a cycle).

3. **DFS with 3 states detects back edges (cycles)** -- Color each node: WHITE (unvisited), GRAY (in current DFS path), BLACK (fully processed). A GRAY->GRAY edge is a back edge = cycle.
   - *Aha:* A back edge in DFS on a directed graph always indicates a cycle.

---

## DS & ALGO CHOICE

| Consideration | Choice | Why |
|---------------|--------|-----|
| Graph representation | Adjacency list | O(V+E) space, efficient traversal |
| Cycle detection (BFS) | Kahn's algorithm | In-degree tracking, natural for topo sort |
| Cycle detection (DFS) | 3-state coloring | GRAY node revisit = cycle |
| In-degree tracking | int[] array | O(1) access per vertex |

---

## APPROACH LADDER

### Approach 1: Brute Force -- DFS Cycle Detection (Visited + RecStack)

**Intuition:** Run DFS from every unvisited node. Maintain two boolean arrays: `visited[]` (ever visited) and `recStack[]` (currently on the DFS recursion stack). If we visit a node that is on the current recursion stack, we found a cycle.

**Steps:**
1. Build adjacency list from prerequisites
2. For each unvisited course, run DFS
3. In DFS: mark `recStack[node] = true`, visit all neighbors
   - If neighbor is on recStack: CYCLE found, return false
   - If neighbor not visited: recurse
4. After processing all neighbors: `recStack[node] = false`
5. If no cycle found in any DFS, return true

```
Dry-run: numCourses=2, prereqs=[[1,0],[0,1]]

DFS from 0:
  recStack = [T, F]
  Visit neighbor 1:
    recStack = [T, T]
    Visit neighbor 0:
      0 is on recStack! => CYCLE => return false
```

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V + E) -- adjacency list + recursion stack |

---

### Approach 2: Optimal -- Kahn's Algorithm (BFS Topological Sort)

**Intuition:** Use BFS to peel off courses layer by layer. Start with courses that have no prerequisites (in-degree 0). After processing them, reduce in-degrees of their dependents. If we process all V courses, no cycle exists.

**Steps:**
1. Build adjacency list and in-degree array
2. Enqueue all courses with in-degree 0
3. BFS: dequeue a course, increment processed count
   - For each dependent course: decrement in-degree
   - If in-degree reaches 0: enqueue it
4. If processed count == numCourses: return true (no cycle)
   Else: return false (cycle exists -- stuck courses remain)

```
Dry-run: numCourses=4, prereqs=[[1,0],[2,0],[3,1],[3,2]]

In-degree: [0, 1, 1, 2]
Queue: [0]  (only course 0 has in-degree 0)

Process 0: count=1
  Reduce 1's in-degree: 1->0, enqueue 1
  Reduce 2's in-degree: 1->0, enqueue 2
  In-degree: [0, 0, 0, 2], Queue: [1, 2]

Process 1: count=2
  Reduce 3's in-degree: 2->1
  In-degree: [0, 0, 0, 1], Queue: [2]

Process 2: count=3
  Reduce 3's in-degree: 1->0, enqueue 3
  In-degree: [0, 0, 0, 0], Queue: [3]

Process 3: count=4

count (4) == numCourses (4) => return true ✓
```

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V + E) |

---

### Approach 3: Best -- DFS with 3-State Coloring

**Intuition:** Same DFS approach but cleaner with 3 states: WHITE (0), GRAY (1), BLACK (2). GRAY means "currently being explored." If DFS hits a GRAY node, it found a back edge = cycle. BLACK nodes are fully processed and safe to skip.

**Steps:**
1. Build adjacency list
2. Initialize all nodes as WHITE (0)
3. For each WHITE node, run DFS:
   a. Color it GRAY
   b. For each neighbor:
      - If GRAY: cycle found, return false
      - If WHITE: recurse; if recursion returns false, propagate
   c. Color it BLACK
4. Return true (no cycles)

**Why this is "best":** Cleaner than separate visited/recStack arrays. Same time/space, but easier to reason about correctness.

| Metric | Value |
|--------|-------|
| Time   | O(V + E) |
| Space  | O(V + E) |

---

## COMPLEXITY INTUITIVELY

**Why O(V + E)?** Both BFS (Kahn's) and DFS visit each vertex once and traverse each edge once. Building the adjacency list is O(E). The in-degree array takes O(V) to initialize and O(E) to populate. Total: O(V + E).

**Is there anything faster?** No. We must examine every edge at least once to determine if a cycle exists, so O(V + E) is optimal.

---

## EDGE CASES & MISTAKES

| Edge Case | What Happens | Watch Out |
|-----------|--------------|-----------|
| No prerequisites | All courses independent, return true | In-degree all 0, all enqueued immediately |
| Self-loop [a, a] | Course requires itself -- cycle | In-degree never reaches 0 |
| Single course | Always true | No edges possible |
| Disconnected components | Must check ALL components | DFS/BFS from every unvisited node |
| Multiple valid orderings | Any valid ordering proves feasibility | We only need to know IF one exists |

**Common Mistakes:**
- Confusing edge direction: `[a, b]` means b->a (take b before a), NOT a->b
- Forgetting to check ALL components (not just starting from node 0)
- In DFS: forgetting to unmark recStack after backtracking (leads to false positives)

---

## INTERVIEW LENS

**Why interviewers ask this:**
- Topological sort is fundamental for dependency resolution
- Tests understanding of cycle detection in directed graphs
- Real-world relevance: build systems, task schedulers, package managers

**Follow-ups to expect:**
1. "Return the actual ordering" -- Course Schedule II (LC 210), return topo order
2. "What if courses have parallel tracks?" -- Same algorithm, multiple valid orders
3. "BFS vs DFS for cycle detection?" -- BFS (Kahn's) is iterative and cleaner; DFS needs careful state management
4. "Minimum semesters to finish?" -- BFS levels = minimum semesters (parallel courses)

---

## CONNECTIONS

| Related Problem | How It Connects |
|----------------|-----------------|
| Course Schedule II (LC 210) | Return the actual topological ordering |
| Alien Dictionary (LC 269) | Build graph from constraints, then topo sort |
| Parallel Courses (LC 1136) | BFS topo sort, count levels = min semesters |
| Build Order (CTCI) | Same problem, different framing |
| Detect Cycle in Directed Graph | Core sub-problem of this question |
| Prerequisite Tasks | Real-world task scheduling with topo sort |
