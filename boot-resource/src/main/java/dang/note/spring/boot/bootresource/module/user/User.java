package dang.note.spring.boot.bootresource.module.user;

import lombok.Data;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@ToString
public class User {

    @Id
    private Long id;    // ID自增长主键

    @Column(nullable = false, unique = true)
    private String userName;    // 用户名,不为空，不能重复

    @Column
    private String realName;    // 真实姓名

    @Column(nullable = false)
    private String password;    // 密码,不为空

    @Column
    private Boolean gender;     // 性别

}
