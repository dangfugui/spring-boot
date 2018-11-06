package dang.note.spring.boot.api.service;

import org.springframework.stereotype.Service;

@Service
public class DemoService {
    public String echo(String echo) {
//        log.info("echo:{}",echo);
        return echo;
    }
}
