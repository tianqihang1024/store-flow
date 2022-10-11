package extend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import extend.bean.FestivalConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 22489
 * @description 针对表【holiday_config(假期配置表)】的数据库操作Mapper
 * @createDate 2022-10-09 23:44:09
 * @Entity extend.bean.FestivalConfig
 */
public interface FestivalConfigMapper extends BaseMapper<FestivalConfig> {


    List<FestivalConfig> getFestivalConfigByNameCn(@Param("list") List<String> festivalNames);

    int updateHolidayConfig(@Param("list") List<FestivalConfig> festivalConfigList);
}
