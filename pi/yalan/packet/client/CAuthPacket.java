package pi.yalan.packet.client;

import pi.yalan.packet.IClientPacketHandler;
import pi.yalan.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CAuthPacket implements IPacket<IClientPacketHandler> {
    private String userName;
    private String password;
    private String hwid;

    public CAuthPacket() {
    }

    public CAuthPacket(String userName, String password, String hwid) {
        this.userName = userName;
        this.password = password;
        this.hwid = hwid;
    }

    @Override
    public void writeData(DataOutputStream buffer) throws IOException {
        buffer.writeUTF(userName);
        buffer.writeUTF(password);
        buffer.writeUTF(hwid);
    }

    @Override
    public void readData(DataInputStream buffer) throws IOException {
        userName = buffer.readUTF();
        password = buffer.readUTF();
        hwid = buffer.readUTF();
    }

    @Override
    public void processPacket(IClientPacketHandler packetHandler) {
        packetHandler.processAuth(this);
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
}
