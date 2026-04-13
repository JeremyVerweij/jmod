package com.jmod.core.common.net;

import com.jmod.JMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(JMod.MODID);

    private static int ID = 0;

    public static <HANDLER extends IMessageHandler<REQ, IMessage>, REQ extends IMessage> void register(Class<HANDLER> handler, Class<REQ> packet, Side side){
        INSTANCE.registerMessage(handler, packet, ID++, side);
    }

    public static <REQ extends IMessage> void sendToServer(REQ packet){
        INSTANCE.sendToServer(packet);
    }

    public static <REQ extends IMessage> void sendToAllClients(REQ packet){
        INSTANCE.sendToAll(packet);
    }

    public static <REQ extends IMessage> void sendClientsInDimension(REQ packet, int dimensionId){
        INSTANCE.sendToDimension(packet, dimensionId);
    }

    public static <REQ extends IMessage> void sendToAllClientsAround(REQ packet, NetworkRegistry.TargetPoint targetPoint){
        INSTANCE.sendToAllAround(packet, targetPoint);
    }

    public static <REQ extends IMessage> void sendToClient(REQ packet, EntityPlayerMP player){
        INSTANCE.sendTo(packet, player);
    }

    public static <REQ extends IMessage> void sendToClientsTracking(REQ packet, NetworkRegistry.TargetPoint targetPoint){
        INSTANCE.sendToAllTracking(packet, targetPoint);
    }

    public static <REQ extends IMessage> void sendToClientsTracking(REQ packet, Entity entity){
        INSTANCE.sendToAllTracking(packet, entity);
    }
}
