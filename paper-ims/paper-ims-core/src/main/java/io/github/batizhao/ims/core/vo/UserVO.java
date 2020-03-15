package io.github.batizhao.ims.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Data
@Accessors(chain = true)
public class UserVO {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String name;

    private Date time;
}
