package extend.param;

import lombok.Data;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/10 20:43
 */
@Data
public class SearchScrollAfterArticleParam {

    /**
     * 查询文本
     */
    private String searchTxt;

    /**
     * 排序列
     */
    private Integer sortColumnCode;

    /**
     * 页面编号
     */
    private Integer pageNum;

    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 排序值
     */
    private Object[] sortValues;

}
