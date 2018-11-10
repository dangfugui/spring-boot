package dang.note.spring.boot.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dang.note.spring.boot.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/")
public class MeetingController {


    @RequestMapping("/")
    public String echo(String echo) throws InterruptedException {
//        Thread.sleep(5000);
        return "{\"code\":200,\"status\":1,\"data\":{\"message\":\"恭喜您！预定成功！\",\"sysTime\":1541489516735}}";
    }


    @PostMapping("/bot")
    public String bot(@RequestBody String jsonStr) throws IOException {
        log.info(jsonStr);
        JSONObject json = JSON.parseObject(jsonStr);
        String query = json.getJSONObject("request").getString("query");
        String session = json.getString("bot_session");
        if (query.contains("取消")) {
            return getCancel(session);
        } else if (query.contains("确定")) {
            return getConfirm(session);
        } else if (query.contains("预订")) {
            return getBook(session, query);
        }
        return "";
    }

    private String getConfirm(String session) throws IOException {

        InputStream in = new ClassPathResource("json/confirm.json").getInputStream();
        String botResultJson = FileUtil.read(in);
        JSONObject json = JSON.parseObject(botResultJson);
        return botResultJson;
    }


    private String getBook(String session, String query) throws IOException {
        if (session == null || session.length() < 5) {
            session = UUID.randomUUID().toString();
        }
        String roomName = query.substring(query.indexOf("的") + 1, query.indexOf("会议"));
        InputStream in = new ClassPathResource("json/book.json").getInputStream();
        String botResultJson = FileUtil.read(in);
        JSONObject json = JSON.parseObject(botResultJson);
        json.getJSONObject("result").getJSONObject("response").getJSONObject("schema").getJSONArray("slots").getJSONObject(0).put("original_word", roomName);
        json.getJSONObject("result").getJSONObject("response").getJSONObject("schema").getJSONArray("slots").getJSONObject(0).put("normalized_word", roomName);
        botResultJson = botResultJson.replaceAll("##userName", roomName);
        return botResultJson;
    }

    private String getCancel(String session) throws IOException {
        InputStream in = new ClassPathResource("json/cancel.json").getInputStream();
        String botResultJson = FileUtil.read(in);
        return botResultJson;
    }
}