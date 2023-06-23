package extend.es;

import extend.param.SearchScrollAfterArticleParam;
import extend.param.SearchScrollArticleParam;
import extend.utils.Result;
import extend.vo.SearchScrollAfterArticlePageVO;
import extend.vo.SearchScrollArticlePageVO;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 20:49
 */
public interface ArticleService {

    void initData();

    Result<SearchScrollArticlePageVO> searchScrollArticle(SearchScrollArticleParam searchScrollArticleParam);

    Result<SearchScrollAfterArticlePageVO> searchScrollAfterArticle(SearchScrollAfterArticleParam searchScrollAfterArticleParam);
}
