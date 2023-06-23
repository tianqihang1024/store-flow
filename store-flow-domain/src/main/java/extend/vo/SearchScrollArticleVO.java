package extend.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 田奇杭
 * @Description 滚动查询文章vo
 * @Date 2022/11/10 22:40
 */
@Data
public class SearchScrollArticleVO {

    private Long id;

    private String title;

    private String context;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime created;
}
