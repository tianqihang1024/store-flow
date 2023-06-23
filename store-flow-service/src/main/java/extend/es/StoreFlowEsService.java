package extend.es;

import extend.bean.StoreFlow;
import extend.param.SearchStoreFlowParam;
import extend.param.SearchStoreFlowScrollParam;
import extend.param.UpdateStoreFlowParam;
import extend.utils.Result;
import extend.vo.SearchScrollStoreFlowVO;

public interface StoreFlowEsService {


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return
     */
    Result<StoreFlow> findStoreFlowById(Long id);

    /**
     * 根据租户、创建时间查询
     *
     * @param searchStoreFlowParam
     * @return
     */
    Result<StoreFlow> searchStoreFlowByParam(SearchStoreFlowParam searchStoreFlowParam);

    /**
     * 修改客流数据
     *
     * @param storeFlowParam 待修改的客流数据
     * @return
     */
    Result<Void> updateStoreFlow(UpdateStoreFlowParam storeFlowParam);

    /**
     * 滚动查询店铺客流数据
     *
     * @param searchStoreFlowScrollParam
     * @return
     */
    Result<SearchScrollStoreFlowVO> searchScrollStoreFlow(SearchStoreFlowScrollParam searchStoreFlowScrollParam);
}
