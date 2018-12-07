package dang.note.spring.boot.bootresource.module.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@CacheConfig(cacheNames = "users")
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Cacheable(key = "#id")
    public User getById(Long id) {
        User user = userDao.getOne(id);
        log.debug("getById  User:{}", user);
        return user;
    }

    // 清除操作默认是在对应方法成功执行之后触发的，即方法如果因为抛出异常而未能成功返回时也不会触发清除操作。使用beforeInvocation可以改变触发清除操作的时间，当我们指定该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素。
    @CacheEvict(key = "#userId", beforeInvocation = true)
    public void delete(Long userId) {
        userDao.deleteById(userId);
    }
}


