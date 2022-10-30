package pi.yalan.packet.server;

import pi.yalan.packet.IPacket;
import pi.yalan.packet.IServerPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SRegisterResultPacket implements IPacket<IServerPacketHandler> {
    private String result;

    public SRegisterResultPacket() {
    }

    public SRegisterResultPacket(String result) {
        this.result = result;
    }

    @Override
    public void writeData(DataOutputStream buffer) throws IOException {
        buffer.writeUTF(result);
    }

    @Override
    public void readData(DataInputStream buffer) throws IOException {
        result = buffer.readUTF();
    }

    @Override
    public void processPacket(IServerPacketHandler packetHandler) {
        packetHandler.processRegisterResult(this);
    }

    public String getResult() {
        return result;
    }
}
