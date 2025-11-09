package com.test.eraser.network.packets;

import com.test.eraser.logic.ILivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EraseEntityPacket {
    private final int entityId;

    public EraseEntityPacket(int entityId) {
        this.entityId = entityId;
    }

    public static void encode(EraseEntityPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityId);
    }

    public static EraseEntityPacket decode(FriendlyByteBuf buf) {
        return new EraseEntityPacket(buf.readVarInt());
    }

    public static void handle(EraseEntityPacket msg, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Level level = mc.level;
            if (level != null) {
                Entity e = level.getEntity(msg.entityId);
                if (e != null) {
                    if(e instanceof ILivingEntity erased) {erased.setErased(true);}
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}