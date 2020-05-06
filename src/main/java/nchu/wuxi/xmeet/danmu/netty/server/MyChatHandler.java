package nchu.wuxi.xmeet.danmu.netty.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import nchu.wuxi.xmeet.entity.TChatRecord;
import nchu.wuxi.xmeet.mapper.TChatRecordMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther WuXi
 * @create 2020/4/29
 */
@Component
public class MyChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> implements ApplicationContextAware {

    private static TChatRecordMapper chatRecordMapper;

    private static ApplicationContext applicationContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame text) throws Exception {
        Map<String,Object> maps = (Map) JSON.parse(text.text());
        System.out.println(maps);
        maps.put("mills",System.currentTimeMillis());
        String fromId = (String) maps.get("fromId");
        String toId = (String) maps.get("toId");
        String msg = (String) maps.get("msg");
        int type = (int) maps.get("type");
        Long mills = (Long)maps.get("mills");
        switch (type){
            //normal string message
            case 1001:
                NettyChannel nettyChannel = ChannelContainer.getInstance().getActiveChannelByUserId(toId);
//                System.out.println(nettyChannel);
//                System.out.println("userId="+toId);
//                System.out.println(nettyChannel.getUserId());
//                System.out.println("channelId="+nettyChannel.getChannelId());
//                System.out.println("inactive="+nettyChannel.isActive());
                //nettyChannel.getChannel().writeAndFlush(new TextWebSocketFrame("return message"));
                if(nettyChannel == null || !nettyChannel.getChannel().isActive()){
                    // 将消息持久化到数据库
                    System.out.println("将消息持久化到数据库");
                    TChatRecord record = new TChatRecord();
                    record.setFromId(fromId);
                    record.setToId(toId);
                    record.setMsg(msg);
                    record.setType(type);
                    record.setMills(mills);
                   // System.out.println(chatRecordMapper);
                    chatRecordMapper.insert(record);
                }else{
                    //在线，直接转发给用户客户端
                  //  System.out.println("在线，直接转发给用户客户端");
                    nettyChannel.getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(maps)));
                }
                break;
            case 1000:
                ChannelContainer.getInstance().saveChannel((new NettyChannel(fromId,ctx.channel())));
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("inactive id = "+ctx.channel().id());
        System.out.println("ServerHandler channelInactive()");
        // 用户断开连接后，移除channel
        ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
        ctx.channel().close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        //System.out.println(ctx.channel().id().toString()+"registered!");
    }

    @Override
    public void setApplicationContext(ApplicationContext arg) throws BeansException {
        applicationContext = arg;
        chatRecordMapper = (TChatRecordMapper) applicationContext.getBean(TChatRecordMapper.class);
    }


    public static class ChannelContainer {

        private ChannelContainer() {

        }

        private static final ChannelContainer INSTANCE = new MyChatHandler.ChannelContainer();

        public static MyChatHandler.ChannelContainer getInstance() {
            return INSTANCE;
        }

        private final Map<String, MyChatHandler.NettyChannel> CHANNELS = new ConcurrentHashMap<>();

        public void saveChannel(MyChatHandler.NettyChannel channel) {
            if (channel == null) {
                return;
            }
            CHANNELS.put(channel.getChannelId(), channel);
        }

        public MyChatHandler.NettyChannel removeChannelIfConnectNoActive(Channel channel) {
            if (channel == null) {
                return null;
            }

            String channelId = channel.id().toString();

            return removeChannelIfConnectNoActive(channelId);
        }

        public MyChatHandler.NettyChannel removeChannelIfConnectNoActive(String channelId) {
            if (CHANNELS.containsKey(channelId) && !CHANNELS.get(channelId).isActive()) {
                return CHANNELS.remove(channelId);
            }

            return null;
        }

        public String getUserIdByChannel(Channel channel) {
            return getUserIdByChannel(channel.id().toString());
        }

        public String getUserIdByChannel(String channelId) {
            if (CHANNELS.containsKey(channelId)) {
                return CHANNELS.get(channelId).getUserId();
            }

            return null;
        }

        public MyChatHandler.NettyChannel getActiveChannelByUserId(String userId) {
            for (Map.Entry<String, MyChatHandler.NettyChannel> entry : CHANNELS.entrySet()) {
                if (entry.getValue().getUserId().equals(userId) && entry.getValue().isActive()) {
                    return entry.getValue();
                }
            }
            return null;
        }
    }

    public class NettyChannel {

        private String userId;
        private Channel channel;

        public NettyChannel(String userId, Channel channel) {
            this.userId = userId;
            this.channel = channel;
        }

        public String getChannelId() {
            return channel.id().toString();
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public boolean isActive() {
            return channel.isActive();
        }
    }
}
