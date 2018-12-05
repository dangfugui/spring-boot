package dang.note.spring.boot.task;

import org.junit.Test;

import java.util.Calendar;

public class MyTest {
    @Test
    public void testd() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
    }
}
