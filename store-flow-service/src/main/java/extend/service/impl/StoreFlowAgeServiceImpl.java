package extend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import extend.bean.StoreFlowAge;
import extend.mapper.StoreFlowAgeMapper;
import extend.service.StoreFlowAgeService;
import org.springframework.stereotype.Service;

/**
 * @author 22489
 * @description 针对表【store_flow_age(店铺客流表-年龄表)】的数据库操作Service实现
 * @createDate 2022-08-19 22:59:58
 */
@Service
public class StoreFlowAgeServiceImpl extends ServiceImpl<StoreFlowAgeMapper, StoreFlowAge> implements StoreFlowAgeService {

}
