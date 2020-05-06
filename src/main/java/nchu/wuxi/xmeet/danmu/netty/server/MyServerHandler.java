package nchu.wuxi.xmeet.danmu.netty.server;

import com.alibaba.fastjson.JSON;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther WuXi
 * @create 2019/10/29
 */
public class MyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static HashMap<String,List<ChannelId>> roomMap = new HashMap<>();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame text) throws Exception {
        Map<String,Object> maps = (Map) JSON.parse(text.text());
        System.out.println(maps);
        String userName = (String) maps.get("userName");
        String roomId = (String) maps.get("roomId");
        String msg = (String) maps.get("msg");
        boolean isOpen = (boolean) maps.get("isOpen");
        System.out.println(isOpen);
        List<ChannelId> list = roomMap.get(roomId);
        if(isOpen){
            if (list == null){
                //System.out.println("-----------isOpen = true list = null");
                ArrayList<ChannelId> ids = new ArrayList<>();
                ids.add(ctx.channel().id());
                roomMap.put(roomId,ids);
                list = ids;
            }else {
               // System.out.println("-----------isOpen = true list != null");
                list.add(ctx.channel().id());
                for (ChannelId channelId : list) {
                    Channel channel = channelGroup.find(channelId);
                    if(channel != null && channel != ctx.channel()){
                        channel.writeAndFlush(new TextWebSocketFrame("提示："+userName+"进入直播间"));
                    }
                }
            }
        }else {
           // System.out.println("-----------isOpen = false");
            for (ChannelId channelId : list) {
                Channel channel = channelGroup.find(channelId);
                if(channel != null){
                    channel.writeAndFlush(new TextWebSocketFrame(userName+":"+msg));
                }
            }
        }











//        /*List<ChannelId> list = roomMap.get(roomId);
//        System.out.println(list);
//        if (list == null){
//            ArrayList<ChannelId> ids = new ArrayList<>();
//            ids.add(ctx.channel().id());
//            roomMap.put(roomId,ids);
//            list = ids;
//            return;
//        }
//
//        for (ChannelId channelId : list) {
//            Channel channel = channelGroup.find(channelId);
//            if(channel != null){
//                channel.writeAndFlush(new TextWebSocketFrame(userName+":"+msg));
//            }
//
//        }

        //System.out.println("["+LocalDateTime.now()+"]"+ctx.channel().remoteAddress()+":"+text.text());*/
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        System.out.println(new TextWebSocketFrame("齉【服务器】"+channel.remoteAddress().toString()+"加入\n"));
//        channelGroup.writeAndFlush(new TextWebSocketFrame("【服务器】"+channel.remoteAddress()+"加入\n"));
      //  channel.writeAndFlush(new TextWebSocketFrame("齉【服务器】闸种你终于来啦！"));
        channelGroup.add(channel);

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
       // System.out.println(LocalDateTime.now()+"【服务器】"+channel.remoteAddress()+"离开\n");
//            channelGroup.writeAndFlush(new TextWebSocketFrame("【服务器】"+channel.remoteAddress().toString()+"离开\n"));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        System.out.println("【服务器】"+channel.remoteAddress()+"闸种你上线拉\n");
        channel.writeAndFlush(new TextWebSocketFrame("您已连接至服务器"));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        System.out.println("【服务器】"+channel.remoteAddress()+"下线\n");
      //  channelGroup.writeAndFlush(new TextWebSocketFrame("齉【服务器】"+channel.remoteAddress()+"已爬\n"));
    }
    private int countStr(String str,String sToFind) {
        int num = 0;
        while (str.contains(sToFind)) {
            str = str.substring(str.indexOf(sToFind) + sToFind.length());
            num ++;
        }
        return num;
    }
}
