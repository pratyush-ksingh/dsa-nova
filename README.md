```
     ____  ____    _      _   _  _____     _____
    |  _ \/ ___|  / \    | \ | |/ _ \ \   / / _ \
    | | | \___ \ / _ \   |  \| | | | \ \ / / |_| |
    | |_| |___) / ___ \  | |\  | |_| |\ V /|  _  |
    |____/|____/_/   \_\ |_| \_|\___/  \_/ |_| |_|
```

# DSA NOVA - Your Quest to Master DSA

> *"Every expert was once a beginner. Every pro was once an amateur."*

A gamified, intelligent DSA preparation system combining **Striver's A2Z Sheet (434)** + **InterviewBit (178)** = **612 problems**.
Every problem includes **Brute Force, Optimal & Best** solutions in **Java + Python**.

**Key Features:**
- **Horizontal Batches** — Each batch covers ALL of DSA, progressively harder. Interview-ready at any point.
- **FSRS Spaced Repetition** — Fight the forgetting curve with science-backed review scheduling
- **ELO-Based Recommendations** — AI picks your next problem based on skill level, topic urgency, and learning flow
- **Real-World Use Cases** — Every algorithm mapped to industry applications (Google Maps, Spotify, Git, etc.)

---

## Player Card

| Stat | Value |
|------|-------|
| Level | **1 - Novice** |
| XP | **0 / 100** |
| Problems Solved | **0 / 612** |
| Current Streak | **0 days** |
| Badges | **0 / 9** |

```
[..................................................] 0% Complete
```

---

## Quick Start

### Prerequisites
- **Python 3.10+** (for game engine & scripts)
- **Java 17+** (for Java solutions)

### Installation
```bash
git clone https://github.com/pratyush-ksingh/dsa-nova.git
cd dsa-nova
```

No dependencies to install — pure Python standard library. No `pip install` needed.

### Launch the Game
```bash
python scripts/play.py
```

### Build Standalone .exe (optional)
```bash
pip install pyinstaller
pyinstaller --onefile --console --name dsa-nova --paths scripts \
  --hidden-import brain --hidden-import shared --hidden-import revision \
  --hidden-import batch_engine --hidden-import real_world \
  --distpath . --workpath build --specpath build --clean scripts/play.py
```
Then run `./dsa-nova.exe` from the project folder. No Python installation needed on the target machine.

### First Problem
```bash
python scripts/brain.py recommend     # AI picks your next problem
python scripts/brain.py complete P001 5 easy  # Mark it done (id, minutes, felt)
python scripts/update_progress.py     # Refresh XP, level, badges & README
```

---

## How It Works

### The Spiral Curriculum (12 Batches)

Unlike traditional linear DSA courses, DSA Nova uses a **horizontal spiral** approach. Every batch covers **all 16 DSA topics** — but each successive batch is harder. This means:

- After **Batch 1** alone, you have basic coverage of every topic
- After **Batch 6** (~316 problems), you're **interview-ready (~62%)**
- After **Batch 12**, you're in the **top 1-2%** of candidates

```
Batch 1:  [Easy]     Foundation     — Build your base across everything
Batch 2:  [Easy+]    Easy+          — Slight twists on fundamentals
Batch 3:  [E-M]      Easy-Medium    — Two-step thinking
Batch 4:  [M-L]      Medium-Light   — Standard interview warm-ups
Batch 5:  [Med]      Medium         — Solid medium-level across all topics
Batch 6:  [Med+]     Medium+        — First Hard problems (INFLECTION POINT)
Batch 7:  [M-H]      Medium-Hard    — Multi-technique combos
Batch 8:  [H-L]      Hard-Light     — Optimization-heavy
Batch 9:  [Hard]     Hard           — Complex constraints
Batch 10: [Hard+]    Hard+          — Advanced patterns
Batch 11: [Expert]   Expert         — Competition-grade
Batch 12: [Master]   Master         — Top-tier Hard. The last stand.
```

### Within Each Batch — Topic Dependency Order
```
Math → Arrays → Strings → Sorting → Bits → Sliding Window → Binary Search
→ Linked List → Stack/Queue → Recursion → Heaps → Greedy
→ Trees → BST → Graphs → DP
```

Topics are ordered so that prerequisite concepts are always covered first.

---

## Spiral Curriculum — 12 Batches

> Every batch covers ALL 16 DSA topics. Each batch is harder than the last.
> After Batch 6 (~316 problems), you're interview-ready (~62%).

| Batch | Name | Problems | Difficulty Mix | Interview Ready | Solved | Progress |
|-------|------|----------|----------------|-----------------|--------|----------|
| 1 | Foundation | 52 | 90% Easy, 10% Med | 10% | 0 | `[..........]` |
| 2 | Easy+ | 54 | 75% Easy, 25% Med | 18% | 0 | `[..........]` |
| 3 | Easy-Medium | 54 | 50% Easy, 50% Med | 28% | 0 | `[..........]` |
| 4 | Medium-Light | 54 | 25% Easy, 75% Med | 39% | 0 | `[..........]` |
| 5 | Medium | 54 | 100% Medium | 52% | 0 | `[..........]` |
| 6 | Medium+ | 53 | 85% Med, 15% Hard | **62%** | 0 | `[..........]` |
| 7 | Medium-Hard | 50 | 70% Med, 30% Hard | 71% | 0 | `[..........]` |
| 8 | Hard-Light | 49 | 50% Med, 50% Hard | 79% | 0 | `[..........]` |
| 9 | Hard | 48 | 25% Med, 75% Hard | 86% | 0 | `[..........]` |
| 10 | Hard+ | 49 | 10% Med, 90% Hard | 91% | 0 | `[..........]` |
| 11 | Expert | 48 | 100% Hard | 94% | 0 | `[..........]` |
| 12 | Master | 47 | 100% Hard (top-tier) | 97% | 0 | `[..........]` |

---

## Topic Coverage (All 16 Topics)

| # | Topic | Total | Solved | Progress | XP |
|---|-------|-------|--------|----------|----|
| 1 | [Basics](step_01_basics/) | 65 | 0 | `[..........]` | 0 XP |
| 2 | [Sorting](step_02_sorting/) | 7 | 0 | `[..........]` | 0 XP |
| 3 | [Arrays](step_03_arrays/) | 58 | 0 | `[..........]` | 0 XP |
| 4 | [Binary Search](step_04_binary_search/) | 37 | 0 | `[..........]` | 0 XP |
| 5 | [Strings](step_05_strings/) | 33 | 0 | `[..........]` | 0 XP |
| 6 | [Linked List](step_06_linked_list/) | 38 | 0 | `[..........]` | 0 XP |
| 7 | [Recursion & Backtracking](step_07_recursion_backtracking/) | 28 | 0 | `[..........]` | 0 XP |
| 8 | [Bit Manipulation](step_08_bit_manipulation/) | 18 | 0 | `[..........]` | 0 XP |
| 9 | [Stack & Queues](step_09_stack_queues/) | 36 | 0 | `[..........]` | 0 XP |
| 10 | [Sliding Window](step_10_sliding_window/) | 19 | 0 | `[..........]` | 0 XP |
| 11 | [Heaps](step_11_heaps/) | 24 | 0 | `[..........]` | 0 XP |
| 12 | [Greedy](step_12_greedy/) | 24 | 0 | `[..........]` | 0 XP |
| 13 | [Binary Trees](step_13_binary_trees/) | 56 | 0 | `[..........]` | 0 XP |
| 14 | [BST](step_14_bst/) | 17 | 0 | `[..........]` | 0 XP |
| 15 | [Graphs](step_15_graphs/) | 67 | 0 | `[..........]` | 0 XP |
| 16 | [Dynamic Programming](step_16_dynamic_programming/) | 85 | 0 | `[..........]` | 0 XP |

---

## Badges Showcase

| Badge | Name | Requirement | Status |
|-------|------|-------------|--------|
| -- | First Blood | Solve your first problem | Locked |
| -- | Array Slayer | Complete all Array problems | Locked |
| -- | DP Demon | Complete all DP problems | Locked |
| -- | Graph Guru | Complete all Graph problems | Locked |
| -- | On Fire | 7-day solve streak | Locked |
| -- | Unstoppable | 30-day solve streak | Locked |
| -- | Polyglot | 50 problems in both Java + Python | Locked |
| -- | Triple Threat | All 3 approaches for 50 problems | Locked |
| -- | A2Z Completionist | Solve every single problem | Locked |

---

## Game Commands

### Interactive Game
```bash
python scripts/play.py          # Launch the interactive game
```

| # | Action | Description |
|---|--------|-------------|
| 1 | Next Challenge | AI picks: revision > batch > best ELO match |
| 2 | Pick Topic | Choose a specific DSA topic to practice |
| 3 | Stats | View skill ratings per topic (ELO-based) |
| 4 | Achievements | View badges & milestones |
| 5 | Weak Areas | See topics that need improvement |
| 6 | Daily Summary | Today's session recap |
| 7 | Revision Queue | FSRS spaced repetition reviews |
| 8 | Batch Progress | Spiral curriculum status & readiness |

### CLI Tools
```bash
# Recommendation & Solving
python scripts/brain.py recommend              # Get AI-picked next problem
python scripts/brain.py complete <id> <mins> <felt>  # Mark problem done
python scripts/brain.py stats                  # View ELO ratings per topic
python scripts/brain.py weak                   # See topics needing work
python scripts/brain.py similar <id>           # Find similar problems

# Batch System
python scripts/batch_engine.py status          # Batch overview (all 12)
python scripts/batch_engine.py batch 3         # View problems in batch 3
python scripts/batch_engine.py next            # Next unsolved in current batch
python scripts/batch_engine.py readiness       # Interview readiness score

# Spaced Repetition
python scripts/revision.py due                 # Check what's due for review
python scripts/revision.py review <id> <grade> # Review a problem (1-4)
python scripts/revision.py stats               # Retention statistics
python scripts/revision.py calendar            # Upcoming review schedule

# Progress & Maintenance
python scripts/update_progress.py              # Refresh XP, badges & README
python scripts/sync_meta.py                    # Sync meta.json from solution files
```

---

## Intelligence System

### 3-Phase Recommendation Engine

The brain picks your next problem using a priority cascade:

```
Phase 1: REVISION (highest priority)
  └─ FSRS detects fading memories, surfaces them first
  └─ "You solved this 8 days ago. Your recall probability is 72%. Review now."

Phase 2: BATCH (structured learning)
  └─ Picks next unsolved from your current batch (dependency-ordered)
  └─ Ensures you cover ALL topics before going deeper

Phase 3: OPEN POOL (adaptive selection)
  └─ ELO-scored selection when no revisions or batch problems remain
  └─ Scoring weights:
      Topic urgency:     35%  (neglected topics score higher)
      Difficulty match:  20%  (matches problem to your ELO)
      Batch proximity:   15%  (prefers current/adjacent batches)
      Topic chaining:    12%  (related to what you just solved)
      Variety:           10%  (avoids same-topic streaks)
      Source variety:      3%  (mixes Striver + InterviewBit)
      Random:             5%  (keeps things fresh)
```

### Spaced Repetition (FSRS v4)

Every solved problem automatically enters the **Free Spaced Repetition Scheduler**:

- Review intervals grow with each successful recall: `1d → 3d → 8d → 21d → 55d → ...`
- Failed recalls reset stability — you review sooner
- Each card tracks: **difficulty**, **stability**, **retrievability**
- Target retention: **90%** long-term recall of every problem
- Grade scale: `1=Again` (forgot), `2=Hard`, `3=Good`, `4=Easy`

### ELO Skill Ratings

- Every topic starts at **1000 ELO**
- Solving HARD problems above your rating = **big gains** (+30-40)
- Struggling with EASY problems = **small drops** (-5-10)
- Recommendations match problem difficulty to your current skill
- After enough data, the system knows exactly where your **zone of proximal development** is

---

## XP & Leveling System

### XP Rewards

| Difficulty | Base XP | All 3 Approaches (1.5x) | Both Languages (1.3x) | Max XP |
|------------|---------|--------------------------|------------------------|--------|
| EASY | 10 | 15 | 19 | 19 |
| MEDIUM | 25 | 37 | 48 | 48 |
| HARD | 50 | 75 | 97 | 97 |

**Bonus: Speed XP** — Solve faster than expected time for +25% or +50% bonus.

**Bonus: Streak XP** — Maintain daily streaks for bonus XP per solve:

| Streak | Bonus XP |
|--------|----------|
| 3 days | +5 |
| 7 days | +15 |
| 14 days | +30 |
| 30 days | +100 |

### Leveling Table

| Level | Title | XP Required | Milestone |
|-------|-------|-------------|-----------|
| 1 | Novice | 0 | Just starting out |
| 2 | | 100 | |
| 3 | | 300 | |
| 4 | | 600 | |
| 5 | Apprentice | 1,000 | Getting the hang of it |
| 6 | | 1,600 | |
| 7 | | 2,400 | |
| 8 | Warrior | 3,500 | Comfortable with basics |
| 9 | | 5,000 | |
| 10 | | 7,000 | |
| 11 | Knight | 9,500 | Interview-ready (easy-medium) |
| 12 | | 12,500 | |
| 13 | | 16,000 | |
| 14 | Master | 20,000 | Strong across all topics |
| 15 | | 25,000 | |
| 16 | | 30,000 | |
| 17 | Grandmaster | 36,000 | Top 5% territory |
| 18 | | 43,000 | |
| 19 | | 51,000 | |
| 20 | Legend | 60,000 | Top 1%. You've mastered DSA. |

---

## Problem Structure

Every problem follows a consistent structure:

```
step_XX_topic/
  X.Y_subtopic/
    P001_problem_name/
      README.md        # Problem statement, examples, real-world analogy, hints
      Solution.java    # 3 approaches in Java (Brute → Optimal → Best)
      solution.py      # 3 approaches in Python
      meta.json        # Metadata (difficulty, tags, platforms, XP, status)
```

### Solution File Format

Each solution file contains 3 clearly separated approaches:

```java
// APPROACH 1: BRUTE FORCE
// Time: O(n^2) | Space: O(1)
// [Implementation]

// APPROACH 2: OPTIMAL
// Time: O(n log n) | Space: O(n)
// [Implementation]

// APPROACH 3: BEST / MOST OPTIMAL
// Time: O(n) | Space: O(1)
// [Implementation]
```

### Problem README Format

Each problem README includes:
1. **UNDERSTAND** — Problem statement, examples, edge cases, real-world analogy
2. **APPROACH 1: BRUTE FORCE** — Intuition, pseudocode, complexity
3. **APPROACH 2: OPTIMAL** — Better algorithm, trade-offs
4. **APPROACH 3: BEST** — Most optimal known solution
5. **Real-World Application** — Where this algorithm is used in industry

---

## Project Architecture

### Directory Structure
```
dsa_nova/
├── scripts/
│   ├── play.py             # Interactive game CLI
│   ├── brain.py            # Recommendation engine (ELO + FSRS hybrid)
│   ├── batch_engine.py     # 12-batch spiral curriculum engine
│   ├── revision.py         # FSRS v4 spaced repetition scheduler
│   ├── update_progress.py  # XP/level/badge aggregation & README updater
│   ├── sync_meta.py        # Detect implemented approaches from code
│   ├── shared.py           # Shared utilities (path→tag inference)
│   ├── fix_tags.py         # Bulk tag correction
│   └── real_world.py       # Real-world use case annotations
│
├── step_01_basics/         # 65 problems (patterns, math, recursion, hashing)
├── step_02_sorting/        # 7 problems (selection, bubble, merge, quick...)
├── step_03_arrays/         # 58 problems (easy → hard)
├── step_04_binary_search/  # 37 problems (1D, 2D, answer-based)
├── step_05_strings/        # 33 problems (manipulation, matching, palindromes)
├── step_06_linked_list/    # 38 problems (singly, doubly, cycle detection)
├── step_07_recursion_backtracking/ # 28 problems (subsets, permutations, N-Queens)
├── step_08_bit_manipulation/      # 18 problems (XOR tricks, counting bits)
├── step_09_stack_queues/   # 36 problems (monotonic stack, LRU, celebrity)
├── step_10_sliding_window/ # 19 problems (fixed/variable window, deque)
├── step_11_heaps/          # 24 problems (top-K, median, merge K lists)
├── step_12_greedy/         # 24 problems (intervals, scheduling, Huffman)
├── step_13_binary_trees/   # 56 problems (traversal, construction, Morris)
├── step_14_bst/            # 17 problems (search, insert, validate, LCA)
├── step_15_graphs/         # 67 problems (BFS, DFS, Dijkstra, MST, topo-sort)
├── step_16_dynamic_programming/   # 85 problems (1D, 2D, strings, stocks, LIS, MCM)
│
├── config.json             # XP rules, FSRS weights, game settings
├── progress.json           # Player state (XP, level, badges, per-topic stats)
├── player_brain.json       # Brain state (ELO ratings, solve history, sessions)
├── batch_state.json        # Batch assignments (problem → batch mapping)
├── revision_state.json     # FSRS cards (stability, difficulty, due dates)
└── templates/              # Problem templates for creating new problems
```

### State Files

| File | Purpose | Updated By |
|------|---------|------------|
| `progress.json` | XP, level, title, badges, per-step stats | `update_progress.py` |
| `player_brain.json` | ELO ratings, solved history, sessions | `brain.py complete` |
| `batch_state.json` | Problem→batch assignments, unlock state | `batch_engine.py` |
| `revision_state.json` | FSRS cards with review schedules | `revision.py` |
| `config.json` | XP multipliers, FSRS weights, settings | Manual edit |
| `meta.json` (per problem) | Status, approaches, tags, difficulty | `sync_meta.py`, `brain.py` |

### Key Design Decisions

1. **User progress is tracked in `player_brain.json`**, not in `meta.json`. The `meta.json` field `status: SOLVED` indicates that solution code exists. Your personal progress (what YOU have actually solved and reviewed) lives in the brain state. This decoupling means the reference solutions are always there for you to study, without inflating your progress.

2. **Path-based UIDs** (`step_03_arrays/3.2_medium/P001`) instead of raw problem IDs, because `P001` exists in every sub-folder but refers to different problems. UIDs prevent collisions.

3. **Horizontal batches** instead of vertical (topic-by-topic) progression, because real interviews test across all topics. Being Batch-6-complete means you can handle most interview questions, even if you haven't touched the hardest problems.

---

## Problem Sources

| Source | Count | ID Prefix | Description |
|--------|-------|-----------|-------------|
| [Striver's A2Z DSA Sheet](https://takeuforward.org/strivers-a2z-dsa-course/strivers-a2z-dsa-course-sheet-2/) | 434 | `P***` | The gold standard for DSA preparation in India. Covers every pattern. |
| [InterviewBit](https://www.interviewbit.com/courses/programming/) | 178 | `IB***` | Curated company-specific problems. Fills gaps Striver doesn't cover. |

**Combined:** 612 unique problems across 16 topics, Easy through Hard.

---

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Languages | Java 17+ & Python 3.10+ |
| Game Engine | Pure Python (no dependencies) |
| Recommendation | Custom ELO rating system |
| Spaced Repetition | FSRS v4 (Free Spaced Repetition Scheduler) |
| Progress Tracking | JSON state files + Python aggregation scripts |
| Dashboard | Auto-generated README.md tables |

---

## Typical Workflow

```
Day 1:
  $ python scripts/play.py
  > [1] Next Challenge
  > AI recommends: "Two Sum" (Batch 1, Arrays, Easy)
  > You study the problem, attempt it, then check the solution
  > Mark complete: brain.py complete P001 10 easy
  > XP +15! Level 1 → still Novice, but 15/100 XP

Day 2:
  > [1] Next Challenge
  > AI says: "Review: Two Sum (recall dropping to 78%)" ← FSRS kicks in
  > After review: "Pattern 2 - Right Triangle" (Batch 1, next in sequence)

Day 7+:
  > Batch 1 nearing 78% complete → Batch 2 auto-unlocks
  > Reviews get spaced out (3d → 8d → 21d)
  > ELO ratings show your strong/weak topics
  > [5] Weak Areas → "Bit Manipulation (1000 ELO, 0 solved recently)"
```

---

## FAQ

**Q: Do I need to actually code the solutions myself?**
A: The solution files are pre-written as reference implementations. The game tracks YOUR learning progress separately. Study the problem, attempt it yourself, then compare with the solution. Mark it done when you understand all approaches.

**Q: What does "3 approaches" mean?**
A: Every problem has Brute Force (naive), Optimal (better), and Best (most optimal) solutions. Understanding all three builds interview intuition — interviewers want to see you improve your approach.

**Q: How does batch unlocking work?**
A: Solve 78%+ of your current batch to unlock the next one. At 90%, the system auto-advances. You can also manually unlock with `batch_engine.py unlock`.

**Q: Can I skip ahead to harder problems?**
A: Use `python scripts/brain.py recommend` and the AI will pick appropriately. Or browse any step folder directly. The batch system is a guide, not a cage.

**Q: How is this different from just doing Striver's sheet linearly?**
A: Three ways: (1) Horizontal batches mean you see ALL topics early, not just arrays for weeks. (2) FSRS ensures you actually remember what you solved. (3) ELO-based difficulty matching keeps you in the optimal learning zone.

**Q: Does my progress save if I close the game?**
A: Yes. Every action (completing a problem, reviewing, etc.) saves immediately to JSON files. Close the terminal, restart your PC — your progress is always there. No "save game" button needed.

**Q: Can I run this without Python installed?**
A: Yes! Build the standalone `.exe` with `pip install pyinstaller` and run the build command in the Quick Start section. The resulting `dsa-nova.exe` runs on any Windows machine without Python.

---

*Start your quest. Become a Legend.*
