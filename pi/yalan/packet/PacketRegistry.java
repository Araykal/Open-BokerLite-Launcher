package pi.yalan.packet;

import pi.yalan.packet.client.CAuthPacket;
import pi.yalan.packet.client.CDisconnectPacket;
import pi.yalan.packet.client.CMessagePacket;
import pi.yalan.packet.client.CRegisterPacket;
import pi.yalan.packet.server.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public final class PacketRegistry {
    private static final HashMap<Integer,Class<? extends IPacket<IPacketHandler>>> PACKETS = new HashMap<>();
    private static final HashMap<Class<? extends IPacket<IPacketHandler>>,Integer> PACKET_IDS = new HashMap<>();

    public static void register() {
        registerPacket(CAuthPacket.class);
        registerPacket(CRegisterPacket.class);
        registerPacket(CMessagePacket.class);
        registerPacket(CDisconnectPacket.class);

        registerPacket(SAuthResultPacket.class);
        registerPacket(SRegisterResultPacket.class);
        registerPacket(SClosePacket.class);
        registerPacket(SKickPacket.class);
        registerPacket(SMessagePacket.class);
    }

    public static IPacket<IPacketHandler> readPacket(int id, DataInputStream buf) throws Throwable {
        final Class<? extends IPacket<IPacketHandler>> packetClass = PACKETS.get(id);

        if (packetClass == null) {
            throw new IllegalArgumentException("Unknown packet id: " + id);
        }

        final IPacket<IPacketHandler> packet = packetClass.newInstance();

        packet.readData(buf);

        return packet;
    }

    public static void writeHead(DataOutputStream buf, Class<? extends IPacket<?>> packetClass) throws IOException {
        buf.writeInt(PACKET_IDS.get(packetClass));
    }

    @SuppressWarnings("unchecked")
    private static void registerPacket(Class<? extends IPacket<?>> packetClass) {
        final Class<? extends IPacket<IPacketHandler>> cast = (Class<? extends IPacket<IPacketHandler>>) packetClass;
        final int id = PACKETS.size();

        PACKETS.put(id, cast);
        PACKET_IDS.put(cast, id);
    }
}
