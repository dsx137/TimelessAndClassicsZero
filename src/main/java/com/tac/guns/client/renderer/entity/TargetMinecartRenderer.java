package com.tac.guns.client.renderer.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tac.guns.GunMod;
import com.tac.guns.client.model.bedrock.BedrockModel;
import com.tac.guns.client.model.bedrock.BedrockPart;
import com.tac.guns.client.resource.InternalAssetLoader;
import com.tac.guns.entity.TargetMinecart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class TargetMinecartRenderer extends MinecartRenderer<TargetMinecart> {
    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(GunMod.MOD_ID, "models/bedrock/target_minecart.json");
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(GunMod.MOD_ID, "textures/entity/target_minecart.png");
    private static final ResourceLocation EMPTY_TEXTURE = new ResourceLocation(GunMod.MOD_ID, "textures/entity/empty.png");
    private static final String HEAD_NAME = "head";

    public TargetMinecartRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, ModelLayers.TNT_MINECART);
        this.shadowRadius = 0.25F;
    }

    public static Optional<BedrockModel> getModel() {
        return InternalAssetLoader.getBedrockModel(MODEL_LOCATION);
    }

    @Override
    public ResourceLocation getTextureLocation(TargetMinecart minecart) {
        return EMPTY_TEXTURE;
    }

    @Override
    protected void renderMinecartContents(TargetMinecart targetMinecart, float pPartialTicks, BlockState pState, PoseStack stack, MultiBufferSource buffer, int pPackedLight) {
        getModel().ifPresent(model -> {
            BedrockPart headModel = model.getNode(HEAD_NAME);
            headModel.visible = false;

            stack.pushPose();
            stack.translate(0.5, 1.875, 0.5);
            stack.scale(1.5f, 1.5f, 1.5f);
            stack.mulPose(Vector3f.ZN.rotationDegrees(180));
            stack.mulPose(Vector3f.YN.rotationDegrees(90));
            RenderType renderType = RenderType.entityTranslucent(TEXTURE_LOCATION);
            model.render(stack, ItemTransforms.TransformType.NONE, renderType, pPackedLight, OverlayTexture.NO_OVERLAY);
            if (targetMinecart.getGameProfile() != null) {
                stack.translate(0, 1, -4.5 / 16d);
                Minecraft minecraft = Minecraft.getInstance();
                GameProfile gameProfile = targetMinecart.getGameProfile();
                var map = minecraft.getSkinManager().getInsecureSkinInformation(gameProfile);
                ResourceLocation skin;
                if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                    skin = minecraft.getSkinManager().registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
                } else {
                    skin = DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(gameProfile));
                }
                headModel.visible = true;
                RenderType skullRenderType = RenderType.entityTranslucentCull(skin);
                headModel.render(stack, ItemTransforms.TransformType.NONE, buffer.getBuffer(skullRenderType), pPackedLight, OverlayTexture.NO_OVERLAY);
            }
            stack.popPose();
        });
    }
}