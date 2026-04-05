"""Problem: Count Inversions - Count pairs (i,j) where i<j and A[i]>A[j] using merge sort"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Check every pair (i,j) with i < j and count if A[i] > A[j]
# ============================================================
def brute_force(A: list) -> int:
    n = len(A)
    count = 0
    for i in range(n):
        for j in range(i + 1, n):
            if A[i] > A[j]:
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL - Merge Sort (recursive)
# Time: O(n log n)  |  Space: O(n)
# During merge, when arr[i] > arr[j], all elements arr[i..mid] > arr[j].
# count += (mid - i + 1) for each such j encountered.
# ============================================================
def optimal(A: list) -> int:
    def merge_sort(arr):
        if len(arr) <= 1:
            return arr, 0
        mid = len(arr) // 2
        left, lc = merge_sort(arr[:mid])
        right, rc = merge_sort(arr[mid:])
        merged, mc = merge(left, right)
        return merged, lc + rc + mc

    def merge(left, right):
        result = []
        i = j = count = 0
        while i < len(left) and j < len(right):
            if left[i] <= right[j]:
                result.append(left[i]); i += 1
            else:
                count += len(left) - i  # left[i..end] all form inversions with right[j]
                result.append(right[j]); j += 1
        result.extend(left[i:])
        result.extend(right[j:])
        return result, count

    _, count = merge_sort(A[:])
    return count


# ============================================================
# APPROACH 3: BEST - Merge Sort (iterative bottom-up)
# Time: O(n log n)  |  Space: O(n)
# Bottom-up merge avoids recursion overhead; same inversion counting logic
# ============================================================
def best(A: list) -> int:
    arr = A[:]
    n = len(arr)
    total = 0
    width = 1
    while width < n:
        for left in range(0, n, 2 * width):
            mid   = min(left + width, n)
            right = min(left + 2 * width, n)
            if mid < right:
                temp_left  = arr[left:mid]
                temp_right = arr[mid:right]
                i = j = 0
                k = left
                while i < len(temp_left) and j < len(temp_right):
                    if temp_left[i] <= temp_right[j]:
                        arr[k] = temp_left[i]; i += 1
                    else:
                        total += len(temp_left) - i
                        arr[k] = temp_right[j]; j += 1
                    k += 1
                while i < len(temp_left):
                    arr[k] = temp_left[i]; i += 1; k += 1
                while j < len(temp_right):
                    arr[k] = temp_right[j]; j += 1; k += 1
        width *= 2
    return total


if __name__ == "__main__":
    print("=== Count Inversions ===")
    tests = [
        ([2, 4, 1, 3, 5], 3),
        ([5, 4, 3, 2, 1], 10),
        ([1, 2, 3, 4, 5], 0),
    ]
    for A, expected in tests:
        b  = brute_force(A)
        o  = optimal(A)
        be = best(A)
        print(f"A={A} => Brute={b}, Optimal={o}, Best={be} (expected {expected})")
        assert b == o == be == expected, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
