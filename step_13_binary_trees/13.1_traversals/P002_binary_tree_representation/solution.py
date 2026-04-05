"""
Problem: Binary Tree Representation
Difficulty: EASY | XP: 10

Array and linked node representations with conversions.
"""
from typing import List, Optional
from collections import deque


# ============================================================
# TreeNode definition (Linked Representation)
# ============================================================
class TreeNode:
    def __init__(self, val: int = 0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

    def __repr__(self):
        return f"TreeNode({self.val})"


# ============================================================
# APPROACH 1: Array Representation -- index arithmetic
# Parent of i: (i-1)//2 | Left child: 2i+1 | Right child: 2i+2
# ============================================================
class ArrayTree:
    def __init__(self, capacity: int):
        self.data: List[Optional[int]] = [None] * capacity

    def set(self, index: int, val: int):
        if index < len(self.data):
            self.data[index] = val

    def get(self, index: int) -> Optional[int]:
        return self.data[index] if index < len(self.data) else None

    @staticmethod
    def parent_index(i: int) -> int:
        return (i - 1) // 2

    @staticmethod
    def left_index(i: int) -> int:
        return 2 * i + 1

    @staticmethod
    def right_index(i: int) -> int:
        return 2 * i + 2

    def parent(self, i: int) -> Optional[int]:
        return self.get(self.parent_index(i)) if i > 0 else None

    def left_child(self, i: int) -> Optional[int]:
        return self.get(self.left_index(i))

    def right_child(self, i: int) -> Optional[int]:
        return self.get(self.right_index(i))

    def __repr__(self):
        return str(self.data)


# ============================================================
# CONVERSION: Array -> Linked (BFS)
# Time: O(n) | Space: O(n)
# ============================================================
def array_to_linked(arr: List[Optional[int]]) -> Optional[TreeNode]:
    if not arr or arr[0] is None:
        return None

    root = TreeNode(arr[0])
    queue = deque([root])
    i = 1

    while queue and i < len(arr):
        curr = queue.popleft()

        # Left child
        if i < len(arr) and arr[i] is not None:
            curr.left = TreeNode(arr[i])
            queue.append(curr.left)
        i += 1

        # Right child
        if i < len(arr) and arr[i] is not None:
            curr.right = TreeNode(arr[i])
            queue.append(curr.right)
        i += 1

    return root


# ============================================================
# CONVERSION: Linked -> Array (BFS with index tracking)
# Time: O(n) | Space: O(n)
# ============================================================
def linked_to_array(root: Optional[TreeNode]) -> List[Optional[int]]:
    if root is None:
        return []

    result_map = {}
    max_index = 0
    queue = deque([(root, 0)])

    while queue:
        node, idx = queue.popleft()
        result_map[idx] = node.val
        max_index = max(max_index, idx)

        if node.left:
            queue.append((node.left, 2 * idx + 1))
        if node.right:
            queue.append((node.right, 2 * idx + 2))

    return [result_map.get(i) for i in range(max_index + 1)]


# Helper: level-order display
def level_order(root: Optional[TreeNode]) -> List[List[int]]:
    if not root:
        return []
    result = []
    queue = deque([root])
    while queue:
        level = []
        for _ in range(len(queue)):
            node = queue.popleft()
            level.append(node.val)
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        result.append(level)
    return result


if __name__ == "__main__":
    print("=== Binary Tree Representation ===\n")

    # --- Array Representation ---
    print("--- Array Representation ---")
    at = ArrayTree(7)
    for i, v in enumerate([1, 2, 3, 4, 5]):
        at.set(i, v)
    print(f"Array: {at}")
    print(f"Root: {at.get(0)}")
    print(f"Left child of root: {at.left_child(0)}")
    print(f"Right child of root: {at.right_child(0)}")
    print(f"Parent of index 3: {at.parent(3)}")
    print(f"Parent of index 4: {at.parent(4)}")

    # --- Array -> Linked ---
    print("\n--- Array -> Linked ---")
    arr = [1, 2, 3, 4, 5, None, 6]
    root = array_to_linked(arr)
    print(f"Input array: {arr}")
    print(f"Level order: {level_order(root)}")

    # --- Linked -> Array ---
    print("\n--- Linked -> Array ---")
    back = linked_to_array(root)
    print(f"Back to array: {back}")

    # --- Round-trip ---
    print("\n--- Round-trip ---")
    original = [1, 2, 3, None, 4, 5, None]
    tree = array_to_linked(original)
    round_trip = linked_to_array(tree)
    print(f"Original:   {original}")
    print(f"Round-trip: {round_trip}")

    # --- Edge Cases ---
    print("\n--- Edge Cases ---")
    print(f"Empty array -> linked: {array_to_linked([])}")
    print(f"None root -> array: {linked_to_array(None)}")

    single = array_to_linked([42])
    print(f"Single node level order: {level_order(single)}")
    print(f"Single node to array: {linked_to_array(single)}")
