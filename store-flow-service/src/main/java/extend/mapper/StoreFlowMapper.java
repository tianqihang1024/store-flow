package extend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import extend.bean.StoreFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 22489
 * @description 针对表【store_flow(店铺客流表)】的数据库操作Mapper
 * @createDate 2022-08-19 22:59:58
 * @Entity extend.bean.StoreFlow
 */
public interface StoreFlowMapper extends BaseMapper<StoreFlow> {

    /**
     * 批量插入
     * {@link com.baomidou.mybatisplus.extension.injector.methods.additional.InsertBatchSomeColumn}
     *
     * @param entityList 要插入的数据
     * @return 成功插入的数据条数
     */
    int insertBatchSomeColumn(@Param("list") List<StoreFlow> entityList);
}
