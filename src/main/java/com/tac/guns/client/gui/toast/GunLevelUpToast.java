package com.tac.guns.client.gui.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GunLevelUpToast implements Toast {
    private final Component title;
    private final Component subTitle;
    private final ItemStack icon;

    public GunLevelUpToast(ItemStack icon, Component titleComponent, @Nullable Component subtitle) {
        this.icon = icon;
        this.title = titleComponent;
        this.subTitle = subtitle;
    }

    @NotNull
    public Visibility render(@NotNull PoseStack pPoseStack, ToastComponent toastComponent, long timeSinceLastVisible) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        toastComponent.blit(pPoseStack, 0, 0, 0, 0, this.width(), this.height());

        List<FormattedCharSequence> list = null;
        if (this.subTitle != null) {
            list = toastComponent.getMinecraft().font.split(this.subTitle, 125);
        }
        int i = 0xffff00;
        if (list != null) {
            if (list.size() == 1) {
                toastComponent.getMinecraft().font.draw(pPoseStack, this.title, 30.0F, 7.0F, i | 0xff000000);
                toastComponent.getMinecraft().font.draw(pPoseStack, list.get(0), 30.0F, 18.0F, -1);
            } else {
                if (timeSinceLastVisible < 1500L) {
                    int k = Mth.floor(Mth.clamp((float) (1500L - timeSinceLastVisible) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x4000000;
                    toastComponent.getMinecraft().font.draw(pPoseStack, this.title, 30.0F, 11.0F, i | k);
                } else {
                    int j = Mth.floor(Mth.clamp((float) (timeSinceLastVisible - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x4000000;
                    int l = this.height() / 2 - list.size() * 9 / 2;

                    for (FormattedCharSequence formattedCharSequence : list) {
                        toastComponent.getMinecraft().font.draw(pPoseStack, formattedCharSequence, 30.0F, (float) l, 0xffffff | j);
                        l += 9;
                    }
                }
            }
        }
        toastComponent.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(this.icon, 8, 8);
        return timeSinceLastVisible >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }
}
