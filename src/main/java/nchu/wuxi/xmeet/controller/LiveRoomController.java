package nchu.wuxi.xmeet.controller;

import nchu.wuxi.xmeet.servlce.LiveRoomService;
import nchu.wuxi.xmeet.utils.RestReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther WuXi
 * @create 2020/4/13
 */
@RestController
@RequestMapping("/liveRoom")
public class LiveRoomController {

    @Autowired
    LiveRoomService liveRoomService;


    @RequestMapping("/{enterpriseId}")
    public RestReturn getRoomByEnterpriseId(@PathVariable int enterpriseId){
        System.out.println("getRoomByEnterpriseId"+enterpriseId);
        return liveRoomService.getRoomByEnterpriseId(enterpriseId);
    }
    @PostMapping(value = "/register")
    public RestReturn registerRoomByPhone(String phone){
        System.out.println("register"+phone);
        return liveRoomService.registerRoomByPhone(phone);
    }
    @RequestMapping("/getInfoById/{roomId}")
    public RestReturn getRoomInfoById(@PathVariable int roomId){
        System.out.println("getInfoById"+roomId);
        return liveRoomService.getRoomInfoById(roomId);
    }

    @PostMapping("/changeStatus")
    public void changeStatus(String roomId,String status){
        System.out.println("changeStatus"+roomId+" status:"+status);
        int id = 0;
        try {
            id = Integer.parseInt(roomId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        liveRoomService.changeStatus(id,status);
    }
}
