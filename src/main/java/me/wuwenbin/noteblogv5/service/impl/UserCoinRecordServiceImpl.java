package me.wuwenbin.noteblogv5.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.wuwenbin.noteblogv5.constant.OperateType;
import me.wuwenbin.noteblogv5.mapper.UserCoinRecordMapper;
import me.wuwenbin.noteblogv5.mapper.UserMapper;
import me.wuwenbin.noteblogv5.model.entity.UserCoinRecord;
import me.wuwenbin.noteblogv5.service.interfaces.UserCoinRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author wuwen
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCoinRecordServiceImpl extends ServiceImpl<UserCoinRecordMapper, UserCoinRecord> implements UserCoinRecordService {

    private final UserCoinRecordMapper userCoinRecordMapper;
    private final UserMapper userMapper;

    public UserCoinRecordServiceImpl(UserCoinRecordMapper userCoinRecordMapper, UserMapper userMapper) {
        this.userCoinRecordMapper = userCoinRecordMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void calcAdminInsertRecord(int targetCoinValue, long userId) {
        UserCoinRecord latestRecord = userCoinRecordMapper.findLatestRecordByUserId(userId);
        int latestRemainCoin = latestRecord.getRemainCoin();
        UserCoinRecord newLine = UserCoinRecord.builder()
                .operateTime(new Date())
                .userId(userId)
                .remainCoin(targetCoinValue)
                .build();
        if (targetCoinValue >= latestRemainCoin) {
            newLine.setOperateType(OperateType.ADMIN_DEDUCT_MINUS);
            newLine.setOperateValue(targetCoinValue - latestRemainCoin);
            newLine.setRemark(OperateType.ADMIN_DEDUCT_MINUS.getDesc());
        } else {
            newLine.setOperateType(OperateType.ADMIN_RECHARGE_ADD);
            newLine.setOperateValue(latestRemainCoin - targetCoinValue);
            newLine.setRemark(OperateType.ADMIN_RECHARGE_ADD.getDesc());
        }
        userCoinRecordMapper.insert(newLine);
        userMapper.updateRemainCoin(userId, targetCoinValue);
    }
}
