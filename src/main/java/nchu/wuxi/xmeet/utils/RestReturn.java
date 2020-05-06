package nchu.wuxi.xmeet.utils;

/**
 * @auther WuXi
 * @create 2020/4/4
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.time.Instant;

/**
 * 接口返回统一数据结构
 */
@Data
public class RestReturn {
    //是否成功标志
    private boolean success;
    //code错误码
    private Integer code;
    //外带数据信息
    private Object data;
    //前端进行页面展示的信息
    private Object message;
    //返回时间戳
    private Integer length;
    private Long currentTime;
    /**
     *构造函数（无参数）
     */
    public RestReturn() {
        //毫秒
        this.currentTime = Instant.now().toEpochMilli();
    }
    /**
     *构造函数（有参数）
     */
    public RestReturn(boolean success, Integer code, Object data, Object message,int length) {
        this.success = success;
        this.code = code;
        this.data = data;
        this.message = message;
        this.length = length;
        //毫秒
        this.currentTime = Instant.now().toEpochMilli();
    }
    @Override
    public String toString() {
        return "RestReturn{" +
                "success=" + success +
                ",code='" + code + '\'' +
                ",data=" + data +
                ",message=" + message +
                ",currentTime=" + currentTime +
                '}';
    }

    public RestReturn success(Object data, Object message,int length) {

        this.success = true;
        this.code = RestReturnEnum.SUCCESS.getCode();
        this.data = data;
        this.message = message;
        this.length = length;
        return this;
    }

    public RestReturn error(Integer code, Object data, Object message,int length) {
        this.success = false;
        this.code = code;
        this.data = data;
        this.message = message;
        this.length = length;
        return this;
    }

    public static boolean isRestReturnJson(String data) {
        //临时实现先判定下字符串的格式和字段
        try {
            /**
             * ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化。
             */
            ObjectMapper mapper = new ObjectMapper();
            RestReturn dataRestReturn = mapper.readValue(data, RestReturn.class);
            //比较两个类的字段,如果一致返回为真，不一致返回为假
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
