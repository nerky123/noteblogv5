package me.wuwenbin.noteblogv5.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.wuwenbin.noteblogv5.annotation.Mapper;
import me.wuwenbin.noteblogv5.model.entity.UserCoinRecord;
import org.apache.ibatis.annotations.Param;

/**
 * @author wuwen
 */
@Mapper
public interface UserCoinRecordMapper extends BaseMapper<UserCoinRecord> {

    /**
     * 找最新的一条
     *
     * @param userId
     * @return
     */
    UserCoinRecord findLatestRecordByUserId(@Param("userId") long userId);

}
