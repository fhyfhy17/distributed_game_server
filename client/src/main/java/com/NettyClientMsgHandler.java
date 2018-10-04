package com;

import com.net.msg.LOGIN_MSG;
import com.net.msg.Message;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class NettyClientMsgHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        final ChannelHandlerContext fctx = ctx;
        ClientSession.getInstance();
        //启动ClientSession 和 Scanner交互器。
        ClientSession.init(fctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
//            System.out.println(LOGIN_MSG.STC_LOGIN.parseFrom(((Message)msg).getData()).getUid());
//            System.out.println(LOGIN_MSG.STC_LOGIN.parseFrom(((Message)msg).getData()).getSuc());
            System.out.println(LOGIN_MSG.STC_TEST.parseFrom(((Message) msg).getData()).getWord());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
