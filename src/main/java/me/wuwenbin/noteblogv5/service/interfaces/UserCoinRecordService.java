package me.wuwenbin.noteblogv5.service.interfaces;

import com.baomidou.mybatisplus.extension.service.IService;
import me.wuwenbin.noteblogv5.model.entity.UserCoinRecord;

/**
 * @author wuwen
 */
public interface UserCoinRecordService extends IService<UserCoinRecord> {

    /**
     * 计算管理员插入的硬币，并插入一条记录
     *
     * @param targetCoinValue
     * @param userId
     */
    void calcAdminInsertRecord(int targetCoinValue, long userId);
}
