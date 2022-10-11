package extend.tasks.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import extend.bean.FestivalConfig;
import extend.dto.NewsListDTO;
import extend.enums.FestivalConfigEnum;
import extend.mapper.FestivalConfigMapper;
import extend.tasks.FestivalConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FestivalConfigServiceImpl implements FestivalConfigService {

    //    private static final String URL = "https://api.tianapi.com/jiejiari/index?key=%s&date=%s";
    private static final String URL = "http://api.tianapi.com/jiejiari/index?key=660068bb549dae9a968ec5c53665ec24&date=2020-10-01";

    @Value("${api.tianapi.key:660068bb549dae9a968ec5c53665ec24}")
    private String appKey;

    @Resource
    private FestivalConfigService festivalConfigService;

    @Resource
    private FestivalConfigMapper festivalConfigMapper;

    @Override
    public void syncHolidayConfig(LocalDateTime taskStartTime) {

        String jsonResult = request(taskStartTime);

        List<FestivalConfig> festivalConfigList = builderHolidayConfig(jsonResult);

        festivalConfigService.updateHolidayConfig(festivalConfigList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHolidayConfig(List<FestivalConfig> festivalConfigList) {
        int count = festivalConfigMapper.updateHolidayConfig(festivalConfigList);
        if (count != festivalConfigList.size()) {
            throw new RuntimeException("更新节日配置表失败");
        }
        return count;
    }

    private List<FestivalConfig> builderHolidayConfig(String jsonResult) {

        JSONObject jsonObject = JSON.parseObject(jsonResult);

        JSONArray jsonArray = JSON.parseArray(jsonObject.getString("newslist"));
        NewsListDTO newsListDTO = JSON.parseObject(JSON.toJSONString(jsonArray.get(0)), NewsListDTO.class);

        if (newsListDTO.getDayCode() == null || newsListDTO.getDayCode() != 1) {
            return new ArrayList<>();
        }

        FestivalConfigEnum festival = FestivalConfigEnum.getFestivalMergeByLunarCalendar(newsListDTO.getLunarMonth(), newsListDTO.getLunarDay());
        List<String> festivalNames = Objects.isNull(festival) ? Collections.singletonList(newsListDTO.getName()) : festival.getFestivalMerge();

        List<FestivalConfig> festivalConfigList = festivalConfigMapper.getFestivalConfigByNameCn(festivalNames);

        for (FestivalConfig config : festivalConfigList) {
            config.setFestivalSize(newsListDTO.getNow() + 1);
            config.setFestivalStartTime(LocalDateTime.now().minusDays(newsListDTO.getNow()));
            config.setFestivalEndTime(LocalDateTime.now());
        }

        return festivalConfigList;
    }

    /**
     * 获取节假日接口json字符串
     *
     * @param taskStartTime 请求参数
     * @return 节假日接口json字符串
     */
    private String request(LocalDateTime taskStartTime) {
        String result = null;
        StringBuilder sbf = new StringBuilder();
//        String httpUrl = String.format(URL, appKey, taskStartTime);
        String httpUrl = URL;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            log.error("请求第三方接口时发生错误 e：", e);
        }
        return result;
    }

}
