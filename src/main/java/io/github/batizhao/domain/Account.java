package io.github.batizhao.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Entity
@Data
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String email;

    private String username;

    private String name;

    private String password;

    private String roles;

    private Date time;

}
