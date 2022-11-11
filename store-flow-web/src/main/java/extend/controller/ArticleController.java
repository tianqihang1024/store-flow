package extend.controller;

import extend.bean.Article;
import extend.es.ArticleService;
import extend.param.SearchScrollAfterArticleParam;
import extend.param.SearchScrollArticleParam;
import extend.utils.Result;
import extend.vo.SearchScrollAfterArticlePageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 21:10
 */
@Slf4j
@RestController
@RequestMapping(value = "/article/es/")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @RequestMapping(value = "initData")
    public void initData() {
        articleService.initData();
    }

    @RequestMapping(value = "searchScrollArticle")
    public SearchScrollHits<Article> searchScrollArticle(@RequestBody SearchScrollArticleParam searchScrollArticleParam) {
        return articleService.searchScrollArticle(searchScrollArticleParam);
    }

    @RequestMapping(value = "searchScrollAfterArticle")
    public Result<SearchScrollAfterArticlePageVO> searchScrollAfterArticle(@RequestBody SearchScrollAfterArticleParam searchScrollAfterArticleParam) {
        return articleService.searchScrollAfterArticle(searchScrollAfterArticleParam);
    }
}
