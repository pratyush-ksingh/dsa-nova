"""
DSA Nova - Real World Use Cases Database

Maps every algorithm/data structure to real-world applications.
Used by brain.py and play.py to show "why this matters" for each problem.
"""

REAL_WORLD = {
    # Data Structures
    "arrays": {
        "name": "Arrays",
        "uses": [
            "Spotify Daily Mix: Track IDs stored as arrays, shuffled with Fisher-Yates",
            "Image Processing: Every JPEG/PNG is a 2D array of RGB pixels - Instagram filters iterate this",
            "Stock Ticker: Bloomberg terminal stores real-time prices in circular array buffers",
        ],
        "interview_cue": "When you see 'contiguous elements', 'subarray', or 'in-place' -> think Arrays",
    },
    "hashing": {
        "name": "HashMap / HashTable",
        "uses": [
            "Browser DNS Cache: domain->IP mapping (google.com -> 142.250.80.46) for O(1) lookups",
            "Every JSON API: Response deserialization creates hash maps - the backbone of web data",
            "Spotify Playlist Dedup: Hash set ensures no song repeats in generated playlists, O(1) per check",
        ],
        "interview_cue": "When you need O(1) lookup/insert, or 'find pair/count occurrences' -> think HashMap",
    },
    "strings": {
        "name": "Strings",
        "uses": [
            "Search Engines: Google processes billions of string queries daily with pattern matching",
            "DNA Sequencing: Bioinformatics matches gene sequences (strings of ACGT) against 3B-char genomes",
            "Compilers: Tokenizers break source code strings into meaningful tokens for parsing",
        ],
        "interview_cue": "When problem involves characters, substrings, or pattern matching -> think Strings",
    },
    "sorting": {
        "name": "Sorting Algorithms",
        "uses": [
            "Database ORDER BY: Every SQL query with ORDER BY runs a sorting algorithm internally",
            "E-commerce: Amazon sorts products by price/rating/relevance - different sorts for different needs",
            "OS Process Scheduling: Priority-based sorting decides which process gets CPU time next",
        ],
        "interview_cue": "When unsorted data needs ordering, or as preprocessing for binary search/two-pointers",
    },
    "binary-search": {
        "name": "Binary Search",
        "uses": [
            "Git Bisect: Finds the exact breaking commit among 1000s in ~10 steps using binary search",
            "Dictionary Lookup: You don't read page 1-2000 to find 'quantum' - you open near Q and narrow down",
            "Amazon Price Filter: Backend runs binary search on price-indexed data for range $20-$50",
        ],
        "interview_cue": "When you see 'sorted array', 'minimize maximum', or 'find boundary' -> think Binary Search",
    },
    "linked-list": {
        "name": "Linked List",
        "uses": [
            "Browser History: Back/Forward navigation is a doubly-linked list of visited URLs",
            "Music Player Playlist: Previous/Next song navigation with O(1) insert/delete",
            "Memory Allocators: malloc/free manages free memory blocks as a linked list in your OS",
        ],
        "interview_cue": "When you see 'insert/delete in middle O(1)', 'reverse', 'detect cycle' -> think LL",
    },
    "stack": {
        "name": "Stack",
        "uses": [
            "Browser Back Button: Every URL pushed on stack; Back = pop; Forward = second stack",
            "Ctrl+Z Undo: Text editors maintain action stack; undo = pop; redo = push to redo stack",
            "Compiler Parenthesis Check: Your IDE uses a stack to match every ( with ), { with }",
        ],
        "interview_cue": "When you see 'matching brackets', 'next greater', 'undo/redo' -> think Stack",
    },
    "queues": {
        "name": "Queue",
        "uses": [
            "Print Queue: Documents sent to printer processed FIFO - OS maintains actual Queue",
            "Customer Support: Zendesk/Freshdesk processes tickets in queue order (FIFO with priority)",
            "CPU Scheduling: Round Robin scheduling uses a queue of processes waiting for CPU time",
        ],
        "interview_cue": "When you see FIFO, BFS, or 'process in order' -> think Queue",
    },
    "recursion": {
        "name": "Recursion & Backtracking",
        "uses": [
            "File System: 'du -sh' does DFS recursion through directory tree to calculate folder sizes",
            "Sudoku Solvers: Apps like Sudoku.com use recursive backtracking to solve/generate puzzles",
            "Compiler Parse Trees: Recursive descent parsers build ASTs for your code, function by function",
        ],
        "interview_cue": "When problem has 'subproblems of same structure' or 'try all possibilities' -> think Recursion",
    },
    "trees": {
        "name": "Binary Trees",
        "uses": [
            "File System Hierarchy: Your entire drive is a tree - folders are nodes, files are leaves",
            "HTML DOM: Every webpage is a tree - browser renders it by tree traversal (React virtual DOM too)",
            "Decision Trees in ML: Random Forests are literally forests of binary decision trees",
        ],
        "interview_cue": "When you see hierarchical data, parent-child, or 'divide at each level' -> think Trees",
    },
    "bst": {
        "name": "Binary Search Tree",
        "uses": [
            "Database Indexes: B-Trees (BST variant) power MySQL/PostgreSQL indexing for fast lookups",
            "File System Directories: Many OS file systems use balanced BSTs for directory lookups",
            "In-Memory Caches: TreeMap in Java, std::map in C++ are BST-backed sorted data structures",
        ],
        "interview_cue": "When you need sorted dynamic data with O(log n) insert/search/delete -> think BST",
    },
    "heaps": {
        "name": "Heap / Priority Queue",
        "uses": [
            "ER Triage: Most critical patient seen first - this IS a max-heap on severity score",
            "Dijkstra/A* Pathfinding: Google Maps uses min-heap to decide 'which intersection to explore next'",
            "Twitter Trending: Top 10 hashtags from millions uses min-heap of size 10 - swap if count exceeds min",
        ],
        "interview_cue": "When you see 'top K', 'Kth largest/smallest', or 'merge K sorted' -> think Heap",
    },
    "greedy": {
        "name": "Greedy Algorithm",
        "uses": [
            "Huffman Encoding (ZIP): File compression assigns shorter codes to frequent chars - greedy choice",
            "Cashier Making Change: $3.93 change using fewest coins by always picking largest denomination first",
            "Meeting Room Scheduling: Google Calendar's 'find free slot' uses interval scheduling greedy",
        ],
        "interview_cue": "When 'locally optimal choice leads to global optimal' AND you can prove it -> think Greedy",
    },
    "graphs": {
        "name": "Graphs (BFS/DFS)",
        "uses": [
            "LinkedIn 'People You May Know': BFS from your profile - 1st, 2nd, 3rd connections = BFS levels",
            "Google Web Crawler: Googlebot discovers pages via BFS - seed URL -> all links -> their links",
            "Java Garbage Collector: DFS from root references marks all reachable objects; unreachable = garbage",
        ],
        "interview_cue": "When you see 'connected', 'reachable', 'shortest path (unweighted)', 'island' -> think Graph BFS/DFS",
    },
    "shortest-path": {
        "name": "Shortest Path Algorithms",
        "uses": [
            "Google Maps Directions: Dijkstra finds fastest route with weighted road distances/speeds",
            "Internet Routing (OSPF): Routers use Dijkstra variant to find lowest-latency packet path",
            "Airline Ticket Pricing: Airports as nodes, flights as weighted edges; shortest path = cheapest trip",
        ],
        "interview_cue": "When edges have weights and you need minimum cost/distance -> think Dijkstra/Bellman-Ford",
    },
    "topological-sort": {
        "name": "Topological Sort",
        "uses": [
            "Build Systems (Make/Webpack): 'Compile A before B because B depends on A' = topological sort",
            "Course Prerequisites: 'CS101 before CS201 before CS301' - valid order IS topological sort",
            "Excel Cell Evaluation: C1=A1+B1 means compute A1,B1 first - cell eval order = topo sort of DAG",
        ],
        "interview_cue": "When you see 'dependencies', 'ordering constraints', 'prerequisites' -> think Topo Sort",
    },
    "dp": {
        "name": "Dynamic Programming",
        "uses": [
            "Spell Checker: Your phone uses Edit Distance (Levenshtein) to suggest 'their' when you type 'thier'",
            "FedEx Package Loading: 'Max value in truck with weight limit' = 0/1 Knapsack solved with DP",
            "Netflix Recommendations: 'Maximize relevance with diversity constraints' = variant of knapsack DP",
        ],
        "interview_cue": "When you see 'optimal substructure' + 'overlapping subproblems' OR 'min/max ways' -> think DP",
    },
    "sliding-window": {
        "name": "Sliding Window / Two Pointers",
        "uses": [
            "Network Monitoring: 'Bytes transferred in last 5 seconds' = sliding window over time-series (Kafka/Flink)",
            "Stock Moving Average: '50-day moving average' = window of size 50 sliding across daily prices",
            "API Rate Limiting: '100 requests per 60s window' - Stripe/Cloudflare maintain sliding window counters",
        ],
        "interview_cue": "When you see 'subarray of size K', 'longest/shortest substring with condition' -> think Sliding Window",
    },
    "bit-manipulation": {
        "name": "Bit Manipulation",
        "uses": [
            "Feature Flags: 32 features in one int - each bit = a toggle. Check with bitwise AND",
            "Unix File Permissions: chmod 755 = rwxr-xr-x, each digit is 3 bits (r=4, w=2, x=1)",
            "IP Subnet Masking: Is 192.168.1.5 in subnet 192.168.1.0/24? Bitwise AND with mask 255.255.255.0",
        ],
        "interview_cue": "When you see 'power of 2', 'XOR', 'subsets', or 'optimize space to O(1)' -> think Bits",
    },
    "mst": {
        "name": "Minimum Spanning Tree",
        "uses": [
            "Telecom Cable Planning: AT&T connecting cities with minimum total cable = Kruskal's MST",
            "Circuit Board Design: Connecting chips with minimum wire length = MST problem",
            "Network Design: Minimum cost to connect all offices in a company's WAN = MST",
        ],
        "interview_cue": "When 'connect all nodes with minimum total edge weight' -> think Prim's/Kruskal's",
    },
}


def get_real_world_for_topic(topic):
    """Get real-world info for a topic. Handles aliases."""
    aliases = {
        # Graph variants
        "bfs": "graphs", "dfs": "graphs", "bipartite": "graphs",
        # Stack variants
        "monotonic-stack": "stack", "postfix": "stack",
        # Sliding window / two pointers
        "two-pointers": "sliding-window",
        # Recursion variants
        "backtracking": "recursion", "divide-conquer": "recursion",
        # Tree variants
        "trie": "trees", "segment-tree": "trees", "traversal": "trees",
        # Math variants
        "math": "arrays", "number-theory": "hashing",
        "combinatorics": "dp", "prime-numbers": "hashing",
        "gcd": "hashing", "permutation": "sorting",
        "base-conversion": "hashing", "catalan": "dp",
        "game-theory": "dp", "matrix-exponentiation": "dp",
        # Array variants
        "prefix-sum": "arrays", "kadane": "arrays",
        "prefix-suffix": "arrays", "matrix": "arrays",
        "interval": "greedy",
        # Sorting variants
        "bucket-sort": "sorting",
        # DP variants
        "knapsack": "dp",
        # String variants
        "palindrome": "strings", "kmp": "strings",
        "pattern-matching": "strings", "big-number": "strings",
        # Logic / basics
        "logic": "arrays",
    }
    key = aliases.get(topic, topic)
    return REAL_WORLD.get(key)


def get_real_world_for_problem(problem):
    """Get real-world use cases for a problem based on its tags or path."""
    tags = problem.get("tags", [])
    meta = {"interviewbit", "implementation", "simulation", "parsing"}
    meaningful = [t for t in tags if t not in meta]

    # If no meaningful tags, infer from folder path
    if not meaningful:
        path = problem.get("_path", "") or problem.get("path", "")
        PATH_TOPIC_MAP = {
            "step_01_basics": "math", "step_02_sorting": "sorting",
            "step_03_arrays": "arrays", "step_04_binary_search": "binary-search",
            "step_05_strings": "strings", "step_06_linked_list": "linked-list",
            "step_07_recursion": "recursion", "step_08_bit_manipulation": "bit-manipulation",
            "step_09_stack_queues": "stack", "step_10_sliding_window": "sliding-window",
            "step_11_heaps": "heaps", "step_12_greedy": "greedy",
            "step_13_binary_trees": "trees", "step_14_bst": "bst",
            "step_15_graphs": "graphs", "step_16_dynamic_programming": "dp",
        }
        for folder_key, topic in PATH_TOPIC_MAP.items():
            if folder_key in path:
                meaningful = [topic]
                break

    results = []
    seen = set()
    for tag in meaningful:
        info = get_real_world_for_topic(tag)
        if info and info["name"] not in seen:
            results.append(info)
            seen.add(info["name"])

    return results
