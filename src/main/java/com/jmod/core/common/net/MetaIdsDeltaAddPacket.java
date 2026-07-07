package com.jmod.core.common.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MetaIdsDeltaAddPacket implements IMessage {
    private int x, z, id;
    private short y;

    public MetaIdsDeltaAddPacket(){
        this(0, 0, 0, 0);
    }

    public MetaIdsDeltaAddPacket(int x, int y, int z, int id){
        this.x = x;
        this.y = (short) y;
        this.z = z;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public BlockPos getBlockPos(){
        return new BlockPos(getX(), getY(), getZ());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x =            buf.readInt();
        this.z =            buf.readInt();
        this.id =           buf.readInt();
        this.y =            buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        buf.writeInt(this.id);
        buf.writeShort(this.y);
    }
}
