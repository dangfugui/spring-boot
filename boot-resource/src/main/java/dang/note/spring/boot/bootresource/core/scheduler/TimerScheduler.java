package dang.note.spring.boot.bootresource.core.scheduler;


import dang.note.spring.boot.bootresource.module.user.User;
import dang.note.spring.boot.bootresource.module.user.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
@Transactional
public class TimerScheduler {

    @Autowired
    private UserDao userDao;

    private long count = 0;

    /**
     * 心跳检测 每10s 打印一次log
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void health() {
        User user = userDao.getOne(1L);
        log.info("count:{} user:{}", count++, user.toString());
    }
}
