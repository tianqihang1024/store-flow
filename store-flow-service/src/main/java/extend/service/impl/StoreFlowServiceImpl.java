package extend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import extend.bean.StoreFlow;
import extend.mapper.StoreFlowMapper;
import extend.service.StoreFlowService;
import org.springframework.stereotype.Service;

/**
 * @author 22489
 * @description 针对表【store_flow(店铺客流表)】的数据库操作Service实现
 * @createDate 2022-08-19 22:59:58
 */
@Service
public class StoreFlowServiceImpl extends ServiceImpl<StoreFlowMapper, StoreFlow> implements StoreFlowService {

}
