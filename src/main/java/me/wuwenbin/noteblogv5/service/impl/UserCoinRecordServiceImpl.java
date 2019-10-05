package me.wuwenbin.noteblogv5.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.wuwenbin.noteblogv5.constant.OperateType;
import me.wuwenbin.noteblogv5.mapper.ParamMapper;
import me.wuwenbin.noteblogv5.mapper.UserCoinRecordMapper;
import me.wuwenbin.noteblogv5.mapper.UserMapper;
import me.wuwenbin.noteblogv5.model.entity.Param;
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
    private final ParamMapper paramMapper;

    public UserCoinRecordServiceImpl(UserCoinRecordMapper userCoinRecordMapper,
                                     UserMapper userMapper, ParamMapper paramMapper) {
        this.userCoinRecordMapper = userCoinRecordMapper;
        this.userMapper = userMapper;
        this.paramMapper = paramMapper;
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
            newLine.setOperateType(OperateType.ADMIN_RECHARGE_ADD);
            newLine.setOperateValue(targetCoinValue - latestRemainCoin);
            newLine.setRemark(OperateType.ADMIN_RECHARGE_ADD.getDesc());
        } else {
            newLine.setOperateType(OperateType.ADMIN_DEDUCT_MINUS);
            newLine.setOperateValue(latestRemainCoin - targetCoinValue);
            newLine.setRemark(OperateType.ADMIN_DEDUCT_MINUS.getDesc());
        }
        userCoinRecordMapper.insert(newLine);
        userMapper.updateRemainCoin(userId, targetCoinValue);
    }

    @Override
    public int todayIsSigned(long userId) {
        return userCoinRecordMapper.todayIsSigned(userId, OperateType.SIGN_ADD);
    }

    @Override
    public boolean userSign(long userId) {
        UserCoinRecord userCoinRecord = userCoinRecordMapper.findLatestRecordByUserId(userId);
        int remainCoin = userCoinRecord.getRemainCoin();
        Param param = paramMapper.selectOne(Wrappers.<Param>query().eq("name", "sign_check_coin"));
        int val = Integer.parseInt(param.getValue());
        int remainCoinAfterSign = remainCoin + val;
        UserCoinRecord ucr = UserCoinRecord.builder()
                .operateTime(new Date()).remainCoin(remainCoinAfterSign).userId(userId).operateType(OperateType.SIGN_ADD)
                .operateValue(val).remark(OperateType.SIGN_ADD.getDesc()).build();
        int cnt = userCoinRecordMapper.insert(ucr);
        if (cnt == 1) {
            userMapper.updateRemainCoin(userId, remainCoinAfterSign);
        }
        return cnt == 1;
    }
}
