package extend.mapper;

import extend.bean.StoreFlow;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 22489
 * @description 针对表【store_flow(店铺客流表)】的数据库操作Mapper
 * @createDate 2022-08-19 22:59:58
 * @Entity extend.bean.StoreFlow
 */
public interface StoreFlowMapper extends ExtendBaseMapper<StoreFlow> {

    /**
     * 按租户 ID 获取商店流量
     *
     * @param tenantId  租户 ID
     * @param startTime 开始时间
     * @param endTimme  结束时间
     * @return StoreFlow 中只有 storeId、flowCount有值
     */
    List<StoreFlow> getStoreFlowByTenantId(@Param("tenantId") String tenantId,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTimme") LocalDateTime endTimme);
}
