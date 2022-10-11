package extend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import extend.bean.Store;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 22489
 * @description 针对表【store(店铺表)】的数据库操作Mapper
 * @createDate 2022-08-19 22:59:58
 * @Entity extend.bean.Store
 */
public interface StoreMapper extends BaseMapper<Store> {

    /**
     * 获取有效租户名单
     * @return 租户Ids
     */
    List<String> getAValidTenantList(@Param("taskStartTime") LocalDateTime taskStartTime);

}
