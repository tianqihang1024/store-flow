package extend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 田奇杭
 * @Description 节假日第三方接口DTO
 * @Date 2022/10/11 19:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsListDTO {

    /**
     * 当前阳历日期
     */
    private String date;

    /**
     * 日期类型，为0表示工作日、为1节假日、为2双休日、3为调休日（上班）
     */
    private Integer dayCode;

    /**
     * 星期（数字）
     */
    private Integer weekday;

    /**
     * 星期（中文）
     */
    private String cnWeekday;

    /**
     * 农历年
     */
    private String lunarYear;

    /**
     * 农历月
     */
    private String lunarMonth;

    /**
     * 农历日
     */
    private String lunarDay;

    /**
     * 文字提示，工作日、节假日、节日、双休日
     */
    private String info;

    /**
     * 假期起点计数
     */
    private Integer start;

    /**
     * 假期当前计数
     */
    private Integer now;

    /**
     * 假期终点计数
     */
    private Integer end;

    /**
     * 节日日期
     */
    private String holiday;

    /**
     * 节假日名称（中文）
     */
    private String name;

    /**
     * 节日名称（英文）
     */
    private String enName;

    /**
     * 是否需要上班，0为工作日，1为休息日
     */
    private Integer isNotwork;

    /**
     * 节假日数组
     */
    private List<String> vacation;

    /**
     * 调休日数组
     */
    private List<String> remark;

    /**
     * 薪资法定倍数/按年查询时为具体日期
     */
    private Integer wage;

    /**
     * 放假提示
     */
    private String tip;

    /**
     * 拼假建议
     */
    private String rest;

}
