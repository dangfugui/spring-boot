package dang.note.spring.boot.task.task.redis;

import dang.note.spring.boot.task.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Component
@Slf4j
public class RedisRunner implements ApplicationRunner {
    @Autowired
    private StringRedisTemplate redisTemplate;
    // 定长线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(30);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> runOne());
        }
        runOne();
    }

    private void runOne() {
        int count = 10000;
        long start = System.currentTimeMillis();
        log.info("redis start set value count {}", count);
        for (int i = 0; i < count; i++) {
            redisTemplate.opsForValue().set("task:redis:" + i, "redis end set value count:{} time:{}ms" + i);
        }
        long end = System.currentTimeMillis();
        log.info("redis end set value count:{} time:{}ms", count, end - start);

        start = System.currentTimeMillis();
        log.info("redis start get value count {}", count);
        for (int i = 0; i < count; i++) {
            String value = redisTemplate.opsForValue().get("task:redis:" + i);
        }
        end = System.currentTimeMillis();
        log.info("redis end get value count:{} time:{}ms", count, end - start);
    }
}
