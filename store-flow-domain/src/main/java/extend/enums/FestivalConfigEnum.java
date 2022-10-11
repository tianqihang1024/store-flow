package extend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 中国传统节假日enum，用于可能存在的节假日合并
 */
@Getter
@AllArgsConstructor
public enum FestivalConfigEnum {

    /**
     * 中国传统节假日与公历节假日可能合并到一起的节假日
     */
    ZHONG_QIU("中秋节", "八月十五", 3, new ArrayList<>()),
    ;

    static {
        ZHONG_QIU.getFestivalMerge().add("中秋节");
        ZHONG_QIU.getFestivalMerge().add("国庆节");
    }

    private final String festivalName;
    private final String lunarCalendar;
    private final Integer festivalSize;
    private final List<String> festivalMerge;

    public static FestivalConfigEnum getFestivalMergeByLunarCalendar(String lunarMonth, String lunarDay) {
        String lunarCalendar = lunarMonth + lunarDay;
        for (FestivalConfigEnum value : FestivalConfigEnum.values()) {
            if (value.lunarCalendar.equals(lunarCalendar)) {
                return value;
            }
        }
        return null;
    }

}
