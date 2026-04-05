# DSA Nova - Rules of the Game

## XP System

Every problem earns you XP based on difficulty:

| Difficulty | Base XP |
|------------|---------|
| EASY       | 10      |
| MEDIUM     | 25      |
| HARD       | 50      |

### Bonus Multipliers (stack multiplicatively)
- **All 3 Approaches** (brute + optimal + best): **1.5x**
- **Both Languages** (Java + Python): **1.3x**

### Streak Bonuses
- 3-day streak: +5 XP bonus
- 7-day streak: +15 XP bonus
- 14-day streak: +30 XP bonus
- 30-day streak: +100 XP bonus

## Leveling

There are **20 levels** with exponential XP requirements:

| Level | Title        | XP Required |
|-------|-------------|-------------|
| 1     | Novice      | 0           |
| 2     | Novice      | 100         |
| 3     | Novice      | 300         |
| 4     | Novice      | 600         |
| 5     | Apprentice  | 1,000       |
| 6     | Apprentice  | 1,600       |
| 7     | Apprentice  | 2,400       |
| 8     | Warrior     | 3,500       |
| 9     | Warrior     | 5,000       |
| 10    | Warrior     | 7,000       |
| 11    | Knight      | 9,500       |
| 12    | Knight      | 12,500      |
| 13    | Knight      | 16,000      |
| 14    | Master      | 20,000      |
| 15    | Master      | 25,000      |
| 16    | Master      | 30,000      |
| 17    | Grandmaster | 36,000      |
| 18    | Grandmaster | 43,000      |
| 19    | Grandmaster | 51,000      |
| 20    | Legend       | 60,000      |

## Badges

| Badge | How to Earn |
|-------|-------------|
| First Blood | Solve your very first problem |
| Array Slayer | Complete ALL problems in Step 3 (Arrays) |
| DP Demon | Complete ALL problems in Step 16 (Dynamic Programming) |
| Graph Guru | Complete ALL problems in Step 15 (Graphs) |
| On Fire | Maintain a 7-day solve streak |
| Unstoppable | Maintain a 30-day solve streak |
| Polyglot | Solve 50+ problems in BOTH Java and Python |
| Triple Threat | Complete all 3 approaches for 50+ problems |
| A2Z Completionist | Solve every single problem in the sheet |

## How to Mark a Problem as Solved

1. Open the problem's `meta.json`
2. Set `"status": "SOLVED"`
3. Mark which approaches and languages you completed
4. Run `python scripts/update_progress.py`

## Problem Approach Guide

For each problem, try to solve in this order:

1. **Brute Force** - Get it working, don't worry about efficiency
2. **Optimal** - Improve time/space complexity
3. **Best** - The most elegant/efficient solution possible

If brute and optimal are the same, or optimal and best are the same, note it in the code.
