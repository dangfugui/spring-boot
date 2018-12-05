package dang.note.spring.boot.task.task.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class LogRunner implements ApplicationRunner {
    // 定长线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(200);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> runOne());
        }
        runOne();
    }

    private void runOne() {
        int count = 1000;
        log.warn("begin test log count {}", count);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            log.info("test info log index:{}", i);
        }
        long end = System.currentTimeMillis();
        log.warn("end test log time: {}", end - start);

    }
}
