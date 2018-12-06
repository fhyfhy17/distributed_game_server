package com;

import com.net.msg.LOGIN_MSG;
import com.pojo.Message;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Sharable
@Slf4j
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
            Message message = (Message) msg;
            if (message.getId() == 10009) {
                LOGIN_MSG.STC_PLAYER_LIST stc_player_list = LOGIN_MSG.STC_PLAYER_LIST.parseFrom(message.getData());
                LOGIN_MSG.PLAYER_INFO player = stc_player_list.getPlayers(0);
                long playerId = player.getPlayerId();
                LOGIN_MSG.CTS_GAME_LOGIN_PLAYER.Builder loginBuilder = LOGIN_MSG.CTS_GAME_LOGIN_PLAYER.newBuilder();
                loginBuilder.setPlayerId(playerId);
                ctx.channel().writeAndFlush(loginBuilder.build());

            }

           log.info(LOGIN_MSG.STC_TEST.parseFrom(((Message) msg).getData()).getWord());

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
