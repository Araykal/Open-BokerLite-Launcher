package cn.BokerLite;


import pi.yalan.packet.IServerPacketHandler;
import pi.yalan.packet.server.*;


public class ServerPacketHandler implements IServerPacketHandler {

    private final Auth instance;

    public ServerPacketHandler(Auth instance) {
        this.instance = instance;
    }

    @Override
    public void processClose(SClosePacket packet) {

    }

    @Override
    public void processKick(SKickPacket packet) {

    }

    @Override
    public void processMessage(SMessagePacket packet) {

    }
    @Override
    public void processAuthResult(SAuthResultPacket packet) {
        Auth.Instance.memory.put(Auth.ADDRESS_AUTH_RESULT,packet.getResult());
    }

    @Override
    public void processRegisterResult(SRegisterResultPacket packet) {
        Auth.Instance.memory.put(Auth.ADDRESS_REGISTER_RESULT,packet.getResult());
    }
}
