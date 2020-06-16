package nchu.wuxi.xmeet.servlce;

import nchu.wuxi.xmeet.utils.RestReturn;

import java.io.File;

/**
 * @auther WuXi
 * @create 2020/4/13
 */

public interface LiveRoomService {
    RestReturn getRoomByEnterpriseId(int enterpriseId);
    RestReturn registerRoomByPhone(String phone);
    RestReturn getRoomInfoById(int roomId);
    void changeStatus(int roomId,String status);
    void updateNotice(int roomId,String msg);
    RestReturn uploadPage(File file, int roomId);
}
