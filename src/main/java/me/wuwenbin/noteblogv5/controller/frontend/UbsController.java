package me.wuwenbin.noteblogv5.controller.frontend;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.wuwenbin.noteblogv5.controller.common.BaseController;
import me.wuwenbin.noteblogv5.model.LayuiTable;
import me.wuwenbin.noteblogv5.model.bo.CommentBo;
import me.wuwenbin.noteblogv5.model.bo.HideBo;
import me.wuwenbin.noteblogv5.model.bo.ReplyBo;
import me.wuwenbin.noteblogv5.service.interfaces.content.HideService;
import me.wuwenbin.noteblogv5.service.interfaces.msg.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author wuwen
 */
@Controller
@RequestMapping("/ubs/token")
public class UbsController extends BaseController {

    private final CommentService commentService;
    private final HideService hideService;

    public UbsController(CommentService commentService, HideService hideService) {
        this.commentService = commentService;
        this.hideService = hideService;
    }

    @GetMapping("/{page}")
    public String uHistory(@PathVariable String page) {
        return "frontend/ubs/" + page;
    }

    @PostMapping("/reply/{userId}")
    @ResponseBody
    public LayuiTable<ReplyBo> myLogs(Page<ReplyBo> replyPage, @PathVariable Long userId) {
        IPage<ReplyBo> newPage = commentService.findReplyPage(replyPage, userId);
        return new LayuiTable<>(newPage.getTotal(), newPage.getRecords());
    }

    @PostMapping("/comment/{userId}")
    @ResponseBody
    public LayuiTable<CommentBo> myComments(Page<CommentBo> replyPage, @PathVariable Long userId) {
        IPage<CommentBo> newPage =
                commentService.findCommentPage(replyPage, null, null, null, userId, true);
        return new LayuiTable<>(newPage.getTotal(), newPage.getRecords());
    }

    @PostMapping("/purchase/{userId}")
    @ResponseBody
    public LayuiTable<HideBo> myPurchases(Page<HideBo> replyPage, @PathVariable Long userId) {
        IPage<HideBo> newPage =
                hideService.findMyPurchases(replyPage, userId);
        return new LayuiTable<>(newPage.getTotal(), newPage.getRecords());
    }

}
