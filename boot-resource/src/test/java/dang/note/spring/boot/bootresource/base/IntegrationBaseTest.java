package dang.note.spring.boot.bootresource.base;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.util.OS;

import java.io.IOException;

/**
 * spring集成测试的父类，构建wiremock及内存数据库.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public abstract class IntegrationBaseTest {

    private static RedisServer redisServer;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
//        redisServer = new RedisServer(6379);
//        redisServer.start();
//        System.setProperty("scheduling.enable", "false");
    }

    @AfterClass
    public static void tearDownAfterClass() {
//        redisServer.stop();
    }
}
