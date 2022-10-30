package pi.yalan.packet;

import pi.yalan.packet.client.CAuthPacket;
import pi.yalan.packet.client.CDisconnectPacket;
import pi.yalan.packet.client.CMessagePacket;
import pi.yalan.packet.client.CRegisterPacket;

public interface IClientPacketHandler extends IPacketHandler {
    void processAuth(CAuthPacket packet);

    void processRegister(CRegisterPacket packet);

    void processMessage(CMessagePacket packet);

    void processDisconnect(CDisconnectPacket packet);
}
