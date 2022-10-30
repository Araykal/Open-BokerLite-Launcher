package pi.yalan.packet.client;

import pi.yalan.packet.IClientPacketHandler;
import pi.yalan.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CDisconnectPacket implements IPacket<IClientPacketHandler> {
    @Override
    public void writeData(DataOutputStream buffer) {

    }

    @Override
    public void readData(DataInputStream buffer) {

    }

    @Override
    public void processPacket(IClientPacketHandler packetHandler) {
        packetHandler.processDisconnect(this);
    }
}
