package extend.param;

import lombok.Data;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 21:17
 */
@Data
public class SearchScrollArticleParam {

    /**
     * 查询文本
     */
    private String searchText;

    /**
     * 排序列
     */
    private Integer sortColumnCode;

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 滚动快照id
     */
    private String scrollId;
}
