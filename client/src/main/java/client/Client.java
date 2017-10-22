package client;

import client.command.Commands;
import client.command.ICommand;
import client.net.ClientInitializer;
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

    public static void main(String[] args) {
        Client client = new Client("localhost", SERVER_PORT);

        try {
            client.run();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            if (client.serverChannel != null && client.serverChannel.isOpen())
                client.serverChannel.close();
        }
    }

    private Channel serverChannel = null;
    private final String host;
    private final int port;

    private Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void sendMsg(String msg) {
        this.serverChannel.writeAndFlush(
                MessageWrapper.newBuilder()
                        .setType(MessageWrapper.MessageType.CHAT)
                        .setChat(MessageChat.newBuilder().setText(msg).build())
                        .setTimestamp(new Timestamp(System.currentTimeMillis()).getTime())
                        .setCode(0)
                        .build()
        );
    }

    private void checkMsg(String msg) {
        if (msg.startsWith("/")) {
            try {
                ((ICommand)(Commands.from(msg).newInstance())).run(msg, this.serverChannel);
            } catch (Exception e) {
                System.out.println("Invalid command");
            }
        } else {
            this.sendMsg(msg);
        }
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
                else {
                    this.checkMsg(str);
                }
            }
        }
        finally {
            if (this.serverChannel != null)
                this.serverChannel.closeFuture().sync();
            group.shutdownGracefully();
        }
    }

}
