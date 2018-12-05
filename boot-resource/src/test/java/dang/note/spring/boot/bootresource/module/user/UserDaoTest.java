package dang.note.spring.boot.bootresource.module.user;

import dang.note.spring.boot.bootresource.base.IntegrationBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@Transactional
public class UserDaoTest extends IntegrationBaseTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void findByUserName() {
        User user = userDao.findByUserName("root");
        Assert.assertNotNull(user);
    }

    @Test
    public void updateRealNameById() {
        userDao.updateRealNameById(1L, "hehe");
        User user = userDao.getOne(1L);
        Assert.assertEquals("hehe", user.getRealName());
    }
}