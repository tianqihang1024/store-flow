package extend.utils;

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


    ;


    private final int code;

    private final String msg;
}
