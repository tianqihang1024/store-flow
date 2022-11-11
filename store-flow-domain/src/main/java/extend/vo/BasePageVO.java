package extend.vo;

import lombok.Data;


/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/10 22:35
 */
@Data
public class BasePageVO {

    private Long total;

    private Integer pageNum;

    private Integer pageSize;
}
