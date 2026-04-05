"""
Problem: Design Twitter
Difficulty: HARD | XP: 50

Design a simplified Twitter with:
- post_tweet(userId, tweetId)
- get_news_feed(userId) -> 10 most recent tweets from user + followees
- follow(followerId, followeeId)
- unfollow(followerId, followeeId)
Real-life use: Social media feeds, news aggregation, event streaming.
"""
from typing import List
from collections import defaultdict
import heapq


# ============================================================
# APPROACH 1: BRUTE FORCE
# Global tweet list; filter + sort on every getNewsFeed call.
# Time: postTweet O(1), getNewsFeed O(N log N)  |  Space: O(N)
# ============================================================
class TwitterBrute:
    def __init__(self) -> None:
        self.time = 0
        self.tweets: List = []  # (time, userId, tweetId)
        self.following: dict = defaultdict(set)

    def post_tweet(self, user_id: int, tweet_id: int) -> None:
        self.tweets.append((self.time, user_id, tweet_id))
        self.time += 1

    def get_news_feed(self, user_id: int) -> List[int]:
        users = self.following[user_id] | {user_id}
        relevant = sorted(
            [(t, tid) for t, uid, tid in self.tweets if uid in users],
            reverse=True
        )
        return [tid for _, tid in relevant[:10]]

    def follow(self, follower_id: int, followee_id: int) -> None:
        self.following[follower_id].add(followee_id)

    def unfollow(self, follower_id: int, followee_id: int) -> None:
        self.following[follower_id].discard(followee_id)


# ============================================================
# APPROACH 2: OPTIMAL
# Per-user tweet lists + heap merge (K-way merge).
# Time: postTweet O(1), getNewsFeed O(K log K)  |  Space: O(total tweets)
# ============================================================
class TwitterOptimal:
    def __init__(self) -> None:
        self.time = 0
        self.user_tweets: dict = defaultdict(list)  # userId -> [(time, tweetId)]
        self.following: dict = defaultdict(set)

    def post_tweet(self, user_id: int, tweet_id: int) -> None:
        self.user_tweets[user_id].append((self.time, tweet_id))
        self.time += 1

    def get_news_feed(self, user_id: int) -> List[int]:
        users = self.following[user_id] | {user_id}
        # max-heap via negative time
        heap = []
        for uid in users:
            tweets = self.user_tweets[uid]
            if tweets:
                idx = len(tweets) - 1
                t, tid = tweets[idx]
                heapq.heappush(heap, (-t, tid, uid, idx))

        result = []
        while heap and len(result) < 10:
            neg_t, tid, uid, idx = heapq.heappop(heap)
            result.append(tid)
            if idx > 0:
                idx -= 1
                t, tid2 = self.user_tweets[uid][idx]
                heapq.heappush(heap, (-t, tid2, uid, idx))
        return result

    def follow(self, follower_id: int, followee_id: int) -> None:
        self.following[follower_id].add(followee_id)

    def unfollow(self, follower_id: int, followee_id: int) -> None:
        self.following[follower_id].discard(followee_id)


# ============================================================
# APPROACH 3: BEST
# Same heap-merge but user tweets stored as linked list for O(1) prepend.
# Most space-efficient for the feed traversal.
# Time: postTweet O(1), getNewsFeed O(K log K)  |  Space: O(tweets)
# ============================================================
class Twitter:
    def __init__(self) -> None:
        self._time = 0
        self._heads: dict = {}          # userId -> (time, tweetId, next)
        self._following: dict = defaultdict(set)

    def post_tweet(self, user_id: int, tweet_id: int) -> None:
        self._heads[user_id] = (self._time, tweet_id, self._heads.get(user_id))
        self._time += 1

    def get_news_feed(self, user_id: int) -> List[int]:
        users = self._following[user_id] | {user_id}
        heap = []
        for uid in users:
            node = self._heads.get(uid)
            if node:
                t, tid, nxt = node
                heapq.heappush(heap, (-t, tid, nxt))

        result = []
        while heap and len(result) < 10:
            neg_t, tid, nxt = heapq.heappop(heap)
            result.append(tid)
            if nxt:
                t2, tid2, nxt2 = nxt
                heapq.heappush(heap, (-t2, tid2, nxt2))
        return result

    def follow(self, follower_id: int, followee_id: int) -> None:
        self._following[follower_id].add(followee_id)

    def unfollow(self, follower_id: int, followee_id: int) -> None:
        self._following[follower_id].discard(followee_id)


if __name__ == "__main__":
    print("=== Design Twitter ===")

    print("\n--- Approach 2 (Optimal) ---")
    tw = TwitterOptimal()
    tw.post_tweet(1, 5)
    print("get_news_feed(1):", tw.get_news_feed(1))  # [5]
    tw.follow(1, 2)
    tw.post_tweet(2, 6)
    print("get_news_feed(1):", tw.get_news_feed(1))  # [6, 5]
    tw.unfollow(1, 2)
    print("get_news_feed(1):", tw.get_news_feed(1))  # [5]

    print("\n--- Approach 3 (Best) ---")
    t3 = Twitter()
    for tid in [5, 3, 101, 13, 10, 7, 2, 9, 4, 8, 11]:
        t3.post_tweet(1, tid)
    t3.post_tweet(2, 99)
    print("get_news_feed(1):", t3.get_news_feed(1))
    t3.follow(1, 2)
    print("after follow(1,2):", t3.get_news_feed(1))
