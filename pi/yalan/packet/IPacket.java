package pi.yalan.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IPacket<H extends IPacketHandler> {
    void writeData(DataOutputStream buffer) throws IOException;

    void readData(DataInputStream buffer) throws IOException;

    void processPacket(H packetHandler);
}
