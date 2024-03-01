package com.tac.guns.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.tac.guns.client.animation.AnimationController;
import com.tac.guns.client.animation.ObjectAnimation;
import com.tac.guns.client.model.BedrockGunModel;
import com.tac.guns.client.resource.ClientGunLoader;
import com.tac.guns.client.resource.cache.data.ClientGunIndex;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ReloadKey {
    public static final KeyMapping RELOAD_KEY = new KeyMapping("key.tac.reload.desc",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.category.tac");

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (RELOAD_KEY.isDown()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getMainHandItem().is(ModItems.GUN.get())) {
                ClientGunIndex gunIndex = ClientGunLoader.getGunIndex(GunItem.DEFAULT);
                BedrockGunModel gunModel = gunIndex.getGunModel();
                AnimationController controller = gunIndex.getController();
                if (gunModel != null && controller != null) {
                    controller.runAnimation(0, "reload_empty", ObjectAnimation.PlayType.PLAY_ONCE_HOLD, 0.02f);
                }
            }
        }
    }
}