package dang.note.spring.boot.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DemoService {
    public String echo(String echo) {
        log.info("echo:{}", echo);
        return echo;
    }
}
