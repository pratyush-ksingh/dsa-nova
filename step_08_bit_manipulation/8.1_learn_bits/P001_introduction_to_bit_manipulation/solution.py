"""
Problem: Introduction to Bit Manipulation
Difficulty: EASY | XP: 10

Implement basic bit operations: get, set, clear, toggle, power-of-2 check.
"""


# ============================================================
# APPROACH 1: BRUTE FORCE (String Conversion)
# Time: O(1) -- 32 bits max | Space: O(1)
# ============================================================
def brute_force_get_bit(n: int, i: int) -> int:
    """Convert to binary string and read the character at position i."""
    binary = bin(n)[2:].zfill(32)  # remove '0b', pad to 32
    return int(binary[31 - i])


def brute_force_set_bit(n: int, i: int) -> int:
    """Convert to binary string, set character, convert back."""
    bits = list(bin(n)[2:].zfill(32))
    bits[31 - i] = '1'
    return int(''.join(bits), 2)


def brute_force_clear_bit(n: int, i: int) -> int:
    """Convert to binary string, clear character, convert back."""
    bits = list(bin(n)[2:].zfill(32))
    bits[31 - i] = '0'
    return int(''.join(bits), 2)


def brute_force_toggle_bit(n: int, i: int) -> int:
    """Convert to binary string, flip character, convert back."""
    bits = list(bin(n)[2:].zfill(32))
    bits[31 - i] = '0' if bits[31 - i] == '1' else '1'
    return int(''.join(bits), 2)


def brute_force_is_power_of_two(n: int) -> bool:
    """Count 1-bits in the binary string -- exactly one means power of 2."""
    if n <= 0:
        return False
    return bin(n).count('1') == 1


# ============================================================
# APPROACH 2: OPTIMAL (Bitwise Mask Operations)
# Time: O(1) | Space: O(1)
# ============================================================
def optimal_get_bit(n: int, i: int) -> int:
    """Shift right by i, AND with 1."""
    return (n >> i) & 1


def optimal_set_bit(n: int, i: int) -> int:
    """OR with (1 << i)."""
    return n | (1 << i)


def optimal_clear_bit(n: int, i: int) -> int:
    """AND with complement of (1 << i)."""
    return n & ~(1 << i)


def optimal_toggle_bit(n: int, i: int) -> int:
    """XOR with (1 << i)."""
    return n ^ (1 << i)


def optimal_is_power_of_two(n: int) -> bool:
    """n & (n-1) == 0 means only one bit is set."""
    return n > 0 and (n & (n - 1)) == 0


# ============================================================
# APPROACH 3: BEST (Alternative expressions -- same O(1))
# Time: O(1) | Space: O(1)
# ============================================================
def best_get_bit(n: int, i: int) -> int:
    """AND with mask, then check non-zero."""
    return 1 if (n & (1 << i)) != 0 else 0


def best_set_bit(n: int, i: int) -> int:
    return n | (1 << i)


def best_clear_bit(n: int, i: int) -> int:
    return n & ~(1 << i)


def best_toggle_bit(n: int, i: int) -> int:
    return n ^ (1 << i)


def best_is_power_of_two(n: int) -> bool:
    """Use bin().count -- Pythonic alternative."""
    return n > 0 and bin(n).count('1') == 1


# ============================================================
# DRIVER
# ============================================================
if __name__ == "__main__":
    print("=== Introduction to Bit Manipulation ===\n")

    # --- Get Bit ---
    print("--- Get Bit ---")
    get_tests = [(13, 2, 1), (13, 1, 0), (13, 0, 1), (13, 3, 1), (0, 0, 0)]
    for n, i, exp in get_tests:
        b = brute_force_get_bit(n, i)
        o = optimal_get_bit(n, i)
        be = best_get_bit(n, i)
        status = "PASS" if b == exp and o == exp and be == exp else "FAIL"
        print(f"  n={n}, i={i} -> Brute:{b} Optimal:{o} Best:{be} Expected:{exp} [{status}]")

    # --- Set Bit ---
    print("\n--- Set Bit ---")
    set_tests = [(9, 2, 13), (13, 2, 13), (0, 0, 1)]
    for n, i, exp in set_tests:
        b = brute_force_set_bit(n, i)
        o = optimal_set_bit(n, i)
        be = best_set_bit(n, i)
        status = "PASS" if b == exp and o == exp and be == exp else "FAIL"
        print(f"  n={n}, i={i} -> Brute:{b} Optimal:{o} Best:{be} Expected:{exp} [{status}]")

    # --- Clear Bit ---
    print("\n--- Clear Bit ---")
    clear_tests = [(13, 2, 9), (9, 2, 9), (1, 0, 0)]
    for n, i, exp in clear_tests:
        b = brute_force_clear_bit(n, i)
        o = optimal_clear_bit(n, i)
        be = best_clear_bit(n, i)
        status = "PASS" if b == exp and o == exp and be == exp else "FAIL"
        print(f"  n={n}, i={i} -> Brute:{b} Optimal:{o} Best:{be} Expected:{exp} [{status}]")

    # --- Toggle Bit ---
    print("\n--- Toggle Bit ---")
    toggle_tests = [(13, 1, 15), (15, 1, 13), (0, 3, 8)]
    for n, i, exp in toggle_tests:
        b = brute_force_toggle_bit(n, i)
        o = optimal_toggle_bit(n, i)
        be = best_toggle_bit(n, i)
        status = "PASS" if b == exp and o == exp and be == exp else "FAIL"
        print(f"  n={n}, i={i} -> Brute:{b} Optimal:{o} Best:{be} Expected:{exp} [{status}]")

    # --- Power of Two ---
    print("\n--- Power of Two ---")
    pow_tests = [(16, True), (18, False), (1, True), (0, False), (1024, True)]
    for n, exp in pow_tests:
        b = brute_force_is_power_of_two(n)
        o = optimal_is_power_of_two(n)
        be = best_is_power_of_two(n)
        status = "PASS" if b == exp and o == exp and be == exp else "FAIL"
        print(f"  n={n} -> Brute:{b} Optimal:{o} Best:{be} Expected:{exp} [{status}]")
