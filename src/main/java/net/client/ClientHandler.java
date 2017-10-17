package net.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import proto.Message.MessageWrapper;

public class ClientHandler extends SimpleChannelInboundHandler<MessageWrapper> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageWrapper msg) {
        if (msg.getType() == MessageWrapper.MessageType.CHAT) {
            System.out.println(msg.getChat().getText());
        }
    }
}
