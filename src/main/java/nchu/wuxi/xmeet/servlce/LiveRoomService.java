package nchu.wuxi.xmeet.servlce;

import nchu.wuxi.xmeet.utils.RestReturn;

/**
 * @auther WuXi
 * @create 2020/4/13
 */

public interface LiveRoomService {
    RestReturn getRoomByEnterpriseId(int enterpriseId);
    RestReturn registerRoomByPhone(String phone);
    RestReturn getRoomInfoById(int roomId);
    void changeStatus(int roomId,String status);
}
