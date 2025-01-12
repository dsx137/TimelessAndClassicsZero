package com.tac.guns.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.tac.guns.client.gui.compat.ClothConfigScreen;
import com.tac.guns.compat.cloth.MenuIntegration;
import com.tac.guns.init.CompatRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.tac.guns.util.InputExtraCheck.isInGame;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ConfigKey {
    public static final KeyMapping OPEN_CONFIG_KEY = new KeyMapping("key.tac.open_config.desc",
            KeyConflictContext.IN_GAME,
            KeyModifier.ALT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_T,
            "key.category.tac");

    @SubscribeEvent
    public static void onOpenConfig(InputEvent.KeyInputEvent event) {
        if (isInGame() && event.getAction() == GLFW.GLFW_PRESS
                && OPEN_CONFIG_KEY.matches(event.getKey(), event.getScanCode())
                && OPEN_CONFIG_KEY.getKeyModifier().equals(KeyModifier.getActiveModifier())) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            if (!ModList.get().isLoaded(CompatRegistry.CLOTH_CONFIG)) {
                ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, ClothConfigScreen.CLOTH_CONFIG_URL);
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("gui.tac.cloth_config_warning.download"));
                MutableComponent component = new TranslatableComponent("gui.tac.cloth_config_warning.tips").withStyle(style ->
                        style.applyFormat(ChatFormatting.BLUE).applyFormat(ChatFormatting.UNDERLINE).withClickEvent(clickEvent).withHoverEvent(hoverEvent));
                player.sendMessage(component, Util.NIL_UUID);
            } else {
                CompatRegistry.checkModLoad(CompatRegistry.CLOTH_CONFIG, () -> Minecraft.getInstance().setScreen(MenuIntegration.getConfigScreen(null)));
            }
        }
    }
}
