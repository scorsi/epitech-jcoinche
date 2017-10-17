package net.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.sql.Timestamp;

import game.GamaManager;
import proto.Message;
import proto.Message.MessageWrapper;

public class ServerHandler extends SimpleChannelInboundHandler<MessageWrapper> {

    private static final GamaManager gameManager = new GamaManager();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        gameManager.disconnectPlayer(ctx.channel());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        gameManager.connectPlayer(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + "has joined.");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        gameManager.disconnectPlayer(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + "has left.");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MessageWrapper msg) {
        gameManager.handleAction(ctx.channel(), msg);
    }

}
