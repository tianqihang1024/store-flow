package extend.param;

import lombok.Getter;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/5 14:43
 */
@Getter
public class SearchStoreFlowScrollParam {

    private String tenantId;

    private String storeId;

    private Integer sortColumnCode;

    private Integer pageSize;

    private String scrollId;
}
