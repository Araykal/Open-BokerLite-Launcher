package pi.yalan.packet.client;

import pi.yalan.packet.IClientPacketHandler;
import pi.yalan.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CMessagePacket implements IPacket<IClientPacketHandler> {
    private String message;

    public CMessagePacket() {
    }

    public CMessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void writeData(DataOutputStream buffer) throws IOException {
        buffer.writeUTF(message);
    }

    @Override
    public void readData(DataInputStream buffer) throws IOException {
        message = buffer.readUTF();
    }

    @Override
    public void processPacket(IClientPacketHandler packetHandler) {
        packetHandler.processMessage(this);
    }

    public String getMessage() {
        return message;
    }
}
