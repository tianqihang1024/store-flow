package extend.tasks.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import extend.bean.HolidayConfig;
import extend.mapper.HolidayConfigMapper;
import extend.tasks.HolidayConfigService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Service
public class HolidayConfigServiceImpl implements HolidayConfigService {

    private static final String URL = "https://api.tianapi.com/jiejiari/index?key=%s&date=%s";

    @Value("${api.tianapi.key:660068bb549dae9a968ec5c53665ec24}")
    private String appKey;

    @Resource
    private HolidayConfigMapper holidayConfigMapper;

    @Override
    public void updateHolidayConfig(LocalDateTime taskStartTime) {

        String jsonResult = request(taskStartTime);

        HolidayConfig holidayConfig = builderHolidayConfig(jsonResult);


    }


    private HolidayConfig builderHolidayConfig(String jsonResult) {

        JSONObject jsonObject = JSON.parseObject(jsonResult);

        jsonObject = JSON.parseObject(jsonObject.getString("newslist"));

        Integer daycode = jsonObject.getInteger("daycode");
        if (daycode == null || daycode != 1) {
            return null;
        }

        String nameCn = jsonObject.getString("name");
        HolidayConfig holidayConfig = holidayConfigMapper.getHolidayConfigByNameCn(nameCn);
        holidayConfig.setHolidaySize(jsonObject.getInteger("now") + 1);

        return holidayConfig;
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
        String httpUrl = String.format(URL, appKey, taskStartTime);

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
