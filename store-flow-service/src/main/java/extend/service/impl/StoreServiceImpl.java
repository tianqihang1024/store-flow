package extend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import extend.bean.Store;
import extend.mapper.StoreMapper;
import extend.service.StoreService;
import org.springframework.stereotype.Service;

/**
 * @author 22489
 * @description 针对表【store(店铺表)】的数据库操作Service实现
 * @createDate 2022-08-19 22:59:58
 */
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

}
