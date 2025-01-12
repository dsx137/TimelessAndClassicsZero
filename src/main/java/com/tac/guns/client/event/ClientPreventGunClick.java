package com.tac.guns.client.event;

import com.tac.guns.GunMod;
import com.tac.guns.api.item.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunMod.MOD_ID)
public class ClientPreventGunClick {
    @SubscribeEvent
    public static void onClickInput(InputEvent.ClickInputEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        // 只要主手有枪，那么禁止交互
        ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemInHand.getItem() instanceof IGun) {
            // 展示框可以交互
            HitResult hitResult = Minecraft.getInstance().hitResult;
            if (hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof ItemFrame) {
                return;
            }
            // 这个设置为 false 就能阻止客户端粒子的生成
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }
}
