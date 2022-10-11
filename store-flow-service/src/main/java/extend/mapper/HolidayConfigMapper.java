package extend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import extend.bean.HolidayConfig;
import org.apache.ibatis.annotations.Param;

/**
* @author 22489
* @description 针对表【holiday_config(假期配置表)】的数据库操作Mapper
* @createDate 2022-10-09 23:44:09
* @Entity extend.bean.HolidayConfig
*/
public interface HolidayConfigMapper extends BaseMapper<HolidayConfig> {


    HolidayConfig getHolidayConfigByNameCn(@Param("nameCn") String nameCn);
}
