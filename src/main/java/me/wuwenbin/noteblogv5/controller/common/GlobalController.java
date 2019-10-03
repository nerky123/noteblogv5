package me.wuwenbin.noteblogv5.controller.common;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.model.entity.Param;
import me.wuwenbin.noteblogv5.service.interfaces.content.ArticleService;
import me.wuwenbin.noteblogv5.service.interfaces.dict.DictService;
import me.wuwenbin.noteblogv5.service.interfaces.msg.CommentService;
import me.wuwenbin.noteblogv5.service.interfaces.property.ParamService;
import me.wuwenbin.noteblogv5.util.CacheUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by Wuwenbin on 2018/9/7 at 9:35
 *
 * @author wuwenbin
 */
@ControllerAdvice(basePackages = "me.wuwenbin.noteblogv5.controller")
public class GlobalController {

    private final ParamService paramService;
    private final DictService dictService;
    private final ArticleService articleService;
    private final CommentService commentService;

    public GlobalController(ParamService paramService, DictService dictService,
                            ArticleService articleService, CommentService commentService) {
        this.paramService = paramService;
        this.dictService = dictService;
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @ModelAttribute("settings")
    public void addSettings(Model model, HttpServletRequest request) {
        String genParams = "gen_params";
        Map settingsMap = CacheUtils.getParamCache().get(genParams, Map.class);
        if (settingsMap != null && settingsMap.size() > 0) {
            model.addAttribute("settings", settingsMap);
        } else {
            List<Param> params = paramService.list(Wrappers.<Param>query().ge("`group`", 0));
            settingsMap = params.stream().collect(Collectors.toMap(Param::getName, p -> p.getValue() == null ? "" : p.getValue()));
            CacheUtils.getParamCache().put(genParams, settingsMap);
            model.addAttribute("settings", settingsMap);
        }
        if (!request.getRequestURL().toString().contains("/management/")) {
            Object cateGroupList = CacheUtils.getDefaultCache().get("cateGroupList");
            if (cateGroupList == null) {
                cateGroupList = dictService.findList(DictGroup.GROUP_CATE);
                CacheUtils.putIntoDefaultCache("cateGroupList", cateGroupList);
            }

            Object articleCount = CacheUtils.getDefaultCache().get("articleCount");
            if (articleCount == null) {
                articleCount = articleService.count();
                CacheUtils.putIntoDefaultCache("articleCount", articleCount);
            }

            Object articleWords = CacheUtils.getDefaultCache().get("articleWords");
            if (articleWords == null) {
                articleWords = articleService.sumArticleWords();
                CacheUtils.putIntoDefaultCache("articleWords", articleWords);
            }

            Object commentCount = CacheUtils.getDefaultCache().get("commentCount");
            if (commentCount == null) {
                commentCount = commentService.count();
                CacheUtils.putIntoDefaultCache("commentCount", commentCount);
            }
            model.addAttribute("cateList", cateGroupList);
            model.addAttribute("blogCount", articleCount);
            model.addAttribute("blogWords", articleWords);
            model.addAttribute("runningDays", paramService.calcRunningDays());
            model.addAttribute("commentCount", commentCount);
        }
    }


}
