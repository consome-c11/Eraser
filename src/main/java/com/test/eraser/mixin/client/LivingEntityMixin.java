package com.test.eraser.mixin.client;

import com.test.eraser.client.ClientEvents;
import com.test.eraser.logic.ILivingEntity;
import com.test.eraser.mixin.eraser.EntityAccessor;
import com.test.eraser.mixin.eraser.LevelEntityGetterAdapterAccessor;
import com.test.eraser.network.PacketHandler;
import com.test.eraser.network.packets.HandleErasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@OnlyIn(Dist.CLIENT)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntity {
    @Override
    public void eraseClientEntity() {
        LivingEntity self = (LivingEntity) (Object) this;
        ((ILivingEntity) self).setErased(false);
        Minecraft mc = Minecraft.getInstance();
        ClientLevel clientLevel = mc.level;
        TransientEntitySectionManager<Entity> tManager = ((ClientLevelAccessor) clientLevel).getTransientEntityManager();

        self.onClientRemoval();
        ((EntityAccessor) (self)).setRemovalReason(Entity.RemovalReason.KILLED);
        //removeFromOtherIndexes(self.getUUID(), clientLevel, tManager);
        clientLevel.removeEntity(self.getId(), Entity.RemovalReason.KILLED);
        //self.remove(Entity.RemovalReason.KILLED);
        self.invalidateCaps();
        Entity e = clientLevel.getEntity(self.getId());
        List<Entity> snapshot = StreamSupport.stream(((LevelEntityGetterAdapterAccessor<Entity>) tManager.getEntityGetter()).getVisibleEntities().getAllEntities().spliterator(), false)
                .collect(Collectors.toList());
        if (self instanceof Player) PacketHandler.CHANNEL.sendToServer(new HandleErasePacket());

        if (e != null) {
            //LOGGER.info("[EraserMod] failed to fully remove client entity id=" + self.getId());
            /*ClientboundRemoveEntitiesPacket packet =
                    new ClientboundRemoveEntitiesPacket(self.getId());
            ClientPacketListener connection = mc.getConnection();
            packet.handle(connection);*/
            ClientEvents.erasedEntities.add(self);
        } else {
            //LOGGER.info("[EraserMod] successfully removed client entity id=" + self.getId());
        }
    }


}