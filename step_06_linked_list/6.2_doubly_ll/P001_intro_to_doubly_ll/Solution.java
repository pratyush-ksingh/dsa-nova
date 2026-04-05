/**
 * Problem: Intro to Doubly Linked List
 * Difficulty: EASY | XP: 10
 *
 * Create a doubly linked list. Implement insert, delete, and traverse operations.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // NODE DEFINITION
    // ============================================================
    static class DLLNode {
        int val;
        DLLNode prev;
        DLLNode next;

        DLLNode(int val) {
            this.val = val;
            this.prev = null;
            this.next = null;
        }
    }

    // ============================================================
    // DOUBLY LINKED LIST CLASS
    // ============================================================
    static class DoublyLinkedList {
        DLLNode head;
        DLLNode tail;
        int size;

        DoublyLinkedList() {
            head = null;
            tail = null;
            size = 0;
        }

        // BUILD from array: O(n) time, O(n) space
        static DoublyLinkedList fromArray(int[] arr) {
            DoublyLinkedList dll = new DoublyLinkedList();
            if (arr == null || arr.length == 0) return dll;

            dll.head = new DLLNode(arr[0]);
            DLLNode current = dll.head;
            dll.size = arr.length;

            for (int i = 1; i < arr.length; i++) {
                DLLNode newNode = new DLLNode(arr[i]);
                current.next = newNode;
                newNode.prev = current;
                current = newNode;
            }
            dll.tail = current;
            return dll;
        }

        // INSERT AT HEAD: O(1)
        void insertAtHead(int val) {
            DLLNode newNode = new DLLNode(val);
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
            size++;
        }

        // INSERT AT TAIL: O(1)
        void insertAtTail(int val) {
            DLLNode newNode = new DLLNode(val);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            size++;
        }

        // INSERT AT POSITION (1-indexed): O(k)
        void insertAtPosition(int val, int position) {
            if (position <= 1) {
                insertAtHead(val);
                return;
            }
            if (position > size) {
                insertAtTail(val);
                return;
            }

            DLLNode current = head;
            for (int i = 1; i < position - 1; i++) {
                current = current.next;
            }

            DLLNode newNode = new DLLNode(val);
            newNode.next = current.next;
            newNode.prev = current;
            if (current.next != null) {
                current.next.prev = newNode;
            }
            current.next = newNode;
            size++;
        }

        // DELETE AT HEAD: O(1)
        int deleteAtHead() {
            if (head == null) {
                System.out.println("List is empty!");
                return -1;
            }
            int val = head.val;
            head = head.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
            size--;
            return val;
        }

        // DELETE AT TAIL: O(1)
        int deleteAtTail() {
            if (tail == null) {
                System.out.println("List is empty!");
                return -1;
            }
            int val = tail.val;
            tail = tail.prev;
            if (tail != null) {
                tail.next = null;
            } else {
                head = null;
            }
            size--;
            return val;
        }

        // DELETE AT POSITION (1-indexed): O(k)
        int deleteAtPosition(int position) {
            if (head == null || position < 1) return -1;
            if (position == 1) return deleteAtHead();
            if (position >= size) return deleteAtTail();

            DLLNode current = head;
            for (int i = 1; i < position; i++) {
                current = current.next;
            }

            current.prev.next = current.next;
            if (current.next != null) {
                current.next.prev = current.prev;
            }
            size--;
            return current.val;
        }

        // TRAVERSE FORWARD: O(n)
        void printForward() {
            DLLNode current = head;
            StringBuilder sb = new StringBuilder("Forward:  ");
            while (current != null) {
                sb.append(current.val);
                if (current.next != null) sb.append(" <-> ");
                current = current.next;
            }
            System.out.println(sb.toString());
        }

        // TRAVERSE BACKWARD: O(n)
        void printBackward() {
            DLLNode current = tail;
            StringBuilder sb = new StringBuilder("Backward: ");
            while (current != null) {
                sb.append(current.val);
                if (current.prev != null) sb.append(" <-> ");
                current = current.prev;
            }
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Intro to Doubly Linked List ===\n");

        // Build from array
        int[] arr = {1, 2, 3, 4, 5};
        DoublyLinkedList dll = DoublyLinkedList.fromArray(arr);
        System.out.println("Built from [1, 2, 3, 4, 5]:");
        dll.printForward();
        dll.printBackward();
        System.out.println("Size: " + dll.size);

        // Insert at head
        System.out.println("\nInsert 0 at head:");
        dll.insertAtHead(0);
        dll.printForward();

        // Insert at tail
        System.out.println("\nInsert 6 at tail:");
        dll.insertAtTail(6);
        dll.printForward();

        // Insert at position
        System.out.println("\nInsert 99 at position 4:");
        dll.insertAtPosition(99, 4);
        dll.printForward();
        dll.printBackward();

        // Delete at head
        System.out.println("\nDelete at head: " + dll.deleteAtHead());
        dll.printForward();

        // Delete at tail
        System.out.println("\nDelete at tail: " + dll.deleteAtTail());
        dll.printForward();

        // Delete at position
        System.out.println("\nDelete at position 3: " + dll.deleteAtPosition(3));
        dll.printForward();
        dll.printBackward();
        System.out.println("Size: " + dll.size);

        // Edge case: single element
        System.out.println("\n--- Edge case: single element ---");
        DoublyLinkedList single = DoublyLinkedList.fromArray(new int[]{42});
        single.printForward();
        single.deleteAtHead();
        System.out.println("After delete, size: " + single.size);
        System.out.println("Head is null: " + (single.head == null));

        // Edge case: empty list
        System.out.println("\n--- Edge case: empty list ---");
        DoublyLinkedList empty = new DoublyLinkedList();
        empty.deleteAtHead();
    }
}
