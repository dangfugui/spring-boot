package dang.note.spring.boot.bootresource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "dang.note")
@EnableScheduling    // 启用定时任务
public class BootResourceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BootResourceApplication.class, args);
	}
}
