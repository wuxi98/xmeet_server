package nchu.wuxi.xmeet.servlce;

import nchu.wuxi.xmeet.entity.TCustomer;
import nchu.wuxi.xmeet.utils.RestReturn;
import org.springframework.stereotype.Service;

/**
 * @auther WuXi
 * @create 2020/4/4
 */

public interface AccountService {
    Object login(TCustomer user);
    RestReturn register(TCustomer user);
    TCustomer find(String phone);
    RestReturn loginWithoutPassword(String phone);
}
