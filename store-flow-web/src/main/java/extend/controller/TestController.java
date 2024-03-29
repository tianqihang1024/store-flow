package extend.controller;

import extend.bean.StoreFlow;
import extend.es.StoreFlowEsService;
import extend.param.SearchStoreFlowParam;
import extend.param.SearchStoreFlowScrollParam;
import extend.param.UpdateStoreFlowParam;
import extend.utils.Result;
import extend.vo.SearchScrollStoreFlowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/20 17:06
 */
@Slf4j
@RestController
@RequestMapping(value = "/storeFlow/es/")
public class TestController {

    @Resource
    private StoreFlowEsService storeFlowEsService;

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "findStoreFlowById")
    public Result<StoreFlow> findStoreFlowById(Long id) {
        return storeFlowEsService.findStoreFlowById(id);
    }

    /**
     * 根据租户、创建时间查询
     *
     * @param searchStoreFlowParam
     * @return
     */
    @RequestMapping(value = "searchStoreFlowByParam")
    public Result<StoreFlow> searchStoreFlowByParam(@RequestBody SearchStoreFlowParam searchStoreFlowParam) {
        return storeFlowEsService.searchStoreFlowByParam(searchStoreFlowParam);
    }

    /**
     * 修改客流数据
     *
     * @param storeFlowParam 待修改的客流数据
     * @return
     */
    @RequestMapping(value = "updateStoreFlow")
    public Result<Void> updateStoreFlow(@RequestBody UpdateStoreFlowParam storeFlowParam) {
        return storeFlowEsService.updateStoreFlow(storeFlowParam);
    }

    /**
     * 修改客流数据
     *
     * @param searchStoreFlowScrollParam
     * @return
     */
    @RequestMapping(value = "searchScrollStoreFlow")
    public Result<SearchScrollStoreFlowVO> searchScrollStoreFlow(@RequestBody SearchStoreFlowScrollParam searchStoreFlowScrollParam) {
        return storeFlowEsService.searchScrollStoreFlow(searchStoreFlowScrollParam);
    }

}
