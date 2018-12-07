package dang.note.spring.boot.bootresource.core.scheduler;


import com.alibaba.fastjson.JSON;
import dang.note.spring.boot.bootresource.module.user.User;
import dang.note.spring.boot.bootresource.module.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Transactional
public class TimerScheduler {

    @Autowired
    private UserService userService;
    @Resource
    private RedisTemplate<String, User> redisTemplate;

    /**
     * 心跳检测 每10s 打印一次log
     */
//    @Scheduled(cron = "*/10 * * * * ?")  秒数能被10整除
//    @Scheduled(fixedDelay =  10 * 1000)  10s + run time
    @Scheduled(fixedRate = 10 * 1000)
    public void health() {
        User user = userService.getById(1L);
        log.info("count:{} DBUser:{}", count++, user.toString());
        redisTemplate.opsForValue().set("redis:user", user, 1, TimeUnit.HOURS);
        user = redisTemplate.opsForValue().get("redis:user");
        log.info("count:{} RedisUser:{}", count++, user);
    }

    private long count = 0;
}
