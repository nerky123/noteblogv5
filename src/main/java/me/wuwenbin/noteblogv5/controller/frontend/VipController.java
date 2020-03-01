package me.wuwenbin.noteblogv5.controller.frontend;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.model.entity.Vip;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import me.wuwenbin.noteblogv5.service.interfaces.vip.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yangxw
 * @describition 用于VIP用户操作
 * @create 2020-01-19 17:37
 */

@Controller
@RequestMapping("/vip/token")
public class VipController extends BaseController {
    @Autowired
    private VipService service;
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseBody
    public String post(HttpServletRequest request, @RequestBody String vipInfo){
        User purchaseUser = userService.getById(getSessionUser(request).getId());
        if (purchaseUser == null) {
            return "{\"code\":\"2\",\"message\":\"登录后再进行操作哦~\"}";
        }
        long userId = purchaseUser.getId();
        JSONObject jsonObject = JSONUtil.parseObj(vipInfo);
        int type = jsonObject.getInt("type");

        Vip vip1 = service.selectByUserId(userId);
        if (vip1 !=null){
            //管理员完成校验，确认时会员了
            Integer state = vip1.getState();
            if (state == 1){
                return "{\"code\":\"2\",\"message\":\"您已经是尊贵的会员啦！\"}";
            }
            //不管有没有充值，重复点击超过50次，直接显示限制购买
            Integer count = vip1.getCount();
            if (count.intValue()>50){
                return "{\"code\":\"2\",\"message\":\"该账号暂时被限制购买会员，请联系管理员解锁！\"}";
            }
            //不是会员重复点击计数，超过一百次直接限制购买
            Vip vip2 = new Vip();
            vip2.setCount(count+1);
            vip2.setVipId(vip1.getVipId());
            int update = service.update(vip2);
            if (update == 1){
                if (vip1.getSource()==0){
                    return "{\"code\":\"2\",\"message\":\"您已购买会员，请勿重复购买！如已付款，请耐心等待十分钟；如未付款，请勿再点击！\"}";
                }else{
                    return "{\"code\":\"2\",\"message\":\"您已购买会员，但没生效，请联系管理员！\"}";
                }
            }
        }


        //用户第一次点击购买，如果是支付宝。。。。
        if (type == 0){
            Vip vip = new Vip();
            vip.setUserId(userId);
            vip.setSource(type);
            vip.setState(-1);
            if (service.insert(vip) == 1){
                return "{\"code\":\"0\",\"message\":\"充值成功！请等待管理员审核\"}";
            }
        }else if(type == 1){
            //如果是硬币购买。。。
            try{
                String str = service.insertByCoin(request,type,purchaseUser);
                updateSessionUser(request, userService.getById(userId));
                return str;
            }catch (Exception e){
                return "{\"code\":\"2\",\"message\":\""+e.getMessage()+"\"}";
            }
        }
        return "{\"code\":\"2\",\"message\":\"充值失败！如果已充值，请联系管理员！\"}";
    }

}
