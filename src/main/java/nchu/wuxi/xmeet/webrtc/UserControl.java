package nchu.wuxi.xmeet.webrtc;



import nchu.wuxi.xmeet.utils.RestReturn;
import nchu.wuxi.xmeet.webrtc.bean.RoomInfo;
import nchu.wuxi.xmeet.webrtc.bean.UserBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
public class UserControl {

    @RequestMapping("/")
    public RestReturn index() {
        return new RestReturn().success("welcome to my webRTC demo","success",0);
    }

    @RequestMapping("/roomList")
    public RestReturn roomList() {
        System.out.println("roomlist:");
        ConcurrentHashMap<String, RoomInfo> rooms = MemCons.rooms;
//        ArrayList<String> list = new ArrayList<>();
//        rooms.values().forEach(e->list.add(e.getRoomName()));
        List<RoomInfo> list = rooms.values().stream().filter(e -> e.getMaxSize() > 2).collect(Collectors.toList());
        for(RoomInfo roomInfo:list){
            System.out.println(roomInfo.toString());
        }
        return new RestReturn().success(list,"success",list.size());
    }

    @RequestMapping("/userList")
    public RestReturn userList() {
        ConcurrentHashMap<String, UserBean> userBeans = MemCons.userBeans;
        Collection<UserBean> values = userBeans.values();
        ArrayList<UserBean> list = new ArrayList<>(values);
        return new RestReturn().success(list,"success",list.size());
    }

}
