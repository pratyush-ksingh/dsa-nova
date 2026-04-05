"""
Problem: Intro to Doubly Linked List
Difficulty: EASY | XP: 10

Create a doubly linked list. Implement insert, delete, and traverse operations.
"""
from typing import List, Optional


# ============================================================
# NODE DEFINITION
# ============================================================
class DLLNode:
    def __init__(self, val: int = 0):
        self.val = val
        self.prev: Optional[DLLNode] = None
        self.next: Optional[DLLNode] = None


# ============================================================
# DOUBLY LINKED LIST CLASS
# ============================================================
class DoublyLinkedList:
    def __init__(self):
        self.head: Optional[DLLNode] = None
        self.tail: Optional[DLLNode] = None
        self.size: int = 0

    @staticmethod
    def from_array(arr: List[int]) -> 'DoublyLinkedList':
        """Build DLL from array. Time: O(n), Space: O(n)."""
        dll = DoublyLinkedList()
        if not arr:
            return dll

        dll.head = DLLNode(arr[0])
        current = dll.head
        dll.size = len(arr)

        for i in range(1, len(arr)):
            new_node = DLLNode(arr[i])
            current.next = new_node
            new_node.prev = current
            current = new_node

        dll.tail = current
        return dll

    def insert_at_head(self, val: int) -> None:
        """Insert at head. Time: O(1)."""
        new_node = DLLNode(val)
        if self.head is None:
            self.head = self.tail = new_node
        else:
            new_node.next = self.head
            self.head.prev = new_node
            self.head = new_node
        self.size += 1

    def insert_at_tail(self, val: int) -> None:
        """Insert at tail. Time: O(1)."""
        new_node = DLLNode(val)
        if self.tail is None:
            self.head = self.tail = new_node
        else:
            self.tail.next = new_node
            new_node.prev = self.tail
            self.tail = new_node
        self.size += 1

    def insert_at_position(self, val: int, position: int) -> None:
        """Insert at 1-indexed position. Time: O(k)."""
        if position <= 1:
            self.insert_at_head(val)
            return
        if position > self.size:
            self.insert_at_tail(val)
            return

        current = self.head
        for _ in range(position - 2):
            current = current.next

        new_node = DLLNode(val)
        new_node.next = current.next
        new_node.prev = current
        if current.next:
            current.next.prev = new_node
        current.next = new_node
        self.size += 1

    def delete_at_head(self) -> int:
        """Delete head node. Time: O(1)."""
        if self.head is None:
            print("List is empty!")
            return -1
        val = self.head.val
        self.head = self.head.next
        if self.head:
            self.head.prev = None
        else:
            self.tail = None
        self.size -= 1
        return val

    def delete_at_tail(self) -> int:
        """Delete tail node. Time: O(1)."""
        if self.tail is None:
            print("List is empty!")
            return -1
        val = self.tail.val
        self.tail = self.tail.prev
        if self.tail:
            self.tail.next = None
        else:
            self.head = None
        self.size -= 1
        return val

    def delete_at_position(self, position: int) -> int:
        """Delete at 1-indexed position. Time: O(k)."""
        if self.head is None or position < 1:
            return -1
        if position == 1:
            return self.delete_at_head()
        if position >= self.size:
            return self.delete_at_tail()

        current = self.head
        for _ in range(position - 1):
            current = current.next

        current.prev.next = current.next
        if current.next:
            current.next.prev = current.prev
        self.size -= 1
        return current.val

    def print_forward(self) -> str:
        """Traverse forward. Time: O(n)."""
        parts = []
        current = self.head
        while current:
            parts.append(str(current.val))
            current = current.next
        result = "Forward:  " + (" <-> ".join(parts) if parts else "(empty)")
        print(result)
        return result

    def print_backward(self) -> str:
        """Traverse backward. Time: O(n)."""
        parts = []
        current = self.tail
        while current:
            parts.append(str(current.val))
            current = current.prev
        result = "Backward: " + (" <-> ".join(parts) if parts else "(empty)")
        print(result)
        return result


if __name__ == "__main__":
    print("=== Intro to Doubly Linked List ===\n")

    # Build from array
    dll = DoublyLinkedList.from_array([1, 2, 3, 4, 5])
    print("Built from [1, 2, 3, 4, 5]:")
    dll.print_forward()
    dll.print_backward()
    print(f"Size: {dll.size}")

    # Insert at head
    print("\nInsert 0 at head:")
    dll.insert_at_head(0)
    dll.print_forward()

    # Insert at tail
    print("\nInsert 6 at tail:")
    dll.insert_at_tail(6)
    dll.print_forward()

    # Insert at position
    print("\nInsert 99 at position 4:")
    dll.insert_at_position(99, 4)
    dll.print_forward()
    dll.print_backward()

    # Delete at head
    print(f"\nDelete at head: {dll.delete_at_head()}")
    dll.print_forward()

    # Delete at tail
    print(f"\nDelete at tail: {dll.delete_at_tail()}")
    dll.print_forward()

    # Delete at position
    print(f"\nDelete at position 3: {dll.delete_at_position(3)}")
    dll.print_forward()
    dll.print_backward()
    print(f"Size: {dll.size}")

    # Edge case: single element
    print("\n--- Edge case: single element ---")
    single = DoublyLinkedList.from_array([42])
    single.print_forward()
    single.delete_at_head()
    print(f"After delete, size: {single.size}")
    print(f"Head is None: {single.head is None}")

    # Edge case: empty list
    print("\n--- Edge case: empty list ---")
    empty = DoublyLinkedList()
    empty.delete_at_head()
