package irc;



import org.apache.commons.io.IOUtils;
import pi.yalan.packet.IClientPacketHandler;
import pi.yalan.packet.IPacket;
import pi.yalan.packet.PacketRegistry;
import pi.yalan.packet.client.CDisconnectPacket;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public final class Auth implements Closeable {
    /* Address */
    public static long ADDRESS_AUTH_RESULT;
    public static long ADDRESS_REGISTER_RESULT;

    public static Auth Instance;

    public final ConcurrentHashMap<Long,Object> memory = new ConcurrentHashMap<>();
    private final ServerPacketHandler handler = new ServerPacketHandler(this);

    private Socket socket;
    private DataOutputStream writer;
    private DataInputStream input;

    private Thread readPacketThread;

    private volatile boolean connected = false;

    public volatile boolean ircEnable = true;

    public void connect() throws IOException, InterruptedException {
        if (connected) {
            return;
        }
        try {
            socket = new Socket("115.126.43.15",3666);
        } catch (Exception e) {
            while (true){
                Thread.sleep(60);

                socket = new Socket("115.126.43.15",3666);
            }
        }



        writer = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());

        connected = true;
    }

    public void openReadPacketThread() {
        checkConnected();

        readPacketThread = new Thread("Read Packet Thread") {
            @Override
            public void run() {
                while (connected) {
                    try {
                        final int id = input.readInt();

                        PacketRegistry.readPacket(id, input).processPacket(handler);
                    } catch (Throwable e) {
                        if (connected) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        readPacketThread.setDaemon(true);
        readPacketThread.start();
    }

    public void close() {
        if (connected) {
            connected = false;

            try {
                sendPacket(new CDisconnectPacket());
            } catch (Throwable ignored) {
            }

            IOUtils.closeQuietly(socket);

            socket = null;
            writer = null;
            input = null;

            readPacketThread.interrupt();
            readPacketThread = null;
        }
    }

    @SuppressWarnings("unchecked")
    public void sendPacket(IPacket<IClientPacketHandler> packet) throws IOException {
        checkConnected();

        PacketRegistry.writeHead(writer, (Class<? extends IPacket<?>>) packet.getClass());
        packet.writeData(writer);
    }

    private void checkConnected() {
        if (!connected) throw new IllegalStateException("No connected");
    }

    @SuppressWarnings("UnnecessaryBoxing")
    private static Long newAddress(ArrayList<Long> list) {
        Long newAddress;

        do {
            newAddress = Long.valueOf(ThreadLocalRandom.current().nextLong());
        } while (list.contains(newAddress));

        return newAddress;
    }

    static {
        final ArrayList<Long> list = new ArrayList<>();

        ADDRESS_AUTH_RESULT = newAddress(list);
        ADDRESS_REGISTER_RESULT = newAddress(list);
    }
}
