#!/usr/bin/env python3
"""
DSA Nova - Problem Scaffolding Script
Generates ALL problem folders with templates for the entire Striver's A2Z sheet.
Run once: python scripts/init_all_problems.py
"""

import os
import json
import re

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# ============================================================
# COMPLETE STRIVER'S A2Z SHEET - ALL PROBLEMS
# ============================================================

PROBLEMS = {
    # ── STEP 1: BASICS ──────────────────────────────────────
    "step_01_basics/1.1_logic_building": [
        ("P001", "Pattern 1 - Rectangular Star", "EASY"),
        ("P002", "Pattern 2 - Right Angled Triangle", "EASY"),
        ("P003", "Pattern 3 - Right Angled Number Triangle", "EASY"),
        ("P004", "Pattern 4 - Right Angled Number Triangle II", "EASY"),
        ("P005", "Pattern 5 - Inverted Right Triangle", "EASY"),
        ("P006", "Pattern 6 - Inverted Numbered Triangle", "EASY"),
        ("P007", "Pattern 7 - Star Pyramid", "EASY"),
        ("P008", "Pattern 8 - Inverted Star Pyramid", "EASY"),
        ("P009", "Pattern 9 - Diamond Star Pattern", "EASY"),
        ("P010", "Pattern 10 - Half Diamond Star", "EASY"),
        ("P011", "Pattern 11 - Binary Number Triangle", "EASY"),
        ("P012", "Pattern 12 - Number Crown Pattern", "EASY"),
        ("P013", "Pattern 13 - Increasing Number Triangle", "EASY"),
        ("P014", "Pattern 14 - Increasing Letter Triangle", "EASY"),
        ("P015", "Pattern 15 - Reverse Letter Triangle", "EASY"),
        ("P016", "Pattern 16 - Alpha Ramp", "EASY"),
        ("P017", "Pattern 17 - Alpha Hill", "EASY"),
        ("P018", "Pattern 18 - Alpha Triangle", "EASY"),
        ("P019", "Pattern 19 - Symmetric Void Pattern", "EASY"),
        ("P020", "Pattern 20 - Symmetric Butterfly", "EASY"),
        ("P021", "Pattern 21 - Hollow Rectangle", "EASY"),
        ("P022", "Pattern 22 - The Number Pattern", "EASY"),
    ],
    "step_01_basics/1.2_basic_math": [
        ("P001", "Count Digits", "EASY"),
        ("P002", "Reverse a Number", "EASY"),
        ("P003", "Check Palindrome", "EASY"),
        ("P004", "GCD or HCF", "EASY"),
        ("P005", "Armstrong Number", "EASY"),
        ("P006", "Print All Divisors", "EASY"),
        ("P007", "Check for Prime", "EASY"),
    ],
    "step_01_basics/1.3_basic_recursion": [
        ("P001", "Print 1 to N", "EASY"),
        ("P002", "Print N to 1", "EASY"),
        ("P003", "Print Name N Times", "EASY"),
        ("P004", "Sum of First N Numbers", "EASY"),
        ("P005", "Factorial of N", "EASY"),
        ("P006", "Reverse an Array", "EASY"),
        ("P007", "Check Palindrome String", "EASY"),
        ("P008", "Fibonacci Number", "EASY"),
    ],
    "step_01_basics/1.4_basic_hashing": [
        ("P001", "Counting Frequency of Elements", "EASY"),
        ("P002", "Highest and Lowest Frequency Element", "EASY"),
    ],

    # ── STEP 2: SORTING ─────────────────────────────────────
    "step_02_sorting/2.1_sorting_1": [
        ("P001", "Selection Sort", "EASY"),
        ("P002", "Bubble Sort", "EASY"),
        ("P003", "Insertion Sort", "EASY"),
    ],
    "step_02_sorting/2.2_sorting_2": [
        ("P001", "Merge Sort", "MEDIUM"),
        ("P002", "Recursive Bubble Sort", "EASY"),
        ("P003", "Recursive Insertion Sort", "EASY"),
        ("P004", "Quick Sort", "MEDIUM"),
    ],

    # ── STEP 3: ARRAYS ──────────────────────────────────────
    "step_03_arrays/3.1_easy": [
        ("P001", "Largest Element in Array", "EASY"),
        ("P002", "Second Largest Element", "EASY"),
        ("P003", "Check if Array is Sorted", "EASY"),
        ("P004", "Remove Duplicates from Sorted Array", "EASY"),
        ("P005", "Left Rotate Array by One", "EASY"),
        ("P006", "Left Rotate Array by K Places", "MEDIUM"),
        ("P007", "Move Zeros to End", "EASY"),
        ("P008", "Linear Search", "EASY"),
        ("P009", "Union of Two Sorted Arrays", "MEDIUM"),
        ("P010", "Find Missing Number", "EASY"),
        ("P011", "Maximum Consecutive Ones", "EASY"),
        ("P012", "Single Number", "EASY"),
        ("P013", "Longest Subarray with Sum K Positives", "MEDIUM"),
        ("P014", "Longest Subarray with Sum K", "MEDIUM"),
    ],
    "step_03_arrays/3.2_medium": [
        ("P001", "Two Sum", "EASY"),
        ("P002", "Sort Array of 0s 1s 2s", "MEDIUM"),
        ("P003", "Majority Element", "MEDIUM"),
        ("P004", "Kadanes Algorithm Max Subarray Sum", "MEDIUM"),
        ("P005", "Print Max Subarray Sum", "MEDIUM"),
        ("P006", "Stock Buy and Sell", "EASY"),
        ("P007", "Rearrange Array by Sign", "MEDIUM"),
        ("P008", "Next Permutation", "MEDIUM"),
        ("P009", "Leaders in an Array", "EASY"),
        ("P010", "Longest Consecutive Sequence", "MEDIUM"),
        ("P011", "Set Matrix Zeroes", "MEDIUM"),
        ("P012", "Rotate Matrix 90 Degrees", "MEDIUM"),
        ("P013", "Spiral Matrix", "MEDIUM"),
        ("P014", "Count Subarrays with Given Sum", "MEDIUM"),
    ],
    "step_03_arrays/3.3_hard": [
        ("P001", "Pascals Triangle", "EASY"),
        ("P002", "Majority Element II", "MEDIUM"),
        ("P003", "3 Sum", "MEDIUM"),
        ("P004", "4 Sum", "MEDIUM"),
        ("P005", "Largest Subarray with Sum 0", "MEDIUM"),
        ("P006", "Count Subarrays with XOR K", "HARD"),
        ("P007", "Merge Overlapping Intervals", "MEDIUM"),
        ("P008", "Merge Two Sorted Arrays Without Extra Space", "HARD"),
        ("P009", "Find Repeating and Missing Number", "HARD"),
        ("P010", "Count Inversions", "HARD"),
        ("P011", "Reverse Pairs", "HARD"),
        ("P012", "Maximum Product Subarray", "MEDIUM"),
    ],

    # ── STEP 4: BINARY SEARCH ───────────────────────────────
    "step_04_binary_search/4.1_bs_on_1d_arrays": [
        ("P001", "Binary Search", "EASY"),
        ("P002", "Lower Bound", "EASY"),
        ("P003", "Upper Bound", "EASY"),
        ("P004", "Search Insert Position", "EASY"),
        ("P005", "Floor and Ceil in Sorted Array", "EASY"),
        ("P006", "First and Last Occurrence", "MEDIUM"),
        ("P007", "Count Occurrences in Sorted Array", "EASY"),
        ("P008", "Search in Rotated Sorted Array I", "MEDIUM"),
        ("P009", "Search in Rotated Sorted Array II", "MEDIUM"),
        ("P010", "Find Minimum in Rotated Sorted Array", "MEDIUM"),
        ("P011", "How Many Times Array is Rotated", "MEDIUM"),
        ("P012", "Single Element in Sorted Array", "MEDIUM"),
        ("P013", "Find Peak Element", "MEDIUM"),
    ],
    "step_04_binary_search/4.2_bs_on_answers": [
        ("P001", "Find Square Root", "EASY"),
        ("P002", "Find Nth Root", "MEDIUM"),
        ("P003", "Koko Eating Bananas", "MEDIUM"),
        ("P004", "Minimum Days to Make M Bouquets", "MEDIUM"),
        ("P005", "Find the Smallest Divisor", "MEDIUM"),
        ("P006", "Capacity to Ship Packages", "MEDIUM"),
        ("P007", "Kth Missing Positive Number", "EASY"),
        ("P008", "Aggressive Cows", "HARD"),
        ("P009", "Book Allocation Problem", "HARD"),
        ("P010", "Split Array Largest Sum", "HARD"),
        ("P011", "Painters Partition", "HARD"),
        ("P012", "Minimize Max Distance to Gas Station", "HARD"),
        ("P013", "Median of Two Sorted Arrays", "HARD"),
        ("P014", "Kth Element of Two Sorted Arrays", "HARD"),
    ],
    "step_04_binary_search/4.3_bs_on_2d_arrays": [
        ("P001", "Find Row with Maximum 1s", "MEDIUM"),
        ("P002", "Search in 2D Matrix", "MEDIUM"),
        ("P003", "Search in 2D Matrix II", "MEDIUM"),
        ("P004", "Find Peak Element II", "HARD"),
        ("P005", "Matrix Median", "HARD"),
    ],

    # ── STEP 5: STRINGS ─────────────────────────────────────
    "step_05_strings/5.1_basic_and_easy": [
        ("P001", "Remove Outermost Parentheses", "EASY"),
        ("P002", "Reverse Words in a String", "MEDIUM"),
        ("P003", "Largest Odd Number in String", "EASY"),
        ("P004", "Longest Common Prefix", "EASY"),
        ("P005", "Isomorphic Strings", "EASY"),
        ("P006", "Rotate String", "EASY"),
        ("P007", "Check Anagram", "EASY"),
    ],
    "step_05_strings/5.2_medium": [
        ("P001", "Sort Characters by Frequency", "MEDIUM"),
        ("P002", "Max Nesting Depth of Parentheses", "EASY"),
        ("P003", "Roman to Integer", "EASY"),
        ("P004", "String to Integer Atoi", "MEDIUM"),
        ("P005", "Count Number of Substrings", "MEDIUM"),
        ("P006", "Longest Palindromic Substring", "MEDIUM"),
        ("P007", "Sum of Beauty of All Substrings", "MEDIUM"),
        ("P008", "Reverse Every Word in a String", "MEDIUM"),
    ],

    # ── STEP 6: LINKED LIST ─────────────────────────────────
    "step_06_linked_list/6.1_singly_ll": [
        ("P001", "Intro to Linked List", "EASY"),
        ("P002", "Inserting a Node in LL", "EASY"),
        ("P003", "Deleting a Node in LL", "EASY"),
        ("P004", "Find Length of LL", "EASY"),
        ("P005", "Search in Linked List", "EASY"),
    ],
    "step_06_linked_list/6.2_doubly_ll": [
        ("P001", "Intro to Doubly LL", "EASY"),
        ("P002", "Insert Node in Doubly LL", "EASY"),
        ("P003", "Delete Node in Doubly LL", "EASY"),
        ("P004", "Reverse a Doubly LL", "EASY"),
    ],
    "step_06_linked_list/6.3_medium_problems": [
        ("P001", "Middle of Linked List", "EASY"),
        ("P002", "Reverse a Linked List", "EASY"),
        ("P003", "Detect Loop in LL", "EASY"),
        ("P004", "Find Starting Point of Loop", "MEDIUM"),
        ("P005", "Length of Loop in LL", "MEDIUM"),
        ("P006", "Check if LL is Palindrome", "MEDIUM"),
        ("P007", "Segregate Odd Even Nodes", "MEDIUM"),
        ("P008", "Remove Nth Node from Back", "MEDIUM"),
        ("P009", "Delete Middle Node", "MEDIUM"),
        ("P010", "Sort Linked List", "MEDIUM"),
        ("P011", "Sort LL of 0s 1s 2s", "MEDIUM"),
        ("P012", "Intersection of Two LL", "MEDIUM"),
        ("P013", "Add One to LL Number", "MEDIUM"),
        ("P014", "Add Two Numbers in LL", "MEDIUM"),
    ],
    "step_06_linked_list/6.4_hard_problems": [
        ("P001", "Reverse Nodes in K Groups", "HARD"),
        ("P002", "Rotate a Linked List", "MEDIUM"),
        ("P003", "Flattening a Linked List", "MEDIUM"),
        ("P004", "Clone LL with Random Pointer", "HARD"),
        ("P005", "Design Browser History", "MEDIUM"),
        ("P006", "Merge K Sorted Lists", "HARD"),
    ],

    # ── STEP 7: RECURSION & BACKTRACKING ────────────────────
    "step_07_recursion_backtracking/7.1_strong_hold": [
        ("P001", "Recursive Implementation of atoi", "MEDIUM"),
        ("P002", "Power Function using Recursion", "MEDIUM"),
        ("P003", "Count Good Numbers", "MEDIUM"),
        ("P004", "Sort a Stack using Recursion", "MEDIUM"),
        ("P005", "Reverse a Stack using Recursion", "MEDIUM"),
        ("P006", "Generate All Binary Strings", "MEDIUM"),
    ],
    "step_07_recursion_backtracking/7.2_subsequences": [
        ("P001", "Generate All Subsequences", "MEDIUM"),
        ("P002", "Generate Parentheses", "MEDIUM"),
        ("P003", "Print All Subsequences with Sum K", "MEDIUM"),
        ("P004", "Combination Sum", "MEDIUM"),
        ("P005", "Combination Sum II", "MEDIUM"),
        ("P006", "Subset Sum I", "MEDIUM"),
        ("P007", "Subset Sum II", "MEDIUM"),
        ("P008", "Combination Sum III", "MEDIUM"),
        ("P009", "Letter Combinations of Phone Number", "MEDIUM"),
    ],
    "step_07_recursion_backtracking/7.3_hard_problems": [
        ("P001", "Palindrome Partitioning", "HARD"),
        ("P002", "Word Search", "MEDIUM"),
        ("P003", "N Queen Problem", "HARD"),
        ("P004", "Rat in a Maze", "HARD"),
        ("P005", "Word Break", "MEDIUM"),
        ("P006", "M Coloring Problem", "HARD"),
        ("P007", "Sudoku Solver", "HARD"),
        ("P008", "Expression Add Operators", "HARD"),
    ],

    # ── STEP 8: BIT MANIPULATION ────────────────────────────
    "step_08_bit_manipulation/8.1_learn_bits": [
        ("P001", "Introduction to Bit Manipulation", "EASY"),
        ("P002", "Check if ith Bit is Set", "EASY"),
        ("P003", "Set Clear Toggle ith Bit", "EASY"),
        ("P004", "Remove Last Set Bit", "EASY"),
        ("P005", "Check if Number is Power of 2", "EASY"),
        ("P006", "Count Set Bits", "EASY"),
        ("P007", "Set the Rightmost Unset Bit", "EASY"),
        ("P008", "Swap Two Numbers", "EASY"),
        ("P009", "Divide Two Integers without Operators", "MEDIUM"),
    ],
    "step_08_bit_manipulation/8.2_interview_problems": [
        ("P001", "Count Bits from 1 to N", "MEDIUM"),
        ("P002", "Power Set using Bits", "MEDIUM"),
        ("P003", "Find Single Number Among Doubles", "EASY"),
        ("P004", "Find Two Numbers Appearing Once", "MEDIUM"),
        ("P005", "XOR of Numbers in a Range", "MEDIUM"),
    ],
    "step_08_bit_manipulation/8.3_advanced_maths": [
        ("P001", "Print Prime Factors", "EASY"),
        ("P002", "All Divisors of a Number", "EASY"),
        ("P003", "Sieve of Eratosthenes", "MEDIUM"),
        ("P004", "Find Prime Factorization using Sieve", "MEDIUM"),
    ],

    # ── STEP 9: STACK & QUEUES ──────────────────────────────
    "step_09_stack_queues/9.1_learning": [
        ("P001", "Implement Stack using Arrays", "EASY"),
        ("P002", "Implement Queue using Arrays", "EASY"),
        ("P003", "Implement Stack using Linked List", "EASY"),
        ("P004", "Implement Queue using Linked List", "EASY"),
        ("P005", "Implement Stack using Queue", "EASY"),
        ("P006", "Implement Queue using Stack", "EASY"),
        ("P007", "Valid Parentheses", "EASY"),
        ("P008", "Implement Min Stack", "MEDIUM"),
    ],
    "step_09_stack_queues/9.2_prefix_infix_postfix": [
        ("P001", "Infix to Postfix", "MEDIUM"),
        ("P002", "Infix to Prefix", "MEDIUM"),
        ("P003", "Postfix to Infix", "MEDIUM"),
        ("P004", "Postfix to Prefix", "MEDIUM"),
        ("P005", "Prefix to Infix", "MEDIUM"),
        ("P006", "Prefix to Postfix", "MEDIUM"),
    ],
    "step_09_stack_queues/9.3_monotonic_stack_queue": [
        ("P001", "Next Greater Element I", "MEDIUM"),
        ("P002", "Next Greater Element II", "MEDIUM"),
        ("P003", "Next Smaller Element", "MEDIUM"),
        ("P004", "Number of NGEs to Right", "MEDIUM"),
        ("P005", "Trapping Rain Water", "HARD"),
        ("P006", "Sum of Subarray Minimums", "MEDIUM"),
        ("P007", "Stock Span Problem", "MEDIUM"),
        ("P008", "Asteroid Collision", "MEDIUM"),
        ("P009", "Sum of Subarray Ranges", "MEDIUM"),
        ("P010", "Remove K Digits", "MEDIUM"),
        ("P011", "Largest Rectangle in Histogram", "HARD"),
        ("P012", "Maximal Rectangle", "HARD"),
    ],
    "step_09_stack_queues/9.4_implementation": [
        ("P001", "Sliding Window Maximum", "HARD"),
        ("P002", "Celebrity Problem", "MEDIUM"),
        ("P003", "LRU Cache", "HARD"),
        ("P004", "LFU Cache", "HARD"),
    ],

    # ── STEP 10: SLIDING WINDOW & TWO POINTER ───────────────
    "step_10_sliding_window/10.1_medium": [
        ("P001", "Longest Substring Without Repeating", "MEDIUM"),
        ("P002", "Max Points from Cards", "MEDIUM"),
        ("P003", "Longest Repeating Character Replacement", "MEDIUM"),
        ("P004", "Fruits into Baskets", "MEDIUM"),
        ("P005", "Number of Nice Subarrays", "MEDIUM"),
        ("P006", "Max Consecutive Ones III", "MEDIUM"),
    ],
    "step_10_sliding_window/10.2_hard": [
        ("P001", "Longest Substring with At Most K Distinct", "HARD"),
        ("P002", "Subarray with K Different Integers", "HARD"),
        ("P003", "Minimum Window Substring", "HARD"),
        ("P004", "Minimum Window Subsequence", "HARD"),
        ("P005", "Count Number of Nice Subarrays", "HARD"),
        ("P006", "Count Subarrays Where Max Appears K Times", "HARD"),
    ],

    # ── STEP 11: HEAPS ──────────────────────────────────────
    "step_11_heaps/11.1_learning": [
        ("P001", "Introduction to Heaps", "EASY"),
        ("P002", "Min Heap Implementation", "MEDIUM"),
        ("P003", "Max Heap Implementation", "MEDIUM"),
        ("P004", "Check if Array is Heap", "EASY"),
        ("P005", "Convert Min Heap to Max Heap", "EASY"),
    ],
    "step_11_heaps/11.2_medium": [
        ("P001", "Kth Largest Element", "MEDIUM"),
        ("P002", "Kth Smallest Element", "MEDIUM"),
        ("P003", "Sort a Nearly Sorted Array", "MEDIUM"),
        ("P004", "Merge K Sorted Arrays", "MEDIUM"),
        ("P005", "Replace Each Element by Rank", "MEDIUM"),
        ("P006", "Task Scheduler", "MEDIUM"),
        ("P007", "Hand of Straights", "MEDIUM"),
    ],
    "step_11_heaps/11.3_hard": [
        ("P001", "Design Twitter", "HARD"),
        ("P002", "Connect N Ropes with Minimum Cost", "MEDIUM"),
        ("P003", "Kth Largest Element in Stream", "EASY"),
        ("P004", "Maximum Sum Combination", "MEDIUM"),
        ("P005", "Find Median from Data Stream", "HARD"),
    ],

    # ── STEP 12: GREEDY ─────────────────────────────────────
    "step_12_greedy/12.1_easy": [
        ("P001", "Assign Cookies", "EASY"),
        ("P002", "Fractional Knapsack", "MEDIUM"),
        ("P003", "Greedy Algorithm to Find Minimum Coins", "MEDIUM"),
        ("P004", "Lemonade Change", "EASY"),
        ("P005", "Valid Parenthesis String", "MEDIUM"),
    ],
    "step_12_greedy/12.2_medium_hard": [
        ("P001", "N Meetings in One Room", "EASY"),
        ("P002", "Jump Game", "MEDIUM"),
        ("P003", "Jump Game II", "MEDIUM"),
        ("P004", "Minimum Platforms", "MEDIUM"),
        ("P005", "Job Sequencing Problem", "MEDIUM"),
        ("P006", "Candy Distribution", "HARD"),
        ("P007", "Shortest Job First", "EASY"),
        ("P008", "Page Faults in LRU", "MEDIUM"),
        ("P009", "Insert Interval", "MEDIUM"),
        ("P010", "Merge Intervals", "MEDIUM"),
        ("P011", "Non Overlapping Intervals", "MEDIUM"),
    ],

    # ── STEP 13: BINARY TREES ───────────────────────────────
    "step_13_binary_trees/13.1_traversals": [
        ("P001", "Introduction to Trees", "EASY"),
        ("P002", "Binary Tree Representation", "EASY"),
        ("P003", "Inorder Traversal", "EASY"),
        ("P004", "Preorder Traversal", "EASY"),
        ("P005", "Postorder Traversal", "EASY"),
        ("P006", "Level Order Traversal", "MEDIUM"),
        ("P007", "Iterative Preorder", "MEDIUM"),
        ("P008", "Iterative Inorder", "MEDIUM"),
        ("P009", "Iterative Postorder using 2 Stacks", "HARD"),
        ("P010", "Iterative Postorder using 1 Stack", "HARD"),
        ("P011", "All Traversals in One Pass", "HARD"),
    ],
    "step_13_binary_trees/13.2_medium": [
        ("P001", "Height of Binary Tree", "EASY"),
        ("P002", "Check if Tree is Balanced", "EASY"),
        ("P003", "Diameter of Binary Tree", "EASY"),
        ("P004", "Maximum Path Sum", "HARD"),
        ("P005", "Same Tree", "EASY"),
        ("P006", "Zig Zag Traversal", "MEDIUM"),
        ("P007", "Boundary Traversal", "MEDIUM"),
        ("P008", "Vertical Order Traversal", "HARD"),
        ("P009", "Top View of Binary Tree", "MEDIUM"),
        ("P010", "Bottom View of Binary Tree", "MEDIUM"),
        ("P011", "Right Side View", "MEDIUM"),
        ("P012", "Left Side View", "MEDIUM"),
        ("P013", "Symmetric Tree", "EASY"),
    ],
    "step_13_binary_trees/13.3_hard": [
        ("P001", "Root to Node Path", "MEDIUM"),
        ("P002", "LCA of Binary Tree", "MEDIUM"),
        ("P003", "Max Width of Binary Tree", "MEDIUM"),
        ("P004", "Children Sum Property", "MEDIUM"),
        ("P005", "All Nodes at Distance K", "MEDIUM"),
        ("P006", "Time to Burn Binary Tree", "HARD"),
        ("P007", "Count Total Nodes in Complete BT", "MEDIUM"),
        ("P008", "Requirements for Unique BT", "MEDIUM"),
        ("P009", "Build BT from Inorder Preorder", "MEDIUM"),
        ("P010", "Build BT from Inorder Postorder", "MEDIUM"),
        ("P011", "Serialize and Deserialize BT", "HARD"),
        ("P012", "Morris Inorder Traversal", "HARD"),
        ("P013", "Morris Preorder Traversal", "HARD"),
        ("P014", "Flatten BT to Linked List", "MEDIUM"),
    ],

    # ── STEP 14: BST ────────────────────────────────────────
    "step_14_bst/14.1_concepts": [
        ("P001", "Search in BST", "EASY"),
        ("P002", "Ceil in BST", "MEDIUM"),
        ("P003", "Floor in BST", "MEDIUM"),
        ("P004", "Insert into BST", "MEDIUM"),
        ("P005", "Delete Node in BST", "MEDIUM"),
        ("P006", "Kth Smallest in BST", "MEDIUM"),
        ("P007", "Kth Largest in BST", "MEDIUM"),
        ("P008", "Check if Tree is BST", "MEDIUM"),
    ],
    "step_14_bst/14.2_practice": [
        ("P001", "LCA of BST", "MEDIUM"),
        ("P002", "Construct BST from Preorder", "MEDIUM"),
        ("P003", "Inorder Successor in BST", "MEDIUM"),
        ("P004", "BST Iterator", "MEDIUM"),
        ("P005", "Two Sum in BST", "EASY"),
        ("P006", "Recover BST", "HARD"),
        ("P007", "Largest BST in BT", "HARD"),
    ],

    # ── STEP 15: GRAPHS ─────────────────────────────────────
    "step_15_graphs/15.1_learning": [
        ("P001", "Graph Representation", "EASY"),
        ("P002", "Connected Components", "EASY"),
        ("P003", "BFS Traversal", "EASY"),
        ("P004", "DFS Traversal", "EASY"),
    ],
    "step_15_graphs/15.2_bfs_dfs": [
        ("P001", "Number of Provinces", "MEDIUM"),
        ("P002", "Number of Islands", "MEDIUM"),
        ("P003", "Flood Fill", "EASY"),
        ("P004", "Rotten Oranges", "MEDIUM"),
        ("P005", "Detect Cycle in Undirected BFS", "MEDIUM"),
        ("P006", "Detect Cycle in Undirected DFS", "MEDIUM"),
        ("P007", "01 Matrix", "MEDIUM"),
        ("P008", "Surrounded Regions", "MEDIUM"),
        ("P009", "Number of Enclaves", "MEDIUM"),
        ("P010", "Word Ladder I", "HARD"),
        ("P011", "Word Ladder II", "HARD"),
        ("P012", "Number of Distinct Islands", "MEDIUM"),
        ("P013", "Bipartite Graph BFS", "MEDIUM"),
        ("P014", "Bipartite Graph DFS", "MEDIUM"),
        ("P015", "Detect Cycle in Directed DFS", "MEDIUM"),
        ("P016", "Eventual Safe States", "MEDIUM"),
    ],
    "step_15_graphs/15.3_topo_sort": [
        ("P001", "Topological Sort DFS", "MEDIUM"),
        ("P002", "Topological Sort BFS Kahns", "MEDIUM"),
        ("P003", "Cycle Detection in Directed Kahns", "MEDIUM"),
        ("P004", "Course Schedule I", "MEDIUM"),
        ("P005", "Course Schedule II", "MEDIUM"),
        ("P006", "Find Eventual Safe States Topo", "MEDIUM"),
        ("P007", "Alien Dictionary", "HARD"),
    ],
    "step_15_graphs/15.4_shortest_path": [
        ("P001", "Shortest Path in Undirected Unit Weight", "MEDIUM"),
        ("P002", "Shortest Path in DAG", "MEDIUM"),
        ("P003", "Dijkstras Algorithm", "MEDIUM"),
        ("P004", "Shortest Distance in Binary Maze", "MEDIUM"),
        ("P005", "Path with Minimum Effort", "MEDIUM"),
        ("P006", "Cheapest Flights with K Stops", "MEDIUM"),
        ("P007", "Network Delay Time", "MEDIUM"),
        ("P008", "Number of Ways to Arrive at Destination", "MEDIUM"),
        ("P009", "Bellman Ford Algorithm", "MEDIUM"),
        ("P010", "Floyd Warshall Algorithm", "MEDIUM"),
        ("P011", "City with Smallest Neighbors at Threshold", "MEDIUM"),
    ],
    "step_15_graphs/15.5_mst_disjoint_set": [
        ("P001", "Minimum Spanning Tree Prims", "MEDIUM"),
        ("P002", "Disjoint Set Union Find", "MEDIUM"),
        ("P003", "Kruskals Algorithm", "MEDIUM"),
        ("P004", "Number of Operations to Make Network Connected", "MEDIUM"),
        ("P005", "Most Stones Removed", "MEDIUM"),
        ("P006", "Accounts Merge", "MEDIUM"),
        ("P007", "Number of Islands II Online", "HARD"),
        ("P008", "Making a Large Island", "HARD"),
        ("P009", "Swim in Rising Water", "HARD"),
    ],
    "step_15_graphs/15.6_other_algorithms": [
        ("P001", "Bridges in a Graph", "HARD"),
        ("P002", "Articulation Points", "HARD"),
        ("P003", "Kosaraju Algorithm SCC", "HARD"),
    ],

    # ── STEP 16: DYNAMIC PROGRAMMING ────────────────────────
    "step_16_dynamic_programming/16.1_introduction": [
        ("P001", "Introduction to DP", "EASY"),
        ("P002", "Climbing Stairs", "EASY"),
        ("P003", "Frog Jump", "EASY"),
        ("P004", "Frog Jump with K Distances", "MEDIUM"),
    ],
    "step_16_dynamic_programming/16.2_1d_dp": [
        ("P001", "Maximum Sum of Non Adjacent Elements", "MEDIUM"),
        ("P002", "House Robber", "MEDIUM"),
        ("P003", "House Robber II", "MEDIUM"),
    ],
    "step_16_dynamic_programming/16.3_2d_3d_dp_grids": [
        ("P001", "Ninjas Training", "MEDIUM"),
        ("P002", "Unique Paths", "MEDIUM"),
        ("P003", "Unique Paths II", "MEDIUM"),
        ("P004", "Minimum Path Sum", "MEDIUM"),
        ("P005", "Triangle Minimum Path Sum", "MEDIUM"),
        ("P006", "Minimum Falling Path Sum", "MEDIUM"),
        ("P007", "Cherry Pickup II", "HARD"),
    ],
    "step_16_dynamic_programming/16.4_dp_on_subsequences": [
        ("P001", "Subset Sum Equal to Target", "MEDIUM"),
        ("P002", "Partition Equal Subset Sum", "MEDIUM"),
        ("P003", "Partition into Two Subsets Min Diff", "HARD"),
        ("P004", "Count Subsets with Sum K", "MEDIUM"),
        ("P005", "Count Partitions with Given Difference", "MEDIUM"),
        ("P006", "01 Knapsack", "MEDIUM"),
        ("P007", "Unbounded Knapsack", "MEDIUM"),
        ("P008", "Rod Cutting Problem", "MEDIUM"),
        ("P009", "Coin Change Minimum Coins", "MEDIUM"),
        ("P010", "Coin Change II Count Ways", "MEDIUM"),
        ("P011", "Target Sum", "MEDIUM"),
    ],
    "step_16_dynamic_programming/16.5_dp_on_strings": [
        ("P001", "Longest Common Subsequence", "MEDIUM"),
        ("P002", "Print Longest Common Subsequence", "MEDIUM"),
        ("P003", "Longest Common Substring", "MEDIUM"),
        ("P004", "Shortest Common Supersequence", "HARD"),
        ("P005", "Distinct Subsequences", "HARD"),
        ("P006", "Edit Distance", "MEDIUM"),
        ("P007", "Wildcard Matching", "HARD"),
    ],
    "step_16_dynamic_programming/16.6_dp_on_stocks": [
        ("P001", "Best Time to Buy and Sell I", "EASY"),
        ("P002", "Best Time to Buy and Sell II", "MEDIUM"),
        ("P003", "Best Time to Buy and Sell III", "HARD"),
        ("P004", "Best Time to Buy and Sell IV", "HARD"),
        ("P005", "Best Time with Cooldown", "MEDIUM"),
        ("P006", "Best Time with Transaction Fee", "MEDIUM"),
    ],
    "step_16_dynamic_programming/16.7_dp_on_lis": [
        ("P001", "Longest Increasing Subsequence", "MEDIUM"),
        ("P002", "Print Longest Increasing Subsequence", "MEDIUM"),
        ("P003", "Longest Increasing Subsequence Binary Search", "MEDIUM"),
        ("P004", "Largest Divisible Subset", "MEDIUM"),
        ("P005", "Longest String Chain", "MEDIUM"),
        ("P006", "Longest Bitonic Subsequence", "MEDIUM"),
        ("P007", "Number of LIS", "MEDIUM"),
    ],
    "step_16_dynamic_programming/16.8_mcm_partition": [
        ("P001", "Matrix Chain Multiplication", "HARD"),
        ("P002", "Minimum Cost to Cut a Stick", "HARD"),
        ("P003", "Burst Balloons", "HARD"),
        ("P004", "Evaluate Boolean Expression to True", "HARD"),
        ("P005", "Palindrome Partitioning II", "HARD"),
        ("P006", "Partition Array for Maximum Sum", "MEDIUM"),
    ],
    "step_16_dynamic_programming/16.9_dp_on_squares": [
        ("P001", "Maximal Rectangle", "HARD"),
        ("P002", "Count Square Submatrices with All Ones", "MEDIUM"),
    ],
}


def slugify(title: str) -> str:
    """Convert title to folder-safe slug."""
    slug = title.lower()
    slug = re.sub(r'[^a-z0-9\s]', '', slug)
    slug = re.sub(r'\s+', '_', slug.strip())
    return slug


def get_xp(difficulty: str) -> int:
    return {"EASY": 10, "MEDIUM": 25, "HARD": 50}.get(difficulty, 10)


def create_problem(base_path: str, prob_id: str, title: str, difficulty: str, step_sub: str):
    """Create a single problem folder with all files."""
    folder_name = f"{prob_id}_{slugify(title)}"
    prob_dir = os.path.join(base_path, folder_name)
    os.makedirs(prob_dir, exist_ok=True)

    step_parts = step_sub.split("/")
    step_num = step_parts[0].split("_")[1]  # "01", "02", etc
    sub_num = step_parts[1].split("_")[0]   # "1.1", "2.1", etc

    xp = get_xp(difficulty)

    # README
    readme_content = f"""# {title}

> **Step {step_num}.{sub_num}** | **Difficulty:** {difficulty} | **XP:** {xp} | **Status:** UNSOLVED

## Problem Statement
TODO - Add problem description here

## Examples
| Input | Output | Explanation |
|-------|--------|-------------|
| TODO  | TODO   | TODO        |

## Constraints
- TODO

---

## Approach 1: Brute Force
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 2: Optimal
**Intuition:** TODO

**Steps:**
1. TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Approach 3: Best
**Intuition:** TODO

| Metric | Value |
|--------|-------|
| Time   | O(?)  |
| Space  | O(?)  |

---

## Real-World Use Case
TODO

## Interview Tips
- TODO
"""

    # Java
    java_content = f"""/**
 * Problem: {title}
 * Difficulty: {difficulty} | XP: {xp}
 *
 * @author DSA_Nova
 */
public class Solution {{

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(?)  |  Space: O(?)
    // ============================================================
    public static void bruteForce() {{
        // TODO: implement
    }}

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(?)  |  Space: O(?)
    // ============================================================
    public static void optimal() {{
        // TODO: implement
    }}

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(?)  |  Space: O(?)
    // ============================================================
    public static void best() {{
        // TODO: implement
    }}

    public static void main(String[] args) {{
        System.out.println("=== {title} ===");
        bruteForce();
        optimal();
        best();
    }}
}}
"""

    # Python
    python_content = f'''"""
Problem: {title}
Difficulty: {difficulty} | XP: {xp}
"""
from typing import List, Optional


# ============================================================
# APPROACH 1: BRUTE FORCE
# Time: O(?)  |  Space: O(?)
# ============================================================
def brute_force():
    """TODO: implement"""
    pass


# ============================================================
# APPROACH 2: OPTIMAL
# Time: O(?)  |  Space: O(?)
# ============================================================
def optimal():
    """TODO: implement"""
    pass


# ============================================================
# APPROACH 3: BEST
# Time: O(?)  |  Space: O(?)
# ============================================================
def best():
    """TODO: implement"""
    pass


if __name__ == "__main__":
    print("=== {title} ===")
    print(f"Brute:   {{brute_force()}}")
    print(f"Optimal: {{optimal()}}")
    print(f"Best:    {{best()}}")
'''

    # meta.json
    meta = {
        "id": prob_id,
        "title": title,
        "step": step_num,
        "substep": sub_num,
        "difficulty": difficulty,
        "xp_reward": xp,
        "platforms": {"leetcode": "", "gfg": ""},
        "status": "UNSOLVED",
        "approaches_completed": {
            "brute_force": {"java": False, "python": False},
            "optimal": {"java": False, "python": False},
            "best": {"java": False, "python": False}
        },
        "date_started": None,
        "date_completed": None,
        "tags": []
    }

    # Write files
    with open(os.path.join(prob_dir, "README.md"), "w", encoding="utf-8") as f:
        f.write(readme_content)
    with open(os.path.join(prob_dir, "Solution.java"), "w", encoding="utf-8") as f:
        f.write(java_content)
    with open(os.path.join(prob_dir, "solution.py"), "w", encoding="utf-8") as f:
        f.write(python_content)
    with open(os.path.join(prob_dir, "meta.json"), "w", encoding="utf-8") as f:
        json.dump(meta, f, indent=2)


def main():
    total = 0
    for step_sub, problems in PROBLEMS.items():
        base_path = os.path.join(BASE_DIR, step_sub)
        os.makedirs(base_path, exist_ok=True)
        for prob_id, title, difficulty in problems:
            create_problem(base_path, prob_id, title, difficulty, step_sub)
            total += 1
        print(f"  Created {len(problems):3d} problems in {step_sub}")

    print(f"\n  TOTAL: {total} problems scaffolded!")
    print("  Run 'python scripts/update_progress.py' to initialize progress tracking.")


if __name__ == "__main__":
    main()
