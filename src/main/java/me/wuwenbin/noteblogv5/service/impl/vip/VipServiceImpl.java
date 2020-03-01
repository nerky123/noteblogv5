package me.wuwenbin.noteblogv5.service.impl.vip;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.OperateType;
import me.wuwenbin.noteblogv5.mapper.VipMapper;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.model.entity.UserCoinRecord;
import me.wuwenbin.noteblogv5.model.entity.Vip;
import me.wuwenbin.noteblogv5.service.interfaces.UserCoinRecordService;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import me.wuwenbin.noteblogv5.service.interfaces.vip.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;
import java.util.Date;

/**
 * @author yangxw
 * @describition
 * @create 2020-01-19 20:57
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class VipServiceImpl implements VipService {
    public static final Integer BUY_VIP_MINUS_COIN = 50;
    @Autowired
    private UserCoinRecordService userCoinRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private VipMapper mapper;

    @Override
    public int insert(Vip vip) {
        int i = mapper.insertSelective(vip);
        return i;
    }

    @Override
    public Vip selectByUserId(Long userId) {
        Vip vip= mapper.selectByUserId(userId.longValue());
        return vip;
    }

    @Override
    public int update(Vip vip) {
        return mapper.updateByPrimaryKeySelective(vip);
    }

    @Override
    public String insertByCoin(HttpServletRequest request, int type,User purchaseUser) {
        long userId = purchaseUser.getId();
        //先查看是否有足够的硬币
        int remainCoin = purchaseUser.getRemainCoin();
        if (remainCoin >= BUY_VIP_MINUS_COIN) {
            //如果有足够的硬币，减去对应的硬币
            boolean r = userService.update(Wrappers.<User>update().set("remain_coin", remainCoin - BUY_VIP_MINUS_COIN).eq("id", userId));
            if (r) {
                UserCoinRecord record = UserCoinRecord.builder()
                        .operateTime(new Date()).remainCoin(remainCoin - BUY_VIP_MINUS_COIN).userId(userId).operateType(OperateType.PURCHASE_MINUS)
                        .operateValue(BUY_VIP_MINUS_COIN).remark(OperateType.PURCHASE_MINUS.getDesc()).build();
                //更新数据库最新的硬币数量
                boolean save = userCoinRecordService.save(record);
                if (save){
                    //插入会员信息
                    Vip vip = new Vip();
                    vip.setUserId(userId);
                    vip.setSource(type);
                    vip.setState(1);
                    if (insert(vip) == 1){
                        return "{\"code\":\"0\",\"message\":\"充值成功！\"}";
                    }
                    throw new RuntimeException("充值失败，请联系管理员！");
                }else{
                    throw new RuntimeException("充值失败，请联系管理员！");
                }
            } else {
                throw new RuntimeException("充值失败，请联系管理员！");
            }
        } else {
            throw new RuntimeException("硬币不足！");
        }
    }

    @Override
    public int updateByUserId(Vip vip) {
        return mapper.updateByUserIdSelective(vip);
    }


}
