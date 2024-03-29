package extend.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StoreFlowFailEnum {


    /**
     * 参数缺失异常
     */
    PARAM_CANNOT_BE_EMPTY(200000, "参数不能为空"),
    TENANT_CANNOT_BE_EMPTY(200001, "租户不能是空"),
    STORE_CANNOT_BE_EMPTY(200002, "店铺不能是空"),
    TENANT_STORE_CANNOT_BE_EMPTY(200003, "租户商店不能为空"),
    SEARCH_TEXT_IS_NULL(200004, "请输入您感兴趣的内容"),

    SEARCH_FAIL(100001, "查询失败"),
    UPDATE_FAIL(100002, "更新失败"),


    ;


    private final int code;

    private final String msg;
}
