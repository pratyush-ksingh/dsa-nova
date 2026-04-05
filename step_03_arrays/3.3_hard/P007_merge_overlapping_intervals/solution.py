"""Problem: Merge Overlapping Intervals - Sort then merge all overlapping intervals"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2 log n)  |  Space: O(n)
# Repeatedly find and merge any overlapping pair until none remain
# ============================================================
def brute_force(intervals: list) -> list:
    intervals = [list(x) for x in intervals]
    changed = True
    while changed:
        intervals.sort()
        changed = False
        merged = []
        i = 0
        while i < len(intervals):
            cur = intervals[i][:]
            j = i + 1
            while j < len(intervals) and intervals[j][0] <= cur[1]:
                cur[1] = max(cur[1], intervals[j][1])
                j += 1
                changed = True
            merged.append(cur)
            i = j
        intervals = merged
    return intervals


# ============================================================
# APPROACH 2: OPTIMAL - Sort + single linear scan
# Time: O(n log n)  |  Space: O(n)
# Sort by start. For each interval, if it overlaps last in result, extend; else add new.
# ============================================================
def optimal(intervals: list) -> list:
    if not intervals:
        return []
    intervals = sorted(intervals, key=lambda x: x[0])
    merged = [list(intervals[0])]
    for start, end in intervals[1:]:
        if start <= merged[-1][1]:
            merged[-1][1] = max(merged[-1][1], end)
        else:
            merged.append([start, end])
    return merged


# ============================================================
# APPROACH 3: BEST - Stack-based merge (same complexity, explicit logic)
# Time: O(n log n)  |  Space: O(n)
# Stack makes the algorithm easy to visualize and extend
# ============================================================
def best(intervals: list) -> list:
    if not intervals:
        return []
    intervals = sorted(intervals, key=lambda x: x[0])
    stack = [list(intervals[0])]
    for interval in intervals[1:]:
        if interval[0] <= stack[-1][1]:
            stack[-1][1] = max(stack[-1][1], interval[1])
        else:
            stack.append(list(interval))
    return stack


if __name__ == "__main__":
    print("=== Merge Overlapping Intervals ===")
    tests = [
        ([[1,3],[2,6],[8,10],[15,18]], [[1,6],[8,10],[15,18]]),
        ([[1,4],[4,5]],                [[1,5]]),
        ([[1,4],[0,4]],                [[0,4]]),
    ]
    for intervals, expected in tests:
        b  = brute_force(intervals)
        o  = optimal(intervals)
        be = best(intervals)
        print(f"Input={intervals} => Brute={b}, Optimal={o}, Best={be} (exp={expected})")
        assert o == be == expected, f"Mismatch"
    print("All tests passed.")
