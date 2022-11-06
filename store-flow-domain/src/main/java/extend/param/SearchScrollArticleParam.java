package extend.param;

import lombok.Data;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 21:17
 */
@Data
public class SearchScrollArticleParam {

    private String searchText;

    private Integer sortColumnCode;

    private Integer pageSize;

    private String scrollId;
}
