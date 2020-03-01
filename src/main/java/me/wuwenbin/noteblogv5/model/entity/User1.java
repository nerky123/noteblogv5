package me.wuwenbin.noteblogv5.model.entity;

import lombok.Builder;
import lombok.Data;
import me.wuwenbin.noteblogv5.constant.RoleEnum;

import java.util.Date;

/**
 * @author yangxw
 * @describition
 * @create 2020-01-20 16:37
 */
@Data
public class User1 {
    private Long id;
    private RoleEnum role;
    private String avatar;
    private Date createDate;
    private String email;
    @Builder.Default
    private Boolean enable = true;
    private String nickname;
    private String password;
    private String openId;
    private String username;
    private Integer remainCoin;
    private Integer vipCount;
    private Integer vipState;
    private Date vipTime;
}
