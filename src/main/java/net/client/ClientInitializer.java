package net.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import proto.Message.MessageWrapper;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast ("frameDecoder", new ProtobufVarint32FrameDecoder())
                .addLast ("protobufDecoder", new ProtobufDecoder(MessageWrapper.getDefaultInstance()))
                .addLast ("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
                .addLast ("protobufEncoder", new ProtobufEncoder());

        pipeline.addLast("handler", new ClientHandler());
    }

}
