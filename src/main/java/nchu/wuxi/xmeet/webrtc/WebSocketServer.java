package nchu.wuxi.xmeet.webrtc;



import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import nchu.wuxi.xmeet.webrtc.bean.DeviceSession;
import nchu.wuxi.xmeet.webrtc.bean.EventData;
import nchu.wuxi.xmeet.webrtc.bean.RoomInfo;
import nchu.wuxi.xmeet.webrtc.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static nchu.wuxi.xmeet.webrtc.MemCons.rooms;


@ServerEndpoint("/ws/{userId}/{device}")
@Component
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    private Session session;
    private String userId;
    private int device;
    private static Gson gson = new Gson();
    private static String avatar = "p1.jpeg";


    // 用户userId登录进来
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("device") String de) {
        System.out.println("onOpen......");

        int device = Integer.parseInt(de);
        UserBean userBean = MemCons.userBeans.get(userId);
        if (userBean == null) {
            userBean = new UserBean(userId, avatar);
        }
        if (device == 0) {
            userBean.setPhoneSession(session, device);
            LOG.info("Phone用户登陆:" + userBean.getUserId() + ",session:" + session.getId());
        } else {
            userBean.setPcSession(session, device);
            LOG.info("PC用户登陆:" + userBean.getUserId() + ",session:" + session.getId());
        }
        this.device = device;
        this.session = session;
        this.userId = userId;

        //加入列表
        MemCons.userBeans.put(userId, userBean);

        // 登陆成功，返回个人信息
        EventData send = new EventData();
        send.setEventName("__login_success");
        Map<String, Object> map = new HashMap<>();
        map.put("userID", userId);
        map.put("avatar", avatar);
        send.setData(map);
        this.session.getAsyncRemote().sendText(gson.toJson(send));


    }

    // 用户下线
    @OnClose
    public void onClose() {
        System.out.println("onClose......");
        // 根据用户名查出房间,
        UserBean userBean = MemCons.userBeans.get(userId);
        if (userBean != null) {
            DeviceSession[] sessions = userBean.getSessions();
            if (device == 0) {
                try {
                    sessions[0].getSession().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sessions[0] = null;
                userBean.setSessions(sessions);
                MemCons.userBeans.put(userId, userBean);
                LOG.info("Phone用户离开:" + userBean.getUserId());
            } else {

                try {
                    sessions[1].getSession().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sessions[1] = null;
                userBean.setSessions(sessions);
                MemCons.userBeans.put(userId, userBean);
                LOG.info("PC用户离开:" + userBean.getUserId());
            }
            if (sessions[0] == null && sessions[1] == null) {
                MemCons.userBeans.remove(userId);
            }
        }


    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("=====================================");
        System.out.println("收到消息:" + message);
        handleMessage(message, session);
    }

    // 发送各种消息
    private void handleMessage(String message, Session session) {
        EventData data;
        try {
            data = gson.fromJson(message, EventData.class);
        } catch (JsonSyntaxException e) {
            System.out.println("json解析错误：" + message);
            return;
        }
        switch (data.getEventName()) {
            case "__create":
                createRoom(message, data.getData());
                break;
            case "__invite":
                invite(message, data.getData());
                break;
            case "__ring":
                ring(message, data.getData());
                break;
            case "__cancel":
                cancel(message, data.getData());
                break;
            case "__reject":
                reject(message, data.getData());
                break;
            case "__join":
                join(message, data.getData());
                break;
            case "__ice_candidate":
                iceCandidate(message, data.getData());
                break;
            case "__offer":
                offer(message, data.getData());
                break;
            case "__answer":
                answer(message, data.getData());
                break;
            case "__leave":
                leave(message, data.getData());
                break;
            case "__audio":
                transAudio(message, data.getData());
                break;
            case "__disconnect":
                disconnet(message, data.getData());
                break;
            default:
                break;
        }

    }

    // 创建房间
    private void createRoom(String message, Map<String, Object> data) {
        String room = (String) data.get("room");
        String userId = (String) data.get("userID");
        String roomName = (String) data.get("roomName");
        RoomInfo roomParam = rooms.get(room);
        // 没有这个房间
        if (roomParam == null) {
            int size = (int) Double.parseDouble(String.valueOf(data.get("roomSize")));
            // 创建房间
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setMaxSize(size);
            roomInfo.setRoomName(roomName);
            roomInfo.setRoomId(room);

            CopyOnWriteArrayList<UserBean> copy = new CopyOnWriteArrayList<>();
            // 将自己加入到房间里
            UserBean my = MemCons.userBeans.get(userId);
            copy.add(my);
            roomInfo.setUserBeans(copy);

            // 将房间储存起来
            rooms.put(room, roomInfo);

            EventData send = new EventData();
            send.setEventName("__peers");
            Map<String, Object> map = new HashMap<>();
            map.put("connections", "");
            map.put("you", userId);
            send.setData(map);
            sendMsg(my, -1, gson.toJson(send));
        }

    }

    // 首次邀请
    private void invite(String message, Map<String, Object> data) {
        String userList = (String) data.get("userList");
        String[] users = userList.split(",");

        // 给其他人发送邀请
        for (String user : users) {
            UserBean userBean = MemCons.userBeans.get(user);
            if (userBean != null) {
                sendMsg(userBean, -1, message);
            }
        }


    }

    // 响铃回复
    private void ring(String message, Map<String, Object> data) {
        String inviteId = (String) data.get("toID");
        UserBean userBean = MemCons.userBeans.get(inviteId);
        if (userBean != null) {
            sendMsg(userBean, -1, message);
        }
    }

    // 取消拨出
    private void cancel(String message, Map<String, Object> data) {
        String userList = (String) data.get("userList");
        String[] users = userList.split(",");
        for (String userId : users) {
            UserBean userBean = MemCons.userBeans.get(userId);
            if (userBean != null) {
                sendMsg(userBean, -1, message);
            }
        }


    }

    // 拒绝接听
    private void reject(String message, Map<String, Object> data) {
        String toID = (String) data.get("toID");
        UserBean userBean = MemCons.userBeans.get(toID);
        if (userBean != null) {
            sendMsg(userBean, -1, message);
        }
    }

    // 加入房间
    private void join(String message, Map<String, Object> data) {
        String room = (String) data.get("room");
        String userID = (String) data.get("userID");

        RoomInfo roomInfo = rooms.get(room);
        CopyOnWriteArrayList<UserBean> roomUserBeans = roomInfo.getUserBeans();
        UserBean my = MemCons.userBeans.get(userID);
        // 1. 將我加入到房间
        roomUserBeans.add(my);
        roomInfo.setUserBeans(roomUserBeans);
        rooms.put(room, roomInfo);

        // 2. 返回房间里的所有人信息
        EventData send = new EventData();
        send.setEventName("__peers");
        Map<String, Object> map = new HashMap<>();

        String[] cons = new String[roomUserBeans.size()];
        for (int i = 0; i < roomUserBeans.size(); i++) {
            UserBean userBean = roomUserBeans.get(i);
            if (userBean.getUserId().equals(userID)) {
                continue;
            }
            cons[i] = userBean.getUserId();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cons.length; i++) {
            if (cons[i] == null) {
                continue;
            }
            sb.append(cons[i]).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        map.put("connections", sb.toString());
        map.put("you", userID);
        send.setData(map);
        sendMsg(my, -1, gson.toJson(send));


        EventData newPeer = new EventData();
        newPeer.setEventName("__new_peer");
        Map<String, Object> sendMap = new HashMap<>();
        sendMap.put("userID", userID);
        newPeer.setData(sendMap);

        // 3. 给房间里的其他人发送消息
        for (UserBean userBean : roomUserBeans) {
            if (userBean.getUserId().equals(userID)) {
                continue;
            }
            sendMsg(userBean, -1, gson.toJson(newPeer));
        }


    }

    // 切换到语音接听
    private void transAudio(String message, Map<String, Object> data) {
        String userId = (String) data.get("userID");
        UserBean userBean = MemCons.userBeans.get(userId);
        if (userBean == null) {
            System.out.println("用户 " + userId + " 不存在");
            return;
        }
        sendMsg(userBean, -1, message);
    }

    // 意外断开
    private void disconnet(String message, Map<String, Object> data) {
        String userId = (String) data.get("userID");
        UserBean userBean = MemCons.userBeans.get(userId);
        if (userBean == null) {
            System.out.println("用户 " + userId + " 不存在");
            return;
        }
        sendMsg(userBean, -1, message);
    }

    // 发送offer
    private void offer(String message, Map<String, Object> data) {
        String userId;
        if((userId = (String) data.get("userID")) == null){
            return;
        }
        System.out.println(userId);
        UserBean userBean;
        if((userBean = MemCons.userBeans.get(userId)) != null)
            sendMsg(userBean, -1, message);
    }

    // 发送answer
    private void answer(String message, Map<String, Object> data) {
        String userId;
        if((userId = (String) data.get("userID")) == null) return;
        UserBean userBean = MemCons.userBeans.get(userId);
        if (userBean == null) {
            System.out.println("用户 " + userId + " 不存在");
            return;
        }
        sendMsg(userBean, -1, message);

    }

    // 发送ice信息
    private void iceCandidate(String message, Map<String, Object> data) {
        String userId;
        if((userId = (String) data.get("userID")) == null) return;
        UserBean userBean = MemCons.userBeans.get(userId);
        if (userBean == null) {
            System.out.println("用户 " + userId + " 不存在");
            return;
        }
        sendMsg(userBean, -1, message);
    }

    // 离开房间
    private void leave(String message, Map<String, Object> data) {
        String room = (String) data.get("room");
        String userId = (String) data.get("fromID");
        if (userId == null) return;
        RoomInfo roomInfo = rooms.get(room);
        if(roomInfo.getMaxSize() == 9) return;
        CopyOnWriteArrayList<UserBean> roomInfoUserBeans = roomInfo.getUserBeans();
        Iterator<UserBean> iterator = roomInfoUserBeans.iterator();
        while (iterator.hasNext()) {
            UserBean userBean = iterator.next();
            if (userId.equals(userBean.getUserId())) {
                roomInfoUserBeans.remove(userBean);
                if (roomInfoUserBeans.size() == 0) {
                    rooms.remove(room);
                }
                continue;
            }
            sendMsg(userBean, -1, message);
            if (roomInfoUserBeans.size() == 1) {
                System.out.println("房间里只剩下一个人");
            }
        }



    }


    private static final Object object = new Object();

    // 给不同设备发送消息
    private void sendMsg(UserBean userBean, int device, String str) {
        System.out.println("====================================================");
        System.out.println("发送消息,device:"+device+" id="+userBean.getUserId());
        System.out.println("data:"+str);
        if (device == 0) {
            Session phoneSession = userBean.getPhoneSession();
            if (phoneSession != null) {
                synchronized (object) {
                    phoneSession.getAsyncRemote().sendText(str);
                }
            }
        } else if (device == 1) {
            Session pcSession = userBean.getPcSession();
            if (pcSession != null) {
                synchronized (object) {
                    pcSession.getAsyncRemote().sendText(str);
                }
            }
        } else {
            Session phoneSession = userBean.getPhoneSession();
            if (phoneSession != null) {
                synchronized (object) {
                    phoneSession.getAsyncRemote().sendText(str);
                }
            }
            Session pcSession = userBean.getPcSession();
            if (pcSession != null) {
                synchronized (object) {
                    pcSession.getAsyncRemote().sendText(str);
                }
            }

        }

    }


}