package dang.note.spring.boot.bootresource.module.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<User, Long> {
    // Spring Data JPA 为我们简化了很多查询SQL,直接通过findBy实体属性名即可执行相关查询，多个字段属性用And/Or连接
    User findByUserName(String userName);

    @Query("update User u set u.realName = ?2 where u.id = ?1")
    @Modifying
    int updateRealNameById(Long id, String realName);
}
