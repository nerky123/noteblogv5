package me.wuwenbin.noteblogv5.service.interfaces.vip;

import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.model.entity.Vip;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yangxw
 * @describition 全是VIP接口
 * @create 2020-01-19 20:57
 */
public interface VipService {

    int insert(Vip vip);

    Vip selectByUserId(Long userId);

    int update(Vip vip);

    String insertByCoin(HttpServletRequest request, int type, User purchaseUser);

    int updateByUserId(Vip vip);
}
