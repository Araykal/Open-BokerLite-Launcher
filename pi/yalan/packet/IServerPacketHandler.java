package pi.yalan.packet;

import pi.yalan.packet.server.*;

public interface IServerPacketHandler extends IPacketHandler {
    void processClose(SClosePacket packet);

    void processKick(SKickPacket packet);

    void processMessage(SMessagePacket packet);

    void processAuthResult(SAuthResultPacket packet);

    void processRegisterResult(SRegisterResultPacket packet);
}
