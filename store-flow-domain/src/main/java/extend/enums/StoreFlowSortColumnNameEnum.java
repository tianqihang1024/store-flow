package extend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 田奇杭
 * @Description 查询ES对数据排序时指定的列名称
 * @Date 2022/11/5 14:56
 */
@Getter
@AllArgsConstructor
public enum StoreFlowSortColumnNameEnum {

    ID(0, "id"),
    CREATE_TIME(1, "createTime"),
    ;

    private final Integer columnCode;
    private final String columnName;


    public static StoreFlowSortColumnNameEnum getEnumByCode(Integer columnCode) {
        for (StoreFlowSortColumnNameEnum value : StoreFlowSortColumnNameEnum.values()) {
            if (value.columnCode.equals(columnCode)) {
                return value;
            }
        }
        return ID;
    }
}
