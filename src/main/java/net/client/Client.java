package net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import proto.Message.MessageWrapper;
import proto.Message.MessageChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;

public class Client {

    private static final int SERVER_PORT = 8000;

    public static void main(String[] args)
    {
        Client client = new Client("localhost", SERVER_PORT);

        try {
            client.run();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            if (client.serverChannel.isOpen())
                client.serverChannel.close();
        }
    }

    private Channel serverChannel = null;
    private final String host;
    private final int port;
    private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private MessageWrapper.Builder wrapperBuilder = MessageWrapper.newBuilder();
    private MessageChat.Builder chatBuilder = MessageChat.newBuilder();

    private Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void sendMsg(String msg) {
        timestamp.setTime(System.currentTimeMillis());

        this.wrapperBuilder.setType(MessageWrapper.MessageType.CHAT)
                .setChat(this.chatBuilder.setText(msg).build())
                .setCode(0)
                .setTimestamp(timestamp.getTime());

        this.serverChannel.writeAndFlush(wrapperBuilder.build());
    }

    private void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());

            this.serverChannel = bootstrap.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            boolean running = true;

            while (running) {
                String str = in.readLine();
                if (str.equals("/exit"))
                    running = false;
                else
                    this.sendMsg(str);
            }
        }
        finally {
            if (this.serverChannel != null)
                this.serverChannel.closeFuture().sync();
            group.shutdownGracefully();
        }
    }

}
