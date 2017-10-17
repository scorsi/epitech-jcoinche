package server.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import server.LobbyManager;
import proto.Message.MessageWrapper;

public class ServerHandler extends SimpleChannelInboundHandler<MessageWrapper> {

    private static final LobbyManager lobbyManager = new LobbyManager();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        lobbyManager.disconnectPlayer(ctx.channel());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        lobbyManager.connectPlayer(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + " has joined.");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        lobbyManager.disconnectPlayer(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + " has left.");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MessageWrapper msg) {
        lobbyManager.handleAction(ctx.channel(), msg);
    }

}
