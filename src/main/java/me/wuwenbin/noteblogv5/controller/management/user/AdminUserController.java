package me.wuwenbin.noteblogv5.controller.management.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.wuwenbin.noteblogv5.constant.RoleEnum;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.LayuiTable;
import me.wuwenbin.noteblogv5.model.ResultBean;
import me.wuwenbin.noteblogv5.model.entity.User;
import me.wuwenbin.noteblogv5.model.entity.User1;
import me.wuwenbin.noteblogv5.model.entity.Vip;
import me.wuwenbin.noteblogv5.service.interfaces.UserCoinRecordService;
import me.wuwenbin.noteblogv5.service.interfaces.UserService;
import me.wuwenbin.noteblogv5.service.interfaces.vip.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author wuwen
 */
@Controller
@RequestMapping("/management/user")
public class AdminUserController extends BaseController {

    private final UserService userService;
    private final UserCoinRecordService userCoinRecordService;
    @Autowired
    private VipService vipService;

    public AdminUserController(UserService userService, UserCoinRecordService userCoinRecordService) {
        this.userService = userService;
        this.userCoinRecordService = userCoinRecordService;
    }

    @GetMapping
    public String index() {
        return "management/users/list";
    }

    @PostMapping("/list")
    @ResponseBody
    public LayuiTable<User1> userLayuiTable(Page<User1> page, String username,
                                           String nickname, String sort, String order) {
        addPageOrder(page, order, sort);
        /*IPage<User> userPage = userService.page(page,
                Wrappers.<User>query()
                        .like(StrUtil.isNotEmpty(username), "username", username)
                        .or().like(StrUtil.isNotEmpty(nickname), "nickname", nickname)
                        .ne("role", RoleEnum.ADMIN.getValue())
        );*/
        page.setRecords(userService.selectByPage(page, username, nickname));
        return new LayuiTable<>(page.getTotal(), page.getRecords());
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultBean update(@RequestParam("id") Long id, boolean enable) {
        boolean res = userService.update(Wrappers.<User>update().set("enable", enable).eq("id", id));
        return handle(res, "状态修改成功！", "状态修改失败！");
    }

    //更新vip状态
    @RequestMapping("/updateVip")
    @ResponseBody
    public ResultBean updateVip(@RequestParam("id") Long id, boolean enable) {
        if (enable){
            Vip vip1 = vipService.selectByUserId(id);
            if (vip1 == null){
                Vip vip = new Vip();
                vip.setUserId(id);
                vip.setState(1);
                vip.setCount(0);
                vip.setSource(2);
                return handle(vipService.insert(vip) == 1?true:false, "状态修改成功！", "状态修改失败！");
            }else{
                Vip vip = new Vip();
                vip.setUserId(id);
                vip.setState(1);
                return handle(vipService.updateByUserId(vip) == 1?true:false, "状态修改成功！", "状态修改失败！");
            }
        }else{
            Vip vip = new Vip();
            vip.setState(-1);
            vip.setUserId(id);
            return handle(vipService.updateByUserId(vip) == 1?true:false, "状态修改成功！", "状态修改失败！");
        }
    }

    @RequestMapping("/update/nickname")
    @ResponseBody
    public ResultBean update(@RequestParam("id") Long id, String nickname) {
        User u = userService.getOne(Wrappers.<User>query().eq("nickname", nickname));
        if (u == null) {
            boolean res = userService.update(Wrappers.<User>update().set("nickname", nickname).eq("id", id));
            return handle(res, "昵称状态修改成功！", "昵称修改失败！");
        } else {
            return ResultBean.error("已存在昵称！");
        }
    }

    @RequestMapping("/update/remainCoin")
    @ResponseBody
    public ResultBean update(@RequestParam("id") Long id, Integer remainCoin) {
        boolean res = userService.update(Wrappers.<User>update().set("remain_coin", remainCoin).eq("id", id));
        if (res) {
            userCoinRecordService.calcAdminInsertRecord(remainCoin, id);
        }
        return handle(res, "修改硬币余额成功！", "修改硬币余额失败！");
    }
}
