"""
Problem: Implement Stack using Arrays
Difficulty: EASY | XP: 10

Implement stack with push, pop, peek, isEmpty, size using array.
"""


# ============================================================
# STACK IMPLEMENTATION (Fixed-Size Array)
# All operations: O(1) time, O(1) space per operation
# Overall space: O(capacity)
# ============================================================
class Stack:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.arr = [0] * capacity
        self.top = -1  # -1 means empty

    def push(self, val: int) -> None:
        """Push element onto the stack. Time: O(1)."""
        if self.top == self.capacity - 1:
            print(f"Stack Overflow! Cannot push {val}")
            return
        self.top += 1
        self.arr[self.top] = val

    def pop(self) -> int:
        """Remove and return top element. Time: O(1)."""
        if self.top == -1:
            print("Stack Underflow! Cannot pop.")
            return -1
        val = self.arr[self.top]
        self.top -= 1
        return val

    def peek(self) -> int:
        """Return top element without removing. Time: O(1)."""
        if self.top == -1:
            print("Stack is empty! Cannot peek.")
            return -1
        return self.arr[self.top]

    def is_empty(self) -> bool:
        """Check if stack is empty. Time: O(1)."""
        return self.top == -1

    def size(self) -> int:
        """Return number of elements. Time: O(1)."""
        return self.top + 1

    def print_stack(self) -> None:
        """Print stack contents (bottom to top)."""
        if self.is_empty():
            print("Stack: (empty)")
            return
        elements = [str(self.arr[i]) for i in range(self.top + 1)]
        print(f"Stack (bottom->top): [{', '.join(elements)}]")


# ============================================================
# DYNAMIC STACK (with resizing) - Amortized O(1)
# Python lists already do this, but we implement it explicitly.
# ============================================================
class DynamicStack:
    def __init__(self):
        self.capacity = 4  # start small
        self.arr = [0] * self.capacity
        self.top = -1

    def push(self, val: int) -> None:
        if self.top == self.capacity - 1:
            self._resize(self.capacity * 2)
        self.top += 1
        self.arr[self.top] = val

    def pop(self) -> int:
        if self.top == -1:
            return -1
        val = self.arr[self.top]
        self.top -= 1
        return val

    def peek(self) -> int:
        if self.top == -1:
            return -1
        return self.arr[self.top]

    def is_empty(self) -> bool:
        return self.top == -1

    def size(self) -> int:
        return self.top + 1

    def _resize(self, new_capacity: int) -> None:
        new_arr = [0] * new_capacity
        for i in range(self.top + 1):
            new_arr[i] = self.arr[i]
        self.arr = new_arr
        self.capacity = new_capacity
        print(f"  (Resized to capacity {new_capacity})")


if __name__ == "__main__":
    print("=== Implement Stack using Arrays ===\n")

    # Fixed-size stack demo
    print("--- Fixed-Size Stack (capacity=5) ---")
    stack = Stack(5)

    stack.push(10)
    stack.push(20)
    stack.push(30)
    stack.print_stack()

    print(f"Peek: {stack.peek()}")       # 30
    print(f"Size: {stack.size()}")       # 3
    print(f"Pop:  {stack.pop()}")        # 30
    print(f"Pop:  {stack.pop()}")        # 20
    stack.print_stack()

    print(f"isEmpty: {stack.is_empty()}")  # False
    print(f"Pop:  {stack.pop()}")          # 10
    print(f"isEmpty: {stack.is_empty()}")  # True

    # Overflow test
    print("\n--- Overflow Test ---")
    small = Stack(2)
    small.push(1)
    small.push(2)
    small.push(3)  # Overflow!

    # Underflow test
    print("\n--- Underflow Test ---")
    empty = Stack(3)
    empty.pop()    # Underflow!
    empty.peek()   # Empty!

    # Dynamic stack demo
    print("\n--- Dynamic Stack (auto-resize) ---")
    d_stack = DynamicStack()
    for i in range(1, 11):
        d_stack.push(i * 10)
        print(f"Pushed {i * 10}, size={d_stack.size()}")
    print(f"Peek: {d_stack.peek()}")    # 100
    print(f"Pop:  {d_stack.pop()}")     # 100
    print(f"Size: {d_stack.size()}")    # 9
