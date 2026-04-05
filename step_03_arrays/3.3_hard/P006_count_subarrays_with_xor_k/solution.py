"""Problem: Count Subarrays with XOR K - Count subarrays where XOR of elements equals K"""

# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(n^2)  |  Space: O(1)
# Check every subarray, compute XOR, count those equal to k
# ============================================================
def brute_force(A: list, k: int) -> int:
    n, count = len(A), 0
    for i in range(n):
        xor = 0
        for j in range(i, n):
            xor ^= A[j]
            if xor == k:
                count += 1
    return count


# ============================================================
# APPROACH 2: OPTIMAL - Prefix XOR + HashMap
# Time: O(n)  |  Space: O(n)
# XOR(i..j) = prefXOR[j] ^ prefXOR[i-1]
# We want XOR(i..j) == k  =>  prefXOR[i-1] = prefXOR[j] ^ k
# Count how many previous prefix XORs equal (current_prefXOR ^ k)
# ============================================================
def optimal(A: list, k: int) -> int:
    from collections import defaultdict
    freq = defaultdict(int)
    freq[0] = 1  # empty prefix has XOR 0
    pref_xor = 0
    count = 0
    for x in A:
        pref_xor ^= x
        count += freq[pref_xor ^ k]
        freq[pref_xor] += 1
    return count


# ============================================================
# APPROACH 3: BEST - Same optimal with explicit clarity
# Time: O(n)  |  Space: O(n)
# Identical algorithm, written to maximally highlight the key insight
# ============================================================
def best(A: list, k: int) -> int:
    from collections import defaultdict
    pref_count = defaultdict(int)
    pref_count[0] = 1
    prefix_xor = 0
    result = 0
    for num in A:
        prefix_xor ^= num
        # If (prefix_xor ^ k) was seen before, those subarrays ending here have XOR = k
        result += pref_count[prefix_xor ^ k]
        pref_count[prefix_xor] += 1
    return result


if __name__ == "__main__":
    print("=== Count Subarrays with XOR K ===")
    tests = [
        ([4, 2, 2, 6, 4], 6),   # expected 4
        ([5, 6, 7, 8, 9], 5),   # expected 2
        ([1, 2, 3], 2),          # expected 2: [2], [1,2,3]? -> [2] and [1^2^3=0 no], [3]? 3!=2, [2,3]=1, [1,2]=3... let verify
    ]
    for A, k in tests:
        b = brute_force(A, k)
        o = optimal(A, k)
        be = best(A, k)
        print(f"A={A}, k={k} => Brute={b}, Optimal={o}, Best={be}")
        assert b == o == be, f"Mismatch: {b} {o} {be}"
    print("All tests passed.")
