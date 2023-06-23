package extend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 20:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private Long id;

    private Author author;

    private String title;

    private String context;

    private LocalDateTime created;
}
