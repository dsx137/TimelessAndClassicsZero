package com.tac.guns.network.message;

import com.tac.guns.api.entity.IGunOperator;
import com.tac.guns.api.gun.FireMode;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientMessagePlayerFireSelect {
    private final int gunItemIndex;

    public ClientMessagePlayerFireSelect(int gunItemIndex) {
        this.gunItemIndex = gunItemIndex;
    }

    public static void encode(ClientMessagePlayerFireSelect message, FriendlyByteBuf buf) {
        buf.writeInt(message.gunItemIndex);
    }

    public static ClientMessagePlayerFireSelect decode(FriendlyByteBuf buf) {
        return new ClientMessagePlayerFireSelect(buf.readInt());
    }

    public static void handle(ClientMessagePlayerFireSelect message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer entity = context.getSender();
                if (entity == null) {
                    return;
                }
                // 暂时只考虑主手
                if (entity.getInventory().selected != message.gunItemIndex) {
                    return;
                }
                ItemStack gunItem = entity.getInventory().getItem(message.gunItemIndex);
                FireMode fireMode = IGunOperator.fromLivingEntity(entity).fireSelect(gunItem);
                entity.sendMessage(new TranslatableComponent("message.tac.fire_select.success", fireMode.name()), Util.NIL_UUID);
            });
        }
        context.setPacketHandled(true);
    }
}
