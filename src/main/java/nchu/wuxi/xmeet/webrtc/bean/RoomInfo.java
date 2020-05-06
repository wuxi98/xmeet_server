package nchu.wuxi.xmeet.webrtc.bean;

import java.util.concurrent.CopyOnWriteArrayList;

public class RoomInfo {
    private String roomId;
    // 房间里的人
    private CopyOnWriteArrayList<UserBean> userBeans;
    // 房间大小
    private int maxSize;

    //房间名
    private String roomName;

    public RoomInfo() {
    }

    public RoomInfo(CopyOnWriteArrayList<UserBean> userBeans, int maxSize, int mediaType) {
        this.userBeans = userBeans;
        this.maxSize = maxSize;
    }

    public CopyOnWriteArrayList<UserBean> getUserBeans() {
        return userBeans;
    }

    public void setUserBeans(CopyOnWriteArrayList<UserBean> userBeans) {
        this.userBeans = userBeans;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "roomId='" + roomId + '\'' +
                ", maxSize=" + maxSize +
                ", roomName='" + roomName + '\'' +
                '}';
    }
}
