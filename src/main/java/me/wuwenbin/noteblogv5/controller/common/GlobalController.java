package me.wuwenbin.noteblogv5.controller.common;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.wuwenbin.noteblogv5.constant.DictGroup;
import me.wuwenbin.noteblogv5.model.entity.Article;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
            //noinspection unchecked
            Object rechargeUrl = settingsMap.getOrDefault("cash_recharge_url", "{\"name\":\"\",\"url\":\"\"}");
            JSONArray jsonArray = JSONUtil.parseArray(rechargeUrl);
            //noinspection unchecked
            settingsMap.put("recharges", jsonArray);
            model.addAttribute("settings", settingsMap);
        } else {
            List<Param> params = paramService.list(Wrappers.<Param>query().ge("`group`", 0));
            settingsMap = params.stream().collect(Collectors.toMap(Param::getName, p -> p.getValue() == null ? "" : p.getValue()));
            //noinspection unchecked
            Object rechargeUrl = settingsMap.getOrDefault("cash_recharge_url", "{\"name\":\"\",\"url\":\"\"}");
            JSONArray jsonArray = JSONUtil.parseArray(rechargeUrl);
            //noinspection unchecked
            settingsMap.put("recharges", jsonArray);
            CacheUtils.getParamCache().put(genParams, settingsMap);
            model.addAttribute("settings", settingsMap);
        }

        if (!request.getRequestURL().toString().contains("/management/")) {
            List cateGroupList = CacheUtils.fetchFromDefaultCache("cateGroupList", List.class);
            if (cateGroupList == null) {
                cateGroupList = dictService.findList(DictGroup.GROUP_CATE);
                CacheUtils.putIntoDefaultCache("cateGroupList", cateGroupList);
            }

            Integer articleCount = CacheUtils.fetchFromDefaultCache("articleCount", Integer.class);
            if (articleCount == null) {
                articleCount = articleService.count(Wrappers.<Article>query().ne("draft",1));
                CacheUtils.putIntoDefaultCache("articleCount", articleCount);
            }

            Long articleWords = CacheUtils.fetchFromDefaultCache("articleWords", Long.class);
            if (articleWords == null) {
                articleWords = articleService.sumArticleWords();
                CacheUtils.putIntoDefaultCache("articleWords", articleWords);
            }

            Integer commentCount = CacheUtils.fetchFromDefaultCache("commentCount", Integer.class);
            if (commentCount == null) {
                commentCount = commentService.count();
                CacheUtils.putIntoDefaultCache("commentCount", commentCount);
            }

            //由于设置的参数，所以把开机时间数据存入parmCache缓存中
            String initDateStr = CacheUtils.fetchFromParamCache("initDateStr", String.class);
            if (initDateStr == null){
                initDateStr = paramService.calcRunningDays();
                CacheUtils.putIntoParamCache("initDateStr", initDateStr);
            }

            model.addAttribute("cateList", cateGroupList);
            model.addAttribute("blogCount", articleCount);
            model.addAttribute("blogWords", articleWords);
            model.addAttribute("runningDays", getRunningDays(initDateStr));
            model.addAttribute("commentCount", commentCount);
        }
    }

    //获取网站运行时间（/天）
    private long getRunningDays(String initDateStr){
        Date initDate = DateUtil.parse(initDateStr);
        Date now = DateUtil.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return DateUtil.between(initDate, now, DateUnit.DAY);
    }
}
