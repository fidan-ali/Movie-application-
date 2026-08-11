package com.example.movies.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentlyViewedService {

    private static final String KEY_PREFIX = "recent_movies:";
    private static final int MAX_RECENT = 10;

    private final RedissonClient redissonClient;

    public void recordView(Long userId, Long movieId) {
        RList<String> list = redissonClient.getList(buildKey(userId));
        String movieIdStr = movieId.toString();

        list.remove(movieIdStr);
        list.add(0, movieIdStr);
        list.trim(0, MAX_RECENT - 1);
    }

    public List<Long> getRecentMovieIds(Long userId) {
        RList<String> list = redissonClient.getList(buildKey(userId));
        List<String> ids = list.range(0, MAX_RECENT - 1);

        return ids.stream()
                .map(Long::parseLong)
                .toList();
    }

    private String buildKey(Long userId) {
        return KEY_PREFIX + userId;
    }
}