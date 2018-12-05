package dang.note.spring.boot.api.controller;

import dang.note.spring.boot.api.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/api/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;
    
    @RequestMapping("/echo")
    public String echo(String echo) {
        String res = demoService.echo(echo);
        return res;
    }

    @RequestMapping("/log")
    public String log(String message) {
        log.debug(message);
        log.info(message);
        log.warn(message);
        log.error(message);
        return message;
    }
}
