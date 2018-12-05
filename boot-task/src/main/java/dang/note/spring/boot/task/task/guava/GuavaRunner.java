package dang.note.spring.boot.task.task.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Component
@Slf4j
public class GuavaRunner implements ApplicationRunner {
    // 定长线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(30);
    Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(10000).build();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> runOne());
        }
        runOne();
    }

    private void runOne() {
        int count = 10000;
        log.info("guava start set value count {}", count);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            cache.put("task:redis:" + i, "redis end set value count:{} time:{}ms" + i);
        }
        long end = System.currentTimeMillis();
        log.info("guava end set value count:{} time:{}ms", count, end - start);

        log.info("guava start get value count {}", count);
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            String value = cache.getIfPresent("task:redis:" + i);
        }
        end = System.currentTimeMillis();
        log.info("guava end get value count:{} time:{}ms", count, end - start);
    }
}
