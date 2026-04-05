"""
Problem: Set Clear Toggle ith Bit
Difficulty: EASY | XP: 10

Given an integer n and bit position i (0-indexed from right),
implement set (make 1), clear (make 0), and toggle (flip) operations.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (String Conversion)
# Time: O(1) | Space: O(1) -- 32-bit numbers
# ============================================================
def set_bit_brute(n: int, i: int) -> int:
    """Convert to binary string, set the character, convert back."""
    bits = list(bin(n)[2:].zfill(i + 1))
    bits[-(i + 1)] = '1'
    return int(''.join(bits), 2)


def clear_bit_brute(n: int, i: int) -> int:
    """Convert to binary string, clear the character, convert back."""
    bits = list(bin(n)[2:].zfill(i + 1))
    bits[-(i + 1)] = '0'
    return int(''.join(bits), 2)


def toggle_bit_brute(n: int, i: int) -> int:
    """Convert to binary string, flip the character, convert back."""
    bits = list(bin(n)[2:].zfill(i + 1))
    idx = -(i + 1)
    bits[idx] = '0' if bits[idx] == '1' else '1'
    return int(''.join(bits), 2)


# ============================================================
# APPROACH 2: OPTIMAL (Bitwise Operations)
# Time: O(1) | Space: O(1)
# ============================================================
def set_bit(n: int, i: int) -> int:
    """OR with mask forces bit i to 1."""
    return n | (1 << i)


def clear_bit(n: int, i: int) -> int:
    """AND with inverted mask forces bit i to 0."""
    return n & ~(1 << i)


def toggle_bit(n: int, i: int) -> int:
    """XOR with mask flips bit i."""
    return n ^ (1 << i)


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Set Clear Toggle ith Bit ===\n")

    # (n, i, expected_set, expected_clear, expected_toggle)
    test_cases = [
        (9,  2, 13, 9,  13),  # 1001: set bit 2 -> 1101=13
        (13, 2, 13, 9,  9),   # 1101: clear bit 2 -> 1001=9
        (0,  0, 1,  0,  1),   # all zeros
        (7,  0, 7,  6,  6),   # 0111: clear/toggle bit 0 -> 0110=6
        (0,  3, 8,  0,  8),   # set bit 3 -> 1000=8
    ]

    all_pass = True
    for n, i, exp_s, exp_c, exp_t in test_cases:
        bs, bc, bt = set_bit_brute(n, i), clear_bit_brute(n, i), toggle_bit_brute(n, i)
        os_, oc, ot = set_bit(n, i), clear_bit(n, i), toggle_bit(n, i)
        ok = bs == os_ == exp_s and bc == oc == exp_c and bt == ot == exp_t
        all_pass &= ok
        print(f"n={n}, i={i} | Set: B={bs} O={os_} (exp {exp_s}) | "
              f"Clear: B={bc} O={oc} (exp {exp_c}) | "
              f"Toggle: B={bt} O={ot} (exp {exp_t}) [{'PASS' if ok else 'FAIL'}]")

    print(f"\nAll pass: {all_pass}")
