package pi.yalan.packet.server;

import pi.yalan.packet.IPacket;
import pi.yalan.packet.IServerPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SAuthResultPacket implements IPacket<IServerPacketHandler> {
    private Result result;

    public SAuthResultPacket() {
    }

    public SAuthResultPacket(Result result) {
        this.result = result;
    }

    @Override
    public void writeData(DataOutputStream buffer) throws IOException {
        buffer.writeInt(result.ordinal());
    }

    @Override
    public void readData(DataInputStream buffer) throws IOException {
        result = Result.values()[buffer.readInt()];
    }

    @Override
    public void processPacket(IServerPacketHandler packetHandler) {
        packetHandler.processAuthResult(this);
    }

    public Result getResult() {
        return result;
    }

    public enum Result {
        SUCCEED,
        FAILED
    }
}
