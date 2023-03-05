package extend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/12/8 21:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer age;

}
