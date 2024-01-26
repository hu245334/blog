package org.example.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        // 在redis中存储键值对
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set("username", "Zhang San");
        valueOperations.set("id", "1", 15, TimeUnit.SECONDS);
    }

    @Test
    public void testGet() {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        System.out.println(valueOperations.get("username"));
    }
}
