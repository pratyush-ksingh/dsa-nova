#!/usr/bin/env python3
"""
DSA Nova - Interactive Game CLI (play.py)

A gamified, interactive DSA practice session with XP, levels,
streaks, achievements, and intelligent problem recommendations.

Usage:
    python scripts/play.py
"""

import os
import sys
import json
import time
import random
import datetime
from pathlib import Path

# Add scripts dir to path for imports (handles both normal and frozen .exe)
if getattr(sys, "frozen", False):
    _scripts_dir = str(Path(sys.executable).resolve().parent / "scripts")
else:
    _scripts_dir = str(Path(__file__).resolve().parent)
sys.path.insert(0, _scripts_dir)
from brain import (
    load_brain, save_brain, recommend, complete, suggest_similar,
    get_stats, get_weak_topics, load_all_problems, BASE_DIR,
    HAS_REVISION, HAS_BATCHES
)
try:
    from revision import (
        get_due_reviews, process_review, get_revision_stats,
        load_revision_state, retrievability
    )
except ImportError:
    HAS_REVISION = False

try:
    from batch_engine import (
        load_batch_state, update_batch_progress, can_unlock_next,
        assign_problems_to_batches, get_batch_problems
    )
except ImportError:
    HAS_BATCHES = False

try:
    from real_world import get_real_world_for_problem
    HAS_REAL_WORLD = True
except ImportError:
    HAS_REAL_WORLD = False

PROGRESS_FILE = BASE_DIR / "progress.json"

# ============================================================
# COLORS & STYLING
# ============================================================
class C:
    """ANSI color codes."""
    RESET   = "\033[0m"
    BOLD    = "\033[1m"
    DIM     = "\033[2m"
    RED     = "\033[91m"
    GREEN   = "\033[92m"
    YELLOW  = "\033[93m"
    BLUE    = "\033[94m"
    MAGENTA = "\033[95m"
    CYAN    = "\033[96m"
    WHITE   = "\033[97m"
    BG_BLUE = "\033[44m"
    BG_GREEN= "\033[42m"
    BG_RED  = "\033[41m"

DIFF_COLOR = {
    "EASY": C.GREEN,
    "MEDIUM": C.YELLOW,
    "HARD": C.RED,
}

# ============================================================
# ASCII ART
# ============================================================
LOGO = f"""{C.CYAN}{C.BOLD}
    ____  ____  ___       _   __
   / __ \\/ __ \\/ _ |     / | / /___ _   ______ _
  / / / / /_/ / __ |    /  |/ / __ \\ | / / __ `/
 / /_/ /\\____/ /_/ /   / /|  / /_/ / |/ / /_/ /
/_____/ ___//_/ |_/   /_/ |_/\\____/|___/\\__,_/
       /  _/____  / /__________  ____ __________
       / // __ \\/ __/ _ \\/ ___/ / __ `/ ___/ __ \\
     _/ // / / / /_/  __/ /    / /_/ / /  / / / /
    /___/_/ /_/\\__/\\___/_/     \\__,_/_/  /_/ /_/
{C.RESET}"""

SWORD = f"""{C.YELLOW}
       />
      /  \\
     / /| \\
    (_/ | _)
        |
       /|\\
      / | \\
{C.RESET}"""

TROPHY = f"""{C.YELLOW}
    ___________
   '._==_==_=_.'
   .-\\:      /-.
  | (|:.     |) |
   '-|:.     |-'
     \\::.    /
      '::. .'
        ) (
      _.' '._
     '-------'
{C.RESET}"""

LEVEL_UP_ART = f"""{C.MAGENTA}{C.BOLD}
  ╔═══════════════════════════╗
  ║    ** LEVEL UP! **        ║
  ║                           ║
  ║   ⬆  You've grown        ║
  ║      stronger! ⬆         ║
  ╚═══════════════════════════╝
{C.RESET}"""

XP_FRAMES = [
    "  [          ] +{xp} XP",
    "  [##        ] +{xp} XP",
    "  [####      ] +{xp} XP",
    "  [######    ] +{xp} XP",
    "  [########  ] +{xp} XP",
    "  [##########] +{xp} XP  !!",
]


def clear_screen():
    os.system('cls' if os.name == 'nt' else 'clear')


def slow_print(text, delay=0.02):
    """Print text character by character for effect."""
    for ch in text:
        sys.stdout.write(ch)
        sys.stdout.flush()
        time.sleep(delay)
    print()


def press_enter():
    input(f"\n  {C.DIM}Press Enter to continue...{C.RESET}")


def xp_animation(xp_gained):
    """Show XP gain animation."""
    print()
    for frame in XP_FRAMES:
        sys.stdout.write(f"\r  {C.GREEN}{frame.format(xp=xp_gained)}{C.RESET}")
        sys.stdout.flush()
        time.sleep(0.15)
    print()


def progress_bar(current, total, width=30, fill_char="#", empty_char="-"):
    """Create a text progress bar."""
    if total == 0:
        pct = 0
    else:
        pct = current / total
    filled = int(width * pct)
    bar = fill_char * filled + empty_char * (width - filled)
    return f"[{bar}] {current}/{total} ({pct*100:.0f}%)"


# ============================================================
# LOAD PROGRESS
# ============================================================
def load_progress():
    if PROGRESS_FILE.exists():
        with open(PROGRESS_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    return {
        "player": {"name": "Nova Coder", "level": 1, "title": "Novice",
                    "total_xp": 0, "current_streak": 0, "longest_streak": 0},
        "global_stats": {"total_problems": 0, "total_solved": 0},
    }


# ============================================================
# SCREENS
# ============================================================
def show_welcome():
    clear_screen()
    print(LOGO)
    progress = load_progress()
    player = progress["player"]
    stats = progress["global_stats"]

    print(f"  {C.BOLD}Welcome back, {C.CYAN}{player['name']}{C.RESET}!")
    print(f"  {C.DIM}Level {player['level']} {player['title']}{C.RESET}")
    print()

    # XP bar to next level
    thresholds = progress.get("xp_thresholds", {})
    current_xp = player["total_xp"]
    current_level = player["level"]
    next_level = current_level + 1
    current_threshold = thresholds.get(str(current_level), 0)
    next_threshold = thresholds.get(str(next_level), current_threshold + 500)
    xp_in_level = current_xp - current_threshold
    xp_needed = next_threshold - current_threshold

    print(f"  XP: {C.YELLOW}{current_xp}{C.RESET}  {progress_bar(xp_in_level, xp_needed, 25)}")
    print(f"  Streak: {C.RED}{'*' * min(player.get('current_streak', 0), 30)}{C.RESET} ({player.get('current_streak', 0)} days)")
    print(f"  Progress: {progress_bar(stats.get('total_solved', 0), stats.get('total_problems', 0), 25)}")
    print()


def show_main_menu():
    # Show quick revision alert if reviews are due
    if HAS_REVISION:
        try:
            due = get_due_reviews()
            if due:
                print(f"  {C.RED}{C.BOLD}!! {len(due)} review(s) due - memory fading!{C.RESET}")
                print()
        except Exception as e:
            print(f"  {C.DIM}(revision check skipped: {e}){C.RESET}", file=sys.stderr)

    # Show batch info
    if HAS_BATCHES:
        try:
            bs = load_batch_state()
            if bs.get("assigned"):
                bp = bs["batch_progress"].get(str(bs["current_batch"]), {})
                total = bp.get("total", 0)
                solved = bp.get("solved", 0)
                pct = int(solved / total * 100) if total > 0 else 0
                print(f"  {C.BLUE}Batch {bs['current_batch']}: {bp.get('name', '')} [{solved}/{total} = {pct}%]{C.RESET}")
                print()
        except Exception as e:
            print(f"  {C.DIM}(batch info skipped: {e}){C.RESET}", file=sys.stderr)

    print(f"  {C.BOLD}=== MAIN MENU ==={C.RESET}")
    print()
    print(f"  {C.CYAN}[1]{C.RESET} Next Challenge     {C.DIM}- AI picks: revision > batch > best match{C.RESET}")
    print(f"  {C.CYAN}[2]{C.RESET} Pick Topic         {C.DIM}- Choose a specific topic{C.RESET}")
    print(f"  {C.CYAN}[3]{C.RESET} Stats              {C.DIM}- View your skill ratings{C.RESET}")
    print(f"  {C.CYAN}[4]{C.RESET} Achievements       {C.DIM}- View badges & milestones{C.RESET}")
    print(f"  {C.CYAN}[5]{C.RESET} Weak Areas         {C.DIM}- See where to improve{C.RESET}")
    print(f"  {C.CYAN}[6]{C.RESET} Daily Summary      {C.DIM}- Today's session recap{C.RESET}")
    print(f"  {C.CYAN}[7]{C.RESET} Revision Queue     {C.DIM}- FSRS spaced repetition reviews{C.RESET}")
    print(f"  {C.CYAN}[8]{C.RESET} Batch Progress     {C.DIM}- Spiral curriculum status{C.RESET}")
    print(f"  {C.CYAN}[0]{C.RESET} Quit")
    print()


def handle_next_challenge(topic=None):
    """Get and display the next recommended problem."""
    brain = load_brain()
    recs = recommend(brain=brain, topic_filter=topic, count=3)

    if not recs:
        print(f"\n  {C.YELLOW}No unsolved problems found")
        if topic:
            print(f"  in topic '{topic}'{C.RESET}")
        else:
            print(f"  -- you've conquered everything!{C.RESET}")
        press_enter()
        return

    problem = recs[0]
    diff = problem.get("difficulty", "MEDIUM")
    dc = DIFF_COLOR.get(diff, "")
    tags = [t for t in problem.get("tags", []) if t != "interviewbit"]
    source = "InterviewBit" if "interviewbit" in problem.get("tags", []) else "Striver A2Z"

    clear_screen()
    print(SWORD)

    # Show recommendation type
    rec_type = problem.get("_recommendation_type", "")
    if rec_type == "REVISION":
        r_info = problem.get("_revision_info", {})
        print(f"  {C.RED}{C.BOLD}=== REVISION CHALLENGE ==={C.RESET}")
        print(f"  {C.RED}Your memory of this is fading! Time to reinforce.{C.RESET}")
    elif rec_type == "BATCH":
        batch_id = problem.get("_batch_id", "?")
        print(f"  {C.BLUE}{C.BOLD}=== BATCH {batch_id} CHALLENGE ==={C.RESET}")
        print(f"  {C.BLUE}Part of your structured learning path.{C.RESET}")
    else:
        print(f"  {C.BOLD}{C.CYAN}=== YOUR NEXT CHALLENGE ==={C.RESET}")

    print()
    print(f"  {C.BOLD}{C.WHITE}{problem['title']}{C.RESET}")
    print(f"  {C.DIM}{'─' * 50}{C.RESET}")
    print(f"  ID:         {problem['id']}")
    print(f"  Difficulty: {dc}{diff}{C.RESET}")
    print(f"  XP Reward:  {C.YELLOW}+{problem.get('xp_reward', '?')} XP{C.RESET}")
    print(f"  Source:     {source}")
    print(f"  Topics:     {', '.join(tags)}")
    print(f"  Path:       {C.DIM}{problem.get('_path', 'N/A')}{C.RESET}")

    # Show revision info if it's a review
    if rec_type == "REVISION" and problem.get("_revision_info"):
        ri = problem["_revision_info"]
        print(f"  {C.YELLOW}Last reviewed: {ri.get('last_review', '?')} | "
              f"Stability: {ri.get('stability', '?')} days{C.RESET}")

    # Show real-world use cases
    if HAS_REAL_WORLD:
        rw_results = get_real_world_for_problem(problem)
        if rw_results:
            print(f"\n  {C.MAGENTA}{C.BOLD}Why This Matters:{C.RESET}")
            for rw in rw_results[:2]:  # Show top 2 relevant topics
                use = random.choice(rw["uses"])
                print(f"  {C.MAGENTA}  > {use}{C.RESET}")
                print(f"  {C.DIM}  Interview cue: {rw['interview_cue']}{C.RESET}")

    print()

    # Show alternatives
    if len(recs) > 1:
        print(f"  {C.DIM}Other options:{C.RESET}")
        for alt in recs[1:]:
            adiff = alt.get("difficulty", "?")
            adc = DIFF_COLOR.get(adiff, "")
            print(f"    - {alt['title']} ({adc}{adiff}{C.RESET})")
        print()

    print(f"  {C.BOLD}Actions:{C.RESET}")
    print(f"    {C.GREEN}[s]{C.RESET} Start solving (opens path)")
    print(f"    {C.YELLOW}[d]{C.RESET} Mark as done")
    print(f"    {C.BLUE}[i]{C.RESET} Show similar problems")
    print(f"    {C.RED}[b]{C.RESET} Back to menu")
    print()

    while True:
        choice = input(f"  {C.CYAN}>{C.RESET} ").strip().lower()

        if choice == "s":
            print(f"\n  {C.GREEN}Go solve it! Open the file at:{C.RESET}")
            print(f"  {C.BOLD}{problem.get('_path', '')}{C.RESET}")
            print()
            print(f"  {C.DIM}When done, come back and press [d] to mark complete.{C.RESET}")
            continue

        elif choice == "d":
            handle_mark_done(problem)
            return

        elif choice == "i":
            similar = suggest_similar(problem["id"])
            if similar:
                print(f"\n  {C.CYAN}Similar problems:{C.RESET}")
                for sp in similar[:5]:
                    sdiff = sp.get("difficulty", "?")
                    sdc = DIFF_COLOR.get(sdiff, "")
                    status = f"{C.GREEN}[SOLVED]{C.RESET}" if sp.get("status") == "SOLVED" else f"{C.DIM}[UNSOLVED]{C.RESET}"
                    print(f"    {sp['id']} {sp['title']} ({sdc}{sdiff}{C.RESET}) {status}")
            else:
                print(f"  {C.DIM}No similar problems found.{C.RESET}")
            print()
            continue

        elif choice == "b":
            return

        else:
            print(f"  {C.DIM}Choose [s]tart, [d]one, [i]nfo, or [b]ack{C.RESET}")


def handle_mark_done(problem):
    """Interactive completion flow."""
    print(f"\n  {C.GREEN}{C.BOLD}Completing: {problem['title']}{C.RESET}")
    print()

    # Get time taken
    while True:
        time_input = input(f"  {C.CYAN}Time taken (minutes):{C.RESET} ").strip()
        try:
            time_taken = float(time_input)
            if time_taken > 0:
                break
        except ValueError:
            pass
        print(f"  {C.RED}Please enter a valid number of minutes.{C.RESET}")

    # Get felt difficulty
    print()
    print(f"  How did it feel?")
    print(f"    {C.GREEN}[1]{C.RESET} Easy - Got it quickly")
    print(f"    {C.YELLOW}[2]{C.RESET} Medium - Had to think")
    print(f"    {C.RED}[3]{C.RESET} Hard - Really struggled")
    print(f"    {C.MAGENTA}[4]{C.RESET} Failed - Couldn't solve it")

    felt_map = {"1": "easy", "2": "medium", "3": "hard", "4": "failed"}
    while True:
        felt_input = input(f"  {C.CYAN}>{C.RESET} ").strip()
        if felt_input in felt_map:
            felt = felt_map[felt_input]
            break
        print(f"  {C.DIM}Choose 1-4{C.RESET}")

    # Process completion (use uid for unique identification)
    progress_before = load_progress()
    level_before = progress_before["player"]["level"]

    result = complete(problem.get("_uid", problem["id"]), time_taken, felt)

    if "error" in result:
        print(f"\n  {C.RED}Error: {result['error']}{C.RESET}")
        press_enter()
        return

    # Show results
    print()
    print(f"  {C.GREEN}{C.BOLD}{'=' * 45}{C.RESET}")
    print(f"  {C.GREEN}{C.BOLD}  PROBLEM SOLVED!{C.RESET}")
    print(f"  {C.GREEN}{C.BOLD}{'=' * 45}{C.RESET}")

    # XP animation
    xp_animation(result["xp_earned"])

    if result["speed_bonus"] > 0:
        print(f"  {C.YELLOW}  Speed Bonus: +{result['speed_bonus']} XP{C.RESET}")

    # Rating changes
    print(f"\n  {C.BOLD}Skill Updates:{C.RESET}")
    for topic, delta in result["rating_changes"].items():
        if delta > 0:
            print(f"    {C.GREEN}  {topic}: +{delta}{C.RESET}")
        elif delta < 0:
            print(f"    {C.RED}  {topic}: {delta}{C.RESET}")
        else:
            print(f"    {C.DIM}  {topic}: ={C.RESET}")

    # Check for level up
    progress_after = load_progress()
    level_after = progress_after["player"]["level"]
    if level_after > level_before:
        print(LEVEL_UP_ART)
        print(f"  {C.MAGENTA}{C.BOLD}  Level {level_before} --> Level {level_after}!{C.RESET}")
        print(f"  {C.MAGENTA}  Title: {progress_after['player']['title']}{C.RESET}")

    # Show revision scheduling
    if result.get("next_review"):
        print(f"\n  {C.BLUE}Next review scheduled: {result['next_review']} "
              f"(stability: {result.get('stability', '?'):.1f} days){C.RESET}")

    print(f"\n  {C.DIM}Problems solved today: {result['problems_solved_today']}{C.RESET}")

    # Suggest next
    print(f"\n  {C.CYAN}Want to keep going? Here's what's next:{C.RESET}")
    brain = load_brain()
    next_recs = recommend(brain=brain, count=2)
    for p in next_recs:
        ndiff = p.get("difficulty", "?")
        ndc = DIFF_COLOR.get(ndiff, "")
        print(f"    -> {p['title']} ({ndc}{ndiff}{C.RESET})")

    press_enter()


def handle_pick_topic():
    """Let user choose a topic to practice."""
    clear_screen()
    print(f"\n  {C.BOLD}{C.CYAN}=== PICK A TOPIC ==={C.RESET}")
    print()

    topics = [
        ("arrays", "Arrays & Matrices"),
        ("math", "Math & Number Theory"),
        ("binary-search", "Binary Search"),
        ("strings", "Strings"),
        ("linked-list", "Linked Lists"),
        ("stack", "Stacks & Queues"),
        ("hashing", "Hashing"),
        ("two-pointers", "Two Pointers"),
        ("sliding-window", "Sliding Window"),
        ("recursion", "Recursion"),
        ("backtracking", "Backtracking"),
        ("trees", "Trees"),
        ("bst", "Binary Search Trees"),
        ("heaps", "Heaps & Priority Queues"),
        ("greedy", "Greedy Algorithms"),
        ("graphs", "Graphs"),
        ("dp", "Dynamic Programming"),
        ("bit-manipulation", "Bit Manipulation"),
    ]

    brain = load_brain()
    for i, (tag, name) in enumerate(topics, 1):
        rating = brain["topic_ratings"].get(tag, 1000)
        solved = brain["topic_solve_counts"].get(tag, 0)
        level_indicator = ""
        if rating >= 1300:
            level_indicator = f"{C.GREEN}***{C.RESET}"
        elif rating >= 1100:
            level_indicator = f"{C.YELLOW}** {C.RESET}"
        else:
            level_indicator = f"{C.RED}*  {C.RESET}"
        print(f"    {C.CYAN}[{i:>2}]{C.RESET} {name:<30} {level_indicator} Rating: {rating:>5.0f}  Solved: {solved}")

    print(f"\n    {C.RED}[0]{C.RESET}  Back to menu")
    print()

    choice = input(f"  {C.CYAN}Choose topic:{C.RESET} ").strip()
    try:
        idx = int(choice)
        if idx == 0:
            return
        if 1 <= idx <= len(topics):
            tag, name = topics[idx - 1]
            print(f"\n  {C.GREEN}Fetching {name} challenge...{C.RESET}")
            handle_next_challenge(topic=tag)
    except ValueError:
        print(f"  {C.RED}Invalid choice.{C.RESET}")
        press_enter()


def handle_stats():
    """Show detailed stats screen."""
    clear_screen()
    brain = load_brain()
    stats = get_stats(brain)
    progress = load_progress()
    player = progress["player"]

    print(TROPHY)
    print(f"  {C.BOLD}{C.CYAN}=== PLAYER STATS ==={C.RESET}")
    print()
    print(f"  {C.BOLD}Player:{C.RESET}   {player['name']}")
    print(f"  {C.BOLD}Level:{C.RESET}    {player['level']} ({player['title']})")
    print(f"  {C.BOLD}XP:{C.RESET}       {C.YELLOW}{player['total_xp']}{C.RESET}")
    print(f"  {C.BOLD}Streak:{C.RESET}   {player.get('current_streak', 0)} days (best: {player.get('longest_streak', 0)})")
    print(f"  {C.BOLD}Solved:{C.RESET}   {stats['total_solved']} / {stats['total_problems']} ({stats['completion_pct']}%)")
    print(f"  {C.BOLD}Today:{C.RESET}    {stats['today_count']} problems")
    print()

    if stats["topic_stats"]:
        print(f"  {C.BOLD}Topic Skill Ratings:{C.RESET}")
        print(f"  {'Topic':<22} {'Rating':>6} {'Level':<14} {'Solved':>6}  {'Bar'}")
        print(f"  {'-'*70}")
        for ts in stats["topic_stats"][:20]:
            bar_len = max(0, min(20, (ts['rating'] - 900) // 20))
            if ts['level'] == 'Advanced':
                bar = f"{C.GREEN}{'#' * bar_len}{C.RESET}"
                lvl_c = C.GREEN
            elif ts['level'] == 'Intermediate':
                bar = f"{C.YELLOW}{'#' * bar_len}{C.RESET}"
                lvl_c = C.YELLOW
            else:
                bar = f"{C.RED}{'#' * bar_len}{C.RESET}"
                lvl_c = C.RED
            print(f"  {ts['topic']:<22} {ts['rating']:>6} {lvl_c}{ts['level']:<14}{C.RESET} {ts['solved']:>6}  {bar}")
    else:
        print(f"  {C.DIM}No topic data yet. Start solving problems!{C.RESET}")

    press_enter()


def handle_achievements():
    """Show achievements/badges screen."""
    clear_screen()
    progress = load_progress()

    print(f"\n  {C.BOLD}{C.YELLOW}=== ACHIEVEMENTS ==={C.RESET}")
    print()

    badges = progress.get("badges", [])
    earned_count = sum(1 for b in badges if b.get("earned"))

    print(f"  Badges Earned: {C.YELLOW}{earned_count}{C.RESET} / {len(badges)}")
    print()

    for badge in badges:
        if badge.get("earned"):
            icon = f"{C.YELLOW}[*]{C.RESET}"
            name_fmt = f"{C.BOLD}{C.YELLOW}{badge['name']}{C.RESET}"
        else:
            icon = f"{C.DIM}[ ]{C.RESET}"
            name_fmt = f"{C.DIM}{badge['name']}{C.RESET}"
        print(f"    {icon} {name_fmt}")
        print(f"        {C.DIM}{badge['desc']}{C.RESET}")
        print()

    # Show milestone progress
    brain = load_brain()
    total_solved = len(brain["solved_problems"])

    print(f"  {C.BOLD}Milestones:{C.RESET}")
    milestones = [
        (1, "First Steps"),
        (10, "Getting Started"),
        (25, "Quarter Century"),
        (50, "Half Century"),
        (100, "Century Club"),
        (200, "Double Century"),
        (300, "Triple Threat"),
        (434, "Striver Complete"),
        (500, "Five Hundred"),
        (600, "Nova Master"),
    ]
    for target, name in milestones:
        if total_solved >= target:
            print(f"    {C.GREEN}[*] {name} ({target} problems){C.RESET}")
        else:
            remaining = target - total_solved
            print(f"    {C.DIM}[ ] {name} ({target} problems) - {remaining} to go{C.RESET}")

    press_enter()


def handle_weak_areas():
    """Show weak areas with actionable recommendations."""
    clear_screen()
    brain = load_brain()
    weak = get_weak_topics(brain, top_n=10)

    print(f"\n  {C.BOLD}{C.RED}=== WEAK AREAS ==={C.RESET}")
    print()

    if not weak:
        print(f"  {C.DIM}No data yet. Solve some problems to see your weak areas!{C.RESET}")
        press_enter()
        return

    print(f"  {C.BOLD}Topics that need the most work:{C.RESET}")
    print()

    for i, (topic, rating) in enumerate(weak, 1):
        urgency_bar_len = max(1, int((1400 - rating) / 40))
        urgency_bar = "#" * min(urgency_bar_len, 15)

        if rating < 1000:
            color = C.RED
            advice = "Start with EASY problems"
        elif rating < 1100:
            advice = "Practice more EASY, try some MEDIUM"
            color = C.YELLOW
        else:
            advice = "Ready for MEDIUM challenges"
            color = C.GREEN

        print(f"  {i}. {C.BOLD}{topic}{C.RESET}")
        print(f"     Rating: {color}{rating:.0f}{C.RESET}  {C.RED}{urgency_bar}{C.RESET}")
        print(f"     Advice: {C.DIM}{advice}{C.RESET}")

        # Show a suggested problem for this topic
        recs = recommend(brain=brain, topic_filter=topic, count=1)
        if recs:
            p = recs[0]
            dc = DIFF_COLOR.get(p.get("difficulty", ""), "")
            print(f"     Try:    {p['title']} ({dc}{p.get('difficulty', '?')}{C.RESET})")
        print()

    press_enter()


def handle_daily_summary():
    """Show today's session summary."""
    clear_screen()
    brain = load_brain()
    progress = load_progress()

    today = datetime.date.today().isoformat()
    today_problems = brain.get("today_solved", [])

    print(f"\n  {C.BOLD}{C.CYAN}=== DAILY SUMMARY - {today} ==={C.RESET}")
    print()

    if not today_problems:
        print(f"  {C.DIM}No problems solved today yet.{C.RESET}")
        print(f"  {C.YELLOW}Start your first challenge!{C.RESET}")
        press_enter()
        return

    print(f"  Problems solved today: {C.GREEN}{C.BOLD}{len(today_problems)}{C.RESET}")
    print()

    # Load XP values from config
    try:
        with open(BASE_DIR / "config.json", "r", encoding="utf-8") as _f:
            _cfg = json.load(_f)
        xp_map = _cfg["xp_rules"]["base_xp"]
    except (FileNotFoundError, json.JSONDecodeError, KeyError):
        xp_map = {"EASY": 10, "MEDIUM": 25, "HARD": 50}

    total_xp_today = 0
    for pid in today_problems:
        if pid in brain["solved_problems"]:
            info = brain["solved_problems"][pid]
            diff = info.get("difficulty", "?")
            dc = DIFF_COLOR.get(diff, "")
            xp = xp_map.get(diff, 0)
            total_xp_today += xp
            felt = info.get("last_felt", "?")
            time_taken = info.get("last_time_mins", "?")
            tags = ", ".join(t for t in info.get("tags", []) if t != "interviewbit")
            print(f"    {C.GREEN}*{C.RESET} {pid} ({dc}{diff}{C.RESET}) - {time_taken} min - felt {felt}")
            if tags:
                print(f"      {C.DIM}Tags: {tags}{C.RESET}")

    print(f"\n  {C.YELLOW}Total XP earned today: ~{total_xp_today}+ XP{C.RESET}")
    print(f"  {C.BOLD}Streak: {progress['player'].get('current_streak', 0)} days{C.RESET}")

    press_enter()


def handle_revision_queue():
    """Show the FSRS revision queue with interactive review."""
    clear_screen()
    print(f"\n  {C.BOLD}{C.RED}=== REVISION QUEUE ==={C.RESET}")
    print(f"  {C.DIM}FSRS Spaced Repetition - fight the forgetting curve{C.RESET}")
    print()

    if not HAS_REVISION:
        print(f"  {C.DIM}Revision system not available.{C.RESET}")
        press_enter()
        return

    try:
        state = load_revision_state()
        stats = get_revision_stats(state)
        due = get_due_reviews(include_upcoming_days=1)
    except Exception as e:
        print(f"  {C.RED}Error loading revision state: {e}{C.RESET}")
        press_enter()
        return

    # Stats header
    print(f"  Cards:    {stats['total_cards']} total | {stats['due_today']} due today | {stats['overdue']} overdue")
    print(f"  States:   {C.RED}{stats['learning']} learning{C.RESET} | {C.YELLOW}{stats['review']} review{C.RESET} | {C.GREEN}{stats['mastered']} mastered{C.RESET}")
    print(f"  Memory:   {stats.get('avg_retrievability', 0)}% avg recall | {stats['retention_rate']}% retention rate")
    print(f"  History:  {stats['total_reviews']} reviews | {stats['total_lapses']} lapses")
    print()

    if not due:
        print(f"  {C.GREEN}No reviews due! Your memory is solid.{C.RESET}")
        print(f"  {C.DIM}Solved problems get scheduled for revision automatically.{C.RESET}")
        press_enter()
        return

    # Show due cards
    print(f"  {C.BOLD}Due for review:{C.RESET}")
    problems = load_all_problems()
    # Dual-key map: both raw id and uid for backward compat with UID/ID mismatch
    problem_map = {}
    for p in problems:
        problem_map[p["id"]] = p
        problem_map[p["_uid"]] = p

    for i, (urgency, card) in enumerate(due[:10], 1):
        elapsed = max(0, (datetime.date.today() -
                datetime.date.fromisoformat(card["last_review"])).days)
        r_pct = retrievability(card["stability"], elapsed) * 100

        state_icon = {"learning": f"{C.RED}L{C.RESET}", "relearning": f"{C.RED}!{C.RESET}",
                     "review": f"{C.YELLOW}R{C.RESET}", "mastered": f"{C.GREEN}M{C.RESET}"}.get(
                     card["state"], "?")

        recall_color = C.GREEN if r_pct > 70 else (C.YELLOW if r_pct > 40 else C.RED)

        print(f"  {i:>2}. [{state_icon}] {card['title']}")
        print(f"      Recall: {recall_color}{r_pct:.0f}%{C.RESET} | "
              f"Stability: {card['stability']:.1f}d | Reviews: {card['review_count']}")

    print(f"\n  {C.CYAN}[r]{C.RESET} Start reviewing | {C.RED}[b]{C.RESET} Back")
    choice = input(f"  {C.CYAN}>{C.RESET} ").strip().lower()

    if choice == "r" and due:
        # Start interactive review session
        for urgency, card in due[:5]:
            if card["problem_id"] in problem_map:
                problem = problem_map[card["problem_id"]]
                problem["_recommendation_type"] = "REVISION"
                problem["_revision_info"] = card

                clear_screen()
                print(f"\n  {C.RED}{C.BOLD}REVIEW: {card['title']}{C.RESET}")
                elapsed = max(0, (datetime.date.today() -
                        datetime.date.fromisoformat(card["last_review"])).days)
                r_pct = retrievability(card["stability"], elapsed) * 100
                print(f"  {C.DIM}Last seen {elapsed} days ago | Recall est: {r_pct:.0f}%{C.RESET}")
                print(f"  Path: {problem.get('_path', 'N/A')}")
                print()
                print(f"  Solve it again, then grade yourself:")
                print(f"    {C.RED}[1]{C.RESET} Again  - Couldn't recall the approach")
                print(f"    {C.YELLOW}[2]{C.RESET} Hard   - Struggled but got it")
                print(f"    {C.GREEN}[3]{C.RESET} Good   - Solved it cleanly")
                print(f"    {C.CYAN}[4]{C.RESET} Easy   - Instant recall, trivial")
                print(f"    {C.DIM}[s]{C.RESET} Skip")

                grade_input = input(f"\n  {C.CYAN}>{C.RESET} ").strip()
                grade_map = {"1": "again", "2": "hard", "3": "good", "4": "easy"}

                if grade_input in grade_map:
                    result = process_review(card["problem_id"], grade_map[grade_input])
                    if "error" not in result:
                        stability_change = result["stability_change"]
                        arrow = f"{C.GREEN}+{stability_change:.1f}d{C.RESET}" if stability_change > 0 else f"{C.RED}{stability_change:.1f}d{C.RESET}"
                        print(f"\n  Stability: {arrow} | Next review: {result['next_due']}")
                        if result["state"] == "mastered":
                            print(f"  {C.GREEN}{C.BOLD}MASTERED! This problem has graduated.{C.RESET}")
                        time.sleep(1)
                elif grade_input == "s":
                    continue
                else:
                    break

    press_enter()


def handle_batch_progress():
    """Show batch/curriculum progress."""
    clear_screen()
    print(f"\n  {C.BOLD}{C.BLUE}=== BATCH PROGRESS ==={C.RESET}")
    print(f"  {C.DIM}12 batches following spiral curriculum & dependency graph{C.RESET}")
    print()

    if not HAS_BATCHES:
        print(f"  {C.DIM}Batch system not available.{C.RESET}")
        press_enter()
        return

    try:
        state = load_batch_state()
    except Exception:
        print(f"  {C.DIM}Error loading batch state.{C.RESET}")
        press_enter()
        return

    if not state.get("assigned"):
        print(f"  {C.YELLOW}Batches not yet assigned!{C.RESET}")
        print(f"  {C.DIM}Assigning 612 problems to 12 intelligent batches...{C.RESET}")
        state = assign_problems_to_batches()
        print(f"  {C.GREEN}Done!{C.RESET}")
        print()

    state = update_batch_progress()

    for bid_str in sorted(state["batch_progress"].keys(), key=int):
        bp = state["batch_progress"][bid_str]
        bid = int(bid_str)
        total = bp["total"]
        solved = bp["solved"]
        pct = (solved / total * 100) if total > 0 else 0

        bar_w = 20
        filled = int(bar_w * pct / 100)
        bar = "#" * filled + "-" * (bar_w - filled)

        if bid == state["current_batch"]:
            color = C.CYAN
            status = ">> ACTIVE"
        elif pct >= 100:
            color = C.GREEN
            status = "COMPLETE"
        elif bp.get("unlocked"):
            color = C.YELLOW
            status = "UNLOCKED"
        else:
            color = C.DIM
            status = "LOCKED"

        name = bp.get("name", f"Batch {bid}")
        print(f"  {color}{bid:>2}. [{bar}] {solved:>3}/{total:<3} ({pct:>4.0f}%) {status}{C.RESET}")
        print(f"      {C.DIM}{name}{C.RESET}")

    # Unlock info
    unlock = can_unlock_next()
    print()
    if unlock["problems_needed"] > 0:
        print(f"  {C.YELLOW}Solve {unlock['problems_needed']} more to unlock: {unlock['next_name']}{C.RESET}")
    elif unlock["next_unlocked"] and unlock["next_batch"] <= 12:
        print(f"  {C.GREEN}Batch {unlock['next_batch']} is ready: {unlock['next_name']}{C.RESET}")

    press_enter()


# ============================================================
# MAIN GAME LOOP
# ============================================================
def main():
    """Main game loop."""
    # Auto-assign batches on first run if needed
    if HAS_BATCHES:
        try:
            bs = load_batch_state()
            if not bs.get("assigned"):
                assign_problems_to_batches()
        except Exception as e:
            print(f"[play] Warning: auto-batch assignment failed: {e}", file=sys.stderr)

    while True:
        show_welcome()
        show_main_menu()

        choice = input(f"  {C.CYAN}Choose:{C.RESET} ").strip()

        if choice == "1":
            handle_next_challenge()
        elif choice == "2":
            handle_pick_topic()
        elif choice == "3":
            handle_stats()
        elif choice == "4":
            handle_achievements()
        elif choice == "5":
            handle_weak_areas()
        elif choice == "6":
            handle_daily_summary()
        elif choice == "7":
            handle_revision_queue()
        elif choice == "8":
            handle_batch_progress()
        elif choice == "0":
            print(f"\n  {C.CYAN}Keep grinding! See you next time.{C.RESET}")
            print(f"  {C.DIM}\"Consistency beats intensity.\"{C.RESET}\n")
            break
        else:
            print(f"  {C.RED}Invalid choice. Try 0-8.{C.RESET}")
            time.sleep(0.5)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print(f"\n\n  {C.CYAN}Session ended. Keep coding!{C.RESET}\n")
        sys.exit(0)
