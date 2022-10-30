package pi.yalan.packet.client;

import pi.yalan.packet.IClientPacketHandler;
import pi.yalan.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CRegisterPacket implements IPacket<IClientPacketHandler> {
    private String userName;
    private String password;
    private String hwid;
    private String activeCode;

    public CRegisterPacket() {
    }

    public CRegisterPacket(String userName, String password, String hwid, String activeCode) {
        this.userName = userName;
        this.password = password;
        this.hwid = hwid;
        this.activeCode = activeCode;
    }

    @Override
    public void writeData(DataOutputStream buffer) throws IOException {
        buffer.writeUTF(userName);
        buffer.writeUTF(password);
        buffer.writeUTF(hwid);
        buffer.writeUTF(activeCode);
    }

    @Override
    public void readData(DataInputStream buffer) throws IOException {
        userName = buffer.readUTF();
        password = buffer.readUTF();
        hwid = buffer.readUTF();
        activeCode = buffer.readUTF();
    }

    @Override
    public void processPacket(IClientPacketHandler packetHandler) {
        packetHandler.processRegister(this);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getHwid() {
        return hwid;
    }

    public String getActiveCode() {
        return activeCode;
    }
}
