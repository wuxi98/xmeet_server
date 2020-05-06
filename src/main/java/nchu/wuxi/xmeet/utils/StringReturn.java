package nchu.wuxi.xmeet.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @auther WuXi
 * @create 2020/4/4
 */
@Data
@AllArgsConstructor
public class StringReturn {
    private Integer code;
    private String msg;
    private Object data;
    private Integer dataSize;

}
