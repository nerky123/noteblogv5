package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.model.entity.User1;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * created by Wuwenbin on 2019-08-05 at 13:37
 *
 * @author wuwenbin
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 今日新增用户
     *
     * @return
     */
    long findTodayUser();

    /**
     * 统计普通注册用户role
     *
     * @param email
     * @param username
     * @return
     */
    int countRegUserEmailAndUsername(@Param("email") String email, @Param("username") String username);

    /**
     * 更新余额硬币
     *
     * @param userId
     * @param remainCoin
     */
    void updateRemainCoin(@Param("userId") long userId, @Param("remainCoin") int remainCoin);

    List<User1> selectByPage(Page<User1> page, @Param("username")String username, @Param("nickname")String nickname);
}
