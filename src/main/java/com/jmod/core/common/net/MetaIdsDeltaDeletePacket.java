package com.jmod.core.common.net;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MetaIdsDeltaDeletePacket implements IMessage {
    private int x, z;
    private short y;

    public MetaIdsDeltaDeletePacket(){
        this(0, 0, 0);
    }

    public MetaIdsDeltaDeletePacket(int x, int y, int z){
        this.x = x;
        this.y = (short) y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x =            buf.readInt();
        this.z =            buf.readInt();
        this.y =            buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        buf.writeShort(this.y);
    }
}
